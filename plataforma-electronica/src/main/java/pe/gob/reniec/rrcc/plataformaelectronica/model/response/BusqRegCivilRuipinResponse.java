package pe.gob.reniec.rrcc.plataformaelectronica.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BusqRegCivilRuipinResponse {
    private String dni;
    private String primerApellido;
    private String segundoApellido;
    private String preNombres;
}
