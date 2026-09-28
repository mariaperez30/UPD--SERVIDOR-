package com.maria.dominio.vo;

import com.maria.dominio.excepciones.DestinatarioIncorrectoException;
import java.util.Objects;

/**
 * Objeto de valor para representar el destino de red de un cliente UDP.
 */
public record Destinatario(String ip, int puerto) {

  public Destinatario {
    if (Objects.isNull(ip) || ip.isBlank()) {
      throw new DestinatarioIncorrectoException("La dirección IP no puede estar vacía.");
    }
    if (puerto <= 0 || puerto > 65535) {
      throw new DestinatarioIncorrectoException("El puerto del destinatario debe estar entre 1 y 65535.");
    }
  }

  public String endpoint() {
    return ip + ":" + puerto;
  }
}
