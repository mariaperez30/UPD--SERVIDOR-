package com.maria.adaptadores.red;

import com.maria.dominio.enums.EstadoServidor;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.puertos.salida.PuertoNotificacionEvento;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

public final class AdaptadorNotificacionEvento implements PuertoNotificacionEvento {

  private final List<ObservadorServidor> observadores = new CopyOnWriteArrayList<>();

  public void registrarObservador(final ObservadorServidor observador) {
    if (Objects.nonNull(observador)) {
      observadores.add(observador);
    }
  }

  public void removerObservador(final ObservadorServidor observador) {
    if (Objects.nonNull(observador)) {
      observadores.remove(observador);
    }
  }

  @Override
  public void notificarEvento(final EventoServidor evento) {
    if (Objects.nonNull(evento)) {
      observadores.forEach(obs -> obs.onEvento(evento));
    }
  }

  public void notificarCambioEstado(final EstadoServidor nuevoEstado, final int puerto) {
    if (Objects.nonNull(nuevoEstado)) {
      observadores.forEach(obs -> obs.onCambioEstado(nuevoEstado, puerto));
    }
  }
}
