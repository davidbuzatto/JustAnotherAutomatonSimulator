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
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author David
 */
public class DrawingTransition extends Shape implements Serializable {
    
    private State source;
    private State target;
    private Set<Character> symbols;
    
    private Color strokeColor;
    private Color selectedStrokeColor;
    private double controlPointMagnitude;
    
    private double labelControlX;
    private double labelControlY;
    private double labelControlXOffeset;
    private double labelControlYOffeset;
    private double labelControlWidth;
    private double labelControlHeight;
    
    private boolean mouseOver;
    
    private class SymbolsComparator implements Comparator<Character>, Serializable {

        @Override
        public int compare( Character o1, Character o2 ) {
            if ( o1 == '\u03B5' && o2 == '\u03B5' ) {
                return 0;
            } else if ( o1 == '\u03B5' ) {
                return -1;
            } else if ( o2 == '\u03B5' ) {
                return -1;
            } else {
                return o1.compareTo( o2 );
            }
        }
        
    }
    
    public DrawingTransition( State source, State target, Character... symbols ) {
        
        this.source = source;
        this.target = target;
        //this.controlPointMagnitude = 40;
        this.controlPointMagnitude = 0;
        
        this.symbols = new TreeSet<>( new SymbolsComparator() );
        this.symbols.addAll( Arrays.<Character>asList( symbols ) );
        
        if ( this.symbols.contains( '\0' ) ) {
            this.symbols.remove( '\0' );
            this.symbols.add( '\u03B5' );
        }
        
        strokeColor = Constants.TRANSITION_STROKE_COLOR;
        selectedStrokeColor = Constants.SELECTED_TRANSITION_STROKE_COLOR;
    
    }

    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        g2d.setStroke( Constants.TRANSITION_STROKE );
        
        if ( selected ) {
            g2d.setColor( selectedStrokeColor );
        } else if ( mouseOver ) {
            g2d.setColor( Constants.MOUSE_OVER_TRANSITION_STROKE_COLOR );
        } else {
            g2d.setColor( strokeColor );
        }
                    
        double angle = Math.atan2( 
                target.getYStart() + Constants.STATE_RADIUS - ( source.getYStart() + Constants.STATE_RADIUS ),
                target.getXStart() + Constants.STATE_RADIUS - ( source.getXStart() + Constants.STATE_RADIUS ) );
        double sAngle = Math.PI / 2;
        double tAngle = Math.PI / 10;
        
        Point2D.Double midPoint = new Point2D.Double(
                source.getXEnd() + ( target.getXStart() - source.getXEnd() ) / 2,
                source.getYEnd() + ( target.getYStart() - source.getYEnd() ) / 2 );

        Point2D.Double controlPoint = new Point2D.Double( 
                midPoint.x + Math.cos( angle - sAngle ) * controlPointMagnitude,
                midPoint.y + Math.sin( angle - sAngle ) * controlPointMagnitude );
        
        double angleControl = Math.atan2( 
                    target.getYStart() + Constants.STATE_RADIUS - ( controlPoint.y ),
                    target.getXStart() + Constants.STATE_RADIUS - ( controlPoint.x ) );
            
        Point2D.Double controlPointArrow = new Point2D.Double( 
                target.getXStart() + Constants.STATE_RADIUS - Math.cos( angleControl ) * Constants.STATE_RADIUS,
                target.getYStart() + Constants.STATE_RADIUS - Math.sin( angleControl ) * Constants.STATE_RADIUS);
            
        /*g2d.setColor( Color.RED );
        g2d.fillRect( (int) (midPoint.x),
                      (int) (midPoint.y), 5, 5 );
        g2d.setColor( Color.BLUE );
        g2d.fillRect( (int) (controlPoint.x),
                      (int) (controlPoint.y), 5, 5 );
        g2d.setColor( Color.BLACK );*/

        double xSymbols;
        double ySymbols;
                    
        if ( source.equals( target ) ) {
            
            xSymbols = source.getXStart() + Constants.STATE_RADIUS;
            ySymbols = source.getYStart() - Constants.STATE_RADIUS + 5;
            
            Point2D.Double symbolsPoint = new Point2D.Double( 
                    xSymbols + labelControlXOffeset, 
                    ySymbols + labelControlYOffeset );

            double angleSymbols = Math.atan2( 
                    target.getYStart() + Constants.STATE_RADIUS - ( symbolsPoint.y ),
                    target.getXStart() + Constants.STATE_RADIUS - ( symbolsPoint.x ) );
            
            /*g2d.setColor( Color.RED );
            g2d.fillRect( (int) ( symbolsPoint.x ),
                          (int) ( symbolsPoint.y ), 5, 5 );*/
            
            Graphics2D g2dArc = (Graphics2D) g2d.create();
            double arcDiameter = Constants.STATE_RADIUS * 2 - 20;
            g2dArc.rotate( angleSymbols - sAngle, 
                    source.getXStart() + Constants.STATE_RADIUS, 
                    source.getYStart() + Constants.STATE_RADIUS );
            g2dArc.draw( new Ellipse2D.Double( 
                    source.getXStart() + Constants.STATE_RADIUS - 15, 
                    source.getYStart() - Constants.STATE_RADIUS + 10, 
                    arcDiameter, 
                    arcDiameter ) );
            
            Graphics2D g2dArrow = (Graphics2D) g2dArc.create();
            g2dArrow.translate( source.getXStart(), source.getYStart() + Constants.STATE_RADIUS );
            g2dArrow.rotate( Math.toRadians( 100 ), 10, -21 );
            g2dArrow.draw( new Line2D.Double( 3, -26, 10, -21 ) );
            g2dArrow.draw( new Line2D.Double( 5, -16, 10, -21 ) );
            g2dArrow.dispose();
            g2dArc.dispose();
                    
        } else {
            
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
                    source.getXStart() + Constants.STATE_RADIUS,
                    source.getYStart() + Constants.STATE_RADIUS,
                    controlPoint.x,
                    controlPoint.y,
                    target.getXStart() + Constants.STATE_RADIUS, 
                    target.getYStart() + Constants.STATE_RADIUS ) );
            
            Graphics2D g2dc = (Graphics2D) g2d.create();
            g2dc.translate( controlPointArrow.x, controlPointArrow.y );
            g2dc.rotate( angleControl - tAngle );
            g2dc.draw( new Line2D.Double( -5, -5, 0, 0 ) );
            g2dc.draw( new Line2D.Double( -5, 5, 0, 0 ) );
            g2dc.dispose();
            
            xSymbols = target.getXStart() > source.getXEnd() ? 
                source.getXEnd() + ( target.getXStart() - source.getXEnd() ) / 2 :
                target.getXEnd() + ( source.getXStart() - target.getXEnd() ) / 2; 
            ySymbols = target.getYStart() > source.getYEnd() ? 
                source.getYEnd() + ( target.getYStart() - source.getYEnd() ) / 2 - 5:
                target.getYEnd() + ( source.getYStart() - target.getYEnd() ) / 2 - 5;
            /*xSymbols = controlPoint.x; 
            ySymbols = controlPoint.y;*/
            
        }
        
        g2d.setFont( Constants.TRANSITION_FONT );
        FontMetrics fm = g2d.getFontMetrics();
        
        String label = symbols.toString();
        label = label.substring( 1, label.length() - 1 );

        if ( symbols.size() > 3 ) {
            int is = label.indexOf( ", ", label.indexOf( ", " ) + 1 );
            if ( is != -1 ) {
                label = label.substring( 0, is + 1 ) + "..., " + label.substring( label.lastIndexOf( " " ) + 1 );
            }
        }

        int w = fm.stringWidth( label );
        
        /*if ( mouseOver ) {
            g2d.setColor( Color.RED );
        } else {
            g2d.setColor( Constants.TRANSITION_STROKE_COLOR );
        }*/
        
        labelControlX = (int) ( xSymbols - w / 2 ) - 5;
        labelControlY = (int) ySymbols - 15;
        labelControlWidth = w + 10;
        labelControlHeight = 20;
        
        if ( selected || mouseOver ) {
            
            if ( selected ) {
                g2d.setColor( Constants.SELECTED_TRANSITION_FILL_COLOR );
            } else {
                g2d.setColor( Constants.MOUSE_OVER_TRANSITION_FILL_COLOR );
            }
            
            g2d.fillRoundRect( (int) ( labelControlX + labelControlXOffeset ), 
                      (int) ( labelControlY + labelControlYOffeset ), 
                      (int) labelControlWidth, 
                      (int) labelControlHeight, 10, 10 );
            
            if ( selected ) {
                g2d.setColor( Constants.SELECTED_TRANSITION_STROKE_COLOR );
            } else {
                g2d.setColor( Constants.MOUSE_OVER_TRANSITION_STROKE_COLOR );
            }
            
            g2d.drawRoundRect( (int) ( labelControlX + labelControlXOffeset ), 
                      (int) ( labelControlY + labelControlYOffeset ), 
                      (int) labelControlWidth, 
                      (int) labelControlHeight, 10, 10 );
            
        }
        
        if ( selected ) {
            g2d.setColor( selectedStrokeColor );
        } else if ( mouseOver ) {
            g2d.setColor( Constants.MOUSE_OVER_TRANSITION_STROKE_COLOR );
        } else {
            g2d.setColor( strokeColor );
        }
        
        g2d.drawString( label, (int) ( labelControlX + labelControlXOffeset ) + 5, (int) ( labelControlY + labelControlYOffeset ) + 15 );
        
        g2d.dispose();
        
    }

    @Override
    public boolean intercepts( double x, double y ) {
        return x >= labelControlX + labelControlXOffeset && x <= labelControlX + labelControlXOffeset + labelControlWidth &&
               y >= labelControlY + labelControlYOffeset && y <= labelControlY + labelControlYOffeset + labelControlHeight;
    }

    @Override
    public void move( double difX, double difY ) {
        labelControlXOffeset += difX;
        labelControlYOffeset += difY;
    }
    
    public void changeControlPointMagnitude( double amount ) {
        controlPointMagnitude += amount;
    }
    
    public void addSymbol( char symbol ) {
        if ( symbol == '\0' ) {
            symbol = '\u03B5';
        }
        if ( !symbols.contains( symbol ) ) {
            symbols.add( symbol );
        }
    }
    
    public void removeSymbol( char symbol ) {
        if ( symbol == '\0' || symbol == '\u03B5' ) {
            symbols.remove( '\0' );
            symbols.remove( '\u03B5' );
        } else {
            symbols.remove( symbol );
        }
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

    public Set<Character> getSymbols() {
        return symbols;
    }

    public void setSymbols( Set<Character> symbols ) {
        this.symbols = symbols;
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

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver( boolean mouseOver ) {
        this.mouseOver = mouseOver;
    }
    
}
