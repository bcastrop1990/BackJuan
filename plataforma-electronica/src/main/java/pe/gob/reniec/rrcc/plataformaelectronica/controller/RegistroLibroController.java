package pe.gob.reniec.rrcc.plataformaelectronica.controller;

import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.reniec.rrcc.plataformaelectronica.controller.mapper.RegistroLibroMapper;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.SolicitudRegLibroRequest;
import pe.gob.reniec.rrcc.plataformaelectronica.model.request.ValidarDatosRegLibroRequest;
import pe.gob.reniec.rrcc.plataformaelectronica.model.response.ApiResponse;
import pe.gob.reniec.rrcc.plataformaelectronica.service.RegistroLibroService;
import pe.gob.reniec.rrcc.plataformaelectronica.utility.ConstantUtil;

@RestController
@RequestMapping("registro-libros")
@AllArgsConstructor
public class RegistroLibroController {

  private RegistroLibroService registroLibroService;
  private RegistroLibroMapper registroLibroMapper;

  @PostMapping("")
  public ResponseEntity<ApiResponse<String>> registrar(@Valid @RequestBody SolicitudRegLibroRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(registroLibroService
                .registrar(registroLibroMapper
                    .RegLibroReqToSolLibroBean(request)))
            .build()
    );
  }

  @PostMapping("validar-datos")
  public ResponseEntity<ApiResponse<String>> validarDatos(@Valid @RequestBody ValidarDatosRegLibroRequest request) {
    return ResponseEntity.ok(
        ApiResponse.<String>builder()
            .code(ConstantUtil.OK_CODE)
            .message(ConstantUtil.OK_MESSAGE)
            .data(registroLibroService.validarDatos(request))
            .build()
    );
  }
}
