package com.maria.dominio.puertos.salida;

public interface ControladorServidorRedPort {
  void iniciar(int puerto);
  void detener();
  boolean estaCorriendo();
}
