package com.maria.adaptadores.red;

import com.maria.dominio.puertos.salida.ControladorServidorRedPort;
import java.util.Objects;
import java.util.function.Consumer;

public final class AdaptadorControlServidorRed implements ControladorServidorRedPort {

  private final CanalUdp canalUdp;
  private final Consumer<Integer> accionIniciar;
  private final Runnable accionDetener;

  public AdaptadorControlServidorRed(
      final CanalUdp canalUdp,
      final Consumer<Integer> accionIniciar,
      final Runnable accionDetener) {
    this.canalUdp = Objects.requireNonNull(canalUdp, "El canal UDP es obligatorio.");
    this.accionIniciar = Objects.requireNonNull(accionIniciar, "La acción de iniciar es obligatoria.");
    this.accionDetener = Objects.requireNonNull(accionDetener, "La acción de detener es obligatoria.");
  }

  @Override
  public void iniciar(final int puerto) {
    accionIniciar.accept(puerto);
  }

  @Override
  public void detener() {
    accionDetener.run();
  }

  @Override
  public boolean estaCorriendo() {
    return canalUdp.estaAbierto();
  }
}
