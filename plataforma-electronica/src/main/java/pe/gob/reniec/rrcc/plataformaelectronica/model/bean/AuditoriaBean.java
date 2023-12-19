package pe.gob.reniec.rrcc.plataformaelectronica.model.bean;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AuditoriaBean {
    private Long id;
    private String usuario;
    private String tipoDoc;
    private String nuDoc;
    private String nombres;
    private String apellidoMaterno;
    private String apellidoPaterno;
}
