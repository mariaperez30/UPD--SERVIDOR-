package com.maria.adaptadores.red;

import com.maria.adaptadores.red.mapper.UdpNetworkMapper;
import com.maria.adaptadores.red.response.UdpResponse;
import com.maria.aplicacion.excepciones.ServidorRedException;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.puertos.salida.PuertoSalidaRed;
import java.util.Objects;

public final class AdaptadorSalidaUdp implements PuertoSalidaRed {

  private final CanalUdp canalUdp;
  private final UdpNetworkMapper mapper;
  private final AdaptadorNotificacionEvento notificador;

  public AdaptadorSalidaUdp(
      final CanalUdp canalUdp,
      final UdpNetworkMapper mapper,
      final AdaptadorNotificacionEvento notificador) {
    this.canalUdp = Objects.requireNonNull(canalUdp, "El canal UDP es obligatorio.");
    this.mapper = Objects.requireNonNull(mapper, "El mapper de red es obligatorio.");
    this.notificador = Objects.requireNonNull(notificador, "El notificador es obligatorio.");
  }

  @Override
  public void enviarRespuesta(final RespuestaCliente respuesta) {
    Objects.requireNonNull(respuesta, "La respuesta no puede ser nula.");
    final UdpResponse networkResponse = mapper.toNetworkResponse(respuesta);
    try {
      canalUdp.enviar(
          networkResponse.ipDestino(),
          networkResponse.puertoDestino(),
          networkResponse.payload());
    } catch (final ServidorRedException excepcion) {
      notificador.notificarEvento(
          new EventoServidor(
              "ERROR",
              respuesta.getDestinatario().endpoint(),
              "Error al enviar respuesta por UDP: " + excepcion.getMessage()));
    }
  }
}
