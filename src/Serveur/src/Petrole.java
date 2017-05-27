import java.rmi.RemoteException;


public class Petrole extends Ressource {
    public Petrole() throws RemoteException {
        super("Petrole", 100);

    }

    public Petrole(int disponible) throws RemoteException {
        super("Petrole", disponible);

    }

    @Override
    public void produire() {
        this.m_disponible += 100; // ressource épuisable
	this.m_disponible = Math.min(10000, this.m_disponible);
        System.out.println("Production de Petrole" + m_disponible);

    }
}
