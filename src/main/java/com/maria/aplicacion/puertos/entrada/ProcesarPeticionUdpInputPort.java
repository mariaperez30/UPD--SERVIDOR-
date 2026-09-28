package com.maria.aplicacion.puertos.entrada;

import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;

public interface ProcesarPeticionUdpInputPort {
  void procesar(ProcesarPeticionUdpCommand comando);
}
