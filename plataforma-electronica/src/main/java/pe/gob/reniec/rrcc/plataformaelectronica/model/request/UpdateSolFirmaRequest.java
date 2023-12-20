package pe.gob.reniec.rrcc.plataformaelectronica.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudFirmaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.DetalleSolicitudLibroBean;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UpdateSolFirmaRequest {
    Long idSolicitud;
    String idTipoArchivoSustento;
    String idArchivoSustento;
    List<DetalleSolicitudFirmaBean> listaDetalleFirma;
    List<DetalleSolicitudLibroBean> listaDetalleLibro;
}
