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
public class Transition extends Shape implements Serializable {
    
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

    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        g2d.setStroke( Constants.TRANSITION_STROKE );
        
        if ( selected ) {
            g2d.setColor( selectedStrokeColor );
        } else {
            g2d.setColor( strokeColor );
        }
        
        if ( source.equals( target ) ) {
            
            g2d.draw( new Ellipse2D.Double( 
                    source.getXStartD() + 10, 
                    source.getYStartD() + 10 - Constants.STATE_RADIUS, 
                    source.getXEndD() - source.getXStartD() - 20, 
                    source.getYEndD() - source.getYStartD() - 20 ) );
            
            Graphics2D g2dc = (Graphics2D) g2d.create();
            g2dc.translate( source.getXStartD(), source.getYStartD() + Constants.STATE_RADIUS );
            g2dc.rotate( Math.toRadians( 100 ), 10, -21 );
            g2dc.draw( new Line2D.Double( 3, -26, 10, -21 ) );
            g2dc.draw( new Line2D.Double( 5, -16, 10, -21 ) );
            g2dc.dispose();
            
        } else {
            
            /*g2d.draw( new Line2D.Double( 
                    source.getXStartD() + Constants.STATE_RADIUS,
                    source.getYStartD() + Constants.STATE_RADIUS,
                    target.getXStartD() + Constants.STATE_RADIUS, 
                    target.getYStartD() + Constants.STATE_RADIUS ) );*/
            
            double angle = Math.atan2( target.getYStartD() + Constants.STATE_RADIUS - ( source.getYStartD() + Constants.STATE_RADIUS ),
                    target.getXStartD() + Constants.STATE_RADIUS - ( source.getXStartD() + Constants.STATE_RADIUS ) );
            double sAngle = Math.PI / 2;
            double tAngle = Math.PI / 10;
            
            Point2D.Double midPoint = new Point2D.Double(
                    source.getXEndD() + ( target.getXStartD() - source.getXEndD() ) / 2,
                    source.getYEndD() + ( target.getYStartD() - source.getYEndD() ) / 2 );
            
            Point2D.Double controlPoint = new Point2D.Double( 
                    midPoint.x + Math.cos( angle - sAngle ) * 60,
                    midPoint.y + Math.sin( angle - sAngle ) * 60 );
            
            double angleControl = Math.atan2( target.getYStartD() + Constants.STATE_RADIUS - ( controlPoint.y ),
                    target.getXStartD() + Constants.STATE_RADIUS - ( controlPoint.x ) );
            
            Point2D.Double controlPointArrow = new Point2D.Double( 
                    target.getXStartD() + Constants.STATE_RADIUS - Math.cos( angleControl ) * Constants.STATE_RADIUS,
                    target.getYStartD() + Constants.STATE_RADIUS - Math.sin( angleControl ) * Constants.STATE_RADIUS);
            
            /*g2d.setColor( Color.RED );
            g2d.fillRect( (int) (midPoint.x),
                          (int) (midPoint.y), 5, 5 );
            g2d.setColor( Color.BLUE );
            g2d.fillRect( (int) (controlPoint.x),
                          (int) (controlPoint.y), 5, 5 );
            g2d.setColor( Color.GREEN );
            g2d.fillRect( (int) (controlPointArrow.x),
                          (int) (controlPointArrow.y), 5, 5 );
            g2d.setColor( Color.BLACK );*/
            
            g2d.draw( new QuadCurve2D.Double( 
                    source.getXStartD() + Constants.STATE_RADIUS,
                    source.getYStartD() + Constants.STATE_RADIUS,
                    controlPoint.x,
                    controlPoint.y,
                    target.getXStartD() + Constants.STATE_RADIUS, 
                    target.getYStartD() + Constants.STATE_RADIUS ) );
            
            Graphics2D g2dc = (Graphics2D) g2d.create();
            g2dc.translate( controlPointArrow.x, controlPointArrow.y );
            g2dc.rotate( angleControl - tAngle );
            g2dc.draw( new Line2D.Double( -5, -5, 0, 0 ) );
            g2dc.draw( new Line2D.Double( -5, 5, 0, 0 ) );
            g2dc.dispose();
            
        }
        
        
        g2d.dispose();
        
    }

    @Override
    public boolean intercepts( double x, double y ) {
        return false;
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
