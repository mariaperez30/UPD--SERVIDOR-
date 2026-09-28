package com.maria.aplicacion.excepciones;

import java.io.Serial;

public class ComandoInvalidoException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ComandoInvalidoException(final String mensaje) {
    super(mensaje);
  }
}
