import java.io.Serializable;

/* Permet de mémoriser les règles de la partie
Inclue dans la classe Partie */

public class Regle implements Serializable {
    int m_max_take; // nombre maximale de ressource que l'on peu prendre en un tour
    int m_punition_vol; // punition (en nombre de ressource ) en cas de vol
    String m_condition_victoire; // condition de victoire (premier arrivé ou tous arrivé)
  //  boolean tourptour; // tour par tour ou non
    boolean m_tourptour;
private static final long serialVersionUID = 6529685098267757690L;

    public Regle(int max_take, int punition_vol, String condition_victoire, boolean tpt) {
        this.m_max_take = max_take;
        this.m_punition_vol = punition_vol;
        this.m_condition_victoire = condition_victoire;
        this.m_tourptour = tpt;
    }

    public int getM_max_take() {
        return m_max_take;
    }

    public int getM_punition_vol() {
        return m_punition_vol;
    }

    public String getM_condition_victoire() {
        return m_condition_victoire;
    }

    public boolean isM_tourptour() {
        return m_tourptour;
    }
}
