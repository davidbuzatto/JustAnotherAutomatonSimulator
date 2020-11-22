/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.davidbuzatto.jaas.utils;

import java.io.File;
import java.util.prefs.Preferences;

/**
 *
 * @author David
 */
public class AppPrefs {
    
    public static String DEFAULT_DIR = "defaultDir";
    
    private static final String PREFERENCES_PATH = "br.com.davidbuzatto.jaas";
    
    private static void preparePreferences() {
        Preferences prefs = Preferences.userRoot().node( PREFERENCES_PATH );
        prefs.get( DEFAULT_DIR, new File( System.getProperty( "user.home" ) ).getAbsolutePath() );
    }
    
    public static String getPref( String key ) {
        preparePreferences();
        Preferences prefs = Preferences.userRoot().node( PREFERENCES_PATH );
        return prefs.get( key, "" );
    }
    
    public static void setPref( String key, String value ) {
        preparePreferences();
        Preferences prefs = Preferences.userRoot().node( PREFERENCES_PATH );
        prefs.put( key, value );
    }
    
}
