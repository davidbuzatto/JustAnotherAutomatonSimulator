/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.utils;

import br.com.davidbuzatto.jaas.fa.FiniteAutomaton;
import br.com.davidbuzatto.jaas.fa.State;
import java.util.List;

/**
 *
 * @author David
 */
public class Utils {
    
    public static void organize( FiniteAutomaton fa, double x, double y, double radius ) {
        
        List<State> states = fa.getStates();
        double incr = 360.0 / fa.getStates().size();
        double ang = 180;
        
        for ( State s : states ) {
            
            double sx = x + Math.cos( Math.toRadians( ang ) ) * radius;
            double sy = y + Math.sin( Math.toRadians( ang ) ) * radius;
            s.setPosition( sx, sy );
            ang += incr;
            
        }
        
    }
    
}
