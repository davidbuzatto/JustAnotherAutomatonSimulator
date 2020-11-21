/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.fa;

import br.com.davidbuzatto.jaas.gui.TransitionFunctionTableModel;
import br.com.davidbuzatto.jaas.gui.geom.Shape;
import java.awt.Graphics2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
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
    protected List<DrawingTransition> drawingTransitions;
    
    public FiniteAutomaton() {
        states = new ArrayList<>();
        drawingTransitions = new ArrayList<>();
    }
    
    @Override
    public void draw( Graphics2D g2d ) {
        
        g2d = (Graphics2D) g2d.create();
        
        for ( State s : states ) {
            for ( DrawingTransition dt : s.getDrawingTransitions() ) {
                dt.draw( g2d );
            }
        }
        
        for ( State s : states ) {
            s.draw( g2d );
        }
        
        g2d.dispose();
        
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
    
    public DrawingTransition getInterceptedDrawingTransition( double x, double y ) {
        
        for ( State s : states ) {
            for ( DrawingTransition dt : s.getDrawingTransitions() ) {
                    if ( dt.intercepts( x, y ) ) {
                    return dt;
                }
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
        
        for ( char c : collectSymbols( true ) ) {
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
    
    protected Set<Character> collectSymbols( boolean removeEpsilon ) {
        Set<Character> symbols = new TreeSet<>();
        for ( State s : states ) {
            for ( Transition t : s.getTransitions() ) {
                symbols.add( t.getSymbol() );
            }
        }
        if ( removeEpsilon ) {
            symbols.remove( '\0' );
            symbols.remove( '\u03B5' );
        }
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
