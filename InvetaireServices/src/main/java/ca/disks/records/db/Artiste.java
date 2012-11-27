package ca.disks.records.db;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Artiste {
   private String nom;
   private TreeMap<Integer, Album> albums;

    public Artiste(String nom) {
        this.nom = nom;
        albums = new TreeMap<Integer, Album>();
    }

    public void addAlbum(Album album){
        albums.put(album.getAnnee(), album);
    }
    
    public String getNom(){
        return nom;
    }

    public Map<Integer, Album> getAlbums(){
        NavigableMap albumsTrie = albums.descendingMap();
        return albumsTrie;
    }
   
   
}
