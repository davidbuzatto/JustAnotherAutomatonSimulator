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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @author David
 */
public class State extends Shape implements Serializable, Comparable<State> {

    private int number;
    private boolean initial;
    private boolean finall;
    private List<Transition> transitions;
    private Set<State> internalStates;
    private String alias;
    
    private Color strokeColor;
    private Color fillColor;
    private Color selectedStrokeColor;
    private Color selectedFillColor;
    
    private boolean mouseOver;

    public State( int number, boolean initial, boolean finall, double x, double y ) {
        
        this.number = number;
        this.initial = initial;
        this.finall = finall;
        
        this.xStart = x - Constants.STATE_RADIUS;
        this.xEnd = x + Constants.STATE_RADIUS;
        this.yStart = y - Constants.STATE_RADIUS;
        this.yEnd = y + Constants.STATE_RADIUS;
        
        this.transitions = new ArrayList<>();
        
        strokeColor = Constants.STATE_STROKE_COLOR;
        fillColor = Constants.STATE_FILL_COLOR;
        selectedStrokeColor = Constants.SELECTED_STATE_STROKE_COLOR;
        selectedFillColor = Constants.SELECTED_STATE_FILL_COLOR;
        
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        g2d.setStroke( Constants.STATE_STROKE );
        
        if ( selected ) {
            g2d.setColor( selectedFillColor );
        } else {
            g2d.setColor( fillColor );
        }
        
        g2d.fill( new Ellipse2D.Double( xStart, yStart, xEnd - xStart, yEnd - yStart ) );
        
        
        if ( selected ) {
            g2d.setColor( selectedStrokeColor );
        } else {
            g2d.setColor( strokeColor );
        }
        
        g2d.draw( new Ellipse2D.Double( xStart, yStart, xEnd - xStart, yEnd - yStart ) );
        
        if ( finall ) {
            g2d.draw( new Ellipse2D.Double( xStart + 4, yStart + 4, xEnd - xStart - 8, yEnd - yStart - 8 ) );
        }
        
        
        g2d.setFont( Constants.STATE_FONT );
        
        String q = toString();
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth( q );
        
        g2d.drawString( q, 
                (int) ( xStart + Constants.STATE_RADIUS ) - w / 2, 
                (int) ( yStart + Constants.STATE_RADIUS ) + 4 );
        
        
        if ( mouseOver ) {
            if ( alias != null ) {
                if ( internalStates != null ) {
                    String is = internalStates.toString();
                    q = "{" + is.substring( 1, is.length() - 1 ) + "}";
                } else {
                    q = "q" + number;
                }
                g2d.drawString( q, 
                        (int) xEnd, 
                        (int) yStart );
            }
        }
        
        g2d.setStroke( Constants.TRANSITION_STROKE );
        
        if ( selected ) {
            g2d.setColor( Constants.SELECTED_TRANSITION_STROKE_COLOR );
        } else {
            g2d.setColor( Constants.TRANSITION_STROKE_COLOR );
        }
        
        if ( initial ) {
            g2d.draw( new Line2D.Double( xStart - 20, yStart + ( yEnd - yStart ) / 2, xStart, yStart + ( yEnd - yStart ) / 2 ) );
            g2d.draw( new Line2D.Double( xStart - 5, yStart + ( yEnd - yStart ) / 2 - 5, xStart, yStart + ( yEnd - yStart ) / 2 ) );
            g2d.draw( new Line2D.Double( xStart - 5, yStart + ( yEnd - yStart ) / 2 + 5, xStart, yStart + ( yEnd - yStart ) / 2 ) );
        }
        
        g2d.dispose();
        
    }

    @Override
    public boolean intercepts( double x, double y ) {
        double c1 = xStart + Constants.STATE_RADIUS - x;
        double c2 = yStart + Constants.STATE_RADIUS - y;
        return c1 * c1 + c2 * c2 <= Constants.STATE_RADIUS * Constants.STATE_RADIUS;
    }
    
    public void addTransition( State target, char symbol ) {
        transitions.add( new Transition( this, target, symbol ) );
    }
    
    public void removeTransition( Transition transition ) {
        transitions.remove( transition );
    }

    public boolean isInitial() {
        return initial;
    }

    public void setInitial( boolean initial ) {
        this.initial = initial;
    }

    public boolean isFinal() {
        return finall;
    }

    public void setFinal( boolean finall ) {
        this.finall = finall;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver( boolean mouseOver ) {
        this.mouseOver = mouseOver;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public void setTransitions( List<Transition> transitions ) {
        this.transitions = transitions;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber( int number ) {
        this.number = number;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor( Color strokeColor ) {
        this.strokeColor = strokeColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor( Color fillColor ) {
        this.fillColor = fillColor;
    }

    public Color getSelectedStrokeColor() {
        return selectedStrokeColor;
    }

    public void setSelectedStrokeColor( Color selectedStrokeColor ) {
        this.selectedStrokeColor = selectedStrokeColor;
    }

    public Color getSelectedFillColor() {
        return selectedFillColor;
    }

    public void setSelectedFillColor( Color selectedFillColor ) {
        this.selectedFillColor = selectedFillColor;
    }

    public Set<State> getInternalStates() {
        return internalStates;
    }

    public void setInternalStates( Set<State> internalStates ) {
        this.internalStates = internalStates;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias( String alias ) {
        this.alias = alias;
    }

    public void setPosition( double x, double y ) {
        this.xStart = x - Constants.STATE_RADIUS;
        this.xEnd = x + Constants.STATE_RADIUS;
        this.yStart = y - Constants.STATE_RADIUS;
        this.yEnd = y + Constants.STATE_RADIUS;
    }
    
    @Override
    public int compareTo( State o ) {
        return this.number - o.number;
    }
    
    @Override
    public String toString() {
        if ( alias != null ) {
            return alias;
        } else if ( internalStates != null ) {
            String is = internalStates.toString();
            return "{" + is.substring( 1, is.length() - 1 ) + "}";
        } else {
            return "q" + number;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this.number;
        hash = 71 * hash + Objects.hashCode( this.internalStates );
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final State other = ( State ) obj;
        if ( this.number != other.number ) {
            return false;
        }
        if ( !Objects.equals( this.internalStates, other.internalStates ) ) {
            return false;
        }
        return true;
    }
    
}
