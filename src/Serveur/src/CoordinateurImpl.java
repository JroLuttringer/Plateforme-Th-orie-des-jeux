
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Timer;
import java.util.TimerTask;

/* Implémentation du Coordinateurs
Régule les partie tour par tour 
en indiquant au joueur ou au producteur qu'il peut jouer
on alterne entre faire jouer un producteur et un joueur */

public class CoordinateurImpl extends Acteur {
    Partie p;
    private int i = 0;
    private int current_j = 0;
    private int current_p = 0;
    public CoordinateurImpl(Partie pa, String nom, String addr) throws RemoteException {
        this.m_addr = addr;
        this.m_nom = nom;
        p = pa;
    }

    public void lancer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gerer_tour();
            }
        }, 0, 100);

    }

    public void gerer_tour() {
            try {
                if (i % 2 == 0) {
                    ProducteurSummary ps = p.getProd().get(current_p % p.getProd().size());
                    String ac = "rmi://" + ps.addr + "/" + ps.nom;
                    Producteur pr = (Producteur) Naming.lookup(ac);
                    if (!pr.get_myturn() && i == 0) pr.set_myturn(true);
                    if (pr.get_finished()) {
                        current_p++;
                        JoueurSummary js = p.getJoueurs().get(current_j % p.getJoueurs().size());
                        ac = "rmi://" + js.addr + "/" + js.nom;
                        Joueur jr = (Joueur) Naming.lookup(ac);
                        jr.set_myturn(true);
                        jr.set_finished(false);
                        i++;
                    } else {
                        return;
                    }
                } else {
                    JoueurSummary js = p.getJoueurs().get(current_j % p.getJoueurs().size());
                    String ac = "rmi://" + js.addr + "/" + js.nom;
                    Joueur jr = (Joueur) Naming.lookup(ac);
                    if (jr.get_finished()) {
                        current_j++;
                        ProducteurSummary ps = p.getProd().get(current_p % p.getProd().size());
                        ac = "rmi://" + ps.addr + "/" + ps.nom;
                        Producteur pr = (Producteur) Naming.lookup(ac);
                        pr.set_myturn(true);
                        pr.set_finished(false);
                        i++;
                    } else {
                        return;
                    }
                }
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    public boolean is_done() {
        return true;
    }

    public void arreter() {
        timer.cancel();
    }


}



