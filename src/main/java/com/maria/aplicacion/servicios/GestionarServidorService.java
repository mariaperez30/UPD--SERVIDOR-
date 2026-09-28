package com.maria.aplicacion.servicios;

import com.maria.aplicacion.puertos.entrada.GestionarServidorInputPort;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.puertos.salida.ControladorServidorRedPort;
import com.maria.dominio.puertos.salida.PuertoNotificacionEvento;
import com.maria.dominio.vo.PuertoRed;
import java.util.Objects;

public final class GestionarServidorService implements GestionarServidorInputPort {

  private final ControladorServidorRedPort controladorRed;
  private final PuertoNotificacionEvento puertoNotificacion;

  public GestionarServidorService(
      final ControladorServidorRedPort controladorRed,
      final PuertoNotificacionEvento puertoNotificacion) {
    this.controladorRed = Objects.requireNonNull(controladorRed, "El controlador de red es obligatorio.");
    this.puertoNotificacion = Objects.requireNonNull(puertoNotificacion, "El puerto de notificación es obligatorio.");
  }

  @Override
  public void iniciarServidor(final int puerto) {
    final PuertoRed puertoValido = new PuertoRed(puerto);
    controladorRed.iniciar(puertoValido.numero());
    puertoNotificacion.notificarEvento(
        new EventoServidor("CONTROL", "127.0.0.1:" + puertoValido.numero(), "Servicio iniciado en puerto " + puertoValido.numero()));
  }

  @Override
  public void detenerServidor() {
    controladorRed.detener();
    puertoNotificacion.notificarEvento(
        new EventoServidor("CONTROL", "127.0.0.1", "Servicio detenido por el usuario."));
  }

  @Override
  public boolean estaCorriendo() {
    return controladorRed.estaCorriendo();
  }
}
