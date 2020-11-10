/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.gui.geom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.Serializable;

/**
 * 
 * @author David
 */
public abstract class Shape implements Serializable {
    
    private static int idCount;
    private int id;
    
    protected double xStart;
    protected double yStart;
    protected double xEnd;
    protected double yEnd;
    
    // drawing coordinates
    protected double xStartD;
    protected double yStartD;
    protected double xEndD;
    protected double yEndD;
    
    protected Color strokeColor;
    protected Color fillColor;
    protected double strokeWidth;
    
    transient protected boolean selected;
    transient private int selectedPhase;
    
    public Shape() {
        strokeColor = Color.BLACK;
        fillColor = Color.WHITE;
        id = idCount++;
    }
    
    public abstract void draw( Graphics2D g2d );
    public abstract boolean intercepts( double x, double y );

    public void move( double difX, double difY ) {
        xStart += difX;
        xEnd += difX;
        yStart += difY;
        yEnd += difY;
    }
    
    public void calculateDrawingBounds() {
        
        xStartD = xStart < xEnd ? xStart : xEnd;
        xEndD = xStart < xEnd ? xEnd : xStart;
        yStartD = yStart < yEnd ? yStart : yEnd;
        yEndD = yStart < yEnd ? yEnd : yStart;
        
    }

    public int getId() {
        return id;
    }
    
    public void setStrokeColor( Color strokeColor ) {
        this.strokeColor = strokeColor;
    }

    public void setFillColor( Color fillColor ) {
        this.fillColor = fillColor;
    }

    public void setStrokeWidth( double strokeWidth ) {
        this.strokeWidth = strokeWidth;
    }

    public void setXStart( double xStart ) {
        this.xStart = xStart;
    }

    public void setXEnd( double xEnd ) {
        this.xEnd = xEnd;
    }

    public void setYStart( double yStart ) {
        this.yStart = yStart;
    }

    public void setYEnd( double yEnd ) {
        this.yEnd = yEnd;
    }

    public double getXStart() {
        return xStart;
    }
    
    public double getXEnd() {
        return xEnd;
    }

    public double getYStart() {
        return yStart;
    }
    
    public double getYEnd() {
        return yEnd;
    }
    
    public double getWidth() {
        return xEnd - xStart;
    }
    
    public double getHeight() {
        return yEnd - yStart;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected( boolean selected ) {
        this.selected = selected;
    }

    public static void setIdCount( int value ) {
        idCount = value;
    }

    public double getXStartD() {
        return xStartD;
    }

    public double getYStartD() {
        return yStartD;
    }

    public double getXEndD() {
        return xEndD;
    }

    public double getYEndD() {
        return yEndD;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( obj == null ) {
            return false;
        }
        if ( getClass() != obj.getClass() ) {
            return false;
        }
        final Shape other = ( Shape ) obj;
        if ( this.id != other.id ) {
            return false;
        }
        return true;
    }
    
    protected static void copyData( Shape origin, Shape target ) {
        
        target.xStart = origin.xStart;
        target.yStart = origin.yStart;
        target.xEnd = origin.xEnd;
        target.yEnd = origin.yEnd;

        target.xStartD = origin.xStartD;
        target.yStartD = origin.yStartD;
        target.xEndD = origin.xEndD;
        target.yEndD = origin.yEndD;

        target.strokeColor = origin.strokeColor;
        target.fillColor = origin.fillColor;
        target.strokeWidth = origin.strokeWidth;
        
    }
    
}
