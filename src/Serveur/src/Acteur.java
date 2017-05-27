import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Timer;


public abstract class Acteur extends UnicastRemoteObject implements Remote {
    protected String m_nom;
    protected String m_addr;
    protected String log = "";
    protected Timer timer;
    protected boolean my_turn = false;
    protected boolean finished = false;
    protected boolean is_player = false;

    public Acteur() throws RemoteException {
    }

    public abstract void arreter();

    public void write_log(String action, int success, Ressource r, ArrayList<Ressource> r2,
                          float time) {
        int nb_petrole = 0, nb_fer = 0, nb_or = 0;
        for (Ressource rc : r2) {
            if (rc.m_nom.equals("Fer")) nb_fer += rc.m_disponible;
            if (rc.m_nom.equals("Or")) nb_or += rc.m_disponible;
            if (rc.m_nom.equals("Petrole")) nb_petrole += rc.m_disponible;
        }
        log += m_nom + "\t" + action + "\t" + success + "\t" + r.m_nom + "\t"
                + r.m_disponible + "\t" + nb_fer
                + "\t" + nb_or + "\t" + nb_petrole + "\t" + time + "\n";
    }

    public abstract boolean is_done();

    public String get_log() {
        return log;
    }

    public String get_nom() {
        return m_nom;
    }

    public void set_nom(String m_nom) {
        this.m_nom = m_nom;
    }

    public String get_addr() {
        return m_addr;
    }

    public void set_addr(String addr) {
        this.m_addr = addr;
    }

    public abstract void lancer();
}
