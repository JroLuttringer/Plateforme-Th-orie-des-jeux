import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/* Interface permettant au client de savoir ce que le serveur
met à disposition comme service */
public interface Setup extends Remote {


    void creer_joueur(Partie p, Ressource[] r, String nom, Comportement cpt, boolean humain, String addr)
            throws RemoteException;

    void creer_producteur(Partie p, Ressource[] production, int intervalle, String nom, String addr)
            throws RemoteException;

    /* lancer tous les acteurs d'un setup */
    void lancer_acteurs() throws RemoteException;

    void creer_coordinateur(Partie p, String nom, String addr) throws RemoteException;

/* vérifier si les acteurs d'un setup ont terminé */
    boolean acteurs_done() throws RemoteException;

    ArrayList<String> get_logs() throws RemoteException;
/* mettre les setup à 0 . permet d'enchainer les expérience */
    void arreter_experience() throws RemoteException;
/* vérifie si un setup contient des joueurs, ou juste des prod/coordinateurs */
    boolean contient_joueurs() throws RemoteException;
	
    void set_partie(Partie p) throws RemoteException;

}
