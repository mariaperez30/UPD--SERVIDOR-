package com.maria;

import com.maria.adaptadores.red.mapper.UdpNetworkMapper;
import com.maria.adaptadores.red.response.UdpResponse;
import com.maria.aplicacion.dto.ConvertirPesoCommand;
import com.maria.aplicacion.dto.ProcesarPeticionUdpCommand;
import com.maria.aplicacion.dto.ResultadoConversionDto;
import com.maria.aplicacion.excepciones.ComandoInvalidoException;
import com.maria.aplicacion.mapper.ConversionMapper;
import com.maria.aplicacion.mapper.PeticionMapper;
import com.maria.dominio.excepciones.RespuestaInvalidaException;
import com.maria.dominio.modelos.Conversion;
import com.maria.dominio.modelos.RespuestaCliente;
import com.maria.dominio.modelos.ResultadoConversion;
import com.maria.dominio.vo.Destinatario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MappersCompletoTest {

  @Test
  @DisplayName("Debe mapear ProcesarPeticionUdpCommand a Destinatario")
  void debeMapearPeticion() {
    final PeticionMapper mapper = new PeticionMapper();
    final ProcesarPeticionUdpCommand cmd = new ProcesarPeticionUdpCommand("CONECTAR", "127.0.0.1", 9876);
    final Destinatario dest = mapper.toDestinatario(cmd);
    assertEquals("127.0.0.1", dest.ip());
    assertEquals(9876, dest.puerto());

    assertThrows(ComandoInvalidoException.class, () -> mapper.toDestinatario(null));
  }

  @Test
  @DisplayName("Debe mapear a dominio y DTO mediante ConversionMapper")
  void debeMapearConversion() {
    final ConversionMapper mapper = new ConversionMapper();
    final Conversion domain = mapper.toDomain(150.0);
    assertEquals(150.0, domain.getPesoLibras().valor());

    final ConvertirPesoCommand cmd = new ConvertirPesoCommand(200.0);
    final Conversion domainCmd = mapper.toDomain(cmd);
    assertEquals(200.0, domainCmd.getPesoLibras().valor());

    final ResultadoConversion res = new ResultadoConversion(90.7185, 200.0);
    final ResultadoConversionDto dto = mapper.toDto(res);
    assertNotNull(dto);
    assertEquals(90.7185, dto.pesoKg(), 0.001);

    assertNull(mapper.toDto(null));
    assertThrows(ComandoInvalidoException.class, () -> mapper.toDomain((ConvertirPesoCommand) null));
  }

  @Test
  @DisplayName("Debe mapear a UdpResponse mediante UdpNetworkMapper")
  void debeMapearUdpResponse() {
    final UdpNetworkMapper mapper = new UdpNetworkMapper();
    final Destinatario dest = new Destinatario("127.0.0.1", 9876);

    final UdpResponse respCon = mapper.toNetworkResponse(RespuestaCliente.conectado(dest, "OK"));
    assertTrue(respCon.payload().startsWith("CONECTADO_OK;"));

    final UdpResponse respDes = mapper.toNetworkResponse(RespuestaCliente.desconectado(dest, "BYE"));
    assertTrue(respDes.payload().startsWith("DESCONECTADO_OK;"));

    final ResultadoConversion res = new ResultadoConversion(45.36, 100.0);
    final UdpResponse respConv = mapper.toNetworkResponse(RespuestaCliente.conversionExitosa(dest, res));
    assertTrue(respConv.payload().startsWith("OK_CONVERSION;"));

    final UdpResponse respErr = mapper.toNetworkResponse(RespuestaCliente.error(dest, "FAIL"));
    assertTrue(respErr.payload().startsWith("ERROR;"));

    assertThrows(RespuestaInvalidaException.class, () -> mapper.toNetworkResponse(null));
  }
}
