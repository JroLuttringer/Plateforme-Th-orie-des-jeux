import java.rmi.RemoteException;

public class Or extends Ressource {
    public Or() throws RemoteException {
        super("Or", 100);

    }

    public Or(int disponible) throws RemoteException {
        super("Or", disponible);

    }

        @Override
    public void produire() {
            this.m_disponible += this.m_disponible/ 10 + 100; // ressource épuisable
	this.m_disponible = Math.min(10000, this.m_disponible);
        System.out.println("Production d'or" + m_disponible);

    }
}
