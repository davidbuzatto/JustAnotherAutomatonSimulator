/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.fa;

import br.com.davidbuzatto.jaas.gui.TransitionFunctionTableModel;
import br.com.davidbuzatto.jaas.gui.geom.Shape;
import br.com.davidbuzatto.jaas.utils.Constants;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author David
 */
public abstract class FiniteAutomaton extends Shape implements Serializable {
    
    protected int stateNumberCount;
    protected State initial;
    protected List<State> states;
    
    public FiniteAutomaton() {
        states = new ArrayList<>();
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        processAndDrawTransitionSymbols( g2d );
        
        Set<String> alreadyDrawnTransitions = new HashSet<>();
        
        for ( State s : states ) {
            for ( Transition t : s.getTransitions() ) {
                String tr = String.format( "%s-%s", t.getSource(), t.getTarget() );
                if ( !alreadyDrawnTransitions.contains( tr ) ) {
                    t.draw( g2d );
                    alreadyDrawnTransitions.add( tr );
                }
            }
        }
        
        for ( State s : states ) {
            s.draw( g2d );
        }
        
        g2d.dispose();
        
    }

    private void processAndDrawTransitionSymbols( Graphics2D g2d ) {
        
        Map<String, Set<Character>> hm = new HashMap<>();
        Map<String, Point2D.Double> hmp = new HashMap<>();
        
        for ( State s : states ) {
            
            for ( Transition t : s.getTransitions() ) {
                
                String key = String.format( "%s-%s", t.getSource(), t.getTarget() );
                
                if ( !hm.containsKey( key ) ) {
                    hm.put( key, new TreeSet<>( new Comparator<Character>(){
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
                    }));
                }
                
                if ( !hmp.containsKey( key ) ) {
                    
                    State source = t.getSource();
                    State target = t.getTarget();
                    
                    double angle = Math.atan2( target.getYStartD() + Constants.STATE_RADIUS - ( source.getYStartD() + Constants.STATE_RADIUS ),
                            target.getXStartD() + Constants.STATE_RADIUS - ( source.getXStartD() + Constants.STATE_RADIUS ) );
                    double sAngle = Math.PI / 2;
            
                    Point2D.Double midPoint = new Point2D.Double(
                            source.getXEndD() + ( target.getXStartD() - source.getXEndD() ) / 2,
                            source.getYEndD() + ( target.getYStartD() - source.getYEndD() ) / 2 );
            
                    Point2D.Double controlPoint = new Point2D.Double( 
                            midPoint.x + Math.cos( angle - sAngle ) * 40,
                            midPoint.y + Math.sin( angle - sAngle ) * 40 );
                    
                    /*g2d.setColor( Color.RED );
                    g2d.fillRect( (int) (midPoint.x),
                                  (int) (midPoint.y), 5, 5 );
                    g2d.setColor( Color.BLUE );
                    g2d.fillRect( (int) (controlPoint.x),
                                  (int) (controlPoint.y), 5, 5 );
                    g2d.setColor( Color.BLACK );*/
                    
                    double x;
                    double y;
                    
                    if ( source.equals( target ) ) {
                        
                        x = (int) ( source.getXStartD() + Constants.STATE_RADIUS );
                        y = (int) ( source.getYStartD() - Constants.STATE_RADIUS + 5 );
                        
                    } else {
                        
                        /*x = target.getXStartD() > source.getXEndD() ? 
                            source.getXEndD() + ( target.getXStartD() - source.getXEndD() ) / 2 :
                            target.getXEndD() + ( source.getXStartD() - target.getXEndD() ) / 2; 
                        y = target.getYStartD() > source.getYEndD() ? 
                            source.getYEndD() + ( target.getYStartD() - source.getYEndD() ) / 2 - 5:
                            target.getYEndD() + ( source.getYStartD() - target.getYEndD() ) / 2 - 5;*/
                        x = controlPoint.x; 
                        y = controlPoint.y;
                        
                    }
                    
                    hmp.put( key, new Point2D.Double( x, y ) );
                    
                }
                
                if ( t.getSymbol() == '\0' ) {
                    hm.get( key ).add( '\u03B5' );
                } else {
                    hm.get( key ).add( t.getSymbol() );
                }
                
            }
            
        }
        
        g2d.setFont( Constants.TRANSITION_FONT );
        FontMetrics fm = g2d.getFontMetrics();
        
        g2d.setColor( Constants.TRANSITION_STROKE_COLOR );
        
        for ( Map.Entry<String, Set<Character>> e : hm.entrySet() ) {
            
            Point2D.Double p = hmp.get( e.getKey() );
            String label = e.getValue().toString();
            label = label.substring( 1, label.length() - 1 );
            
            if ( e.getValue().size() > 3 ) {
                int is = label.indexOf( ", ", label.indexOf( ", " ) + 1 );
                if ( is != -1 ) {
                    label = label.substring( 0, is + 1 ) + "..., " + label.substring( label.lastIndexOf( " " ) + 1 );
                }
            }
            
            int w = fm.stringWidth( label );
            
            g2d.drawString( label, (int) ( p.x - w / 2 ), (int) p.y );
            
        }
        
    }
    
    @Override
    public boolean intercepts( double x, double y ) {
        return false;
    }
    
    public State getInterceptedState( double x, double y ) {
        
        ListIterator<State> li = states.listIterator( states.size() );
        
        while ( li.hasPrevious() ) {
            
            State state = li.previous();
            
            if ( state.intercepts( x, y ) ) {
                return state;
            }
            
        }
        
        return null;
        
    }
    
    public State addState( boolean initial, boolean finall, double x, double y ) 
            throws IllegalArgumentException {
        
        State newState = new State( stateNumberCount++, initial, finall, x, y );
        states.add( newState );
        
        if ( initial ) {
            if ( this.initial != null ) {
                this.initial.setInitial( false );
            }
            this.initial = newState;
        }
        
        return newState;
        
    }

    public List<State> getStates() {
        return states;
    }
    
    public void removeState( State state ) {
        
        for ( State s : states ) {
            if ( !s.equals( state ) ) {
                List<Transition> ts = new ArrayList<>();
                for ( Transition t : s.getTransitions() ) {
                    if ( t.getTarget().equals( state ) ) {
                        ts.add( t );
                    }
                }
                s.getTransitions().removeAll( ts );
            }
        }
        
        states.remove( state );
        
        if ( initial != null ) {
            if ( initial.equals( state ) ) {
                initial = null;
            }
        }
        
    }
    
    public State getInitial() {
        return initial;
    }

    public void setInitial( State initial ) {
        this.initial = initial;
    }
    
    public void clear() {
        while ( !states.isEmpty() ) {
            removeState( states.get( 0 ) );
        }
        stateNumberCount = 0;
    }
    
    protected int countFinalStates() {
        int count = 0;
        for ( State e : states ) {
            if ( e.isFinal() ) {
                count++;
            }
        }
        return count;
    }
    
    public abstract void addTransition( State source, State target, char symbol );
    public abstract boolean accepts( String string, List<State> stepByStepList );
    public abstract String generateTransitionFunctionRep();
    public abstract TransitionFunctionTableModel createTransitionFunctionTableModel();
    
    public String generateQRep() {
        
        StringBuilder sb = new StringBuilder();
        sb.append( "Q = { " );
        
        for ( int i = 0; i < states.size(); i++ ) {
            if ( i != 0 ) {
                sb.append( ", " );
            }
            sb.append( states.get( i ) );
        }
        
        sb.append( " }" );
        
        return sb.toString();
        
    }
    
    
    public String generateSigmaRep() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( "\u03A3 = { " );
        boolean first = true;
        
        for ( char c : collectSymbols() ) {
            if ( !first ) {
                sb.append( ", " );
            } else {
                first = false;
            }
            sb.append( String.format( "%c", c ) );
        }
        
        sb.append( " }" );
        
        return sb.toString();
        
    }
    
    
    public String generateQ0Rep() {
        return initial != null ? initial.toString() : "no initial state!";
    }
    
    public String generateFRep() {
        
        boolean first = true;
        
        StringBuilder sb = new StringBuilder();
        sb.append( "F = { " );
        
        for ( int i = 0; i < states.size(); i++ ) {
            if ( states.get( i ).isFinal() ) {
                if ( !first ) {
                    sb.append( ", " );
                } else {
                    first = false;
                }
                sb.append( states.get( i ) );
            }
        }
        
        sb.append( " }" );
        
        return sb.toString();
        
    }
    
    protected Set<Character> collectSymbols() {
        Set<Character> symbols = new TreeSet<>();
        for ( State s : states ) {
            for ( Transition t : s.getTransitions() ) {
                symbols.add( t.getSymbol() );
            }
        }
        symbols.remove( '\0' );
        return symbols;
    }
    
    protected String generateLine( int q ) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < q; i++ ) {
            sb.append( "--------" );
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( "A = ( Q, \u03A3, \u03B4, ").append( generateQ0Rep() ).append( ", F )" ).append( "\n" );
        sb.append( generateQRep() ).append( "\n" );
        sb.append( generateSigmaRep() ).append( "\n" );
        sb.append( generateFRep() ).append( "\n" );
        sb.append( generateTransitionFunctionRep() );
        
        return sb.toString();
        
    }
    
    
}
