/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.fa;

import br.com.davidbuzatto.jaas.gui.geom.Shape;
import br.com.davidbuzatto.jaas.utils.Constants;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;

/**
 *
 * @author David
 */
public class Transition implements Serializable {
    
    private State source;
    private State target;
    private char symbol;
    
    private Color strokeColor;
    private Color selectedStrokeColor;
    
    public Transition( State source, State target, char symbol ) {
        
        this.source = source;
        this.target = target;
        this.symbol = symbol;
        
        strokeColor = Constants.TRANSITION_STROKE_COLOR;
        selectedStrokeColor = Constants.SELECTED_TRANSITION_STROKE_COLOR;
    
    }

    public State getSource() {
        return source;
    }

    public void setSource( State source ) {
        this.source = source;
    }

    public State getTarget() {
        return target;
    }

    public void setTarget( State target ) {
        this.target = target;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol( char symbol ) {
        this.symbol = symbol;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor( Color strokeColor ) {
        this.strokeColor = strokeColor;
    }

    public Color getSelectedStrokeColor() {
        return selectedStrokeColor;
    }

    public void setSelectedStrokeColor( Color selectedStrokeColor ) {
        this.selectedStrokeColor = selectedStrokeColor;
    }

    @Override
    public String toString() {
        return String.format( "(%s) --(%c)--> (%s)", 
                source, 
                symbol == '\0' ? '\u03B5' : symbol, 
                target );
    }
    
}
