/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.dfa;

import br.com.davidbuzatto.jaas.gui.TransitionFunctionTableModel;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author David
 */
public class DFA extends FiniteAutomaton implements Serializable {
    
    @Override
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
    
    @Override
    public boolean accepts( String string, List<State> stepByStepList ) throws IllegalStateException {
        
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
        
        if ( stepByStepList != null ) {
            stepByStepList.add( current );
        }
        
        for ( char c : string.toCharArray() ) {
            
            found = false;
            
            for ( Transition t : current.getTransitions() ) {
                
                if ( t.getSymbol() == c ) {
                    
                    current = t.getTarget();
                    found = true;
                    acceptedSymbols++;
                    
                    if ( stepByStepList != null ) {
                        stepByStepList.add( current );
                    }
                    
                    break;
                    
                }
                
            }
            
            if ( !found ) {
                return false;
            }

        }
        
        return current.isFinal() && acceptedSymbols == string.length();
        
    }
    
    @Override
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
            
            sb.append( e ).append( "\t" );
            
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
    
    @Override
    public TransitionFunctionTableModel createTransitionFunctionTableModel() {
        
        TransitionFunctionTableModel tm = new TransitionFunctionTableModel();
        Set<Character> symbols = collectSymbols();
        
        for ( char s : symbols ) {
            tm.getSymbols().add( String.valueOf( s ) );
        }
        
        for ( State e : states ) {
            
            StringBuilder sb = new StringBuilder();
            
            if ( e.isFinal() ) {
                sb.append( "*" );
            }
            
            if ( e == initial ) {
                sb.append( "->" );
            }
            
            sb.append( e );
            
            Collections.sort( e.getTransitions(), new Comparator<Transition>(){
                @Override
                public int compare( Transition o1, Transition o2 ) {
                    return o1.getSymbol() - o2.getSymbol();
                }
            });
            
            tm.getStates().add( sb.toString() );
            
            List<String> data = new ArrayList<>();
            data.add( sb.toString() );
            
            for ( char s : symbols ) {
                
                Transition tf = null;
                
                for ( Transition t : e.getTransitions() ) {
                    if ( s == t.getSymbol() ) {
                        tf = t;
                        break;
                    }
                }
                if ( tf != null ) {
                    data.add( tf.getTarget().toString() );
                } else {
                    data.add( "\u2205" );
                }
                
            }
            
            tm.getData().add( data );
            
        }
        
        return tm;
        
    }
    
    public static void main( String[] args ) {
        
        FiniteAutomaton dfa = new DFA();
        
        /*State q0 = dfa.addState( true, false, 100, 100 );
        State q1 = dfa.addState( false, false, 300, 100 );
        State q2 = dfa.addState( false, false, 500, 100 );
        State q3 = dfa.addState( false, true, 700, 100 );
        
        dfa.addTransition( q0, q0, '0' );
        dfa.addTransition( q0, q0, '5' );
        dfa.addTransition( q0, q1, '1' );
        dfa.addTransition( q0, q1, 'a' );
        
        dfa.addTransition( q1, q1, '1' );
        dfa.addTransition( q1, q1, '5' );
        dfa.addTransition( q1, q2, '2' );
        dfa.addTransition( q1, q2, 'b' );
        
        dfa.addTransition( q2, q2, '2' );
        dfa.addTransition( q2, q2, '5' );
        dfa.addTransition( q2, q3, '3' );
        dfa.addTransition( q2, q3, 'c' );
        
        dfa.addTransition( q3, q3, '3' );
        dfa.addTransition( q3, q3, '5' );*/
        
        /*State q0 = dfa.addState( true, false, 100, 200 );
        State q1 = dfa.addState( false, false, 300, 100 );
        State q2 = dfa.addState( false, true, 500, 100 );
        State q3 = dfa.addState( false, true, 300, 300 );
        State q4 = dfa.addState( false, false, 500, 300 );
        
        dfa.addTransition( q0, q1, '0' );
        dfa.addTransition( q0, q3, '1' );
        
        dfa.addTransition( q1, q2, '0' );
        dfa.addTransition( q2, q1, '0' );
        
        dfa.addTransition( q3, q4, '1' );
        dfa.addTransition( q4, q3, '1' );*/
        
        State q0 = dfa.addState( true, false, 100, 200 );
        State q1 = dfa.addState( false, false, 250, 200 );
        State q2 = dfa.addState( false, true, 400, 200 );
        
        dfa.addTransition( q0, q0, '1' );
        dfa.addTransition( q0, q1, '0' );
        
        dfa.addTransition( q1, q1, '0' );
        dfa.addTransition( q1, q2, '1' );
        
        dfa.addTransition( q2, q2, '0' );
        dfa.addTransition( q2, q2, '1' );
        
        System.out.println( dfa );
        
    }
    
}
