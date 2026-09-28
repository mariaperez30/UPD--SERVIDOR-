package com.maria.dominio.puertos.salida;

import com.maria.dominio.modelos.EventoServidor;

public interface PuertoNotificacionEvento {
  void notificarEvento(EventoServidor evento);
}
