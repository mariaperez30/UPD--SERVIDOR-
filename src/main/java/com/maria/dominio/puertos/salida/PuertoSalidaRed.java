package com.maria.dominio.puertos.salida;

import com.maria.dominio.modelos.RespuestaCliente;

public interface PuertoSalidaRed {
  void enviarRespuesta(RespuestaCliente respuesta);
}
