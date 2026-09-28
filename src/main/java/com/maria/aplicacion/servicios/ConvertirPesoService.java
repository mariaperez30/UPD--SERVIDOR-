package com.maria.aplicacion.servicios;

import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;
import com.maria.aplicacion.mapper.ConversionMapper;
import com.maria.aplicacion.puertos.entrada.ConvertirPesoInputPort;
import com.maria.dominio.modelos.Conversion;
import com.maria.dominio.modelos.ResultadoConversion;
import java.util.Objects;

public final class ConvertirPesoService implements ConvertirPesoInputPort {

  private final ConversionMapper conversionMapper;

  public ConvertirPesoService(final ConversionMapper conversionMapper) {
    this.conversionMapper = Objects.requireNonNull(conversionMapper, "El mapper de conversión es obligatorio.");
  }

  @Override
  public ResultadoConversionDto convertir(final ConvertirPesoCommand comando) {
    final Conversion conversion = conversionMapper.toDomain(comando);
    final ResultadoConversion resultado = conversion.convertir();
    return conversionMapper.toDto(resultado);
  }
}
