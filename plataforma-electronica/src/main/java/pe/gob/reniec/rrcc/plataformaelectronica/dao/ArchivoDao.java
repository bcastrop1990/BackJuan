package pe.gob.reniec.rrcc.plataformaelectronica.dao;

import java.util.Optional;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArchivoBean;

public interface ArchivoDao {
  void registrar(ArchivoBean archivo);
  Optional<ArchivoBean> obtener(Long id);
  Optional<ArchivoBean> obtenerPorCodigo(String nombre);
  void actualizarEstado(Long id, String estado);
}
