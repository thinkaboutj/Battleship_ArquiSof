package Pantallas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import java.util.ArrayList;
import java.util.List;

public class SetUpView extends JFrame implements MouseListener, MouseMotionListener {
    private JLabel[][] tableroLabels = new JLabel[10][10];
    private JLayeredPane capaJuego;
    private List<JLabel> barcosColocados = new ArrayList<>();
    private JButton btnConfirmar;
    private boolean tableroConfirmado = false;
    public static boolean debugModeActive;

    // Tipos y cantidades de barcos
    private int[] tiposBarcos = {4, 3, 2, 1}; 
    private int[] cantidadesBarcos = {2, 2, 4, 3};

    public SetUpView() {
        initComponents();
        crearInterfazJuego();
    }

    private void crearInterfazJuego() {
        capaJuego = new JLayeredPane();
        capaJuego.setLayout(null);
        capaJuego.setBounds(0, 0, 800, 600);
        this.add(capaJuego);

        // Agregar imagen de fondo
        JLabel lblFondo = new JLabel(new ImageIcon(getClass().getResource("/images/PantallaInicio.jpg")));
        lblFondo.setBounds(0, 0, 800, 600);
        capaJuego.add(lblFondo, Integer.valueOf(0));

        // Crear el tablero
        JPanel tablero = crearTablero();
        tablero.setBounds(150, 50, 500, 500); // Ajustar posición y tamaño del tablero
        capaJuego.add(tablero, Integer.valueOf(1));

        // Agregar los barcos
        agregarBarcos();

        // Agregar botón de confirmación
        agregarBotonConfirmar();
    }

    private JPanel crearTablero() {
        JPanel tablero = new JPanel(new GridLayout(10, 10));
        tablero.setPreferredSize(new Dimension(900, 900));
        tablero.setOpaque(false); // Transparente para ver los barcos

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                JLabel casilla = new JLabel();
                casilla.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Borde gris
                casilla.setBackground(new Color(173, 216, 230)); // Azul claro (agua)
                casilla.setOpaque(true); // Hacer visible el color
                tableroLabels[i][j] = casilla;
                tablero.add(casilla);
            }
        }

        return tablero;
    }

    private void agregarBarcos() {
        String[] rutasImagenes = {
            "/images/PortaAviones.png",
            "/images/Crucero.png",
            "/images/Submarino.png",
            "/images/Barco.png"
        };

        int[] anchosBase = {200, 150, 100, 50};
        int alturaBase = 50;
        int inicioX = 50;
        int inicioY = 550; // Colocamos los barcos debajo del tablero

        for (int tipo = 0; tipo < tiposBarcos.length; tipo++) {
            for (int i = 0; i < cantidadesBarcos[tipo]; i++) {
                JLabel barco = crearBarco(
                    rutasImagenes[tipo],
                    anchosBase[tipo],
                    alturaBase,
                    inicioX + (i * (anchosBase[tipo] + 10)), // Espaciado entre barcos
                    inicioY
                );
                capaJuego.add(barco, Integer.valueOf(1));
            }
            inicioX += 10; // Separamos los tipos de barcos
        }
    }

    private void agregarBotonConfirmar() {
        btnConfirmar = new JButton("Confirmar Tablero");
        btnConfirmar.setBounds(600, 500, 150, 50); // Ajustar posición del botón
        btnConfirmar.addActionListener(e -> confirmarTablero());
        capaJuego.add(btnConfirmar, Integer.valueOf(2));
    }

    private void confirmarTablero() {
        if (barcosColocados.size() == 11) { // Total de naves
            tableroConfirmado = true;
            btnConfirmar.setEnabled(false);
            deshabilitarMovimientoBarcos();
            JOptionPane.showMessageDialog(this, "¡Tablero confirmado!");
            // Aquí podrías llamar a la siguiente pantalla
            // irASiguientePantalla();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Debes colocar todas las naves antes de confirmar. Faltan: " + (11 - barcosColocados.size()));
        }
    }

    private void deshabilitarMovimientoBarcos() {
        for (JLabel barco : barcosColocados) {
            for (MouseListener ml : barco.getMouseListeners()) {
                barco.removeMouseListener(ml);
            }
            for (MouseMotionListener mml : barco.getMouseMotionListeners()) {
                barco.removeMouseMotionListener(mml);
            }
        }
    }


    private JLabel crearBarco(String rutaImagen, int ancho, int alto, int x, int y) {
        JLabel barco = new JLabel();
        barco.setBounds(x, y, ancho, alto);
        barco.setIcon(escalarImagen(rutaImagen, ancho, alto));
        agregarEventosArrastre(barco);
        return barco;
    }

    private void agregarEventosArrastre(JLabel barco) {
        MouseAdapter dragListener = new MouseAdapter() {
            Point inicial;
            boolean rotado = false;

            @Override
            public void mousePressed(MouseEvent e) {
                if (!tableroConfirmado) {
                    inicial = e.getPoint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!tableroConfirmado) {
                    int deltaX = e.getX() - inicial.x;
                    int deltaY = e.getY() - inicial.y;
                    barco.setLocation(barco.getX() + deltaX, barco.getY() + deltaY);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!tableroConfirmado) {
                    if (esPositionValida(barco) && !hayColision(barco)) {
                        ajustarBarcoATablero(barco);
                        if (!barcosColocados.contains(barco)) {
                            barcosColocados.add(barco);
                        }
                    } else {
                        JOptionPane.showMessageDialog(SetUpView.this, 
                            "El barco debe estar completamente dentro del tablero sin superponerse");
                    }
                }
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (!tableroConfirmado && e.getClickCount() == 2) {
                    rotarBarco(barco);
                }
            }
        };

        barco.addMouseListener(dragListener);
        barco.addMouseMotionListener(dragListener);
    }

    private void ajustarBarcoATablero(JLabel barco) {
        Point tableroPos = capaJuego.getComponent(0).getLocation();
        int celdaTamano = 50; // Tamaño de cada celda en el tablero

        int xAjustado = ((barco.getX() - tableroPos.x) / celdaTamano) * celdaTamano + tableroPos.x;
        int yAjustado = ((barco.getY() - tableroPos.y) / celdaTamano) * celdaTamano + tableroPos.y;

        barco.setLocation(xAjustado, yAjustado);
    }

    private boolean esPositionValida(JLabel barco) {
        Rectangle areaTotalTablero = capaJuego.getComponent(0).getBounds();
        Rectangle areaBarco = barco.getBounds();

        // Verificar que el barco esté completamente dentro del tablero
        return areaTotalTablero.contains(areaBarco);
    }

    private boolean hayColision(JLabel barcoNuevo) {
        for (JLabel barcoExistente : barcosColocados) {
            if (barcoNuevo != barcoExistente && 
                barcoNuevo.getBounds().intersects(barcoExistente.getBounds())) {
                return true;
            }
        }
        return false;
    }

    private void rotarBarco(JLabel barco) {
        Rectangle areaTotalTablero = capaJuego.getComponent(0).getBounds();
        
        // Rotar y cambiar tamaño
        int anchoOriginal = barco.getWidth();
        int altoOriginal = barco.getHeight();
        barco.setSize(altoOriginal, anchoOriginal);
        
        // Rotar imagen
        ImageIcon iconoOriginal = (ImageIcon) barco.getIcon();
        Image imagenRotada = rotarImagen(iconoOriginal.getImage());
        barco.setIcon(new ImageIcon(imagenRotada));
        
        // Verificar si la rotación cabe en el tablero y no choca con otros barcos
        if (!areaTotalTablero.contains(barco.getBounds()) || hayColision(barco)) {
            // Si no cabe, revertir rotación
            barco.setSize(anchoOriginal, altoOriginal);
            barco.setIcon(iconoOriginal);
            JOptionPane.showMessageDialog(this, "No se puede rotar el barco en esta posición");
        }
    }

    private Image rotarImagen(Image imagen) {
        int ancho = imagen.getWidth(null);
        int alto = imagen.getHeight(null);
        
        BufferedImage bufferedImagen = new BufferedImage(alto, ancho, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bufferedImagen.createGraphics();
        
        // Rotar 90 grados
        g2d.rotate(Math.PI / 2, alto / 2.0, alto / 2.0);
        g2d.drawImage(imagen, 0, 0, null);
        g2d.dispose();
        
        return bufferedImagen;
    }

    private ImageIcon escalarImagen(String ruta, int ancho, int alto) {
        try {
            BufferedImage originalImage = ImageIO.read(getClass().getResource(ruta));
            Image scaledImage = originalImage.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 710, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 630, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SetUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SetUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SetUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SetUpView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SetUpView().setVisible(true);
            }
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
