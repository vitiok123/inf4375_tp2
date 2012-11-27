package ca.disks.records.db;

import ca.disks.records.security.EncryptionKey;
import ca.disks.records.security.XmlDecryption;
import ca.disks.records.security.XmlEncryption;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.w3c.dom.*;


public class Inventaire {

    private static Inventaire instance = null;
    private Document document;
    private String path;
    private int ID;
    private String erreur;

    public synchronized static Inventaire getInstance(){
        if ( instance == null ){
            instance = new Inventaire();
        }
        return instance;
    }
    
    public Inventaire() {
        decryptDocument("D:\\Uqam\\INF4375\\inf4375_tp2\\InvetaireServices\\db\\data.xml");
        this.path = path;
        ID = getNombreAlbums();
    }

    
    private void decryptDocument(String path){
        try {
            EncryptionKey decryptKey = new EncryptionKey();
            decryptKey.loadFromFile("D:\\Uqam\\INF4375\\inf4375_tp2\\InvetaireServices\\db\\encryption.key");
            XmlDecryption decrypter = new XmlDecryption();
            decrypter.decryptXmlDocument(path, decryptKey.getKey());
            document = decrypter.getDocument();
            
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(document);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            trans.transform(source, result);
            String xmlString = sw.toString();
            
            System.out.println(xmlString);
        } catch (Exception ex) {
            Logger.getLogger(Inventaire.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Document encryptDocument(Document doc){
        try {
            EncryptionKey decryptKey = new EncryptionKey();
            decryptKey.loadFromFile("db/encryption.key");
            XmlEncryption encrypter = new XmlEncryption(doc);
            encrypter.encryptXmlDocument(decryptKey.getKey());
            return encrypter.getDocument();
        } catch (Exception ex) {
            Logger.getLogger(Inventaire.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    private void save(Document document, String path) throws Exception {
        FileOutputStream outputStream = new FileOutputStream(new File(path));

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer xmlTransformer = factory.newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputStream);
        xmlTransformer.transform(source, result);

        outputStream.close();
    }
    
    public int ajouterAlbum(String titreAlbum, String nomArtiste, int anneeAlbum){
        
        if(!existeAlbum(titreAlbum, nomArtiste, anneeAlbum)){
            ID++;
            Element id = document.createElement("id");
            id.setTextContent(Integer.toString(ID));
            Element titre = document.createElement("titre");
            titre.setTextContent(titreAlbum);
            Element artiste = document.createElement("artiste");
            artiste.setTextContent(nomArtiste);
            Element annee = document.createElement("annee");
            annee.setTextContent(Integer.toString(anneeAlbum));
            Element qte = document.createElement("qte");
            qte.setTextContent(Integer.toString(0));
            Element album = document.createElement("album");
            album.appendChild(id);
            album.appendChild(titre);
            album.appendChild(artiste);
            album.appendChild(annee);
            album.appendChild(qte);
            Element stock = (Element) document.getElementsByTagName("stock").item(0);
            stock.appendChild(album);
            try {
                save(encryptDocument(document), path);
            } catch (Exception ex) {
                Logger.getLogger(Inventaire.class.getName()).log(Level.SEVERE, null, ex);
            }
            return ID;
        }
        return -1;
    }
    
    public int modifierQuantite(int id, int quantite){
        //int ancientQte;
        Element album = obtenirAlbum(id);
        if(album != null){
            //ancientQte = Integer.parseInt(album.getElementsByTagName("qte").item(0).getTextContent());
            album.getElementsByTagName("qte").item(0).setTextContent(Integer.toString(quantite));
            return quantite;
        }
        return -1;
    }

    public String listeAlbums(){
        
        return jsonArtistes(obtenirAlbumsParArtiste());
    }
    
    public String obtenirStock(){
        
        return jsonStock();
    }
    
    private Map<String, Artiste> obtenirAlbumsParArtiste(){
        Map<String, Artiste> artistes = new TreeMap<String, Artiste>();
        NodeList listeAlbums = document.getElementsByTagName("album");
        for(int i = 0; i < listeAlbums.getLength(); ++i){
            String artiste = ((Element)listeAlbums.item(i)).getElementsByTagName("artiste").item(0).getTextContent();
            if(artistes.get(artiste) != null){
                int annee = Integer.parseInt(((Element)listeAlbums.item(i)).getElementsByTagName("annee").item(0).getTextContent());
                if(artistes.get(artiste).getAlbums().get(annee) != null){
                    int id = Integer.parseInt(((Element)listeAlbums.item(i)).getElementsByTagName("id").item(0).getTextContent());
                    if(artistes.get(artiste).getAlbums().get(annee).getId() != id){
                        artistes.get(artiste).addAlbum(creerAlbum(((Element)listeAlbums.item(i))));
                    }
                }else{
                    artistes.get(artiste).addAlbum(creerAlbum(((Element)listeAlbums.item(i))));
                }
            }else{
                Artiste newArtiste = new Artiste(artiste);
                artistes.put(artiste, newArtiste);
                artistes.get(artiste).addAlbum(creerAlbum(((Element)listeAlbums.item(i))));
            }
        }
        return artistes;
    }
    
    private String jsonArtistes(Map<String, Artiste> artistes){
        Map<String, Artiste> artistesTrie = new TreeMap<String, Artiste>(artistes);
        JSONObject jsonResultat = new JSONObject();
        JSONArray jsonArtistes = new JSONArray();
        for (String nome : artistesTrie.keySet()) {
            Artiste artiste = artistesTrie.get(nome);
            JSONObject jsonArtiste = new JSONObject();
            jsonArtiste.put("nom", nome);
            JSONArray jsonAlbums = new JSONArray();
            for(Integer annee : artiste.getAlbums().keySet()){
                Album album = artiste.getAlbums().get(annee);
                JSONObject newAlbum = new JSONObject();
                newAlbum.put("id", album.getId());
                newAlbum.put("titre", album.getTitre());
                newAlbum.put("annee", album.getAnnee());
                newAlbum.put("qte", album.getQuantite());
                jsonAlbums.add(newAlbum);
            }
            jsonArtiste.accumulate("albums", jsonAlbums);
            jsonArtistes.add(jsonArtiste);
        }
        jsonResultat.accumulate("artistes", jsonArtistes);
        
        return jsonResultat.toString(2);
    }
    
    private String jsonStock(){
        JSONObject jsonResultat = new JSONObject();
        jsonResultat.put("type", "stock");
        jsonResultat.put("nbAlbums", obtenirNbAlbums());
        jsonResultat.put("qte", obtenirQte());
        jsonResultat.put("nbArtistes", obtenirNbArtiste());
        
        return jsonResultat.toString(2);
    }
    
    private Integer obtenirNbAlbums() {
        NodeList listeAlbums = document.getElementsByTagName("album");
        return listeAlbums.getLength();
    }
    
    private Integer obtenirQte(){
        NodeList listeAlbums = document.getElementsByTagName("album");
        int qteTotale = 0;
        for(int i = 0; i < listeAlbums.getLength(); ++i){
            int qte = Integer.parseInt(((Element)listeAlbums.item(i)).getElementsByTagName("qte").item(0).getTextContent());
            qteTotale += qte;
        }
        return qteTotale;
    }
    
    private Integer obtenirNbArtiste(){
        return obtenirAlbumsParArtiste().size();
    }
    
    private Album creerAlbum(Element album){
        int id = Integer.parseInt(album.getElementsByTagName("id").item(0).getTextContent());
        int annee = Integer.parseInt(album.getElementsByTagName("annee").item(0).getTextContent());
        String titre = album.getElementsByTagName("titre").item(0).getTextContent();
        int qte = Integer.parseInt(album.getElementsByTagName("qte").item(0).getTextContent());
        Album newAlbum = new Album(id, titre, annee, qte);
        return newAlbum;
    }
    
    private boolean existeAlbum(String titreAlbum, String nomArtiste, int anneeAlbum){
        NodeList listeAlbums = document.getElementsByTagName("album");
        if(listeAlbums != null){
            for(int i = 0; i < listeAlbums.getLength(); ++i){
                String titre = ((Element)listeAlbums.item(i)).getElementsByTagName("titre").item(0).getTextContent();
                String artiste = ((Element)listeAlbums.item(i)).getElementsByTagName("artiste").item(0).getTextContent();
                int annee = Integer.parseInt(((Element)listeAlbums.item(i)).getElementsByTagName("annee").item(0).getTextContent());
                if(titre.equals(titreAlbum) && artiste.equals(nomArtiste) && annee == anneeAlbum)
                    return true;
            }
        }else{
            return true;
        }
        return false;
    }
    
    private Element obtenirAlbum(int id){
        NodeList listeAlbums = document.getElementsByTagName("album");
        if(listeAlbums != null){
            for(int i = 0; i < listeAlbums.getLength(); ++i){
                int courrId = Integer.parseInt(((Element)listeAlbums.item(i)).getElementsByTagName("id").item(0).getTextContent());
                if(courrId == id)
                    return (Element)listeAlbums.item(i);
            }
        }else{
            return null;
        }
        return null;
    }
    
    private int getNombreAlbums(){
        NodeList listeAlbums = document.getElementsByTagName("album");
        if(listeAlbums == null)
            return -1;
        return listeAlbums.getLength();
    }
    
}
