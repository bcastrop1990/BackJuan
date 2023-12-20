package pe.gob.reniec.rrcc.plataformaelectronica.dao.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.AuditoriaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.AuditoriaBean;

import java.sql.PreparedStatement;

@Repository
@AllArgsConstructor
public class AuditoriaDaoImpl implements AuditoriaDao {
    private JdbcTemplate jdbcTemplate;
    @Override
    public void registrarAuditoria(AuditoriaBean auditoria) {
        String sql = "INSERT INTO IDO_PLATAFORMA_EXPE.EDTC_LOG_CONSULTA_DOC(ID_LOG, CO_USUARIO, CO_TIPO_DOC, " +
                "NU_DOC, PRE_NOMBRES, APELLIDO_PATERNO,APELLIDO_MATERNO, FE_CONSULTA) \n" +
                "VALUES(IDO_PLATAFORMA_EXPE.EDSE_LOG_CONSULTA_DOC.nextval,?,?,?,?,?, ?, SYSDATE)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(sql, new String[]{"ID_LOG"});
                    ps.setString(1, auditoria.getUsuario());
                    ps.setString(2,  auditoria.getTipoDoc());
                    ps.setString(3, auditoria.getNuDoc());
                    ps.setString(4, auditoria.getNombres());
                    ps.setString(5, auditoria.getApellidoPaterno());
                    ps.setString(6, auditoria.getApellidoMaterno());
                    return ps;
                },
                keyHolder);
        auditoria.setId(keyHolder.getKey().longValue());
    }
}
