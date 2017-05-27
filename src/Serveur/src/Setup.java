import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Created by jro on 20/04/17.
 */
public interface Setup extends Remote {


    void creer_joueur(Partie p, Ressource[] r, String nom, Comportement cpt, boolean humain, String addr)
            throws RemoteException;

    void creer_producteur(Partie p, Ressource[] production, int intervalle, String nom, String addr)
            throws RemoteException;

    void lancer_acteurs() throws RemoteException;

    void creer_coordinateur(Partie p, String nom, String addr) throws RemoteException;

    boolean acteurs_done() throws RemoteException;

    ArrayList<String> get_logs() throws RemoteException;

    void arreter_experience() throws RemoteException;

    boolean contient_joueurs() throws RemoteException;

    void set_partie(Partie p) throws RemoteException;

}
