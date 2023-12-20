package pe.gob.reniec.rrcc.plataformaelectronica.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantUtil {
  public static final String OK_CODE = "000";
  public static final String OK_MESSAGE = "Operación realizada con éxito.";
  public static final String ERROR_MESSAGE = "Ocurrió un error al procesar la operación.";
  public static final String ERROR_CODE = "999";
  public static final String MSG_PERSONA_NO_ENCONTRADA = "Datos no corresponde al DNI.";
  public static final String MSG_OREC_NO_EXISTE = "La oficina OREC no existe.";
  public static final String MSG_REGISTRADOR_CIVIL_NO_VALIDO = "Registrado civil no válido.";
  public static final String MSG_DATOS_INVALIDO = "Datos ingresados no son válidos.";
  public static final String DATE_FORMAT = "dd/MM/yyyy";
  public static final String MSG_ACCESS_DENIED = "No tiene permisos para realizar esta operación";
  public static final String ROLE_ATENDER = "hasRole('ROLE_ATENDER')";
  public static final String ROLE_RECEPCIONAR = "hasRole('ROLE_RECEPCIONAR')";
  public static final String ROLE_ASIGNAR = "hasRole('ROLE_ASIGNAR')";
    public static final String MSG_FORMATO_DNI_INCORRECTO = "El dni no tiene el formato correcto.";
  public static final int INT_MAX_DIG_DNI = 8;
  public static final String MSG_CAMPO_REQUERIDO = "El campo %s es requerido.";
  public static final String MSG_ERROR_PARSE_FECHA = "Error al convertir la fecha";
  public static final String DATE_FORMAT_BASIC = "yyyy-MM-dd";
  public static final String TEXT_DNI = "DNI";
  public static final String COD_ESTADO_ACTIVO = "1";
  public static final String COD_ESTADO_INACTIVO = "0";
}
