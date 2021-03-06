/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.fa;

import br.com.davidbuzatto.jaas.gui.geom.Shape;
import br.com.davidbuzatto.jaas.utils.Constants;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

/**
 * 
 * @author David
 */
public class ProcessingString extends Shape implements Serializable {

    private static final long serialVersionUID = Constants.SERIAL_VERSION;
    
    private String string;
    private char[] stringData;
    
    private Color color;
    private Color currentSymbolColor;
    private Color controlColor;
    
    private int currentSymbol;
    private State currentState;
    
    private boolean accepted;
    private boolean rejected;

    private ProcessingStringLabelType labelType = ProcessingStringLabelType.DEFAULT;
    
    public ProcessingString( String string,
            Color color,
            Color currentSymbolColor,
            Color controlColor,
            int currentSymbol,
            ProcessingStringLabelType labelType,
            double x, 
            double y ) {
        if ( string.isEmpty() ) {
            string = "\u03B5";
        }
        this.string = string;
        this.stringData = string.toCharArray();
        this.color = color;
        this.currentSymbolColor = currentSymbolColor;
        this.controlColor = controlColor;
        this.currentSymbol = currentSymbol;
        this.labelType = labelType;
        this.xStart = x;
        this.yStart = y;
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        g2d.setStroke( Constants.PROCESSING_STRING_STROKE );
        g2d.setFont( Constants.PROCESSING_STRING_FONT );
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth( string );
        
        int x = (int) ( xStart - w / 2 );
        int y = (int) yStart;
        int p = 0;
        
        int xc = x;
        
        for ( int i = 0; i < stringData.length; i++ ) {
            
            g2d.setFont( Constants.PROCESSING_STRING_FONT );
            fm = g2d.getFontMetrics();
                    
            char c = stringData[i];
            int cw = fm.charWidth( c );
            
            if ( p == currentSymbol ) {
                g2d.setColor( currentSymbolColor );
            } else {
                g2d.setColor( color );
            }
            
            g2d.drawString( c + "", xc, y );
            
            if ( p == currentSymbol ) {
                
                g2d.setColor( controlColor );
                xc -= 1;
                
                g2d.draw( new Line2D.Double( xc + cw / 2, y - 25, xc + cw / 2, y - 40 ) );
                g2d.draw( new Line2D.Double( xc + cw / 2, y - 25, xc + cw / 2 - 5, y - 30 ) );
                g2d.draw( new Line2D.Double( xc + cw / 2, y - 25, xc + cw / 2 + 5, y - 30 ) );
                
                if ( currentState != null ) {
                    
                    String st = generateLabel();
                    
                    g2d.setFont( Constants.PROCESSING_STRING_STATE_FONT );
                    fm = g2d.getFontMetrics();
                    w = fm.stringWidth( st );
                    
                    g2d.drawString( st, ( xc + cw / 2 ) - w / 2, y - 50 );
                    
                }
                
                xc += 1;
                
            }
            
            xc += cw;
            p++;
            
        }
        
        g2d.setFont( Constants.PROCESSING_STRING_FONT );
        
        if ( accepted ) {
            g2d.drawString( String.format( "Accepted at %s!", generateLabel() ), xc + 30, y );
        } else if ( rejected ) {
            g2d.drawString( String.format( "Rejected at %s!", generateLabel() ), xc + 30, y );
        }
        
        g2d.dispose();
        
    }

    @Override
    public boolean intercepts( double x, double y ) {
        return false;
    }

    public void setAccepted( boolean accepted ) {
        this.accepted = accepted;
    }

    public void setRejected( boolean rejected ) {
        this.rejected = rejected;
    }
    
    public String getString() {
        return string;
    }

    public void setString( String string ) {
        this.string = string;
    }
    
    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState( State currentState ) {
        this.currentState = currentState;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor( Color color ) {
        this.color = color;
    }

    public Color getCurrentSymbolColor() {
        return currentSymbolColor;
    }

    public void setCurrentSymbolColor( Color currentSymbolColor ) {
        this.currentSymbolColor = currentSymbolColor;
    }

    public Color getControlColor() {
        return controlColor;
    }

    public void setControlColor( Color controlColor ) {
        this.controlColor = controlColor;
    }

    public int getCurrentSymbol() {
        return currentSymbol;
    }

    public void setCurrentSymbol( int currentSymbol ) {
        this.currentSymbol = currentSymbol;
    }
    
    private String generateLabel() {
        
        switch ( labelType ) {
            case DEFAULT:
                return currentState.generateDefaultRep();
            case ALIAS:
                return currentState.generateAliasRep();
            case INTERNAL_STATES:
                return currentState.generateInternalStatesRep();
        }
        
        return "something wrong...";
        
    }
    
}
