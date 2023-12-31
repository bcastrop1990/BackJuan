package pe.gob.reniec.rrcc.plataformaelectronica.controller;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.gob.reniec.rrcc.plataformaelectronica.controller.mapper.RegistroFirmaMapper;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.BusqPorDatosRegCivilRuipinRequest;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.SolicitudRegFirmaRequest;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.ValidarDatosRegFirmaRequest;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.ApiResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.BusqPersonaResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.BusqRegCivilRuipinResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.TipoSolicitudRegFirmaResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.service.RegistroFirmaService;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.ConstantUtil;

@RestController
@RequestMapping("registro-firmas")
@AllArgsConstructor
public class RegistroFirmaController {

  private RegistroFirmaService registroFirmaService;
  private RegistroFirmaMapper registroFirmaMapper;

  @PostMapping("")
  public ResponseEntity<ApiResponse<String>> registrar(@Valid @RequestBody SolicitudRegFirmaRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(registroFirmaService
                .registrar(registroFirmaMapper
                    .RegFirmaReqToSolBean(request)))
            .build()
    );
  }

  @PostMapping("validar-datos")
  public ResponseEntity<ApiResponse<String>> validarDatos(@Valid @RequestBody ValidarDatosRegFirmaRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(registroFirmaService.validarDatos(request))
            .build()
    );
  }

  @GetMapping("tipo-solicitud")
  public ResponseEntity<ApiResponse<List<TipoSolicitudRegFirmaResponse>>> listarTipoSolicitud() {
    return ResponseEntity.ok(
        ApiResponse.<List<TipoSolicitudRegFirmaResponse>>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(registroFirmaService
                .listarTipoSolicitud()
                .stream()
                .map(registroFirmaMapper::tipoSolBeanToTipoSolResponse)
                .collect(Collectors.toList()))
            .build()
    );
  }

  @GetMapping("consultar-personas")
  public ResponseEntity<ApiResponse<BusqPersonaResponse>> consultarPersonaPorDni(@RequestParam("dni") String dni) {
    return ResponseEntity.ok(
            ApiResponse.<BusqPersonaResponse>builder()
                    .code(ConstantUtil.OK_CODE)
                    .message(ConstantUtil.OK_MESSAGE)
                    .data(registroFirmaMapper.personaBeanToBusqPersonaResponse(
                            registroFirmaService.consultarPersonaPorDni(dni)
                    )).build()
    );
  }
  @PostMapping("consultar-por-datos-ruipin")
  public ResponseEntity<BusqRegCivilRuipinResponse> consultarPorDatosRuipin(@RequestBody BusqPorDatosRegCivilRuipinRequest request) {

    return ResponseEntity.ok(registroFirmaService.consultarRegCivilPorDatosRuipin(request));
  }
}
