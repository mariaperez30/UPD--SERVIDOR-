package com.maria.dominio.modelos;

import com.maria.dominio.excepciones.ConversionInvalidaException;
import com.maria.dominio.vo.Peso;
import java.util.Objects;

/**
 * Entidad/Modelo del dominio para la conversión de peso expresado en libras a kilogramos.
 */
public final class Conversion {

  private static final double FACTOR_CONVERSION_KG_POR_LIBRA = 0.45359237;
  private final Peso pesoLibras;

  public Conversion(final Peso pesoLibras) {
    if (Objects.isNull(pesoLibras)) {
      throw new ConversionInvalidaException("El peso en libras es obligatorio para realizar la conversión.");
    }
    this.pesoLibras = pesoLibras;
  }

  /**
   * Efectúa la conversión del peso de libras a kilogramos.
   *
   * @return Objeto ResultadoConversion con los valores de la conversión.
   */
  public ResultadoConversion convertir() {
    final double pesoKg = pesoLibras.valor() * FACTOR_CONVERSION_KG_POR_LIBRA;
    return new ResultadoConversion(pesoKg, pesoLibras.valor());
  }

  public Peso getPesoLibras() {
    return pesoLibras;
  }
}
