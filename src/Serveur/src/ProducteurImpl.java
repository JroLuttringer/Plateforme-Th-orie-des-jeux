import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class ProducteurImpl extends Acteur implements Producteur, Serializable {

    Ressource[] m_ressources;
    Partie p;
    private int m_intervalle; // intervalle de production
    private float time = 0; // utile pour les logs

    public ProducteurImpl(Partie pa, Ressource[] r, int intervalle, String nom, String addr)
            throws RemoteException {
        super();
        this.m_nom = nom;
        this.m_addr = addr;
        this.m_ressources = r;
        this.m_intervalle = intervalle;
        timer = new Timer();

        if (pa.getM_regle().isM_tourptour()) {
            my_turn = false;
            finished = false;
            m_intervalle = 1000;
        } else {
            my_turn = true;
            finished = true;
        }
        p = pa;
    }

    public void lancer() {
        System.out.println("Lancement prod");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!my_turn) return;
                finished = false;
                for (Ressource r : get_ressources()) {
                    r.produire();
                    write_log("produire", 1, r,
                            new ArrayList<Ressource>(Arrays.asList(m_ressources)),
                            time * m_intervalle / 1000);
                }
                if (p.getM_regle().isM_tourptour()) {
                    finished = true;
                    my_turn = false;
                }
                time++;
            }
        }, 0, m_intervalle);
    }

    public void arreter() {
        this.timer.cancel();
    }
    // prendre des ressources chez un producteur
    @Override
    public synchronized int prendre_ressource(Ressource r) {
        for (Ressource x : get_ressources())
            if (x.m_nom.equals(r.m_nom)) return x.enlever(r.m_disponible);
        return 0;
    }

    public boolean is_done() {
        return true;
    }

    @Override
    public int combien(Ressource r) {
        for (Ressource x : get_ressources())
            if (x.m_nom.equals(r.m_nom)) return x.m_disponible;
        return 0;
    }

    public Ressource[] get_ressources() {
        return m_ressources;
    }

    public boolean get_finished() {
        return finished;
    }

    public void set_finished(boolean x) throws RemoteException {
        finished = x;
    }

    public boolean get_myturn() {
        return my_turn;
    }

    public void set_myturn(boolean x) {
        my_turn = x;
    }



}
