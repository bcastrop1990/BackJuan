package pe.gob.reniec.rrcc.plataformaelectronica.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetalleSolDetLibroResponse {
  private Long idDetalleSolLibro;
  private String codArticulo;
  private String articulo;
  private String codLengua;
  private String lengua;
  private int cantidad;
  private String numeroUltimaActa;
}
