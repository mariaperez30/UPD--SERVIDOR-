package com.maria.dominio.excepciones;

import java.io.Serial;

public class ResultadoInvalidoException extends DominioException {
  @Serial
  private static final long serialVersionUID = 1L;

  public ResultadoInvalidoException(final String mensaje) {
    super(mensaje);
  }
}
