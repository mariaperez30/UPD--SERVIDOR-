package com.maria;

import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;
import com.maria.aplicacion.excepciones.ComandoInvalidoException;
import com.maria.aplicacion.excepciones.ServidorRedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AplicacionTest {

  @Test
  @DisplayName("Debe validar DTOs y Comandos")
  void debeValidarDtos() {
    final ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand("MSG", "127.0.0.1", 9876);
    assertEquals("MSG", cmd.mensaje());

    assertThrows(ComandoInvalidoException.class, () -> new ProcesarPeticionUdpCommand("MSG", "", 9876));
    assertThrows(ComandoInvalidoException.class, () -> new ProcesarPeticionUdpCommand("MSG", "127.0.0.1", -1));

    final ConvertirPesoCommand cCmd = new ConvertirPesoCommand(100.0);
    assertEquals(100.0, cCmd.pesoLibras());

    final ResultadoConversionDto dto = new ResultadoConversionDto(45.36, "45.36", "100.00");
    assertEquals(45.36, dto.pesoKg());
  }

  @Test
  @DisplayName("Debe probar ServidorRedException")
  void debeProbarExcepcionRed() {
    final ServidorRedException ex1 = new ServidorRedException("Error red");
    assertEquals("Error red", ex1.getMessage());

    final ServidorRedException ex2 = new ServidorRedException("Con causa", ex1);
    assertEquals("Con causa", ex2.getMessage());
  }
}
