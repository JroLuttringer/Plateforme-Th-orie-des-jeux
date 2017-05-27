import java.io.Serializable;

public class Regle implements Serializable {
private static final long serialVersionUID = 6529685098267757690L;
    int m_max_take;
    int m_punition_vol;
    String m_condition_victoire;
    boolean tourptour;
    boolean m_tourptour;

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
