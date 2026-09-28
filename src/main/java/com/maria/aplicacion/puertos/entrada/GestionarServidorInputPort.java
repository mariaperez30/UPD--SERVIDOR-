package com.maria.aplicacion.puertos.entrada;

public interface GestionarServidorInputPort {
  void iniciarServidor(int puerto);
  void detenerServidor();
  boolean estaCorriendo();
}
