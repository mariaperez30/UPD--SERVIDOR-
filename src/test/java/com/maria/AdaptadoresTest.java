package com.maria;

import com.maria.adaptadores.red.AdaptadorSalidaUdp;
import com.maria.adaptadores.red.CanalUdp;
import com.maria.adaptadores.red.mapper.UdpNetworkMapper;
import com.maria.adaptadores.red.response.UdpResponse;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.vo.Destinatario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdaptadoresTest {

  @Test
  @DisplayName("Debe validar UdpResponse record")
  void debeValidarUdpResponse() {
    final UdpResponse response = new UdpResponse("PAYLOAD", "127.0.0.1", 9876);
    assertEquals("PAYLOAD", response.payload());
    assertEquals("127.0.0.1", response.ipDestino());
    assertEquals(9876, response.puertoDestino());
  }

  @Test
  @DisplayName("Debe fallar al enviar en socket cerrado")
  void debeFallarEnvioConSocketCerrado() {
    final CanalUdp canal = new CanalUdp();
    assertThrows(Exception.class, () -> canal.enviar("127.0.0.1", 9876, "TEST"));
  }
}
