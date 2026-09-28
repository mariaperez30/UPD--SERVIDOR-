package com.maria.adaptadores.red.mapper;

import com.maria.adaptadores.red.response.UdpResponse;
import com.maria.dominio.excepciones.RespuestaInvalidaException;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.modelos.ResultadoConversion;
import java.util.Objects;

public final class UdpNetworkMapper {

  public UdpResponse toNetworkResponse(final RespuestaCliente respuesta) {
    if (Objects.isNull(respuesta)) {
      throw new RespuestaInvalidaException("No se puede serializar una respuesta nula.");
    }

    final String payload = switch (respuesta.getTipo()) {
      case CONECTADO -> "CONECTADO_OK;" + respuesta.getMensaje();
      case DESCONECTADO -> "DESCONECTADO_OK;" + respuesta.getMensaje();
      case OK_CONVERSION -> buildPayloadConversion(respuesta.getResultado());
      case ERROR ->
        "ERROR;"
            + (Objects.nonNull(respuesta.getMensaje())
                ? respuesta.getMensaje()
                : "Error desconocido.");
    };

    return new UdpResponse(
        payload, respuesta.getDestinatario().ip(), respuesta.getDestinatario().puerto());
  }

  private static String buildPayloadConversion(final ResultadoConversion resultado) {
    if (Objects.isNull(resultado)) {
      throw new RespuestaInvalidaException("No se puede serializar una conversión sin resultado.");
    }
    return String.format(
        "OK_CONVERSION;%s;%s;%s",
        resultado.getPesoKgFormateado(), resultado.getPesoLibrasFormateado(), "Conversión realizada exitosamente");
  }
}
