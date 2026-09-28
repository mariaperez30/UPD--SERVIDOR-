package com.maria.dominio.excepciones;

import java.io.Serial;

public class ConversionInvalidaException extends DominioException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ConversionInvalidaException(final String mensaje) {
    super(mensaje);
  }
}
