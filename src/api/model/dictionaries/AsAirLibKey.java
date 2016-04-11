/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.model.dictionaries;

import api.model.AsAirLibTrack;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author furma_000
 */
public class AsAirLibKey {
    private static final HashMap<String, String> keyNotations = new HashMap<>();

    static {
        keyNotations.put("a", "8A");
        keyNotations.put("a#", "3A");
        keyNotations.put("b", "10A");
        keyNotations.put("c", "5A");
        keyNotations.put("c#", "12A");
        keyNotations.put("d", "7A");
        keyNotations.put("d#", "2A");
        keyNotations.put("e", "9A");
        keyNotations.put("f", "4A");
        keyNotations.put("f#", "11A");
        keyNotations.put("g", "6A");
        keyNotations.put("g#", "1A");
        
        keyNotations.put("A", "11B");
        keyNotations.put("A#", "6B");
        keyNotations.put("B", "1B");
        keyNotations.put("C", "8B");
        keyNotations.put("C#", "3B");
        keyNotations.put("D", "10B");
        keyNotations.put("D#", "5B");
        keyNotations.put("E", "12B");
        keyNotations.put("F", "7B");
        keyNotations.put("F#", "2B");
        keyNotations.put("G", "9B");
        keyNotations.put("G#", "4B");
    }
    
    private static final HashMap<String, AsAirLibKey> cachedKeys = new HashMap<>();
    
    private static void cacheKeys() {
        for(String key : AsAirLibKey.keyNotations.keySet()) {
            AsAirLibKey.cachedKeys.put(key, new AsAirLibKey(key));
        }
    }
    
    public static AsAirLibKey get(String notation) {
        if (notation == null || notation.equals(""))
            return null;
        if (AsAirLibKey.cachedKeys.isEmpty())
            AsAirLibKey.cacheKeys();
        if (AsAirLibKey.cachedKeys.containsKey(notation))
            return AsAirLibKey.cachedKeys.get(notation);
        return null;
    }
    
    public String notation;
    String camelotNotation;
    private HashSet<AsAirLibTrack> tracks;
    
    public AsAirLibKey(String notation) {
        this.notation = notation;
        this.camelotNotation = this.camelotNotation(notation);
        this.tracks = new HashSet<>();
    }
    
    public void addTrack(AsAirLibTrack t) {
        this.tracks.add(t);
    }
    
    public void removeTrack(AsAirLibTrack t) {
        this.tracks.remove(t);
    }
    
    private String camelotNotation(String notation) {
        return AsAirLibKey.keyNotations.get(notation);
    }
}
