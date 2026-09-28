package com.maria.dominio.excepciones;

import java.io.Serial;

public class RespuestaInvalidaException extends DominioException {
  @Serial
  private static final long serialVersionUID = 1L;

  public RespuestaInvalidaException(final String mensaje) {
    super(mensaje);
  }
}
