package pe.gob.reniec.rrcc.plataformaelectronica.service.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.AnalistaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.ArticuloDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.LenguaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.SolicitudEstadoDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.TipoArchivoDao;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.TipoRegistroDao;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.AnalistaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArticuloBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.LenguaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.SolicitudEstadoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.TipoArchivoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.TipoRegistroBean;
import pe.gob.reniec.rrcc.plataformaelectronica.service.MaestroService;

@Service
@AllArgsConstructor
public class MaestroServiceImpl implements MaestroService {
  private TipoArchivoDao tipoArchivoDao;
  private ArticuloDao articuloDao;
  private LenguaDao lenguaDao;
  private AnalistaDao analistaDao;
  private TipoRegistroDao tipoRegistroDao;
  private SolicitudEstadoDao solicitudEstadoDao;

  @Override
  public List<TipoArchivoBean> listarTipoArchivo(String idTipoUso) {
    return tipoArchivoDao.listarPorTipoUso(idTipoUso);
  }

  @Override
  public List<ArticuloBean> listarArticulos() {
    return articuloDao.listar();
  }

  @Override
  public List<LenguaBean> listarLenguas() {
    return lenguaDao.listar();
  }

  @Override
  public List<AnalistaBean> listarAnalistas() {
    return analistaDao.listar();
  }

  @Override
  public List<TipoRegistroBean> listarTipoRegistro() {
    return tipoRegistroDao.listar();
  }

  @Override
  public List<SolicitudEstadoBean> listarSolicitudEstado() {
    return solicitudEstadoDao.listar();
  }
}
