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
public class AsAirLibGenre {
    private static final HashMap<String, AsAirLibGenre> cachedGenres = new HashMap<>();
    
    public static AsAirLibGenre get(String text) {
        if (text == null || text.equals(""))
            return null;
        if (!AsAirLibGenre.cachedGenres.containsKey(text))
            AsAirLibGenre.cachedGenres.put(text, new AsAirLibGenre(text));
        return AsAirLibGenre.cachedGenres.get(text);
    }
    
    private String text;
    private HashSet<AsAirLibTrack> tracks;
    
    public AsAirLibGenre(String text) {
        this.text = text;
        this.tracks = new HashSet<>();
    }
    
    public void addTrack(AsAirLibTrack track) {
        this.tracks.add(track);
    }
    
    public void removeTrack(AsAirLibTrack track) {
        this.tracks.remove(track);
    }
}
