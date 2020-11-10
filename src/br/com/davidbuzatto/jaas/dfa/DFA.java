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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author David
 */
public class DFA extends Shape {
    
    private int stateNumberCount;
    private State initial;
    private List<State> states;
    
    public DFA() {
        states = new ArrayList<>();
    }

    @Override
    public void draw( Graphics2D g2d ) {
        
        calculateDrawingBounds();
        g2d = (Graphics2D) g2d.create();
        
        processAndDrawTransitionSymbols( g2d );
        
        for ( State s : states ) {
            for ( Transition t : s.getTransitions() ) {
                t.draw( g2d );
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
                    hm.put( key, new TreeSet<>() );
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
                
                hm.get( key ).add( t.getSymbol() );
                
            }
            
        }
        
        g2d.setFont( Constants.TRANSITION_FONT );
        FontMetrics fm = g2d.getFontMetrics();
        
        g2d.setColor( Constants.TRANSITION_STROKE_COLOR );
        
        for ( Entry<String, Set<Character>> e : hm.entrySet() ) {
            
            Point2D.Double p = hmp.get( e.getKey() );
            String label = e.getValue().toString();
            label = label.substring( 1, label.length() - 1 );
            
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
    
    public void addTransition( State source, State target, char symbol ) 
            throws IllegalArgumentException {
        
        for ( Transition t : source.getTransitions() ) {
            if ( t.getSymbol() == symbol ) {
                throw new IllegalArgumentException( 
                    "The \"" + source + " state already have a transition "
                  + "with the \"" + symbol + "\" symbol!" );
            }
        }
        
        source.addTransition( target, symbol );
        
    }
    
    public boolean accepts( String string ) throws IllegalStateException {
        
        if ( initial == null ) {
            throw new IllegalStateException( 
                    "This DFA doesn't have an initial state!" );
        }
        
        if ( countFinalStates() == 0 ) {
            throw new IllegalStateException( 
                    "This DFA doesn't have at least one final state!" );
        }
        
        boolean found;
        int acceptedSymbols = 0;
        State current = initial;
        
        for ( char c : string.toCharArray() ) {
            
            found = false;
            
            for ( Transition t : current.getTransitions() ) {
                
                if ( t.getSymbol() == c ) {
                    
                    current = t.getTarget();
                    found = true;
                    acceptedSymbols++;
                    break;
                    
                }
                
            }
            
            if ( !found ) {
                return false;
            }

        }
        
        return current.isFinal() && acceptedSymbols == string.length();
        
    }

    private int countFinalStates() {
        int count = 0;
        for ( State e : states ) {
            if ( e.isFinal() ) {
                count++;
            }
        }
        return count;
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
    
    public String generateTransitionFunctionRep() {
        
        StringBuilder sb = new StringBuilder();
        Set<Character> symbols = collectSymbols();
        String line = generateLine( symbols.size() + 1 );
        
        sb.append( "\u03B4:\t" );
        for ( char s : symbols ) {
            sb.append( "| " ).append( s ).append( "\t" );
        }
        sb.append( "\n" ).append( line ).append( "\n" );
        
        boolean first = true;
        
        for ( State e : states ) {
            
            if ( !first ) {
                sb.append( "\n" ).append( line ).append( "\n" );
            } else {
                first = false;
            }
            
            if ( e.isFinal() ) {
                sb.append( "*" );
            }
            
            if ( e == initial ) {
                sb.append( "->" );
            }
            
            sb.append( "q" ).append( e.getNumber() ).append( "\t" );
            
            Collections.sort( e.getTransitions(), new Comparator<Transition>(){
                @Override
                public int compare( Transition o1, Transition o2 ) {
                    return o1.getSymbol() - o2.getSymbol();
                }
            });
            
            
            for ( char s : symbols ) {
                
                Transition tf = null;
                
                for ( Transition t : e.getTransitions() ) {
                    if ( s == t.getSymbol() ) {
                        tf = t;
                        break;
                    }
                }
                if ( tf != null ) {
                    sb.append( "| " ).append( tf.getTarget() ).append( "\t" );
                } else {
                    sb.append( "| " ).append( "\u2205" ).append( "\t" );
                }
                
            }
            
        }
        
        return sb.toString();
        
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
                sb.append( String.format( "q%d", i ) );
            }
        }
        
        sb.append( " }" );
        
        return sb.toString();
        
    }
    
    private Set<Character> collectSymbols() {
        Set<Character> symbols = new TreeSet<>();
        for ( State s : states ) {
            for ( Transition t : s.getTransitions() ) {
                symbols.add( t.getSymbol() );
            }
        }
        return symbols;
    }
    
    private String generateLine( int q ) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i < q; i++ ) {
            sb.append( "--------" );
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append( "A = { Q, \u03A3, \u03B4, ").append( generateQ0Rep() ).append( ", F }" ).append( "\n" );
        sb.append( generateQRep() ).append( "\n" );
        sb.append( generateSigmaRep() ).append( "\n" );
        sb.append( generateFRep() ).append( "\n" );
        sb.append( generateTransitionFunctionRep() );
        
        return sb.toString();
        
    }
    
}
