package pe.gob.reniec.rrcc.plataformaelectronica.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudArchivoFirmaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudLibroBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudFirmaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.SolicitudBean;

public interface SolicitudDao {
  void registrar(SolicitudBean solicitud);
  void registrarDetalleFirma(DetalleSolicitudFirmaBean detalle);
  void registrarDetalleLibro(DetalleSolicitudLibroBean detalle);
  void registrarDetalleArchivoFirma(DetalleSolicitudArchivoFirmaBean archivo);
  Optional<SolicitudBean> obtenerPorNumero(String numero);
  Optional<SolicitudBean> obtenerSolFirmaByDniReg(String dni);
  List<DetalleSolicitudFirmaBean> listarByIdSolicitud(Long idSolicitud);
  List<DetalleSolicitudFirmaBean> listarByDni(String dni);
  Page<SolicitudBean> consultarSeguimiento(String nroSolicitud,Pageable pageable, String dni, String fechaIni, String fechaFin);
  Page<SolicitudBean> consultar(SolicitudBean bean, Pageable pageable, String fechaIni, String fechaFin);
  Page<SolicitudBean> consultarExpediente(SolicitudBean bean, Pageable pageable, String fechaIni, String fechaFin);
  void recepcionar(String nroSolicitud, String codigoEstado, String codigoUsuario);
  void asignarAnalista(String nroSolicitud, String codigoAnalista, String codigoEstado);
  List<DetalleSolicitudLibroBean> listarLibrosFullBySolicitud(Long idSolicitud);
  void actualizarArchivoRespuesta(Long idSolicitud, String codigoTipoArchivo, Long idArchivoSustento);
  void actualizarEstadoDetalleSolLibroBySol(Long idSolicitud, String estado);
  void actualizarEstadoSolicitud(Long idSolicitud, String estado);
  List<DetalleSolicitudArchivoFirmaBean> listarArchivoFirmaByDetalleId(Long idDetalle);
  void actualizarEstadoFirmaDetSolById(Long idDetalleSolicitud, String codEstadoFirma);
  void actualizarEstadoActivo(Long idSolicitud, String estado);
  void actualizarSolicitud(SolicitudBean solicitud);
  void actualizarDetalleSolFirma(DetalleSolicitudFirmaBean detalle);
  void actualizarEstadoDetArchivoFirma(Long idDetalleSol, Long idArchivo, String estado);
  void actualizarDetalleSolLibro(DetalleSolicitudLibroBean detalle);

}
