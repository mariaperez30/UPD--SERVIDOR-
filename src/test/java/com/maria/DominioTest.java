package com.maria;

import com.maria.dominio.excepciones.ConversionInvalidaException;
import com.maria.dominio.excepciones.DestinatarioIncorrectoException;
import com.maria.dominio.excepciones.PesoIncorrectoException;
import com.maria.dominio.excepciones.PuertoIncorrectoException;
import com.maria.dominio.modelos.Conversion;
import com.maria.dominio.modelos.ResultadoConversion;
import com.maria.dominio.vo.Destinatario;
import com.maria.dominio.vo.Peso;
import com.maria.dominio.vo.PuertoRed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DominioTest {

  @Test
  @DisplayName("Debe realizar la conversión correctamente de 100 libras a kilogramos")
  void debeConvertirLibrasAKilogramosCorrectamente() {
    final Peso peso = new Peso(100.0);
    final Conversion conversion = new Conversion(peso);
    final ResultadoConversion resultado = conversion.convertir();

    assertNotNull(resultado);
    assertEquals(100.0, resultado.getPesoLibras(), 0.001);
    assertEquals(45.359237, resultado.getPesoKg(), 0.001);
    assertEquals("45.36", resultado.getPesoKgFormateado());
  }

  @Test
  @DisplayName("Debe lanzar excepción si el peso en libras es menor o igual a cero")
  void debeLanzarExcepcionParaPesoInvalido() {
    assertThrows(PesoIncorrectoException.class, () -> new Peso(0.0));
    assertThrows(PesoIncorrectoException.class, () -> new Peso(-10.0));
  }

  @Test
  @DisplayName("Debe lanzar excepción al instanciar Conversion con peso nulo")
  void debeLanzarExcepcionConPesoNulo() {
    assertThrows(ConversionInvalidaException.class, () -> new Conversion(null));
  }

  @Test
  @DisplayName("Debe validar correctamente un Destinatario")
  void debeValidarDestinatario() {
    final Destinatario destinatario = new Destinatario("127.0.0.1", 9876);
    assertEquals("127.0.0.1", destinatario.ip());
    assertEquals(9876, destinatario.puerto());
    assertEquals("127.0.0.1:9876", destinatario.endpoint());

    assertThrows(DestinatarioIncorrectoException.class, () -> new Destinatario(null, 9876));
    assertThrows(DestinatarioIncorrectoException.class, () -> new Destinatario("127.0.0.1", 0));
  }

  @Test
  @DisplayName("Debe validar correctamente el PuertoRed")
  void debeValidarPuertoRed() {
    final PuertoRed puerto = new PuertoRed(9876);
    assertEquals(9876, puerto.numero());

    assertThrows(PuertoIncorrectoException.class, () -> new PuertoRed(80));
    assertThrows(PuertoIncorrectoException.class, () -> new PuertoRed(70000));
  }
}
