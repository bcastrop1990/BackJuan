package pe.gob.reniec.rrcc.plataformaelectronica.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EliminarSolicitudRequest {
    @NotNull(message = "El campo numeroSolicitud es requerido.")
    private String numeroSolicitud;
}
