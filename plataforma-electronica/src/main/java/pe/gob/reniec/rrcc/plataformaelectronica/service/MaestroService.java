package pe.gob.reniec.rrcc.plataformaelectronica.service;

import java.util.List;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.AnalistaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArticuloBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.LenguaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.SolicitudEstadoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.TipoArchivoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.TipoRegistroBean;

public interface MaestroService {
  List<TipoArchivoBean> listarTipoArchivo(String idTipoUso);
  List<ArticuloBean> listarArticulos();
  List<LenguaBean> listarLenguas();
  List<AnalistaBean> listarAnalistas();
  List<TipoRegistroBean> listarTipoRegistro();
  List<SolicitudEstadoBean> listarSolicitudEstado();
}
