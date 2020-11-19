/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.gui;

import br.com.davidbuzatto.jaas.dfa.FiniteAutomaton;
import com.formdev.flatlaf.FlatDarkLaf;

/**
 *
 * @author David
 */
public class FormalDefinitionDialog extends javax.swing.JDialog {

    private FiniteAutomaton fa;
    
    /**
     * Creates new form ChangeTransitionsDialog
     */
    public FormalDefinitionDialog( java.awt.Frame parent, FiniteAutomaton fa, boolean modal ) {
        
        super( parent, modal );
        this.fa = fa;
        initComponents();
        
        getRootPane().setDefaultButton( btnAccept );
        
        areaFormal.setText( generateTextRep( fa ) );
        tableTransition.setDefaultRenderer( String.class, new TransitionFunctionTableCellRenderer() );
        tableTransition.setModel( fa.createTransitionFunctionTableModel() );
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings( "unchecked" )
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAccept = new javax.swing.JButton();
        panelFormal = new javax.swing.JPanel();
        scrollFormal = new javax.swing.JScrollPane();
        areaFormal = new javax.swing.JTextArea();
        panelTransition = new javax.swing.JPanel();
        scrollTransition = new javax.swing.JScrollPane();
        tableTransition = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Formal Definition");
        setResizable(false);

        btnAccept.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/davidbuzatto/jaas/gui/icons/accept.png"))); // NOI18N
        btnAccept.setToolTipText("Accept changes");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        panelFormal.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formal Definition", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12), new java.awt.Color(0, 204, 153))); // NOI18N
        panelFormal.setToolTipText("");

        areaFormal.setEditable(false);
        areaFormal.setFont(new java.awt.Font("Monospaced", 1, 18)); // NOI18N
        scrollFormal.setViewportView(areaFormal);

        javax.swing.GroupLayout panelFormalLayout = new javax.swing.GroupLayout(panelFormal);
        panelFormal.setLayout(panelFormalLayout);
        panelFormalLayout.setHorizontalGroup(
            panelFormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollFormal)
                .addContainerGap())
        );
        panelFormalLayout.setVerticalGroup(
            panelFormalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFormalLayout.createSequentialGroup()
                .addComponent(scrollFormal, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelTransition.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "\u03B4", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(51, 153, 255))); // NOI18N

        tableTransition.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableTransition.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableTransition.setShowGrid(false);
        scrollTransition.setViewportView(tableTransition);

        javax.swing.GroupLayout panelTransitionLayout = new javax.swing.GroupLayout(panelTransition);
        panelTransition.setLayout(panelTransitionLayout);
        panelTransitionLayout.setHorizontalGroup(
            panelTransitionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransitionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTransition, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addContainerGap())
        );
        panelTransitionLayout.setVerticalGroup(
            panelTransitionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTransitionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTransition, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAccept))
                    .addComponent(panelFormal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelTransition, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelFormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelTransition, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAccept)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        dispose();
    }//GEN-LAST:event_btnAcceptActionPerformed
    
    private String generateTextRep( FiniteAutomaton fa ) {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( "A = ( Q, \u03A3, \u03B4, ").append( fa.generateQ0Rep() ).append( ", F )" ).append( "\n" );
        sb.append( fa.generateQRep() ).append( "\n" );
        sb.append( fa.generateSigmaRep() ).append( "\n" );
        sb.append( fa.generateFRep() );
        
        return sb.toString();
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main( String args[] ) {
        FlatDarkLaf.install();

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater( new Runnable() {
            public void run() {
                FormalDefinitionDialog dialog = new FormalDefinitionDialog( new javax.swing.JFrame(), null, true );
                dialog.addWindowListener( new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing( java.awt.event.WindowEvent e ) {
                        System.exit( 0 );
                    }
                } );
                dialog.setVisible( true );
            }
        } );
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea areaFormal;
    private javax.swing.JButton btnAccept;
    private javax.swing.JPanel panelFormal;
    private javax.swing.JPanel panelTransition;
    private javax.swing.JScrollPane scrollFormal;
    private javax.swing.JScrollPane scrollTransition;
    private javax.swing.JTable tableTransition;
    // End of variables declaration//GEN-END:variables
}