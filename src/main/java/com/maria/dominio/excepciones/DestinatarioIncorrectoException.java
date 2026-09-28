package com.maria.dominio.excepciones;

import java.io.Serial;

public class DestinatarioIncorrectoException extends DominioException {
  @Serial
  private static final long serialVersionUID = 1L;

  public DestinatarioIncorrectoException(final String mensaje) {
    super(mensaje);
  }
}
