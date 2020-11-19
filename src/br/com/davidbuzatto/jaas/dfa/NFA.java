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
public class NFA extends FiniteAutomaton implements Serializable {
    
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
        
        if ( initial == null ) {
            throw new IllegalStateException( 
                    "This NFA doesn't have an initial state!" );
        }
        
        if ( countFinalStates() == 0 ) {
            throw new IllegalStateException( 
                    "This NFA doesn't have at least one final state!" );
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
            
            sb.append( "q" ).append( e.getNumber() ).append( "\t" );
            
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
    
}
