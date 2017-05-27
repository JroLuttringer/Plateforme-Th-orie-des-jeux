import java.rmi.Remote;
import java.rmi.RemoteException;


public interface Producteur extends Remote {
    int prendre_ressource(Ressource r) throws RemoteException;

    int combien(Ressource r) throws RemoteException;

    boolean get_finished() throws RemoteException;

    void set_finished(boolean x) throws RemoteException;

    boolean get_myturn() throws RemoteException;

    void set_myturn(boolean x) throws RemoteException;


}
