/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.model.dictionaries;

import api.model.AsAirLibTrack;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author furma_000
 */
public class AsAirLibArtist {
    private static final HashMap<String, AsAirLibArtist> cachedArtists = new HashMap<>();
    
    public static AsAirLibArtist get(String name) {
        if (name == null || name.equals(""))
            return null;
        if (!AsAirLibArtist.cachedArtists.containsKey(name))
            AsAirLibArtist.cachedArtists.put(name, new AsAirLibArtist(name));
        return AsAirLibArtist.cachedArtists.get(name);
    }
    
    public static Set<String> allArtists() {
        return AsAirLibArtist.cachedArtists.keySet();
    }
    
    public String name;
    public HashSet<AsAirLibTrack> tracks;
    
    public AsAirLibArtist(String name) {
        this.name = name;
        this.tracks = new HashSet<>();
    }
    
    public void addTrack(AsAirLibTrack track) {
        this.tracks.add(track);
    }
    
    public void removeTrack(AsAirLibTrack track) {
        this.tracks.remove(track);
    }
}
