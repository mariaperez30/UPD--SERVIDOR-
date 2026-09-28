package com.maria.aplicacion.mapper;

import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.excepciones.ComandoInvalidoException;
import com.maria.dominio.vo.Destinatario;
import java.util.Objects;

public final class PeticionMapper {

  public Destinatario toDestinatario(final ProcesarPeticionUdpCommand comando) {
    if (Objects.isNull(comando)) {
      throw new ComandoInvalidoException("El comando de petición no puede ser nulo.");
    }
    return new Destinatario(comando.ipCliente(), comando.puertoCliente());
  }
}
