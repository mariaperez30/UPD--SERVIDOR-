package com.maria.aplicacion.dto;

import com.maria.aplicacion.excepciones.ComandoInvalidoException;
import java.util.Objects;

public record ProcesarPeticionUdpCommand(String mensaje, String ipCliente, int puertoCliente) {

  public ProcesarPeticionUdpCommand {
    if (Objects.isNull(ipCliente) || ipCliente.isBlank()) {
      throw new ComandoInvalidoException("La IP del cliente es obligatoria.");
    }
    if (puertoCliente <= 0 || puertoCliente > 65535) {
      throw new ComandoInvalidoException("El puerto del cliente debe estar entre 1 y 65535.");
    }
  }
}
