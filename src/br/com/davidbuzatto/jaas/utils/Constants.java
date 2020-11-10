/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author David
 */
public class Constants {
    
    public static final String VERSION = "v0.1";
    
    public static final double STATE_RADIUS = 25;
    
    public static final Color STATE_FILL_COLOR = Color.WHITE;
    public static final Color SELECTED_STATE_FILL_COLOR = new Color( 217, 242, 255 );
    public static final Color STATE_STROKE_COLOR = Color.BLACK;
    public static final Color SELECTED_STATE_STROKE_COLOR = new Color( 0, 105, 155 );
    
    public static final Color TRANSITION_STROKE_COLOR = Color.BLACK;
    public static final Color SELECTED_TRANSITION_STROKE_COLOR = new Color( 0, 105, 155 );
    
    public static final BasicStroke STATE_STROKE = new BasicStroke( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    public static final BasicStroke TRANSITION_STROKE = new BasicStroke( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    
    public static final Font STATE_FONT = new Font( "monospaced", Font.BOLD, 16 );
    public static final Font TRANSITION_FONT = new Font( "monospaced", Font.BOLD, 16 );
    
}
