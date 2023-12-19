package pe.gob.reniec.rrcc.plataformaelectronica.service;

import org.springframework.web.multipart.MultipartFile;
import pe.gob.reniec.rrcc.plataformaelectronica.model.bean.ArchivoBean;

public interface ArchivoService {
    String upload(MultipartFile file);
    void delete(String codigo);
    ArchivoBean download(String codigo);
    Long getIdByCodigo(String codigo);
}
