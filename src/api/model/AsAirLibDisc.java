/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.model;

import api.model.dictionaries.AsAirLibArtist;
import java.util.HashMap;
import java.util.Map.Entry;

/**
 *
 * @author furma_000
 */
public class AsAirLibDisc {
    public String serie;
    public String name;
    public int volume;
    public int year;
    
    public AsAirLibArtist rootArtist;
        
    public HashMap<Integer, AsAirLibTrack> tracks;
    
    public AsAirLibDisc(String name, String serie, int volume, int year) {
        this.name = name;
        this.serie = serie;
        this.volume = volume;
        this.year = year;
        
        this.tracks = new HashMap<>();
    }
    
    public AsAirLibTrack trackForNumber(int no) {
        return this.tracks.get(no);
    }
    
    public void addTrack(int no, AsAirLibTrack track) {
        if (this.tracks.containsKey(no))
            this.tracks.get(no).removeDisc(this);
        this.tracks.put(no, track);
        track.addDisc(this);
    }
    
    public void removeTrack(int no) {
        if (this.tracks.containsKey(no)) {
            this.tracks.get(no).removeDisc(this);
            this.tracks.remove(no);
        }
    }
    
    public void removeTrack(AsAirLibTrack track) {
        for(Entry<Integer, AsAirLibTrack> entry : this.tracks.entrySet()) {
            if (entry.getValue() == track) {
                track.removeDisc(this);
                this.tracks.remove(entry.getKey());
            }
        }
    }
    
    public void delete() {
        for(int no : this.tracks.keySet()) {
            this.removeTrack(no);
        }
    }
    
    public String uniqueIdentifier() {
        return this.serie+this.name+this.volume;
    }

    public String parseAttribute(String colName, AsAirLibTrack track) {
        if (colName.equals("disc!compact_name")) {
            return this.serie + " " + this.name + " vol." + this.volume;
        }
        else if (colName.equals("disc!track_no")) {
            for (Entry<Integer, AsAirLibTrack> entrySet : tracks.entrySet()) {
                Integer key = entrySet.getKey();
                AsAirLibTrack value = entrySet.getValue();
                
                if (track == value)
                    return ""+ key;
            }
        }
        return null;
    }
}
