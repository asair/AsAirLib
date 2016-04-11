/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.extractor;

import api.model.dictionaries.AsAirLibArtist;
import api.model.dictionaries.AsAirLibGenre;
import api.model.dictionaries.AsAirLibKey;
import api.model.dictionaries.AsAirLibFlavour;
import api.model.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author furma_000
 */
public class AsAirLibCSVExtractor {
    public HashMap<String, AsAirLibDisc> foundDiscs;
    public HashMap<String, AsAirLibTrack> foundTracks;
    
    public AsAirLibCSVExtractor() {
        this.foundDiscs = new HashMap<>();
        this.foundTracks = new HashMap<>();
    }
    
    public AsAirLibCSVExtractor(
            HashMap<String, AsAirLibDisc> discs,
            HashMap<String, AsAirLibTrack> tracks) {
        this.foundDiscs = discs;
        this.foundTracks = tracks;
    }

    public HashMap<String, AsAirLibDisc> processFile(String fileName, LinkedList<String> headerData) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                this.processFileLine(line, headerData);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AsAirLibCSVExtractor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AsAirLibCSVExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return foundDiscs;
    }

    private void processFileLine(String line,
            LinkedList<String> headerData) {
        HashMap<String, String> lineProperties = this.emptyProperties();

        String[] lineSegments = line.split("\\t");
        for (int i = 0; i < Math.min(lineSegments.length, headerData.size()); i++) {
            String attrName = headerData.get(i);
            if (attrName.equals("track!flavour")) {
                // Split flavour into two flavours
                int splitInd;
                if ((splitInd = lineSegments[i].indexOf(";")) < 0) {
                    lineProperties.put("track!flavour1", lineSegments[i]);
                } else {
                    lineProperties.put("track!flavour1", lineSegments[i].substring(0, splitInd));
                    lineProperties.put("track!flavour2", lineSegments[i].substring(splitInd+1));
                }
            } else {
                lineProperties.put(attrName, lineSegments[i]);
            }
        }
        
        AsAirLibDisc disc = this.figureDiscData(lineProperties);
        int trackNo = Integer.parseInt(lineProperties.get("disc!track_no"));
        if (disc.trackForNumber(trackNo) != null)
            return;
        AsAirLibTrack track = this.figureTrackData(lineProperties);
        disc.addTrack(trackNo, track);
        // Now Check for Specialised track data
        this.processTrackData(track, lineProperties);
    }

    private HashMap<String, String> emptyProperties() {
        HashMap<String, String> lineProperties = new HashMap<>();

        lineProperties.put("disc!serie", "");
        lineProperties.put("disc!name", "");
        lineProperties.put("disc!volume", "1");
        lineProperties.put("disc!year", "0");
        lineProperties.put("disc!track_no", "");

        lineProperties.put("artist!root", "");
        lineProperties.put("artist!vocal", "");
        lineProperties.put("artist!remix", "");

        lineProperties.put("track!name", "");
        lineProperties.put("track!bpm", "0");
        lineProperties.put("track!year", "0");
        lineProperties.put("track!genre1", "");
        lineProperties.put("track!genre2", "");
        lineProperties.put("track!key", "");
        lineProperties.put("track!flavour1", "");
        lineProperties.put("track!flavour2", "");

        return lineProperties;
    }

    private AsAirLibDisc figureDiscData(HashMap<String, String> lineProperties) {
        AsAirLibDisc newDisc = new AsAirLibDisc(
                lineProperties.get("disc!name"),
                lineProperties.get("disc!serie"),
                Integer.parseInt(lineProperties.get("disc!volume")),
                Integer.parseInt(lineProperties.get("disc!year")));
        
        String discId = newDisc.uniqueIdentifier();
        if (foundDiscs.containsKey(discId))
            return foundDiscs.get(discId);
        foundDiscs.put(discId, newDisc);
        return newDisc;
    }

    private AsAirLibTrack figureTrackData(HashMap<String, String> lineProperties) {
        AsAirLibTrack newTrack = new AsAirLibTrack(
                lineProperties.get("track!name"),
                Integer.parseInt(lineProperties.get("track!bpm")),
                Integer.parseInt(lineProperties.get("track!year")));
        String trackId = newTrack.uniqueIdentifier(
                lineProperties.get("artist!root"),
                lineProperties.get("artist!vocal"),
                lineProperties.get("artist!remix"));
        if (foundTracks.containsKey(trackId))
            return foundTracks.get(trackId);
        foundTracks.put(trackId, newTrack);
        return newTrack;
    }

    private void processTrackData(AsAirLibTrack track,
            HashMap<String, String> lineProperties) {
        // Artists
        AsAirLibArtist root = AsAirLibArtist.get(lineProperties.get("artist!root"));
        AsAirLibArtist vocal = AsAirLibArtist.get(lineProperties.get("artist!vocal"));
        AsAirLibArtist remix = AsAirLibArtist.get(lineProperties.get("artist!remix"));
        // Genre
        AsAirLibGenre rootGenre = AsAirLibGenre.get(lineProperties.get("track!genre1"));
        AsAirLibGenre altGenre = AsAirLibGenre.get(lineProperties.get("track!genre2"));
        //Flavour
        AsAirLibFlavour rootFla = AsAirLibFlavour.get(lineProperties.get("track!flavour1"));
        AsAirLibFlavour altFla = AsAirLibFlavour.get(lineProperties.get("track!flavour2"));
        //Key
        AsAirLibKey key = AsAirLibKey.get(lineProperties.get("track!key"));
        
        //Perform Update/Merge
        track.setArtist(root, vocal, remix);
        track.setGenre(rootGenre, altGenre);
        track.setFlavour(rootFla, altFla);
        if (key != null)
            track.setKey(key);
    }
}
