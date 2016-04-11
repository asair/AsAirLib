/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.model;

import api.extractor.AsAirLibCSVExtractor;
import api.model.dictionaries.AsAirLibArtist;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeSet;

/**
 *
 * @author furma_000
 */
public class AsAirLibDataProvider {
    public static String[] supportedDataTypes = new String[]
        {"disc", "artist"};
    public static String[] supportedColFormats = new String[] {
        "disc!compact_name", "disc!track_no",
        "artist!root", "artist!vocal", "artist!remix",
        "track!name", "track!bpm", "track!key"};
            
    public HashMap<String, AsAirLibDisc> discs;
    public HashMap<String, AsAirLibTrack> tracks;
    
    private String dataType;
    
    public AsAirLibDataProvider() {
        this.dataType = AsAirLibDataProvider.supportedDataTypes[0];
        this.discs = new HashMap<>();
        this.tracks = new HashMap<>();
    }
    
    public String getType() {
        return this.dataType;
    }
    
    public boolean setType(String type) {
        if (type.equals(this.dataType))
            return false;
        this.dataType = type;
        return true;
    }
    
    public LinkedList<String> getCategories(String filter) {
        LinkedList<String> categories = new LinkedList<>();
        if (this.dataType.equals("disc")) {
            TreeSet<String> serieNames = new TreeSet<>();
            for(Entry<String, AsAirLibDisc> discEntry : this.discs.entrySet()) {
                if (filter == null || discEntry.getValue().serie.toLowerCase().contains(filter))
                    serieNames.add(discEntry.getValue().serie);
            }
            categories.addAll(serieNames);
        }
        if (this.dataType.equals("artist")) {
            // Return Sorted List of artist names
            TreeSet<String> artistNames = new TreeSet<>();
            AsAirLibArtist.allArtists().stream().
                    filter((artistName) -> (filter == null || artistName.toLowerCase().contains(filter))).
                    forEach((artistName) -> {artistNames.add(artistName);});
            categories.addAll(artistNames);
        }
        return categories;
    }
    
    public LinkedList<String> getSubcategories(String category) {
        LinkedList<String> categories = new LinkedList<>();
        if (this.dataType.equals("disc")) {
            TreeSet<String> serieNames = new TreeSet<>();
            for(Entry<String, AsAirLibDisc> discEntry : this.discs.entrySet()) {
                if (category.equals(discEntry.getValue().serie))
                    serieNames.add(discEntry.getValue().name);
            }
            categories.addAll(serieNames);
        }
        return categories;
    }
    
    public LinkedList<String[]> getData(
            String category, String subcategory,
            String[] colFormat, String[] colFilter, int colSorting) {
        HashSet<AsAirLibTrack> allTracks = new HashSet<>();
        if (this.dataType.equals("disc")) {
            // Figure all discs
            for(Entry<String, AsAirLibDisc> discEntry : this.discs.entrySet()) {
                if (category.equals(discEntry.getValue().serie) && 
                        (subcategory == null || subcategory.equals(discEntry.getValue().name)))
                    for(Entry<Integer, AsAirLibTrack> trackEntry : discEntry.getValue().tracks.entrySet())
                        allTracks.add(trackEntry.getValue());
            }
        }
        if (this.dataType.equals("artist")) {
            allTracks.addAll(AsAirLibArtist.get(category).tracks);
        }
        
        LinkedList<String[]> data = new LinkedList<>();
        //Handle Conversion & Filtering
        for(AsAirLibTrack track : allTracks) {
            boolean isFiltered = false;
            String[] trackData = new String[colFormat.length];
            for(int i=0; i< colFormat.length; i++) {
                trackData[i] = track.parseAttribute(colFormat[i]);
                if(isFiltered = track.isFiltered(colFormat[i], colFilter[i]))
                    break;
            }
            if (!isFiltered) {
                // Append Disc info
                track.discs.stream().filter((disc) -> 
                        (!this.dataType.equals("disc") || 
                                (category.equals(disc.serie) && 
                                (subcategory == null || subcategory.equals(disc.name))))).forEach((disc) -> {
                    String[] trackListData = trackData.clone();
                    for(int i=0; i< colFormat.length; i++) {
                        String discAttribute = disc.parseAttribute(colFormat[i], track);
                        if (discAttribute != null)
                            trackListData[i] = discAttribute;
                    }
                    data.add(trackListData);
                });
            }
        }
        return data;
    }
}
