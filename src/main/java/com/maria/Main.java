package com.maria;

import com.maria.adaptadores.red.AdaptadorControlServidorRed;
import com.maria.adaptadores.red.AdaptadorNotificacionEvento;
import com.maria.adaptadores.red.AdaptadorSalidaUdp;
import com.maria.adaptadores.red.CanalUdp;
import com.maria.adaptadores.red.mapper.UdpNetworkMapper;
import com.maria.aplicacion.mapper.ConversionMapper;
import com.maria.aplicacion.mapper.PeticionMapper;
import com.maria.aplicacion.puertos.entrada.GestionarServidorInputPort;
import com.maria.aplicacion.puertos.entrada.ProcesarPeticionUdpInputPort;
import com.maria.aplicacion.servicios.GestionarServidorService;
import com.maria.aplicacion.servicios.ProcesarPeticionUdpService;
import com.maria.dominio.puertos.salida.ControladorServidorRedPort;
import com.maria.dominio.puertos.salida.PuertoSalidaRed;
import com.maria.entrypoint.gui.ServidorFrame;
import com.maria.entrypoint.udp.ReceptorPeticionesUdp;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Punto de entrada del programa Servidor (Composition Root).
 */
public class Main {

  private static final System.Logger LOG = System.getLogger(Main.class.getName());

  private Main() {
    // Evita instanciación
  }

  public static void main(final String[] args) {
    aplicarLookAndFeel();

    // 1. Infraestructura de red compartida
    final CanalUdp canalUdp = new CanalUdp();
    final AdaptadorNotificacionEvento notificador = new AdaptadorNotificacionEvento();
    final UdpNetworkMapper redMapper = new UdpNetworkMapper();
    final PuertoSalidaRed puertoSalidaRed = new AdaptadorSalidaUdp(canalUdp, redMapper, notificador);

    // 2. Mappers de aplicación
    final PeticionMapper peticionMapper = new PeticionMapper();
    final ConversionMapper conversionMapper = new ConversionMapper();

    // 3. Casos de uso (servicios de aplicación)
    final ProcesarPeticionUdpInputPort procesarPeticionPort = new ProcesarPeticionUdpService(
        puertoSalidaRed,
        notificador,
        peticionMapper,
        conversionMapper);

    // 4. Entrypoints
    final ReceptorPeticionesUdp receptorUdp = new ReceptorPeticionesUdp(canalUdp, procesarPeticionPort, notificador);

    final ControladorServidorRedPort controladorRed = new AdaptadorControlServidorRed(
        canalUdp,
        receptorUdp::iniciar,
        receptorUdp::detener);

    final GestionarServidorInputPort gestionarServidorPort = new GestionarServidorService(controladorRed, notificador);

    // 5. Lanzar GUI en el Event Dispatch Thread de Swing
    SwingUtilities.invokeLater(
        () -> {
          final ServidorFrame frame = new ServidorFrame(gestionarServidorPort);
          notificador.registrarObservador(frame);
          frame.setVisible(true);
        });
  }

  private static void aplicarLookAndFeel() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (final ClassNotFoundException
        | InstantiationException
        | IllegalAccessException
        | UnsupportedLookAndFeelException excepcion) {
      LOG.log(System.Logger.Level.DEBUG,
          "No fue posible aplicar la apariencia del sistema; se usará la predeterminada.", excepcion);
    }
  }
}
