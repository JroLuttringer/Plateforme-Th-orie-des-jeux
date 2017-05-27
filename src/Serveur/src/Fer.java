import java.rmi.RemoteException;

public class Fer extends Ressource {
    public Fer() throws RemoteException {
        super("Fer", 100);
    }

    public Fer(int disponible) throws RemoteException {
        super("Fer", disponible);
    }

    @Override
    public void produire() {
        this.m_disponible += this.m_disponible/4 + 100; // ressource épuisable
	this.m_disponible = Math.min(10000, this.m_disponible);
        System.out.println("Production de Fer " + m_disponible);
    }
}
