package com.maria.aplicacion.puertos.entrada;

import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;

public interface ConvertirPesoInputPort {
  ResultadoConversionDto convertir(ConvertirPesoCommand comando);
}
