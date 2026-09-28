package com.maria.adaptadores.red;

import com.maria.dominio.enums.EstadoServidor;
import com.maria.dominio.modelos.EventoServidor;

public interface ObservadorServidor {
  void onEvento(EventoServidor evento);
  void onCambioEstado(EstadoServidor nuevoEstado, int puerto);
}
