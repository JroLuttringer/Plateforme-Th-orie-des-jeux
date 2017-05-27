import java.io.Serializable;

/* Super classe des ressources */
public class Ressource implements Serializable {
    private static final long serialVersionUID = -4522570133652136392L;
    protected String m_nom;
    protected int m_disponible;

    public Ressource() {
        super();
    }

    public Ressource(String m_nom, int m_disponible) {
        super();
        this.m_disponible = m_disponible;
        this.m_nom = m_nom;
    }

    public void setM_nom(String m_nom) {
        this.m_nom = m_nom;
    }

    public int getM_disponible() {
        return m_disponible;
    }

    public void setM_disponible(int m_disponible) {
        this.m_disponible = m_disponible;
    }

    public void ajouter(int nombre) {
        this.m_disponible += nombre;
    }

    public int enlever(int nombre) {
        if (nombre > m_disponible) {
            return 0;
        }
        else {
            this.m_disponible -= nombre;
            return nombre;
        }
    }
// Les ressources ont des fonctions de production différents, elle est donc implémenté 
// dans les sous classes
    public void produire() {
        System.out.println("improduisible");
    }
}
