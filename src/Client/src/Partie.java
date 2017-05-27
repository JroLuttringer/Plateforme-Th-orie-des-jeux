import java.io.Serializable;
import java.util.ArrayList;

/* Contient toute les infos de la partie :
Acteurs, adresse des acteurs, etc, ainsi
que les regles de la partie 
Diffusée au début de la partie à tous les acteurs */
public class Partie implements Serializable {
    private static final long serialVersionUID = 7627958224543394614L;

    private Regle m_regle;
    private int m_nb_ias;
    private int m_nb_joueurs;
    private int m_nb_prod;
    private int m_nb_coord;
    private ArrayList<ProducteurSummary> prod;
    private ArrayList<CoordinateurSummary> coords;
    private ArrayList<JoueurSummary> joueurs;
    private String m_nom;

    public Partie() {
        prod = new ArrayList<>();
        coords = new ArrayList<>();
        joueurs = new ArrayList<>();
    }

    public Partie(Regle m_regle, boolean m_tourpartour, int m_nb_ias, int m_nb_joueurs, int m_nb_prod, int m_nb_coord, ArrayList<ProducteurSummary> prod, ArrayList<CoordinateurSummary> coords, ArrayList<JoueurSummary> joueurs, String m_nom) {
        this.m_regle = m_regle;
        this.m_nb_ias = m_nb_ias;
        this.m_nb_joueurs = m_nb_joueurs;
        this.m_nb_prod = m_nb_prod;
        this.m_nb_coord = m_nb_coord;
        this.prod = prod;
        this.coords = coords;

        this.joueurs = joueurs;
        this.m_nom = m_nom;
    }

    public void setM_nb_ias(int m_nb_ias) {
        this.m_nb_ias = m_nb_ias;
    }

    public void setM_nb_joueurs(int m_nb_joueurs) {
        this.m_nb_joueurs = m_nb_joueurs;
    }

    public void setM_nb_prod(int m_nb_prod) {
        this.m_nb_prod = m_nb_prod;
    }

    public void setM_nb_coord(int m_nb_coord) {
        this.m_nb_coord = m_nb_coord;
    }

    public ArrayList<ProducteurSummary> getProd() {
        return prod;
    }

    public void setProd(ArrayList<ProducteurSummary> prod) {
        this.prod = prod;
    }

    public ArrayList<CoordinateurSummary> getCoords() {
        return coords;
    }

    public ArrayList<JoueurSummary> getJoueurs() {
        return joueurs;
    }


    public String getM_nom() {
        return m_nom;
    }

    public void setM_nom(String m_nom) {
        this.m_nom = m_nom;
    }

    public Regle getM_regle() {
        return m_regle;
    }

    public void setM_regle(Regle m_regle) {

        this.m_regle = m_regle;
    }
}
