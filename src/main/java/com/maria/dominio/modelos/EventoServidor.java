package com.maria.dominio.modelos;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Representa un evento del sistema del servidor.
 */
public final class EventoServidor {

  private final String categoria;
  private final String endpoint;
  private final String descripcion;
  private final LocalDateTime fechaHora;

  public EventoServidor(final String categoria, final String endpoint, final String descripcion) {
    this.categoria = Objects.requireNonNull(categoria, "La categoría es obligatoria.");
    this.endpoint = Objects.requireNonNull(endpoint, "El endpoint es obligatorio.");
    this.descripcion = Objects.requireNonNull(descripcion, "La descripción es obligatoria.");
    this.fechaHora = LocalDateTime.now();
  }

  public String getCategoria() {
    return categoria;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public LocalDateTime getFechaHora() {
    return fechaHora;
  }
}
