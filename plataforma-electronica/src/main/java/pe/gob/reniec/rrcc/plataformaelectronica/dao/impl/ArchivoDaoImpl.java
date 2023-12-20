package pe.gob.reniec.rrcc.plataformaelectronica.dao.impl;

import java.sql.PreparedStatement;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.ArchivoDao;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArchivoBean;
import pe.gob.reniec.rrcc.plataformaelectronica.security.SecurityUtil;

@Repository
@AllArgsConstructor
public class ArchivoDaoImpl implements ArchivoDao {

  private JdbcTemplate jdbcTemplate;

  @Override
  public void registrar(ArchivoBean archivo) {
    String sql = "INSERT INTO IDO_PLATAFORMA_EXPE.EDTR_ARCHIVO(ID_ARCHIVO,CO_NOMBRE," +
        "DE_NOMBRE_ORIGINAL, CO_EXTENSION, IM_ARCHIVO, CO_ESTADO, FE_CREA, ID_CREA) \n" +
        "VALUES(IDO_PLATAFORMA_EXPE.EDSE_ARCHIVO.nextval,?,?,?,?,?,SYSDATE, ?)";
    KeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(connection -> {
          PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID_ARCHIVO"});
          ps.setString(1, archivo.getCodigoNombre());
          ps.setString(2, archivo.getNombreOriginal());
          ps.setString(3, archivo.getExtension());
          ps.setBytes(4, archivo.getArchivo());
          ps.setString(5, archivo.getEstado());
          ps.setString(6, archivo.getIdCrea());
          return ps;
        },
        keyHolder);
    archivo.setId(keyHolder.getKey().longValue());
  }

  @Override
  public Optional<ArchivoBean> obtener(Long id) {
    String sql = "SELECT ID_ARCHIVO AS id,CO_NOMBRE as codigoNombre,DE_NOMBRE_ORIGINAL as nombreOriginal, " +
        " CO_EXTENSION extension, CO_ESTADO as estado, IM_ARCHIVO as archivo " +
        " FROM IDO_PLATAFORMA_EXPE.EDTR_ARCHIVO WHERE ID_ARCHIVO = ?";
    try {
      return Optional.of(jdbcTemplate.queryForObject(sql,
          BeanPropertyRowMapper.newInstance(ArchivoBean.class),
          id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public Optional<ArchivoBean> obtenerPorCodigo(String codigo) {
    String sql = "SELECT ID_ARCHIVO AS id,CO_NOMBRE as codigoNombre,DE_NOMBRE_ORIGINAL as nombreOriginal, " +
        " CO_EXTENSION extension, CO_ESTADO as estado, IM_ARCHIVO as archivo " +
        " FROM IDO_PLATAFORMA_EXPE.EDTR_ARCHIVO WHERE CO_NOMBRE = ?";
    try {
      return Optional.of(jdbcTemplate.queryForObject(sql,
          BeanPropertyRowMapper.newInstance(ArchivoBean.class),
          codigo));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public void actualizarEstado(Long id, String estado) {
    String sql = "UPDATE IDO_PLATAFORMA_EXPE.EDTR_ARCHIVO SET CO_ESTADO = ?, ID_ACTUALIZA = ?, FE_ACTUALIZA = SYSDATE " +
        " WHERE ID_ARCHIVO = ?";
    jdbcTemplate.update(sql, estado, SecurityUtil.getUserInfo().getDni(), id);
  }
}
