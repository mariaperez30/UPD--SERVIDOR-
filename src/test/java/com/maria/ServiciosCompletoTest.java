package com.maria;

import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;
import com.maria.aplicacion.mapper.ConversionMapper;
import com.maria.aplicacion.mapper.PeticionMapper;
import com.maria.aplicacion.servicios.ConvertirPesoService;
import com.maria.aplicacion.servicios.GestionarServidorService;
import com.maria.aplicacion.servicios.ProcesarPeticionUdpService;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.puertos.salida.ControladorServidorRedPort;
import com.maria.dominio.puertos.salida.PuertoNotificacionEvento;
import com.maria.dominio.puertos.salida.PuertoSalidaRed;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ServiciosCompletoTest {

  private MockPuertoSalidaRed puertoSalidaRed;
  private MockPuertoNotificacionEvento puertoNotificacion;
  private ProcesarPeticionUdpService servicioProcesar;
  private ConvertirPesoService servicioConvertir;
  private GestionarServidorService servicioGestionar;
  private MockControladorRed controladorRed;

  @BeforeEach
  void setUp() {
    puertoSalidaRed = new MockPuertoSalidaRed();
    puertoNotificacion = new MockPuertoNotificacionEvento();
    controladorRed = new MockControladorRed();

    final PeticionMapper peticionMapper = new PeticionMapper();
    final ConversionMapper conversionMapper = new ConversionMapper();

    servicioProcesar = new ProcesarPeticionUdpService(
        puertoSalidaRed, puertoNotificacion, peticionMapper, conversionMapper);
    servicioConvertir = new ConvertirPesoService(conversionMapper);
    servicioGestionar = new GestionarServidorService(controladorRed, puertoNotificacion);
  }

  @Test
  @DisplayName("Debe procesar comando CONECTAR")
  void debeProcesarConectar() {
    final ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand("CONECTAR", "127.0.0.1", 9876);
    servicioProcesar.procesar(cmd);

    assertEquals(1, puertoSalidaRed.respuestas.size());
    assertTrue(puertoSalidaRed.respuestas.get(0).getMensaje().contains("Servidor UDP listo"));
  }

  @Test
  @DisplayName("Debe procesar comando DESCONECTAR")
  void debeProcesarDesconectar() {
    final ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand("DESCONECTAR", "127.0.0.1", 9876);
    servicioProcesar.procesar(cmd);

    assertEquals(1, puertoSalidaRed.respuestas.size());
    assertEquals("Sesión finalizada", puertoSalidaRed.respuestas.get(0).getMensaje());
  }

  @Test
  @DisplayName("Debe procesar comando CONVERTIR;100")
  void debeProcesarConvertir() {
    final ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand("CONVERTIR;100", "127.0.0.1", 9876);
    servicioProcesar.procesar(cmd);

    assertEquals(1, puertoSalidaRed.respuestas.size());
    assertNotNull(puertoSalidaRed.respuestas.get(0).getResultado());
    assertEquals(45.359237, puertoSalidaRed.respuestas.get(0).getResultado().getPesoKg(), 0.001);
  }

  @Test
  @DisplayName("Debe ejecutar caso de uso ConvertirPesoService")
  void debeEjecutarConvertirPesoService() {
    final ResultadoConversionDto res = servicioConvertir.convertir(new ConvertirPesoCommand(150.0));
    assertNotNull(res);
    assertEquals(68.0388, res.pesoKg(), 0.001);
  }

  @Test
  @DisplayName("Debe gestionar ciclo de vida del servidor")
  void debeGestionarServidor() {
    servicioGestionar.iniciarServidor(9876);
    assertTrue(controladorRed.corriendo);

    servicioGestionar.detenerServidor();
    assertTrue(!controladorRed.corriendo);
  }

  static class MockPuertoSalidaRed implements PuertoSalidaRed {
    List<RespuestaCliente> respuestas = new ArrayList<>();

    @Override
    public void enviarRespuesta(RespuestaCliente respuesta) {
      respuestas.add(respuesta);
    }
  }

  static class MockPuertoNotificacionEvento implements PuertoNotificacionEvento {
    List<EventoServidor> eventos = new ArrayList<>();

    @Override
    public void notificarEvento(EventoServidor evento) {
      eventos.add(evento);
    }
  }

  static class MockControladorRed implements ControladorServidorRedPort {
    boolean corriendo = false;

    @Override
    public void iniciar(int puerto) {
      corriendo = true;
    }

    @Override
    public void detener() {
      corriendo = false;
    }

    @Override
    public boolean estaCorriendo() {
      return corriendo;
    }
  }
}
