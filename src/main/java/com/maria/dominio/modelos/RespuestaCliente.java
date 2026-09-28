package com.maria.dominio.modelos;

import com.maria.dominio.enums.TipoRespuesta;
import com.maria.dominio.vo.Destinatario;
import java.util.Objects;

/**
 * Representa la respuesta de dominio generada por el servidor para ser enviada a un cliente.
 */
public final class RespuestaCliente {

  private final TipoRespuesta tipo;
  private final Destinatario destinatario;
  private final String mensaje;
  private final ResultadoConversion resultado;

  private RespuestaCliente(
      final TipoRespuesta tipo,
      final Destinatario destinatario,
      final String mensaje,
      final ResultadoConversion resultado) {
    this.tipo = Objects.requireNonNull(tipo, "El tipo de respuesta es obligatorio.");
    this.destinatario = Objects.requireNonNull(destinatario, "El destinatario es obligatorio.");
    this.mensaje = mensaje;
    this.resultado = resultado;
  }

  public static RespuestaCliente conectado(final Destinatario destinatario, final String mensaje) {
    return new RespuestaCliente(TipoRespuesta.CONECTADO, destinatario, mensaje, null);
  }

  public static RespuestaCliente desconectado(final Destinatario destinatario, final String mensaje) {
    return new RespuestaCliente(TipoRespuesta.DESCONECTADO, destinatario, mensaje, null);
  }

  public static RespuestaCliente conversionExitosa(
      final Destinatario destinatario, final ResultadoConversion resultado) {
    return new RespuestaCliente(TipoRespuesta.OK_CONVERSION, destinatario, null, resultado);
  }

  public static RespuestaCliente error(final Destinatario destinatario, final String mensaje) {
    return new RespuestaCliente(TipoRespuesta.ERROR, destinatario, mensaje, null);
  }

  public TipoRespuesta getTipo() {
    return tipo;
  }

  public Destinatario getDestinatario() {
    return destinatario;
  }

  public String getMensaje() {
    return mensaje;
  }

  public ResultadoConversion getResultado() {
    return resultado;
  }
}
