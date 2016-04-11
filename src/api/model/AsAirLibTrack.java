/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api.model;

import api.model.dictionaries.AsAirLibArtist;
import api.model.dictionaries.AsAirLibGenre;
import api.model.dictionaries.AsAirLibKey;
import api.model.dictionaries.AsAirLibFlavour;
import java.util.HashSet;

/**
 *
 * @author furma_000
 */
public class AsAirLibTrack {

    public String name;
    public int year;
    public int bpm;

    public AsAirLibArtist rootArtist;
    public AsAirLibArtist vocalArtist;
    public AsAirLibArtist remixArtist;

    public AsAirLibGenre genre;
    public AsAirLibGenre altGenre;

    public AsAirLibFlavour flavour;
    public AsAirLibFlavour altFlavour;

    private AsAirLibKey key;

    public HashSet<AsAirLibDisc> discs;

    public AsAirLibTrack(String name, int bpm, int year) {
        this.name = name;
        this.bpm = bpm;
        this.year = year;

        this.discs = new HashSet<>();
    }

    public void setArtist(AsAirLibArtist root, AsAirLibArtist vocal, AsAirLibArtist remix) {
        if (this.rootArtist != root) {
            if (this.rootArtist != null) {
                this.rootArtist.removeTrack(this);
            }
            this.rootArtist = root;
            if (this.rootArtist != null) {
                this.rootArtist.addTrack(this);
            }
        }

        if (this.vocalArtist != vocal) {
            if (this.vocalArtist != null) {
                this.vocalArtist.removeTrack(this);
            }
            this.vocalArtist = vocal;
            if (this.vocalArtist != null) {
                this.vocalArtist.addTrack(this);
            }
        }

        if (this.remixArtist != remix) {
            if (this.remixArtist != null) {
                this.remixArtist.removeTrack(this);
            }
            this.remixArtist = remix;
            if (this.remixArtist != null) {
                this.remixArtist.addTrack(this);
            }
        }
    }

    public void setGenre(AsAirLibGenre main, AsAirLibGenre alt) {
        if (this.genre != main) {
            if (this.genre != null) {
                this.genre.removeTrack(this);
            }
            this.genre = main;
            if (this.genre != null) {
                this.genre.addTrack(this);
            }
        }

        if (this.altGenre != alt) {
            if (this.altGenre != null) {
                this.altGenre.removeTrack(this);
            }
            this.altGenre = alt;
            if (this.altGenre != null) {
                this.altGenre.addTrack(this);
            }
        }
    }

    public void setFlavour(AsAirLibFlavour main, AsAirLibFlavour alt) {
        if (this.flavour != main) {
            if (this.flavour != null) {
                this.flavour.removeTrack(this);
            }
            this.flavour = main;
            if (this.flavour != null) {
                this.flavour.addTrack(this);
            }
        }

        if (this.altFlavour != alt) {
            if (this.altFlavour != null) {
                this.altFlavour.removeTrack(this);
            }
            this.altFlavour = alt;
            if (this.altFlavour != null) {
                this.altFlavour.addTrack(this);
            }
        }
    }

    public void setKey(AsAirLibKey key) {
        if (this.key != key) {
            if (this.key != null) {
                this.key.removeTrack(this);
            }
            this.key = key;
            if (this.key != null) {
                this.key.addTrack(this);
            }
        }
    }

    public void addDisc(AsAirLibDisc disc) {
        this.discs.add(disc);
    }

    public void removeDisc(AsAirLibDisc disc) {
        this.discs.remove(disc);
    }

    public void delete() {
        if (this.rootArtist != null) {
            this.rootArtist.removeTrack(this);
        }
        if (this.vocalArtist != null) {
            this.vocalArtist.removeTrack(this);
        }
        if (this.remixArtist != null) {
            this.remixArtist.removeTrack(this);
        }
        if (this.genre != null) {
            this.genre.removeTrack(this);
        }
        if (this.altGenre != null) {
            this.altGenre.removeTrack(this);
        }
        if (this.flavour != null) {
            this.flavour.removeTrack(this);
        }
        if (this.altFlavour != null) {
            this.altFlavour.removeTrack(this);
        }
        if (this.key != null) {
            this.key.removeTrack(this);
        }
    }
    
    public String uniqueIdentifier() {
        return this.uniqueIdentifier(this.rootArtist.name, this.vocalArtist.name, this.remixArtist.name);
    }
    
    public String uniqueIdentifier(String root, String vocal, String remix) {
        return this.name+root+vocal+remix;
    }
    
    public String parseAttribute(String attrName) {
        if (attrName.equals("artist!root"))
            if (this.rootArtist != null)
                return this.rootArtist.name;
            else
                return "<<Unknown Artist>>";
        else if (attrName.equals("artist!vocal"))
            if (this.vocalArtist != null)
                return this.vocalArtist.name;
            else
                return "";
        else if (attrName.equals("artist!remix"))
            if (this.remixArtist != null)
                return this.remixArtist.name;
            else
                return "";
        else if (attrName.equals("track!name"))
            return this.name;
        else if (attrName.equals("track!bpm"))
            if (this.bpm != 0)
                return ""+this.bpm;
            else
                return "";
        else if (attrName.equals("track!key"))
            if (this.key != null)
                return this.key.notation;
            else
                return "";
        
        return "<INVALID_COLUMN: "+attrName+">";
    }
    
    public boolean isFiltered(String attrName, String filter) {
        return false;
    }
}
