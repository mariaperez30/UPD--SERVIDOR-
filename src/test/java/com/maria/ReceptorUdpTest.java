package com.maria;

import com.maria.adaptadores.red.AdaptadorNotificacionEvento;
import com.maria.adaptadores.red.CanalUdp;
import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.excepciones.ServidorRedException;
import com.maria.aplicacion.puertos.entrada.ProcesarPeticionUdpInputPort;
import com.maria.entrypoint.udp.ReceptorPeticionesUdp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReceptorUdpTest {

  @Test
  @DisplayName("Debe iniciar y detener ReceptorPeticionesUdp")
  void debeIniciarYDetenerReceptor() throws Exception {
    final MockCanalUdp canal = new MockCanalUdp();
    final MockProcesarInputPort inputPort = new MockProcesarInputPort();
    final AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();

    final ReceptorPeticionesUdp receptor = new ReceptorPeticionesUdp(canal, inputPort, notificador);
    receptor.iniciar(9999);
    assertTrue(canal.abierto);

    receptor.detener();
    assertFalse(canal.abierto);
  }

  @Test
  @DisplayName("Debe manejar fallos al abrir puerto")
  void debeManejarFalloAlAbrir() {
    final MockCanalFallo canal = new MockCanalFallo();
    final MockProcesarInputPort inputPort = new MockProcesarInputPort();
    final AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();

    final ReceptorPeticionesUdp receptor = new ReceptorPeticionesUdp(canal, inputPort, notificador);
    assertThrows(ServidorRedException.class, () -> receptor.iniciar(9999));
  }

  static class MockCanalUdp extends CanalUdp {
    boolean abierto = false;

    @Override
    public synchronized void abrir(int puerto) {
      abierto = true;
    }

    @Override
    public synchronized void cerrar() {
      abierto = false;
    }

    @Override
    public synchronized boolean estaAbierto() {
      return abierto;
    }
  }

  static class MockCanalFallo extends CanalUdp {
    @Override
    public synchronized void abrir(int puerto) {
      throw new ServidorRedException("Puerto ocupado");
    }
  }

  static class MockProcesarInputPort implements ProcesarPeticionUdpInputPort {
    @Override
    public void procesar(ProcesarPeticionUdpCommand comando) {
    }
  }
}
