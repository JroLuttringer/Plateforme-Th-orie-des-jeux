import java.io.Serializable;

public class Comportement implements Serializable {
    String m_nom;
    int m_proba_vol;
static final long serialVersionUID = 42L;
    public Comportement(String nom, int m_proba_vol) {
        this.m_nom = nom;
        this.m_proba_vol = m_proba_vol;
    }

    public Comportement(String nom) {
        this.m_nom = nom;
        this.m_proba_vol = -1;
    }

    public String getM_nom() {
        return m_nom;
    }

}
