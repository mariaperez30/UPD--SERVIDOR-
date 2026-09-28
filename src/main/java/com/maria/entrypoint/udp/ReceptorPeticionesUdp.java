package com.maria.entrypoint.udp;

import com.maria.adaptadores.red.AdaptadorNotificacionEvento;
import com.maria.adaptadores.red.CanalUdp;
import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.excepciones.ServidorRedException;
import com.maria.aplicacion.puertos.entrada.ProcesarPeticionUdpInputPort;
import com.maria.dominio.enums.EstadoServidor;
import com.maria.dominio.modelos.EventoServidor;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ReceptorPeticionesUdp {

  private static final int BUFFER_SIZE = 4096;

  private final CanalUdp canalUdp;
  private final ProcesarPeticionUdpInputPort procesarPeticionPort;
  private final AdaptadorNotificacionEvento notificador;

  private ExecutorService executorEscucha;
  private volatile boolean corriendo = false;

  public ReceptorPeticionesUdp(
      final CanalUdp canalUdp,
      final ProcesarPeticionUdpInputPort procesarPeticionPort,
      final AdaptadorNotificacionEvento notificador) {
    this.canalUdp = Objects.requireNonNull(canalUdp, "El canal UDP es obligatorio.");
    this.procesarPeticionPort = Objects.requireNonNull(procesarPeticionPort, "El puerto de procesamiento es obligatorio.");
    this.notificador = Objects.requireNonNull(notificador, "El notificador es obligatorio.");
  }

  public synchronized void iniciar(final int puerto) {
    if (corriendo) {
      return;
    }
    try {
      canalUdp.abrir(puerto);
      corriendo = true;
      executorEscucha = Executors.newSingleThreadExecutor();
      executorEscucha.submit(() -> bucleEscucha(puerto));
      notificador.notificarCambioEstado(EstadoServidor.EN_LINEA, puerto);
      notificador.notificarEvento(
          new EventoServidor("SISTEMA", "0.0.0.0:" + puerto, "Servidor UDP iniciado en puerto " + puerto));
    } catch (final ServidorRedException excepcion) {
      corriendo = false;
      canalUdp.cerrar();
      notificador.notificarCambioEstado(EstadoServidor.DETENIDO, puerto);
      throw excepcion;
    }
  }

  public synchronized void detener() {
    if (!corriendo) {
      return;
    }
    corriendo = false;
    canalUdp.cerrar();
    if (Objects.nonNull(executorEscucha) && !executorEscucha.isShutdown()) {
      executorEscucha.shutdownNow();
    }
    notificador.notificarCambioEstado(EstadoServidor.DETENIDO, 0);
    notificador.notificarEvento(
        new EventoServidor("SISTEMA", "127.0.0.1", "Servidor UDP detenido correctamente."));
  }

  private void bucleEscucha(final int puerto) {
    final byte[] buffer = new byte[BUFFER_SIZE];
    while (corriendo) {
      try {
        final DatagramPacket paquete = canalUdp.recibir(buffer);
        final String mensaje = new String(paquete.getData(), 0, paquete.getLength(), StandardCharsets.UTF_8);
        final String ipCliente = paquete.getAddress().getHostAddress();
        final int puertoCliente = paquete.getPort();

        procesarPeticionPort.procesar(new ProcesarPeticionUdpCommand(mensaje, ipCliente, puertoCliente));

      } catch (final SocketException excepcion) {
        if (!corriendo) {
          break; // Cierre normal del socket
        }
        notificador.notificarEvento(
            new EventoServidor("ERROR", "0.0.0.0:" + puerto, "Error de Socket UDP: " + excepcion.getMessage()));
      } catch (final IOException excepcion) {
        if (corriendo) {
          notificador.notificarEvento(
              new EventoServidor("ERROR", "0.0.0.0:" + puerto, "Error de E/S en recepción UDP: " + excepcion.getMessage()));
        }
      }
    }
  }
}
