package com.maria;

import com.maria.dominio.enums.EstadoServidor;
import com.maria.dominio.enums.TipoRespuesta;
import com.maria.dominio.excepciones.DominioException;
import com.maria.dominio.excepciones.RespuestaInvalidaException;
import com.maria.dominio.excepciones.ResultadoInvalidoException;
import com.maria.dominio.modelos.EventoServidor;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.modelos.ResultadoConversion;
import com.maria.dominio.vo.Destinatario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DominioCompletoTest {

  @Test
  @DisplayName("Debe construir respuestas del cliente adecuadamente")
  void debeConstruirRespuestasCliente() {
    final Destinatario dest = new Destinatario("127.0.0.1", 9876);
    final RespuestaCliente con = RespuestaCliente.conectado(dest, "OK");
    assertEquals(TipoRespuesta.CONECTADO, con.getTipo());
    assertEquals("OK", con.getMensaje());
    assertNull(con.getResultado());

    final RespuestaCliente des = RespuestaCliente.desconectado(dest, "BYE");
    assertEquals(TipoRespuesta.DESCONECTADO, des.getTipo());
    assertEquals("BYE", des.getMensaje());

    final ResultadoConversion res = new ResultadoConversion(45.36, 100.0);
    final RespuestaCliente conv = RespuestaCliente.conversionExitosa(dest, res);
    assertEquals(TipoRespuesta.OK_CONVERSION, conv.getTipo());
    assertNotNull(conv.getResultado());

    final RespuestaCliente err = RespuestaCliente.error(dest, "ERROR");
    assertEquals(TipoRespuesta.ERROR, err.getTipo());
    assertEquals("ERROR", err.getMensaje());
  }

  @Test
  @DisplayName("Debe validar ResultadoConversion e imprevistos")
  void debeValidarResultadoConversion() {
    assertThrows(ResultadoInvalidoException.class, () -> new ResultadoConversion(Double.NaN, 100.0));
    assertThrows(ResultadoInvalidoException.class, () -> new ResultadoConversion(45.36, -10.0));
  }

  @Test
  @DisplayName("Debe instanciar EventoServidor correctamente")
  void debeInstanciarEventoServidor() {
    final EventoServidor evento = new EventoServidor("TEST", "127.0.0.1:9876", "Prueba");
    assertEquals("TEST", evento.getCategoria());
    assertEquals("127.0.0.1:9876", evento.getEndpoint());
    assertEquals("Prueba", evento.getDescripcion());
    assertNotNull(evento.getFechaHora());
  }

  @Test
  @DisplayName("Debe probar DominioException constructors")
  void debeProbarDominioException() {
    final DominioException ex1 = new DominioException("Error de dominio");
    assertEquals("Error de dominio", ex1.getMessage());

    final DominioException ex2 = new DominioException("Con causa", ex1);
    assertEquals("Con causa", ex2.getMessage());
    assertEquals(ex1, ex2.getCause());
  }

  @Test
  @DisplayName("Debe validar enums")
  void debeValidarEnums() {
    assertEquals(2, EstadoServidor.values().length);
    assertEquals(4, TipoRespuesta.values().length);
  }
}
