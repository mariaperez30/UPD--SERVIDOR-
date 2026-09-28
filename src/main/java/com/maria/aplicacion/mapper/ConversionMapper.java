package com.maria.aplicacion.mapper;

import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;
import com.maria.aplicacion.excepciones.ComandoInvalidoException;
import com.maria.dominio.modelos.Conversion;
import com.maria.dominio.modelos.ResultadoConversion;
import com.maria.dominio.vo.Peso;
import java.util.Objects;

public final class ConversionMapper {

  public Conversion toDomain(final double pesoLibras) {
    return new Conversion(new Peso(pesoLibras));
  }

  public Conversion toDomain(final ConvertirPesoCommand comando) {
    if (Objects.isNull(comando)) {
      throw new ComandoInvalidoException("El comando de conversión no puede ser nulo.");
    }
    return toDomain(comando.pesoLibras());
  }

  public ResultadoConversionDto toDto(final ResultadoConversion resultado) {
    if (Objects.isNull(resultado)) {
      return null;
    }
    return new ResultadoConversionDto(
        resultado.getPesoKg(),
        resultado.getPesoKgFormateado(),
        resultado.getPesoLibrasFormateado());
  }
}
