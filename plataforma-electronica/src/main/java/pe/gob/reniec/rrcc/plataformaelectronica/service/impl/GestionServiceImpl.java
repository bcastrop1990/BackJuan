package pe.gob.reniec.rrcc.plataformaelectronica.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.gob.reniec.rrcc.plataformaelectronica.config.ApiGestionUsuarioProperties;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.ArchivoDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.OficinaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.SolicitudDao;
import pe.gob.reniec.rrcc.plataformaelectronica.exception.ApiValidateException;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArchivoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudArchivoFirmaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudFirmaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudLibroBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.OficinaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.SolicitudBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.*;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.ApiPageResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.ArchivoResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.DetalleSolDetFirmaResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.DetalleSolDetLibroResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.DetalleSolFirmaResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.DetalleSolLibroResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.ExpedienteConsultaResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.GestionConsultaSolResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.SolDetalleLibroResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.SolicitudLibroResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.security.SecurityUtil;
import pe.gob.reniec.rrcc.plataformaelectronica.security.UserInfo;
import pe.gob.reniec.rrcc.plataformaelectronica.service.ArchivoService;
import pe.gob.reniec.rrcc.plataformaelectronica.service.GestionService;
import pe.gob.reniec.rrcc.plataformaelectronica.service.SeguridadService;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.ArchivoConstant;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.ConstantUtil;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.SolicitudConstant;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GestionServiceImpl implements GestionService {
  private SolicitudDao solicitudDao;
  private OficinaDao oficinaDao;
  private ArchivoDao archivoDao;
  private ApiGestionUsuarioProperties ApiGUproperties;
  private SeguridadService seguridadService;

  private ArchivoService archivoService;
  @Override
  public ApiPageResponse<GestionConsultaSolResponse> consultaSolicitud(GestionConsultaSolRequest request,
                                                                       int page, int size) {
    UserInfo userInfo = seguridadService.getUserAuth();
    if (userInfo.getPerfil().getCodigo().equals(ApiGUproperties.getCodigoPerfilAnalista())) {
      if (request.getCodigoEstado().equals(SolicitudConstant.ESTADO_REGISTRADO)
          || request.getCodigoEstado().equals(SolicitudConstant.ESTADO_RECEPCIONADO)) {
        throw new ApiValidateException("Usuario no autorizado para el estado de solicitud.");
      }
      request.setCodigoTipoRegistro(SolicitudConstant.TIPO_LIBRO);
      request.setCodigoAnalistaAsignado(userInfo.getDni());
    }

    Page<SolicitudBean> solicitudes = solicitudDao.consultar(buildSolicitudBean(request),
        PageRequest.of(page - 1, size),
        request.getFechaIni(),
        request.getFechaFin());

    ApiPageResponse<GestionConsultaSolResponse> response = new ApiPageResponse<>();
    response.setCode(ConstantUtil.OK_CODE);
    response.setMessage(ConstantUtil.OK_MESSAGE);
    response.setData(solicitudes.getContent().stream().map(this::mapper).collect(Collectors.toList()));
    response.setPage(solicitudes.getNumber());
    response.setSize(solicitudes.getSize());
    response.setTotalPage(solicitudes.getTotalPages());
    response.setTotalElements(solicitudes.getTotalElements());
    response.setNumberOfElements(solicitudes.getNumberOfElements());
    return response;
  }

  @Override
  @Transactional
  public String recepcionarSolicitud(RecepcionSolicitudRequest request) {
    AtomicInteger total = new AtomicInteger();
    UserInfo userInfo = seguridadService.getUserAuth();
    request.getSolicitudes().forEach(nroSol -> {
      Optional<SolicitudBean> solicitudBean = solicitudDao.obtenerPorNumero(nroSol);
      if (solicitudBean.isPresent()
          && solicitudBean.get().getCodigoEstado().equals(SolicitudConstant.ESTADO_REGISTRADO)) {
        solicitudDao.recepcionar(nroSol, SolicitudConstant.ESTADO_RECEPCIONADO, userInfo.getDni());
        total.getAndIncrement();
      }
    });
    return String.format("Se recepcionaron %s solicitudes.", total);
  }

  @Override
  @Transactional
  public String asignarSolicitud(AsignacionSolicitudRequest request) {
    AtomicInteger total = new AtomicInteger();
    request.getSolicitudes().forEach(nroSol -> {
      Optional<SolicitudBean> solicitudBean = solicitudDao.obtenerPorNumero(nroSol);
      if (solicitudBean.isPresent()
          && solicitudBean.get().getCodigoEstado().equals(SolicitudConstant.ESTADO_RECEPCIONADO)) {
        solicitudDao.asignarAnalista(nroSol, request.getCodigoAnalista(), SolicitudConstant.ESTADO_ASIGNADO);
        total.getAndIncrement();
      }
    });
    return String.format("Se asignaron %s solicitudes.", total);
  }

  @Override
  public DetalleSolLibroResponse consultarSolicitudLibro(String nroSolicitud) {
    SolicitudBean solicitudBean = solicitudDao.obtenerPorNumero(nroSolicitud)
        .orElseThrow(() -> new ApiValidateException("El numero de solicitud no existe"));
    List<DetalleSolicitudLibroBean> libros = solicitudDao.listarLibrosFullBySolicitud(solicitudBean.getIdSolicitud());
    OficinaBean oficinaBean = oficinaDao.obtener(solicitudBean.getCodigoOrec())
        .orElseThrow(() -> new ApiValidateException("La oficina no existe"));
    ArchivoBean archivoSustento = archivoDao.obtener(solicitudBean.getIdArchivoSustento())
        .orElseThrow(() -> new ApiValidateException(ArchivoConstant.MSG_ARCHIVO_NO_EXISTE));

    DetalleSolLibroResponse detSolResp = new DetalleSolLibroResponse();
    detSolResp.setIdSolicitud(solicitudBean.getIdSolicitud());
    detSolResp.setCodigoOrec(solicitudBean.getCodigoOrec());
    detSolResp.setDescripcionOrecLarga(solicitudBean.getDescripcionOrecLarga());
    detSolResp.setUbigeo(String.format("%s-%s-%s",
        oficinaBean.getNombreDepartamento(),
        oficinaBean.getNombreProvincia(),
        oficinaBean.getNombreDistrito()));
    detSolResp.setArchivoSustento(ArchivoResponse.builder()
        .tipoArchivo(solicitudBean.getTipoArchivoSustento().getNombreArchivo())
        .codigo(archivoSustento.getCodigoNombre())
        .nombreOriginal(String.format("%s.%s",
            archivoSustento.getNombreOriginal(),
            archivoSustento.getExtension()))
        .build());
    libros.forEach(libro -> detSolResp.getDetalleSolicitudLibro().add(
        new DetalleSolDetLibroResponse(
                libro.getIdDetalleSolLibro(),
                libro.getArticuloBean().getCodigo(),
            libro.getArticuloBean().getDescripcion(),
            libro.getLenguaBean().getCodigo(),
                libro.getLenguaBean().getDescripcion(),
            libro.getCantidad(),
            libro.getNumeroUltimaActa()
        )));
    return detSolResp;
  }

  @Override
  public SolicitudLibroResponse consultarSolicitudAtencion(String nroSolicitud) {
    SolicitudBean solicitudBean = solicitudDao.obtenerPorNumero(nroSolicitud)
        .orElseThrow(() -> new ApiValidateException("El numero de solicitud no existe"));
    List<DetalleSolicitudLibroBean> libros = solicitudDao.listarLibrosFullBySolicitud(solicitudBean.getIdSolicitud());
    OficinaBean oficinaBean = oficinaDao.obtener(solicitudBean.getCodigoOrec())
        .orElseThrow(() -> new ApiValidateException("La oficina no existe"));

    SolicitudLibroResponse solResponse = new SolicitudLibroResponse();
    solResponse.setCodigoOrec(oficinaBean.getCodigoOrec());
    solResponse.setDescripcionOrecLarga(oficinaBean.getDescripcionLocalLarga());
    solResponse.setUbigeo(String.format("%s-%s-%s",
        oficinaBean.getNombreDepartamento(),
        oficinaBean.getNombreProvincia(),
        oficinaBean.getNombreDistrito()));

    libros.forEach(libro -> solResponse.getDetalleSolicitudLibro().add(
        new SolDetalleLibroResponse(
            libro.getIdDetalleSolLibro(),
            libro.getCodigoArticulo(),
            libro.getCodigoLengua(),
            libro.getCantidad(),
            libro.getNumeroUltimaActa()
        )));
    return solResponse;
  }

  @Override
  @Transactional
  public Boolean atenderSolicitud(AtencionSolLibroRequest request) {
    SolicitudBean solicitudBean = solicitudDao.obtenerPorNumero(request.getNumeroSolicitud())
        .orElseThrow(() -> new ApiValidateException(SolicitudConstant.SOLICITUD_NO_EXISTE));

    if (!solicitudBean.getCodigoEstado().equals(SolicitudConstant.ESTADO_ASIGNADO)) {
      throw new ApiValidateException("La solicitud debe estar asignada");
    }

    Long idArchivo = archivoService.getIdByCodigo(request.getArchivoRespuesta().getCodigoNombre());
    solicitudDao.actualizarArchivoRespuesta(solicitudBean.getIdSolicitud(),
        request.getCodigoTipoArchivoRespuesta(),
        idArchivo);
    archivoDao.actualizarEstado(idArchivo, ArchivoConstant.ESTADO_ASIGNADO);
    solicitudDao.actualizarEstadoSolicitud(solicitudBean.getIdSolicitud(),
        SolicitudConstant.ESTADO_ATENTIDO);
    solicitudDao.actualizarEstadoDetalleSolLibroBySol(solicitudBean.getIdSolicitud(), SolicitudConstant.INACTIVO);
    UserInfo userInfo = seguridadService.getUserAuth();
    request.getDetalleSolicitud().forEach(detalle -> {
      DetalleSolicitudLibroBean detalleLibro = DetalleSolicitudLibroBean.builder()
          .idSolicitud(solicitudBean.getIdSolicitud())
          .cantidad(detalle.getCantidad())
          .codigoArticulo(detalle.getCodigoArticulo())
          .codigoLengua(detalle.getCodigoLengua())
          .numeroUltimaActa(detalle.getNumeroUltimaActa())
          .idCrea(userInfo.getDni())
          .build();
      solicitudDao.registrarDetalleLibro(detalleLibro);
    });

    return Boolean.TRUE;
  }

  @Override
  public DetalleSolFirmaResponse consultarSolicitudFirma(String nroSolicitud) {
    SolicitudBean solicitudBean = solicitudDao.obtenerPorNumero(nroSolicitud)
        .orElseThrow(() -> new ApiValidateException("El numero de solicitud no existe"));
    List<DetalleSolicitudFirmaBean> firmas = solicitudDao.listarByIdSolicitud(solicitudBean.getIdSolicitud());
    OficinaBean oficinaBean = oficinaDao.obtener(solicitudBean.getCodigoOrec())
        .orElseThrow(() -> new ApiValidateException("La oficina no existe"));
    ArchivoBean archivoSustento = archivoDao.obtener(solicitudBean.getIdArchivoSustento())
        .orElseThrow(() -> new ApiValidateException(ArchivoConstant.MSG_ARCHIVO_NO_EXISTE));

    DetalleSolFirmaResponse detSolResp = new DetalleSolFirmaResponse();

    detSolResp.setIdSolicitud(solicitudBean.getIdSolicitud());
    detSolResp.setCodigoOrec(solicitudBean.getCodigoOrec());
    detSolResp.setDescripcionOrecLarga(solicitudBean.getDescripcionOrecLarga());
    detSolResp.setUbigeo(String.format("%s-%s-%s",
        oficinaBean.getNombreDepartamento(),
        oficinaBean.getNombreProvincia(),
        oficinaBean.getNombreDistrito()));
    detSolResp.setArchivoSustento(ArchivoResponse.builder()
            .idTipoArchivo(solicitudBean.getTipoArchivoSustento().getIdTipoArchivo())
        .tipoArchivo(solicitudBean.getTipoArchivoSustento().getNombreArchivo())
        .codigo(archivoSustento.getCodigoNombre())
        .nombreOriginal(String.format("%s.%s",
            archivoSustento.getNombreOriginal(),
            archivoSustento.getExtension()))
        .build());

    firmas.forEach(firma -> {

      List<DetalleSolicitudArchivoFirmaBean> detalleArchivos =
          solicitudDao.listarArchivoFirmaByDetalleId(firma.getIdDetalleSolicitud())
              .stream()
              .filter(archivo -> archivo.getCodigoUsoArchivo().equals(SolicitudConstant.USO_ARCH_SUSTENTO))
              .collect(Collectors.toList());

      detSolResp.getDetalleSolicitudFirma().add(
          DetalleSolDetFirmaResponse.builder()
               .idDetalleSolicitud(firma.getIdDetalleSolicitud())
              .idTipoSolicitud(firma.getIdTipoSolicitud())
              .numeroDocumento(firma.getNumeroDocumento())
              .primerApellido(firma.getPrimerApellido())
              .segundoApellido(firma.getSegundoApellido())
              .preNombres(firma.getPreNombres())
              .celular(firma.getCelular())
              .email(firma.getEmail())
              .archivos(this.mapperArchivosLibro(detalleArchivos))
              .tipoSolicitud(firma.getTipoSolicitud().getDescripcion())
              .codigoEstadoFirma(firma.getCodigoEstadoFirma())
              .build());
    });
    return detSolResp;
  }

  private List<ArchivoResponse> mapperArchivosLibro(List<DetalleSolicitudArchivoFirmaBean> detalleArchivos) {
    List<ArchivoResponse> archivos = new ArrayList<>();
    detalleArchivos.forEach(archivo ->
        archivos.add(ArchivoResponse.builder()
                        .idTipoArchivo(archivo.getTipoArchivo().getIdTipoArchivo())
            .tipoArchivo(archivo.getTipoArchivo().getNombreArchivo())
            .nombreOriginal(String.format("%s.%s",
                archivo.getArchivo().getNombreOriginal(),
                archivo.getArchivo().getExtension()))
            .codigo(archivo.getArchivo().getCodigoNombre())
            .build()));
    return archivos;
  }

  private SolicitudBean buildSolicitudBean(GestionConsultaSolRequest request) {
    return SolicitudBean.builder()
        .numeroSolicitud(request.getNumeroSolicitud())
        .idTipoRegistro(request.getCodigoTipoRegistro())
        .codigoEstado(request.getCodigoEstado())
        .codigoOrec(request.getCodigoOrec())
        .codigoDepartamentoOrec(request.getCodigoDepartamento())
        .codigoDistritoOrec(request.getCodigoDistrito())
        .codigoProvinciaOrec(request.getCodigoProvincia())
        .codigoCentroPobladoOrec(request.getCodigoCentroPoblado())
        .codigoAnalistaAsignado(request.getCodigoAnalistaAsignado())
        .build();
  }

  private GestionConsultaSolResponse mapper(SolicitudBean bean) {
    GestionConsultaSolResponse response = new GestionConsultaSolResponse();
    response.setNumeroSolicitud(bean.getNumeroSolicitud());
    response.setFechaSolicitud(bean.getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    response.setTipoRegistro(bean.getTipoRegistro().getDescripcion());
    response.setEstadoSolicitud(bean.getSolicitudEstado().getDescripcion());
    if (Objects.nonNull(bean.getFechaRecepcion())) {
      response.setFechaRecepcion(bean.getFechaRecepcion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    if (Objects.nonNull(bean.getFechaAsignacion())) {
      response.setFechaAsignacion(bean.getFechaAsignacion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    if (Objects.nonNull(bean.getFechaAtencion())) {
      response.setFechaAtencion(bean.getFechaAtencion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    response.setOficinaAutorizada(bean.getDescripcionOrecLarga());
    if (Objects.nonNull(bean.getAnalistaAsignado().getPreNombres())) {
      response.setAnalistaAsignado(bean.getAnalistaAsignado().getPreNombres());
    }
    if (Objects.nonNull(bean.getAnalistaAsignado().getPrimerApellido())) {
      response.setAnalistaAsignado(String.format("%s %s",
          response.getAnalistaAsignado(),
          bean.getAnalistaAsignado().getPrimerApellido()));
    }
    if (Objects.nonNull(bean.getAnalistaAsignado().getSegundoApellido())) {
      response.setAnalistaAsignado(String.format("%s %s",
          response.getAnalistaAsignado(),
          bean.getAnalistaAsignado().getSegundoApellido()));
    }

    return response;
  }

  @Override
  public ApiPageResponse<ExpedienteConsultaResponse> consultarExpediente(ExpedienteConsultaRequest request, int page, int size) {

    SolicitudBean solicitudBean = SolicitudBean.builder()
            .numeroSolicitud(request.getNumeroSolicitud())
            .idTipoRegistro(request.getCodigoTipoRegistro())
            .codigoEstado(request.getCodigoEstado())
            .codigoOrec(request.getCodigoOrec())
            .codigoDepartamentoOrec(request.getCodigoDepartamento())
            .codigoDistritoOrec(request.getCodigoDistrito())
            .codigoProvinciaOrec(request.getCodigoProvincia())
            .codigoCentroPobladoOrec(request.getCodigoCentroPoblado())
            .codigoAnalistaAsignado(request.getCodigoAnalistaAsignado())
            .build();

    Page<SolicitudBean> solicitudes = solicitudDao.consultarExpediente(
            solicitudBean,
            PageRequest.of(page - 1, size),
            request.getFechaIni(),
            request.getFechaFin());

    ApiPageResponse<ExpedienteConsultaResponse> response = new ApiPageResponse<>();
    response.setCode(ConstantUtil.OK_CODE);
    response.setMessage(ConstantUtil.OK_MESSAGE);
    response.setData(solicitudes.getContent()
            .stream().map(this::mapSolicitudToExpResponse)
            .collect(Collectors.toList()));
    response.setPage(solicitudes.getNumber());
    response.setSize(solicitudes.getSize());
    response.setTotalPage(solicitudes.getTotalPages());
    response.setTotalElements(solicitudes.getTotalElements());
    response.setNumberOfElements(solicitudes.getNumberOfElements());
    return response;
  }
  private ExpedienteConsultaResponse mapSolicitudToExpResponse(SolicitudBean bean) {
    ExpedienteConsultaResponse response = new ExpedienteConsultaResponse();
    response.setNumeroSolicitud(bean.getNumeroSolicitud());
    response.setFechaSolicitud(bean.getFechaSolicitud().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    response.setTipoRegistro(bean.getTipoRegistro().getDescripcion());
    response.setEstadoSolicitud(bean.getSolicitudEstado().getDescripcion());
    if (Objects.nonNull(bean.getFechaRecepcion())) {
      response.setFechaRecepcion(bean.getFechaRecepcion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    if (Objects.nonNull(bean.getFechaAsignacion())) {
      response.setFechaAsignacion(bean.getFechaAsignacion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    if (Objects.nonNull(bean.getFechaAtencion())) {
      response.setFechaAtencion(bean.getFechaAtencion().format(DateTimeFormatter.ofPattern(ConstantUtil.DATE_FORMAT)));
    }
    response.setOficinaAutorizada(bean.getDescripcionOrecLarga());
    if (Objects.nonNull(bean.getAnalistaAsignado().getPreNombres())) {
      response.setAnalistaAsignado(bean.getAnalistaAsignado().getPreNombres());
    }
    if (Objects.nonNull(bean.getAnalistaAsignado().getPrimerApellido())) {
      response.setAnalistaAsignado(String.format("%s %s",
              response.getAnalistaAsignado(),
              bean.getAnalistaAsignado().getPrimerApellido()));
    }
    if (Objects.nonNull(bean.getAnalistaAsignado().getSegundoApellido())) {
      response.setAnalistaAsignado(String.format("%s %s",
              response.getAnalistaAsignado(),
              bean.getAnalistaAsignado().getSegundoApellido()));
    }

    return response;
  }

  @Override
  @Transactional
  public Boolean eliminarSolicitud(EliminarSolicitudRequest request) {
    SolicitudBean solicitudBean = solicitudDao.obtenerPorNumero(request.getNumeroSolicitud())
            .orElseThrow(() -> new ApiValidateException(SolicitudConstant.SOLICITUD_NO_EXISTE));

    if (!solicitudBean.getCodigoEstado().equals(SolicitudConstant.ESTADO_REGISTRADO)) {
      throw new ApiValidateException("La solicitud debe estar en estado registrado.");
    }

    solicitudDao.actualizarEstadoActivo(solicitudBean.getIdSolicitud(), SolicitudConstant.INACTIVO);

    return Boolean.TRUE;
  }

  @Override
  @Transactional
  public String actualizarSolFirma(UpdateSolFirmaRequest request) {
    UserInfo userInfo = (UserInfo) SecurityUtil.getAuthentication().getPrincipal();

    SolicitudBean solicitudBean = SolicitudBean
            .builder()
            .idSolicitud(request.getIdSolicitud())
            .idArchivoSustento(archivoService.getIdByCodigo(request.getIdArchivoSustento()))
            .codigoTipoArchivoSustento(request.getIdTipoArchivoSustento())
            .build();

    solicitudDao.actualizarSolicitud(solicitudBean);

    request.getListaDetalleFirma().forEach(detalle -> {
        solicitudDao.actualizarDetalleSolFirma(detalle);

        detalle.getDetalleArchivo().forEach( archivo -> {

          archivo.setIdArchivo(archivoService.getIdByCodigo(archivo.getCodigoNombre()));
          archivo.setIdDetalleSolicitud(detalle.getIdDetalleSolicitud());
          archivo.setIdCrea(userInfo.getDni());
          archivo.setCodigoUsoArchivo(SolicitudConstant.USO_ARCH_SUSTENTO);

          if("D".equals(archivo.getOperacion())){
            solicitudDao.actualizarEstadoDetArchivoFirma(archivo.getIdDetalleSolicitud(), archivo.getIdArchivo(), "0");
          } else if ("R".equals(archivo.getOperacion())) {
            solicitudDao.registrarDetalleArchivoFirma(archivo);
            archivoDao.actualizarEstado(archivo.getIdArchivo(), ArchivoConstant.ESTADO_ASIGNADO);
          }
        } );

    });
    return String.format("Se actualizó la solicitud.");
  }

  @Override
  @Transactional
  public String actualizarSolLibro(UpdateSolFirmaRequest request) {
    UserInfo userInfo = (UserInfo) SecurityUtil.getAuthentication().getPrincipal();

    SolicitudBean solicitudBean = SolicitudBean
            .builder()
            .idSolicitud(request.getIdSolicitud())
            .idArchivoSustento(archivoService.getIdByCodigo(request.getIdArchivoSustento()))
            .codigoTipoArchivoSustento(request.getIdTipoArchivoSustento())
            .build();

    solicitudDao.actualizarSolicitud(solicitudBean);

    request.getListaDetalleLibro().forEach( detalle -> {
      solicitudDao.actualizarDetalleSolLibro(detalle);
    });

    return String.format("Se actualizó la solicitud.");
  }

}
