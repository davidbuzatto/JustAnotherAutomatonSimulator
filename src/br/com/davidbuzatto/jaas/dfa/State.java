/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.dfa;

import br.com.davidbuzatto.jaas.gui.geom.Shape;
import br.com.davidbuzatto.jaas.utils.Constants;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author David
 */
public class State extends Shape {

    private int number;
    private boolean initial;
    private boolean finall;
    private List<Transition> transitions;

    public State( int number, boolean initial, boolean finall, double x, double y ) {
        
        this.number = number;
        this.initial = initial;
        this.finall = finall;
        
        this.xStart = x - Constants.STATE_RADIUS;
        this.xEnd = x + Constants.STATE_RADIUS;
        this.yStart = y - Constants.STATE_RADIUS;
        this.yEnd = y + Constants.STATE_RADIUS;
        
        this.transitions = new ArrayList<>();
        
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        calculateDrawingBounds();
        g2d = (Graphics2D) g2d.create();
        
        g2d.setStroke( Constants.STATE_STROKE );
        
        if ( selected ) {
            g2d.setColor( Constants.SELECTED_STATE_FILL_COLOR );
        } else {
            g2d.setColor( Constants.STATE_FILL_COLOR );
        }
        
        g2d.fill( new Ellipse2D.Double( xStartD, yStartD, xEndD - xStartD, yEndD - yStartD ) );
        
        
        if ( selected ) {
            g2d.setColor( Constants.SELECTED_STATE_STROKE_COLOR );
        } else {
            g2d.setColor( Constants.STATE_STROKE_COLOR );
        }
        
        g2d.draw( new Ellipse2D.Double( xStartD, yStartD, xEndD - xStartD, yEndD - yStartD ) );
        
        if ( finall ) {
            g2d.draw( new Ellipse2D.Double( xStartD + 4, yStartD + 4, xEndD - xStartD - 8, yEndD - yStartD - 8 ) );
        }
        
        
        g2d.setFont( Constants.STATE_FONT );
        
        String q = toString();
        FontMetrics fm = g2d.getFontMetrics();
        int w = fm.stringWidth( q );
        
        g2d.drawString( "q" + number, 
                (int) ( xStartD + Constants.STATE_RADIUS ) - w / 2, 
                (int) ( yStartD + Constants.STATE_RADIUS ) + 4 );
        
        
        g2d.setStroke( Constants.TRANSITION_STROKE );
        
        if ( selected ) {
            g2d.setColor( Constants.SELECTED_TRANSITION_STROKE_COLOR );
        } else {
            g2d.setColor( Constants.TRANSITION_STROKE_COLOR );
        }
        
        if ( initial ) {
            g2d.draw( new Line2D.Double( xStartD - 20, yStartD + ( yEndD - yStartD ) / 2, xStartD, yStartD + ( yEndD - yStartD ) / 2 ) );
            g2d.draw( new Line2D.Double( xStartD - 5, yStartD + ( yEndD - yStartD ) / 2 - 5, xStartD, yStartD + ( yEndD - yStartD ) / 2 ) );
            g2d.draw( new Line2D.Double( xStartD - 5, yStartD + ( yEndD - yStartD ) / 2 + 5, xStartD, yStartD + ( yEndD - yStartD ) / 2 ) );
        }
        
        g2d.dispose();
        
    }

    @Override
    public boolean intercepts( double x, double y ) {
        return x >= xStartD && x <= xEndD && y >= yStartD && y <= yEndD;
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

    @Override
    public String toString() {
        return "q" + number;
    }
    
}
