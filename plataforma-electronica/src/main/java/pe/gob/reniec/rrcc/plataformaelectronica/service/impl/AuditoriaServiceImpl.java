package pe.gob.reniec.rrcc.plataformaelectronica.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pe.gob.reniec.rrcc.plataformaelectronica.dao.AuditoriaDao;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.AuditoriaBean;
import pe.gob.reniec.rrcc.plataformaelectronica.service.AuditoriaService;
@Service
@AllArgsConstructor
@Slf4j
public class AuditoriaServiceImpl implements AuditoriaService {

    private AuditoriaDao auditoriaDao;
    @Override
    public void registrarAuditoria(AuditoriaBean auditoria) {
        auditoriaDao.registrarAuditoria(auditoria);
    }
}
