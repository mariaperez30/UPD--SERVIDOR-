package com.maria;

import com.maria.adaptadores.red.AdaptadorControlServidorRed;
import com.maria.adaptadores.red.AdaptadorNotificacionEvento;
import com.maria.adaptadores.red.AdaptadorSalidaUdp;
import com.maria.adaptadores.red.CanalUdp;
import com.maria.adaptadores.red.ObservadorServidor;
import com.maria.adaptadores.red.mapper.UdpNetworkMapper;
import com.maria.adaptadores.red.util.RedUtil;
import com.maria.dominio.enums.EstadoServidor;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.vo.Destinatario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedCompletoTest {

  @Test
  @DisplayName("Debe notificar eventos y cambios de estado")
  void debeNotificarEventos() {
    final AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();
    final AtomicBoolean eventoRecibido = new AtomicBoolean(false);
    final AtomicBoolean estadoRecibido = new AtomicBoolean(false);

    final ObservadorServidor observador = new ObservadorServidor() {
      @Override
      public void onEvento(EventoServidor evento) {
        eventoRecibido.set(true);
      }

      @Override
      public void onCambioEstado(EstadoServidor nuevoEstado, int puerto) {
        estadoRecibido.set(true);
      }
    };

    notificador.registrarObservador(observador);
    notificador.notificarEvento(new EventoServidor("TEST", "127.0.0.1", "Msg"));
    notificador.notificarCambioEstado(EstadoServidor.EN_LINEA, 9876);

    assertTrue(eventoRecibido.get());
    assertTrue(estadoRecibido.get());

    notificador.removerObservador(observador);
  }

  @Test
  @DisplayName("Debe testear RedUtil")
  void debeTestearRedUtil() {
    final String ip = RedUtil.obtenerIpLocal();
    assertNotNull(ip);
  }

  @Test
  @DisplayName("Debe validar nulls en AdaptadorControlServidorRed")
  void debeValidarNullsControlador() {
    final CanalUdp canal = new CanalUdp();
    assertThrows(NullPointerException.class, () -> new AdaptadorControlServidorRed(null, port -> {}, () -> {}));
  }
}
