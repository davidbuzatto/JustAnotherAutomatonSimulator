/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.fa;

import br.com.davidbuzatto.jaas.gui.TransitionFunctionTableModel;
import br.com.davidbuzatto.jaas.utils.Constants;
import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author David
 */
public class NFA extends FiniteAutomaton implements Serializable {
    
    private static final long serialVersionUID = Constants.SERIAL_VERSION;
    
    private Map<State, Set<State>> ecloses;
    
    public NFA() {
        ecloses = new HashMap<>();
    }
    
    @Override
    public void addTransition( State source, State target, char symbol ) 
            throws IllegalArgumentException {
        
        for ( Transition t : source.getTransitions() ) {
            if ( t.getSymbol() == symbol && t.getTarget().equals( target ) ) {
                throw new IllegalArgumentException( 
                    "The \"" + source + " state already have a transition "
                  + "with the \"" + symbol + "\" symbol to the \"" + target + " state!" );
            }
        }
        
        source.addTransition( target, symbol );
        
    }
    
    @Override
    public boolean accepts( String string, List<State> stepByStepList ) throws IllegalStateException {
        DFA dfa = constructEquivalentDFA();
        return dfa.accepts( string, stepByStepList );
    }
        
    @Override
    public String generateTransitionFunctionRep() {
        
        StringBuilder sb = new StringBuilder();
        Set<Character> symbols = collectSymbols( false );
        String line = generateLine( symbols.size() + 1 );
        
        sb.append( "\u03B4:\t" );
        for ( char s : symbols ) {
            if ( s == '\0' ) {
                s = '\u03B5';
            }
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
                StringBuilder tt = new StringBuilder();
                boolean firstT = true;
                
                for ( Transition t : e.getTransitions() ) {
                    if ( s == t.getSymbol() ) {
                        if ( firstT ) {
                            firstT = false;
                        } else {
                            tt.append( ", " );
                        }
                        tt.append( t.getTarget() );
                    }
                }
                
                if ( !tt.isEmpty() ) {
                    sb.append( "| " ).append( "{" ).append( tt ).append( "}\t" );
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
        Set<Character> symbols = collectSymbols( false );
        
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
                
                StringBuilder tt = new StringBuilder();
                boolean firstT = true;
                
                for ( Transition t : e.getTransitions() ) {
                    if ( s == t.getSymbol() ) {
                        if ( firstT ) {
                            firstT = false;
                        } else {
                            tt.append( ", " );
                        }
                        tt.append( t.getTarget() );
                    }
                }
                
                if ( !tt.isEmpty() ) {
                    data.add( "{" + tt + "}" );
                } else {
                    data.add( "\u2205" );
                }
                
                
                
            }
            
            tm.getData().add( data );
            
        }
        
        return tm;
        
    }
    
    public void discoverEcloses() {
        ecloses.clear();
        for ( State s : states ) {
            ecloses.put( s, discoverEclose( s ) );
        }
    }
    
    public Set<State> eclose( State s ) {
        return ecloses.get( s );
    }
    
    private Set<State> discoverEclose( State q ) {
        
        Set<State> eclose = new TreeSet<>();
        Set<State> visited = new HashSet<>();
        
        discoverEcloseR( q, eclose, visited );
        
        return eclose;
        
    }
    
    private void discoverEcloseR( State q, Set<State> eclose, Set<State> visited ) {
        
        if ( !visited.contains( q ) ) {
            eclose.add( q );
            visited.add( q );
            for ( Transition t : q.getTransitions() ) {
                if ( t.getSymbol() == '\0' ) {
                    discoverEcloseR( t.getTarget(), eclose, visited );
                }
            }
        }
        
    }
    
    public DFA constructEquivalentDFA() throws IllegalStateException {
        
        if ( initial == null ) {
            throw new IllegalStateException( 
                    "This NFA doesn't have an initial state!" );
        }
        
        if ( countFinalStates() == 0 ) {
            throw new IllegalStateException( 
                    "This NFA doesn't have at least one final state!" );
        }
        
        // pretty naive algorithm... it really needs to be improved,
        // but not now :P
        DFA dfa = new DFA();
        Queue<State> queue = new ArrayDeque<>();
        Set<State> processed = new HashSet<>();
        Set<State> finalStates = new HashSet<>();
        Set<Character> symbols = collectSymbols( true );
        char aliasC = 'A';
        
        discoverEcloses();
        
        State q0 = dfa.addState( true, initial.isFinal(), 0, 0 );
        q0.setInternalStates( eclose( initial ) );
        q0.setAlias( Character.toString( aliasC ) );
        queue.offer( q0 );
        
        for ( State s : states ) {
            if ( s.isFinal() ) {
                finalStates.add( s );
            }
        }
        
        while ( !queue.isEmpty() ) {
            
            State current = queue.remove();
            
            if ( !processed.contains( current ) ) {
                
                processed.add( current );
            
                for ( char s : symbols ) {

                    if ( s != '\0' ) {
                        
                        Set<State> nextInternalStates = new TreeSet<>();
                        Set<State> eclosedStates = new TreeSet<>();

                        for ( State st : current.getInternalStates() ) {
                            //System.out.println( st + " " + s );
                            for ( Transition t : st.getTransitions() ) {
                                if ( t.getSymbol() == s ) {
                                    //System.out.println( t );
                                    nextInternalStates.add( t.getTarget() );
                                    //break;
                                }
                            }
                        }
                        //System.out.println( "first: " + nextInternalStates );

                        if ( !nextInternalStates.isEmpty() ) {

                            for ( State st : nextInternalStates ) {
                                eclosedStates.addAll( eclose( st ) );
                            }
                            
                            //System.out.println( "eclose: " + eclosedStates );
                            //System.out.println( "" );

                            State n = null;
                            
                            for ( State st : dfa.states ) {
                                if ( st.getInternalStates().equals( eclosedStates ) ) {
                                    n = st;
                                    break;
                                }
                            }
                            
                            if ( n == null ) {

                                n = dfa.addState( false, false, 0, 0 );
                                if ( aliasC < 'Z' ) {
                                    aliasC++;
                                }
                                n.setAlias( Character.toString( aliasC ) );
                                
                            } 
                            
                            n.setInternalStates( eclosedStates );
                            dfa.addTransition( current, n, s );
                            queue.offer( n );

                        }

                    }

                }
            
            }
            
        }
        
        for ( State s : dfa.getStates() ) {
            for ( State is : s.getInternalStates() ) {
                if ( finalStates.contains( is ) ) {
                    s.setFinal( true );
                }
            }
        }
        
        /*for ( State s : dfa.states ) {
            System.out.println( s );
            for ( Transition t : s.getTransitions() ) {
                System.out.println( "    " + t );
            }
        }*/
        
        return dfa;
        
    }
    
    public static void main( String[] args ) {
        
        /*FiniteAutomaton nfa = new NFA();
        
        State q0 = nfa.addState( true, false, 100, 200 );
        State q1 = nfa.addState( false, false, 250, 200 );
        State q2 = nfa.addState( false, true, 400, 200 );
        
        nfa.addTransition( q0, q0, '0' );
        nfa.addTransition( q0, q0, '1' );
        nfa.addTransition( q0, q1, '0' );
        
        nfa.addTransition( q1, q2, '1' );
        
        System.out.println( nfa );*/
        
        NFA enfa = new NFA();
        
        State q0 = enfa.addState( true, false, 100, 200 );
        State q1 = enfa.addState( false, false, 250, 100 );
        State q2 = enfa.addState( false, false, 400, 150 );
        State q3 = enfa.addState( false, false, 550, 100 );
        State q4 = enfa.addState( false, false, 400, 350 );
        State q5 = enfa.addState( false, true, 700, 200 );
        
        enfa.addTransition( q0, q1, '+' );
        enfa.addTransition( q0, q1, '-' );
        enfa.addTransition( q0, q1, '\0' );
        
        enfa.addTransition( q1, q2, '.' );
        enfa.addTransition( q4, q3, '.' );
        
        enfa.addTransition( q3, q5, '\0' );
        
        for ( char c : new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' } ) {
            enfa.addTransition( q1, q1, c );
            enfa.addTransition( q1, q4, c );
            enfa.addTransition( q2, q3, c );
            enfa.addTransition( q3, q3, c );
        }
        
        /*enfa.discoverEcloses();
        for ( State s : enfa.states ) {
            System.out.println( enfa.eclose( s ) );
        }*/
        
        DFA equivalent = enfa.constructEquivalentDFA();
        System.out.println( equivalent );
        
    }
    
}
