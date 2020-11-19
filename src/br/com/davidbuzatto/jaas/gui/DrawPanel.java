/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.gui;

import br.com.davidbuzatto.jaas.gui.geom.Shape;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author David
 */
public class DrawPanel extends JPanel {
    
    private Shape mainShape;
    private Shape tempShape;
    private BufferedImage image;
    
    /*private Deque<ChangeAction> undoStack;
    private Deque<ChangeAction> redoStack;*/
    
    public DrawPanel() {
        
        /*setBackground( new Color( 0, 0, 0, 0 ) );
        setOpaque( false );*/
        
        /*undoStack = new ArrayDeque<>();
        redoStack = new ArrayDeque<>();*/
        
    }
    
    @Override
    protected void paintComponent( Graphics g ) {
        
        super.paintComponent( g );
        image = new BufferedImage( getWidth(), getHeight(), 
                BufferedImage.TYPE_INT_ARGB );
        
        Graphics2D g2d = ( Graphics2D ) image.getGraphics();
        g2d.setRenderingHint( RenderingHints.KEY_ANTIALIASING, 
                RenderingHints.VALUE_ANTIALIAS_ON );
        
        g2d.setPaint( Color.WHITE );
        g2d.fillRect( 0, 0, getWidth(), getHeight() );
        
        if ( mainShape != null ) {
            mainShape.draw( g2d );
        }
        
        if ( tempShape != null ) {
            tempShape.draw( g2d );
        }
        
        g2d.dispose();
        
        g.drawImage( image, 0, 0, null );
        
    }

    public BufferedImage getImage() {
        return image;
    }
    
    /*public void addChangeAction( ChangeAction sca ) {
        undoStack.push( sca );
        redoStack.clear();
    }
    
    public void undo() {
        if ( !undoStack.isEmpty() ) {
            ChangeAction sca = undoStack.pop();
            redoStack.push( sca );
            sca.applyBeforeChange();
        }
    }
    
    public void redo() {
        if ( !redoStack.isEmpty() ) {
            ChangeAction sca = redoStack.pop();
            undoStack.push( sca );
            sca.applyAfterChange();
        }
    }
    
    public boolean isAbleToUndo() {
        return !undoStack.isEmpty();
    }
    
    public boolean isAbleToRedo() {
        return !redoStack.isEmpty();
    }*/

    public void setTempShape( Shape tempShape ) {
        this.tempShape = tempShape;
    }
    
    public void setMainShape( Shape shape ) {
        mainShape = shape;
    }
    
    public void reset() {
        mainShape = null;
        tempShape = null;
    }
    
}
