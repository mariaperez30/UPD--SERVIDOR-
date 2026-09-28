package com.maria.dominio.modelos;

import com.maria.dominio.excepciones.ResultadoInvalidoException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Representa el resultado inmutable de la conversión de libras a kilogramos.
 */
public final class ResultadoConversion {

  private final double pesoKg;
  private final double pesoLibras;

  public ResultadoConversion(final double pesoKg, final double pesoLibras) {
    if (!Double.isFinite(pesoKg) || !Double.isFinite(pesoLibras) || pesoKg <= 0 || pesoLibras <= 0) {
      throw new ResultadoInvalidoException("El resultado de la conversión contiene datos inválidos.");
    }
    this.pesoKg = pesoKg;
    this.pesoLibras = pesoLibras;
  }

  public double getPesoKg() {
    return pesoKg;
  }

  public double getPesoLibras() {
    return pesoLibras;
  }

  public String getPesoKgFormateado() {
    final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    final DecimalFormat df = new DecimalFormat("#.##", symbols);
    return df.format(pesoKg);
  }

  public String getPesoLibrasFormateado() {
    final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    final DecimalFormat df = new DecimalFormat("#.##", symbols);
    return df.format(pesoLibras);
  }
}
