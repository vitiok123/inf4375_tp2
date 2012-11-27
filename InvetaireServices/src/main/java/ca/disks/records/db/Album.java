package ca.disks.records.db;

public class Album {
    private int id;
    private String titre;
    private int annee;
    private int quantite;

    public Album(int id, String titre, int annee, int quantite) {
        this.id = id;
        this.titre = titre;
        this.annee = annee;
        this.quantite = quantite;
    }

    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public int getAnnee() {
        return annee;
    }

    public int getQuantite() {
        return quantite;
    }

}
