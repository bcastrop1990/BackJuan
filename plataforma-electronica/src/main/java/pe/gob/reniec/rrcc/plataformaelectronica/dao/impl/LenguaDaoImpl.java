package pe.gob.reniec.rrcc.plataformaelectronica.dao.impl;

import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.LenguaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.LenguaBean;

@Repository
@AllArgsConstructor
public class LenguaDaoImpl implements LenguaDao {
  private JdbcTemplate jdbcTemplate;
  private final String sql = "SELECT CO_LENGUA as codigo, DE_LENGUA as descripcion \n" +
      " FROM IDORRCC.RCTR_LENGUA WHERE ES_LENGUA = '1' AND TIPO_LENGUA = 'O' AND IN_ACTA = '1'";

  @Override
  public List<LenguaBean> listar() {
    return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(LenguaBean.class));
  }
}
