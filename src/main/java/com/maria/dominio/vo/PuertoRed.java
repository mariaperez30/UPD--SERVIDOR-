package com.maria.dominio.vo;

import com.maria.dominio.excepciones.PuertoIncorrectoException;

/**
 * Objeto de valor para representar y validar un puerto de red.
 */
public record PuertoRed(int numero) {

  public PuertoRed {
    if (numero < 1024 || numero > 65535) {
      throw new PuertoIncorrectoException("El puerto debe estar entre 1024 y 65535.");
    }
  }
}
