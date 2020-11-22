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
    
    public static final String VERSION = "v1.01";
    public static final long SERIAL_VERSION = 1;
    
    public static final double STATE_RADIUS = 25;
    
    public static final Color STATE_FILL_COLOR = Color.WHITE;
    public static final Color STATE_STROKE_COLOR = Color.BLACK;
    public static final Color SELECTED_STATE_FILL_COLOR = new Color( 217, 242, 255 );
    public static final Color MOUSE_OVER_STATE_FILL_COLOR = new Color( 255, 223, 202 );
    public static final Color SELECTED_STATE_STROKE_COLOR = new Color( 0, 105, 155 );
    public static final Color MOUSE_OVER_STATE_STROKE_COLOR = new Color( 255, 107, 9 );
    
    public static final Color TRANSITION_STROKE_COLOR = Color.BLACK;
    public static final Color SELECTED_TRANSITION_FILL_COLOR = new Color( 217, 242, 255, 200 );
    public static final Color SELECTED_TRANSITION_STROKE_COLOR = new Color( 0, 105, 155, 200 );
    public static final Color MOUSE_OVER_TRANSITION_FILL_COLOR = new Color( 255, 223, 202, 200 );
    public static final Color MOUSE_OVER_TRANSITION_STROKE_COLOR = new Color( 255, 107, 9, 200 );
    
    public static final Color PROCESSING_STRING_COLOR = Color.BLACK;
    public static final Color PROCESSING_STRING_CURRENT_SYMBOL_COLOR = new Color( 0, 105, 155 );
    public static final Color PROCESSING_STRING_CONTROL_COLOR = new Color( 0, 105, 155 );
    public static final Color PROCESSING_STRING_ACCEPTED_COLOR = new Color( 0, 128, 64 );
    public static final Color PROCESSING_STRING_REJECTED_COLOR = new Color( 150, 0, 0 );
    public static final Color PROCESSING_STRING_REJECTED_CURRENT_SYMBOL_COLOR = Color.RED;
    public static final Color PROCESSING_STRING_REJECTED_CONTROL_COLOR = Color.RED;
    
    public static final BasicStroke STATE_STROKE = new BasicStroke( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    public static final BasicStroke TRANSITION_STROKE = new BasicStroke( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    public static final BasicStroke PROCESSING_STRING_STROKE = new BasicStroke( 2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    
    public static final Font STATE_FONT = new Font( "monospaced", Font.BOLD, 16 );
    public static final Font TRANSITION_FONT = new Font( "monospaced", Font.BOLD, 16 );
    public static final Font PROCESSING_STRING_FONT = new Font( "monospaced", Font.BOLD, 30 );
    public static final Font PROCESSING_STRING_STATE_FONT = new Font( "monospaced", Font.BOLD, 16 );
    
}
