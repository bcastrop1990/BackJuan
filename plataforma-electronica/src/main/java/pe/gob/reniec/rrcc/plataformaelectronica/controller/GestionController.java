package pe.gob.reniec.rrcc.plataformaelectronica.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.reniec.rrcc.plataformaelectronica.controller.mapper.GestionMapper;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.*;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.*;
import pe.gob.reniec.rrcc.plataformaelectronica.service.GestionService;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.ConstantUtil;

@RestController
@RequestMapping("gestion")
@AllArgsConstructor
public class GestionController {

  private GestionService gestionService;
  private GestionMapper gestionMapper;

  @PostMapping("solicitudes/consultar")
  public ResponseEntity<ApiPageResponse<GestionConsultaSolResponse>> consultar(@Valid @RequestBody GestionConsultaSolRequest request,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(gestionService.consultaSolicitud(request, page, size));
  }

  @PreAuthorize(ConstantUtil.ROLE_RECEPCIONAR)
  @PostMapping("solicitudes/recepcionar")
  public ResponseEntity<ApiResponse<String>> recepcionarSolicitud(@Valid @RequestBody RecepcionSolicitudRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(gestionService.recepcionarSolicitud(request))
            .build());
  }

  @PreAuthorize(ConstantUtil.ROLE_ASIGNAR)
  @PostMapping("solicitudes/asignar")
  public ResponseEntity<ApiResponse<String>> asignarSolicitud(@Valid @RequestBody AsignacionSolicitudRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(gestionService.asignarSolicitud(request))
        .build());
  }

  @GetMapping("solicitudes/{nroSolicitud}/firma")
  public ResponseEntity<ApiResponse<DetalleSolFirmaResponse>> consultarSolicitudFirma(@PathVariable String nroSolicitud) {
    return ResponseEntity.ok(
        ApiResponse.<DetalleSolFirmaResponse>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(gestionService.consultarSolicitudFirma(nroSolicitud))
            .build());
  }

  @GetMapping("solicitudes/{nroSolicitud}/libro")
  public ResponseEntity<ApiResponse<DetalleSolLibroResponse>> consultarSolicitudLibro(@PathVariable String nroSolicitud) {
    return ResponseEntity.ok(
        ApiResponse.<DetalleSolLibroResponse>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(gestionService.consultarSolicitudLibro(nroSolicitud))
            .build());
  }
  @PreAuthorize(ConstantUtil.ROLE_ATENDER)
  @GetMapping("solicitudes/{nroSolicitud}/atencion")
  public ResponseEntity<ApiResponse<SolicitudLibroResponse>> consultarSolicitudAtencion(@PathVariable String nroSolicitud) {
    return ResponseEntity.ok(
            ApiResponse.<SolicitudLibroResponse>builder()
                    .code(ConstantUtil.OK_CODE)
                    .message(ConstantUtil.OK_MESSAGE)
                    .data(gestionService.consultarSolicitudAtencion(nroSolicitud))
                    .build());
  }
  @PreAuthorize(ConstantUtil.ROLE_ATENDER)
  @PostMapping("solicitudes/atender")
  public ResponseEntity<ApiResponse<Boolean>> atenderSolicitud(@Valid @RequestBody AtencionSolLibroRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<Boolean>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(gestionService.atenderSolicitud(request))
            .build());
  }

  @PostMapping("expedientes/consultar")
  public ResponseEntity<ApiPageResponse<ExpedienteConsultaResponse>> consultar(@Valid @RequestBody ExpedienteConsultaRequest request,
                                                                               @RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(gestionService.consultarExpediente(request, page, size));
  }

  @PostMapping("solicitudes/eliminar")
  public ResponseEntity<ApiResponse<Boolean>> eliminarSolicitud(@Valid @RequestBody EliminarSolicitudRequest request) {
    return ResponseEntity.ok(
            ApiResponse.<Boolean>builder()
                    .code(ConstantUtil.OK_CODE)
                    .message(ConstantUtil.OK_MESSAGE)
                    .data(gestionService.eliminarSolicitud(request))
                    .build());
  }

  @PostMapping("solicitudes/actualizarSolFirma")
  public ResponseEntity<ApiResponse<String>> actualizarSolFirma(@Valid @RequestBody UpdateSolFirmaRequest request) {
    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .code(ConstantUtil.OK_CODE)
                    .message(ConstantUtil.OK_MESSAGE)
                    .data(gestionService.actualizarSolFirma(request))
                    .build());
  }

  @PostMapping("solicitudes/actualizarSolLibro")
  public ResponseEntity<ApiResponse<String>> actualizarSolLibro(@Valid @RequestBody UpdateSolFirmaRequest request) {
    return ResponseEntity.ok(
            ApiResponse.<String>builder()
                    .code(ConstantUtil.OK_CODE)
                    .message(ConstantUtil.OK_MESSAGE)
                    .data(gestionService.actualizarSolLibro(request))
                    .build());
  }

}
