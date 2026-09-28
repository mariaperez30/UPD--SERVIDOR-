package com.maria.dominio.vo;

import com.maria.dominio.excepciones.PesoIncorrectoException;

/**
 * Objeto de Valor (VO) que representa el peso en libras.
 */
public record Peso(double valor) {

  public Peso {
    if (!Double.isFinite(valor) || valor <= 0) {
      throw new PesoIncorrectoException("El peso debe ser un número positivo mayor que cero.");
    }
  }
}
