import java.rmi.Remote;
import java.rmi.RemoteException;

/* Interface indiquant ce que les serveurs peuvent demander aux autres serveurs 
voler() permet de voler un joueur
possede() de voir les ressources qu'il possède 
set_myturn() pour lui indiquer que c'est son tour 
finished() permet de savoir si le joueur a terminé son action 
*/
public interface Joueur extends Remote {
    Ressource voler() throws RemoteException;

    boolean possede(Ressource r) throws RemoteException;

    void set_myturn(boolean x) throws RemoteException;

    boolean get_finished() throws RemoteException;

    void set_finished(boolean x) throws RemoteException;

}
