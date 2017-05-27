import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/* On peut créer autant d'acteurs différent que l'on veut sur un serveur */


public class SetupImpl extends UnicastRemoteObject implements Setup {

    private ArrayList<Acteur> acteurs;
    private Partie p;

    public SetupImpl() throws RemoteException {
        acteurs = new ArrayList<Acteur>();
    }

    public SetupImpl(Partie par) throws RemoteException {
        acteurs = new ArrayList<Acteur>();
        p = par;
    }

    @Override
    public void creer_joueur(Partie p, Ressource[] r, String nom, Comportement cpt, boolean humain,
                             String addr) {
        try {
            JoueurImpl j = new JoueurImpl(p, r, nom, cpt, humain, addr);
            get_acteurs().add(j);
            Naming.rebind("rmi://" + addr + "/" + nom, j);
            System.out.println("Joueur crée et ajouté : " + j.get_nom());
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void creer_coordinateur(Partie p, String nom, String addr) {
        try {
            this.acteurs.add(new CoordinateurImpl(p, nom, addr));
            System.out.println("Coordinateur crée");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    public boolean acteurs_done() {
        boolean first = this.p.getM_regle().m_condition_victoire.equals("First");
        
        for (Acteur a : this.acteurs) {
            if (first && a.is_done() && a.is_player) 
                {   
                    
                    return true;
                }
            if (!a.is_done()) return false;
        }
        System.out.println("Everyone on this server is done");
        return true;
    }

    public ArrayList<String> get_logs() {
        ArrayList<String> l = new ArrayList<>();
        for (Acteur a : get_acteurs()) {
            l.add(a.get_nom());
            l.add(a.get_log());
        }

        return l;
    }

    public void arreter_experience() {
        for (Acteur a : get_acteurs()) {
            a.arreter();
            a = null;
        }

        this.acteurs = new ArrayList<Acteur>();
    }


    @Override
    public void creer_producteur(Partie pa, Ressource[] r, int intervalle, String nom, String addr) {
        try {
            ProducteurImpl p = new ProducteurImpl(pa, r, intervalle, nom, addr);
            get_acteurs().add(p);
            Naming.rebind("rmi://" + addr + "/" + nom, p);
            System.out.println("Producteur crée et ajouté : " + nom);
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }

        System.out.println(get_acteurs().get(0));

    }

    public void lancer_acteurs() {
        System.out.println("Lancement de " + acteurs.size());
        for (Acteur a : this.acteurs) {
            System.out.println("Lancement de " + a.get_nom());
            a.lancer();
        }
    }

    public boolean contient_joueurs() {
        for (Acteur a : this.acteurs) {
            if (a.is_player) return true;
        }

        return false;
    }

    public ArrayList<Acteur> get_acteurs() {
        return acteurs;
    }

    public void set_partie(Partie p){
    	this.p = p;
    }

}
