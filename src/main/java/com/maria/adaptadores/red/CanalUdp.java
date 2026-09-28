package com.maria.adaptadores.red;

import com.maria.aplicacion.excepciones.ServidorRedException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class CanalUdp {

  private DatagramSocket socket;

  public synchronized void abrir(final int puerto) {
    if (Objects.nonNull(socket) && !socket.isClosed()) {
      throw new ServidorRedException("El socket UDP ya está abierto.");
    }
    try {
      socket = new DatagramSocket(puerto);
    } catch (final SocketException excepcion) {
      throw new ServidorRedException("No se pudo abrir el socket UDP en el puerto " + puerto, excepcion);
    }
  }

  public synchronized void cerrar() {
    if (Objects.nonNull(socket) && !socket.isClosed()) {
      socket.close();
    }
  }

  public synchronized boolean estaAbierto() {
    return Objects.nonNull(socket) && !socket.isClosed();
  }

  public DatagramPacket recibir(final byte[] buffer) throws IOException {
    final DatagramSocket currentSocket;
    synchronized (this) {
      if (!estaAbierto()) {
        throw new SocketException("Socket cerrado");
      }
      currentSocket = socket;
    }
    final DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
    currentSocket.receive(paquete);
    return paquete;
  }

  public void enviar(final String ip, final int puerto, final String mensaje) {
    final DatagramSocket currentSocket;
    synchronized (this) {
      if (!estaAbierto()) {
        throw new ServidorRedException("No se puede enviar. El socket UDP está cerrado.");
      }
      currentSocket = socket;
    }
    try {
      final byte[] buffer = mensaje.getBytes(StandardCharsets.UTF_8);
      final InetAddress direccion = InetAddress.getByName(ip);
      final DatagramPacket paquete = new DatagramPacket(buffer, buffer.length, direccion, puerto);
      currentSocket.send(paquete);
    } catch (final IOException excepcion) {
      throw new ServidorRedException("Error al enviar paquete UDP a " + ip + ":" + puerto, excepcion);
    }
  }
}
