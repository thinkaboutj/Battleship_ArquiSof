/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pantallas;

//import com.pruebapantallas.NewMain;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author jesus
 */
public class SeleccionCrear extends javax.swing.JFrame {

    private JLabel lblSeleccionado; // Para rastrear el color seleccionado
    private javax.swing.Timer parpadeoTimer; // Referencia al temporizador


    public SeleccionCrear() {
        initComponents(); // Inicialización generada por NetBeans
        configurarInteractividad(); // Método personalizado
        
       ajustarImagenes(); // Ajustar imágenes al tamaño de los labels
        iniciarAnimacionParpadeo(); // Animación de parpadeo
        agregarEventoClic(); // Hacer clickeable lblPressStart
    }

    /**
     * Método personalizado para configurar la lógica de selección.
     */
    private void configurarInteractividad() {
        // Hacer que el lblRed sea interactivo
        lblRed.setOpaque(true);
        lblRed.setBackground(Color.RED);
        lblRed.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        
         // Hacer que el lblBlue sea interactivo
        lblBlue.setOpaque(true);
        lblBlue.setBackground(Color.BLUE);
        lblBlue.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

        // Configurar listeners para ambos cuadros
        lblRed.addMouseListener(new ColorSelector(lblRed));
        lblBlue.addMouseListener(new ColorSelector(lblBlue));
    }

    /**
     * Clase interna para manejar clics en los cuadros de color.
     */
    private class ColorSelector extends MouseAdapter {
        private final JLabel label;

        public ColorSelector(JLabel label) {
            this.label = label;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (lblSeleccionado != null) {
                // Restablecer el borde del cuadro previamente seleccionado
                lblSeleccionado.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            }

            // Resaltar el cuadro seleccionado
            label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
            lblSeleccionado = label;

            // Guardar o mostrar el color seleccionado
            String color = label.getBackground() == Color.RED ? "Rojo" : "Azul";
            System.out.println("Color seleccionado: " + color);
        }
    }

        private void ajustarImagenes() {
        lblPressStart.setIcon(resizeImage("/images/Press_Start.png", lblPressStart.getWidth(), lblPressStart.getHeight()));
        lblTitulo.setIcon(resizeImage("/images/Titulo.png", lblTitulo.getWidth(), lblTitulo.getHeight()));
        jLabel1.setIcon(resizeImage("/images/PantallaInicio.jpg", jLabel1.getWidth(), jLabel1.getHeight()));

       
    }

    private ImageIcon resizeImage(String path, int width, int height) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private void iniciarAnimacionParpadeo() {
        parpadeoTimer = new javax.swing.Timer(500, e -> {
            lblPressStart.setVisible(!lblPressStart.isVisible());
        });
        parpadeoTimer.start();
    }
private void agregarEventoClic() {
    lblPressStart.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            // Ejecutar la pantalla de NewMain
            dispose(); // Cierra la ventana actual
//            new NewMain(); // Inicia la lógica de NewMain
        }
    });
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Agrupador = new javax.swing.JPanel();
        lblPressStart = new javax.swing.JLabel();
        lblBlue = new javax.swing.JLabel();
        lblRed = new javax.swing.JLabel();
        lblNombre1 = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        lblTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Agrupador.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblPressStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Press_Start.png"))); // NOI18N
        Agrupador.add(lblPressStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 490, 260, 110));

        lblBlue.setBackground(new java.awt.Color(204, 0, 51));
        Agrupador.add(lblBlue, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 380, 130, 100));

        lblRed.setBackground(new java.awt.Color(204, 0, 51));
        Agrupador.add(lblRed, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 380, 130, 100));

        lblNombre1.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        lblNombre1.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre1.setText("Seleccione un color:");
        Agrupador.add(lblNombre1, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 340, 230, 20));

        txtNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreActionPerformed(evt);
            }
        });
        Agrupador.add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 270, -1));

        lblNombre.setFont(new java.awt.Font("Arial Black", 1, 18)); // NOI18N
        lblNombre.setForeground(new java.awt.Color(255, 255, 255));
        lblNombre.setText("Nombre:");
        Agrupador.add(lblNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 120, 30));

        lblTitulo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Titulo.png"))); // NOI18N
        Agrupador.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 600, 220));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/PantallaInicio.jpg"))); // NOI18N
        jLabel1.setText("jLabel1");
        Agrupador.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 630));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Agrupador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Agrupador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreActionPerformed

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
            java.util.logging.Logger.getLogger(SeleccionCrear.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SeleccionCrear.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SeleccionCrear.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SeleccionCrear.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SeleccionCrear().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Agrupador;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblBlue;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblNombre1;
    private javax.swing.JLabel lblPressStart;
    private javax.swing.JLabel lblRed;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JTextField txtNombre;
    // End of variables declaration//GEN-END:variables
}
