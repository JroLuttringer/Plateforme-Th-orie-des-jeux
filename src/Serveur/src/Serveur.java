import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Serveur {
/* lance un serveur hébergant un stup de nom args[0] 
On peut lancer plusieurs setup sur la même machine */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage :  java Serveur [nom]");
            return;
        }
	Setup stup;
        try {
            stup = new SetupImpl();
            Naming.rebind("rmi://localhost/" + args[0], stup);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
        System.out.println("Setup mis à disposition. Nom : " + args[0]);
    }

}
