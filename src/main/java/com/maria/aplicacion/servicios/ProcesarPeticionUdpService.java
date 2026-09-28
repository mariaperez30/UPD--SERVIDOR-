package com.maria.aplicacion.servicios;

import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.mapper.ConversionMapper;
import com.maria.aplicacion.mapper.PeticionMapper;
import com.maria.aplicacion.puertos.entrada.ProcesarPeticionUdpInputPort;
import com.maria.dominio.excepciones.DominioException;
import com.maria.dominio.modelos.Conversion;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.modelos.ResultadoConversion;
import com.maria.dominio.puertos.salida.PuertoNotificacionEvento;
import com.maria.dominio.puertos.salida.PuertoSalidaRed;
import com.maria.dominio.vo.Destinatario;
import java.util.Locale;
import java.util.Objects;

/**
 * Servicio de aplicación para procesar las peticiones del protocolo UDP de conversión de libras a kg.
 */
public final class ProcesarPeticionUdpService implements ProcesarPeticionUdpInputPort {

  private final PuertoSalidaRed puertoSalidaRed;
  private final PuertoNotificacionEvento puertoNotificacion;
  private final PeticionMapper peticionMapper;
  private final ConversionMapper conversionMapper;

  public ProcesarPeticionUdpService(
      final PuertoSalidaRed puertoSalidaRed,
      final PuertoNotificacionEvento puertoNotificacion,
      final PeticionMapper peticionMapper,
      final ConversionMapper conversionMapper) {
    this.puertoSalidaRed = Objects.requireNonNull(puertoSalidaRed, "El puerto de salida es obligatorio.");
    this.puertoNotificacion = Objects.requireNonNull(puertoNotificacion, "El puerto de notificación es obligatorio.");
    this.peticionMapper = Objects.requireNonNull(peticionMapper, "El mapper de petición es obligatorio.");
    this.conversionMapper = Objects.requireNonNull(conversionMapper, "El mapper de conversión es obligatorio.");
  }

  @Override
  public void procesar(final ProcesarPeticionUdpCommand comando) {
    Objects.requireNonNull(comando, "El comando es obligatorio.");

    final Destinatario destinatario = peticionMapper.toDestinatario(comando);
    final String mensaje = comando.mensaje();

    if (Objects.isNull(mensaje) || mensaje.isBlank()) {
      puertoSalidaRed.enviarRespuesta(
          RespuestaCliente.error(destinatario, "Mensaje vacío recibido."));
      notificarEvento(destinatario.endpoint(), "datos recibidos --> [Vacío]");
      return;
    }

    final String comandoTexto = mensaje.trim();

    if (comandoTexto.equalsIgnoreCase("CONECTAR")) {
      puertoSalidaRed.enviarRespuesta(
          RespuestaCliente.conectado(
              destinatario, "Servidor UDP listo para realizar conversiones de libras a kilogramos"));
      notificarEvento(
          destinatario.endpoint(),
          "conectado --> Solicitud de verificación recibida y aceptada");
      return;
    }

    if (comandoTexto.equalsIgnoreCase("DESCONECTAR")) {
      puertoSalidaRed.enviarRespuesta(
          RespuestaCliente.desconectado(destinatario, "Sesión finalizada"));
      notificarEvento(destinatario.endpoint(), "desconectado --> Cliente ha cerrado la sesión");
      return;
    }

    final String comandoUpper = comandoTexto.toUpperCase(Locale.ROOT);
    if (comandoUpper.startsWith("CONVERTIR;") || comandoUpper.startsWith("CALCULAR;")) {
      procesarConversion(destinatario, comandoTexto);
      return;
    }

    puertoSalidaRed.enviarRespuesta(
        RespuestaCliente.error(destinatario, "Comando no reconocido por el servidor UDP."));
    notificarEvento(
        destinatario.endpoint(),
        "datos recibidos --> Comando desconocido: [" + comandoTexto + "]");
  }

  private void procesarConversion(final Destinatario destinatario, final String comandoTexto) {
    final String[] partes = comandoTexto.split(";", -1);
    if (partes.length < 2) {
      puertoSalidaRed.enviarRespuesta(
          RespuestaCliente.error(
              destinatario, "Formato inválido. Se esperaba CONVERTIR;pesoLibras"));
      notificarEvento(
          destinatario.endpoint(),
          "datos recibidos --> Formato incorrecto: " + comandoTexto);
      return;
    }

    try {
      final double valorPesoLibras = Double.parseDouble(partes[1].trim().replace(',', '.'));

      final Conversion conversion = conversionMapper.toDomain(valorPesoLibras);
      final ResultadoConversion resultado = conversion.convertir();

      puertoSalidaRed.enviarRespuesta(RespuestaCliente.conversionExitosa(destinatario, resultado));

      final String logInfo = String.format(
          Locale.US,
          "datos recibidos (Peso: %s lbs) --> datos enviados (Peso: %s kg)",
          resultado.getPesoLibrasFormateado(),
          resultado.getPesoKgFormateado());

      notificarEvento(destinatario.endpoint(), logInfo);

    } catch (final NumberFormatException excepcion) {
      puertoSalidaRed.enviarRespuesta(
          RespuestaCliente.error(
              destinatario, "El parámetro de peso en libras debe ser numérico."));
      notificarEvento(
          destinatario.endpoint(),
          "datos recibidos --> Error de formato numérico: " + comandoTexto);

    } catch (final DominioException excepcion) {
      puertoSalidaRed.enviarRespuesta(RespuestaCliente.error(destinatario, excepcion.getMessage()));
      notificarEvento(
          destinatario.endpoint(),
          "datos recibidos --> Validación fallida: " + excepcion.getMessage());
    }
  }

  private void notificarEvento(final String endpoint, final String descripcion) {
    puertoNotificacion.notificarEvento(new EventoServidor("EVENTO", endpoint, descripcion));
  }
}
