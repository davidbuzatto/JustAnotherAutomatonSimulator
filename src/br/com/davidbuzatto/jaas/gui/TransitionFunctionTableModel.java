/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.gui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author David
 */
public class TransitionFunctionTableModel implements TableModel {

    private List<String> symbols;
    private List<String> states;
    private List<List<String>> data;
    
    public TransitionFunctionTableModel() {
        symbols = new ArrayList<>();
        states = new ArrayList<>();
        data = new ArrayList<>();
    }

    public List<String> getSymbols() {
        return symbols;
    }

    public void setSymbols( List<String> symbols ) {
        this.symbols = symbols;
    }

    public List<String> getStates() {
        return states;
    }

    public void setStates( List<String> states ) {
        this.states = states;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData( List<List<String>> data ) {
        this.data = data;
    }
    
    @Override
    public int getRowCount() {
        return states.size();
    }

    @Override
    public int getColumnCount() {
        return symbols.size() + 1;
    }

    @Override
    public Object getValueAt( int rowIndex, int columnIndex ) {
        if ( data.isEmpty() ) {
            return "null";
        }
        return data.get( rowIndex ).get( columnIndex );
    }

    @Override
    public String getColumnName( int columnIndex ) {
        if ( columnIndex == 0 ) {
            return "";
        }
        String s = symbols.get( columnIndex - 1 );
        return s.equals( "\0" ) ? "\u03B5" : s;
    }

    @Override
    public Class<?> getColumnClass( int columnIndex ) {
        return String.class;
    }

    @Override
    public boolean isCellEditable( int rowIndex, int columnIndex ) {
        return false;
    }

    @Override
    public void setValueAt( Object aValue, int rowIndex, int columnIndex ) {
        data.get( rowIndex ).set( columnIndex, aValue.toString() );
    }

    @Override
    public void addTableModelListener( TableModelListener l ) {
    }

    @Override
    public void removeTableModelListener( TableModelListener l ) {
    }
    
}
