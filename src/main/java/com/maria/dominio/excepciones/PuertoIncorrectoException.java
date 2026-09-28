package com.maria.dominio.excepciones;

import java.io.Serial;

public class PuertoIncorrectoException extends DominioException {
  @Serial
  private static final long serialVersionUID = 1L;

  public PuertoIncorrectoException(final String mensaje) {
    super(mensaje);
  }
}
