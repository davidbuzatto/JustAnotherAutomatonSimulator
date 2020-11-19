/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.gui;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author David
 */
public class TransitionFunctionTableCellRenderer implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column ) {
        JLabel lbl = new JLabel( value.toString() );
        lbl.setHorizontalAlignment( JLabel.CENTER );
        return lbl;
    }
    
}
