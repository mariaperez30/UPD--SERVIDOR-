package com.maria.aplicacion.excepciones;

import java.io.Serial;

public class ServidorRedException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ServidorRedException(final String mensaje) {
    super(mensaje);
  }

  public ServidorRedException(final String mensaje, final Throwable causa) {
    super(mensaje, causa);
  }
}
