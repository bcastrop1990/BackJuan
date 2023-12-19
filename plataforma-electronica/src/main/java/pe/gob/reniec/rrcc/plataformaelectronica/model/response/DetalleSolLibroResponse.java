package pe.gob.reniec.rrcc.plataformaelectronica.model.response;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleSolLibroResponse {
  private Long idSolicitud;
  private String codigoOrec;
  private String descripcionOrecLarga;
  private String ubigeo;
  private ArchivoResponse archivoSustento;
  private List<DetalleSolDetLibroResponse> detalleSolicitudLibro = new ArrayList<>();
}
