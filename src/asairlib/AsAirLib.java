/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asairlib;

import api.extractor.AsAirLibCSVExtractor;
import api.model.AsAirLibDataProvider;
import api.model.AsAirLibDisc;
import api.model.AsAirLibTrack;
import gui.manager.AsAirLibGuiManagerFrame;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author furma_000
 */
public class AsAirLib {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        AsAirLibDataProvider data = new AsAirLibDataProvider();
        
        // TODO code application logic here
        String filename = "C:\\Users\\furma_000\\Desktop\\AsAir Library - CDPOOL Radio.tsv";
        LinkedList<String> headerData = new LinkedList<>();
        
        headerData.add("disc!serie");
        headerData.add("disc!name");
        headerData.add("disc!track_no");
        headerData.add("artist!root");
        headerData.add("track!name");
        headerData.add("track!bpm");
        headerData.add("track!genre1");
        headerData.add("track!genre2");
        headerData.add("track!flavour");
        headerData.add("track!key");
        
        AsAirLibCSVExtractor extractor = new AsAirLibCSVExtractor(data.discs, data.tracks);
        extractor.processFile(filename, headerData);
        
        System.out.println(data.discs);
        System.out.println(data.tracks);
        
        (new AsAirLibGuiManagerFrame(data)).openManager();
    }
    
}
