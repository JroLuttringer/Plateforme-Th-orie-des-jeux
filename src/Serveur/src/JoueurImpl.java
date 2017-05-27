import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/* CLasse la plus complexe 
Elle implémente le comportement des joueurs à chaque tour
ainsi que les action disponible pour les joueurs humain 
*/
public class JoueurImpl extends Acteur implements Joueur {

    private boolean m_observe;
    private Ressource[] m_objectifs; 
    private ArrayList<Ressource> m_possession;
    private Comportement m_comportement;
    private boolean m_humain;
    private Partie p;
    private boolean m_done = false;
    private int m_intervalle; // délai entre chaque action . Change dynamiquement pour éviter des actions trop périodique
    private float time = 0; // utile pour les logs

    public JoueurImpl(Partie p, Ressource[] objectifs, String nom, Comportement comportement,
                      boolean humain, String addr) throws RemoteException {
        super();
        this.m_nom = nom;
        this.m_observe = false;
        this.m_possession = new ArrayList<>();
        this.m_objectifs = objectifs;
        this.m_humain = humain;
        this.m_comportement = comportement;
        this.p = p;
        this.is_player = true;
        m_intervalle = 50 + (new Random().nextInt(100));
        if (p.getM_regle().isM_tourptour()) {
            finished = false;
            my_turn = false;
            m_intervalle = 50;
        } else {
            my_turn = true;

        }
        for (Ressource r : this.m_objectifs)
        {
            if (r.m_nom.equals("Petrole")) m_possession.add(new Petrole(0));
            if (r.m_nom.equals("Or")) m_possession.add(new Or(0));
            if (r.m_nom.equals("Fer")) m_possession.add(new Fer(0));
        }

    }

    public void lancer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!my_turn) return;
                finished = false;
                if (is_humain()) {
                    jouer_humain();
                    finished = true;
                    my_turn = false;
                    time++;
                    update_done();
                    return;
                }
                try {
                    if (!m_done) {
                        choisir_et_prendre();
                        update_done();
                        if (p.getM_regle().isM_tourptour()) time++;
                        else time+=(m_intervalle/(float)1000);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (p.getM_regle().isM_tourptour()) {
                    finished = true;
                    my_turn = false;
                }
                m_intervalle = 50 + (new Random().nextInt(100));
            }
        }, 0, m_intervalle);
    }

    public void arreter() {
        this.timer.cancel();
    }

    public void update_done() {
        m_done = this.objectifs_remplis();
        if (m_done) {
            write_log("Fin", 1, new Ressource("None", -1), m_possession,
                    time);
		System.out.println(m_nom+" is done");
        }
    }

    @Override
    public Ressource voler() throws RemoteException {
        if (!m_observe) {
            if (m_done) return new Ressource("FAIL", 0);
            Random r = new Random();
            System.out.println(m_nom + " se fait voler");
            if (m_possession.size() == 0) return new Ressource("FAIL", 0);
            int j = r.nextInt(m_possession.size());
            if (m_possession.get(j).m_disponible == 0) return new Ressource("FAIL", 0);
            int k = r.nextInt(p.getM_regle().getM_max_take());
            m_possession.get(j).enlever(k);
            return new Ressource(m_possession.get(j).m_nom, k);
        }
        return new Ressource("FAIL", p.getM_regle().getM_punition_vol());
    }

    public void afficher_infos_humain() {
        System.out.println("C'est votre tour, " + m_nom);
        System.out.println("Vos objectifs : ");
        for (Ressource r : m_objectifs)
            System.out.println(r.m_nom + "," + r.m_disponible + "  ||  ");
        System.out.println("==========================");
        System.out.println("Vos possessions :");
        for (Ressource r : m_possession)
            System.out.println(r.m_nom + "," + r.m_disponible + "  ||  ");
        System.out.println("==========================");
        System.out.println("Ce que propose les producteurs ( Fer, Or, Petrole):");
        for (ProducteurSummary p : p.getProd()) {
            try {
                Producteur pr = (Producteur) Naming.lookup("rmi://" + p.addr + "/" + p.nom);
                System.out.print(p.nom + ": ");
                Ressource[] chk = new Ressource[3];
                chk[0] = new Fer();
                chk[1] = new Or();
                chk[2] = new Petrole();
                for (Ressource r : chk)
                    System.out.print(pr.combien(r) + ", ");
                System.out.println("\n");
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Chez quel producteur souhaitez vous prendre les ressources ?");

    }

    public void jouer_humain() {
        afficher_infos_humain();
        String prod = "";
        boolean prod_ok = false;

        while (!prod_ok) {
            System.out.println("Veuillez entrer un nom correct)");
            BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
            String s = "";
            try {
                s = entree.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            prod = s;
            for (ProducteurSummary ps : p.getProd())
                if (ps.nom.equals(s)){ prod_ok = true; prod="rmi://"+ps.addr+"/"+ps.nom;}
		
        }
        Ressource souhaitee = new Ressource();
        souhaitee.setM_nom("None");
        souhaitee.setM_disponible(0);
        while (!souhaitee.m_nom.equals("Or") &&
                !souhaitee.m_nom.equals("Fer") &&
                !souhaitee.m_nom.equals("Petrole")) {
        System.out.println("Quelle ressource souhaitez vous ?");
        System.out.print("(Fer, Or ou Petrole)");
            BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
            String s = "";
            try {
                s = entree.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            souhaitee.setM_nom(s);
        }

        System.out.println("Combien de ressource souhaitez vous ?");
        System.out.println("Entre 0 et " + p.getM_regle().getM_max_take());
        String s = "";
        BufferedReader entree = new BufferedReader(new InputStreamReader(System.in));
        try {
            s = entree.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (s.equals("")) s = "0";
        int souhait = Integer.parseInt(s);
        souhaitee.setM_disponible(Math.min(p.getM_regle().getM_max_take(), souhait));
        try {
            int recu = prendre(souhaitee, prod);
            System.out.println("Vous avez recu " + recu + " exemplaires de la ressource");
            System.out.println("Tour terminé");
            if (recu != 0)
            write_log("Prendre", 1, new Ressource(souhaitee.m_nom,recu), m_possession, time);
            else
            write_log("Prendre", 0, new Ressource(souhaitee.m_nom,recu), m_possession, time );

        } catch (RemoteException e) {
            e.printStackTrace();
        }
                return;
    }

    public boolean possede(Ressource r) {
        for (Ressource r1 : this.m_possession) {
            if (r1.m_nom.equals(r.m_nom)) return true;
        }
        return false;
    }

    public boolean obj_ressource_accompli(Ressource r) {
        // if (!possede(r)) return false;
        for (Ressource r1 : this.m_possession)
        {
            if (r1.m_nom.equals(r.m_nom))
            {
                for (Ressource r2 : this.m_objectifs)
                {
                    if (r1.m_nom.equals(r2.m_nom))
                    {
                        return r1.m_disponible > r2.m_disponible;
                    }
                }
            }
        }

        return false;
    }

    public Ressource choisir_et_prendre() throws RemoteException {
        // On regarde si on veut voler un joueur
        Random i = new Random();
        int chance = i.nextInt(100);
        if (chance < m_comportement.m_proba_vol && p.getJoueurs().size() > 1) {
            // choix d'un joueur à voler
            int j = i.nextInt(p.getJoueurs().size());
            while (p.getJoueurs().get(j).nom.equals(this.m_nom)) j = i.nextInt(p.getJoueurs().size());
            try {
                System.out.println(m_nom + " tente de voler " + p.getJoueurs().get(j).nom);
                Joueur jr = (Joueur) Naming.lookup("rmi://" + p.getJoueurs().get(j).addr + "/" + p.getJoueurs().get(j).nom);
                Ressource vol = jr.voler();
                if (vol.m_nom.equals("FAIL")) {
                    if (m_possession.size() == 0) return vol;
                    j = i.nextInt(m_possession.size());
                    m_possession.get(j).enlever(vol.m_disponible);
                    write_log("Voler", 0, new Ressource(m_possession.get(j).m_nom, -vol.m_disponible),
                            m_possession, time );
                    return vol;
                }
                if (possede(vol)) {
                    for (Ressource x : m_possession)
                        if (x.m_nom.equals(vol.m_nom))
                            x.ajouter(vol.m_disponible);
                } else {
                    if (vol.m_nom.equals("Fer"))
                        m_possession.add(new Fer(vol.m_disponible));
                    if (vol.m_nom.equals("Or")) {
                        m_possession.add(new Or(vol.m_disponible));
                    }
                    if (vol.m_nom.equals("Petrole"))
                        m_possession.add(new Petrole(vol.m_disponible));
                }
                write_log("Vol", 1, vol, m_possession, time);
                return vol;
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        int j = i.nextInt(this.m_objectifs.length);
        Ressource recu = null;
        if (this.m_comportement.m_nom.equals("Agressif")) {
            j = i.nextInt(100);
            if (j < 30)
                recu = greed();
            else
                recu = normal();

        }
        if (this.m_comportement.m_nom.equals("Gentil"))
            recu = altruiste();
        if (this.m_comportement.m_nom.equals("Average"))
            recu = normal();

        if (recu.m_disponible != -1)
            write_log("Prendre", 1, recu, m_possession, time);
        else
            write_log("Prendre", 0, recu, m_possession, time );

        return recu;
    }

/* prend la ressource la plus rare ou le max de ce qu'il faut */
    public Ressource greed() {
        int total_fer = -1;
        int total_petrole = -1;
        int total_or = -1;
        int recu = 0;
        for (ProducteurSummary ps : p.getProd()) {
            String prod = "rmi://" + ps.addr + "/" + ps.nom;
            try {
                Producteur pr = (Producteur) Naming.lookup(prod);
                for (Ressource r : ps.ressources) {
                    if (r.m_nom.equals("Fer")) {
                        total_fer = 0;
                        total_fer += pr.combien(r);
                    }
                    if (r.m_nom.equals("Petrole")) {
                        total_petrole = 0;
                        total_petrole += pr.combien(r);
                    }
                    if (r.m_nom.equals("Or")) {
                        total_or = 0;
                        total_or += pr.combien(r);
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
        Ressource smallest = null;
        try {
            smallest = new Ressource();
            if (total_or != -1) {
                smallest.setM_nom("Or");
                smallest.setM_disponible(total_or);
            } else if (total_petrole != -1) {
                smallest.setM_nom("Petrole");
                smallest.setM_disponible(total_petrole);
            } else {
                smallest.setM_nom("Fer");
                smallest.setM_disponible(total_fer);
            }


            if (smallest.m_disponible > total_fer && total_fer != -1) {
                smallest.setM_nom("Fer");
                smallest.setM_disponible(total_fer);
            }
            if (smallest.m_disponible > total_petrole && total_petrole != -1) {
                smallest.setM_nom("Petrole");
                smallest.setM_disponible(total_petrole);
            }
            if (smallest.m_disponible > total_or && total_or != -1) {
                smallest.setM_nom("Or");
                smallest.setM_disponible(total_or);
            }

            smallest.setM_disponible(p.getM_regle().getM_max_take());

            for (ProducteurSummary ps : p.getProd()) {
                for (Ressource r : ps.ressources) {
                    if (r.m_nom.equals(smallest.m_nom)) {
                        String prod = "rmi://" + ps.addr + "/" + ps.nom;
                        recu = prendre(smallest, prod);
                    }
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return new Ressource(smallest.m_nom, recu);

    }

/* prend le max de ce qu'il faut */
    public Ressource normal() {
        Ressource a_prendre = this.m_possession.get(0);
        int recu = 0;
        for (Ressource x : this.m_possession)
        {
            if (!obj_ressource_accompli(x) && x.m_disponible < a_prendre.m_disponible)
                a_prendre = x;
        }
        String prod = "";
        for (ProducteurSummary ps : p.getProd())
            for (Ressource r : ps.ressources)
                if (r.m_nom.equals(a_prendre.m_nom))
                    prod = "rmi://" + ps.addr + "/" + ps.nom;
        try {
            recu = prendre(new Ressource(a_prendre.m_nom, p.getM_regle().getM_max_take()), prod);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return new Ressource(a_prendre.m_nom, recu);
    }

/* prend sa part de ce qu'il lui faut */
    public Ressource altruiste() {
        Ressource a_prendre = this.m_possession.get(0);
        int recu = 0;
        for (Ressource x : this.m_possession)
            if (!obj_ressource_accompli(x) && x.m_disponible < a_prendre.m_disponible)
                a_prendre = x;
        String prod = "";
        int max = 0;
        for (ProducteurSummary ps : p.getProd())
            for (Ressource r : ps.ressources)
                if (r.m_nom.equals(a_prendre.m_nom))
                    try {
                        String prod_temp = "rmi://" + ps.addr + "/" + ps.nom;
                        Producteur p = (Producteur) Naming.lookup(prod_temp);
                        if (p.combien(a_prendre) >= max)
                            prod = prod_temp;
                    } catch (NotBoundException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
        try {
            recu = prendre(new Ressource(a_prendre.m_nom, p.getM_regle().getM_max_take() / p.getJoueurs().size()), prod);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        return new Ressource(a_prendre.m_nom, recu);
    }

/* prendre chez un producteur*/
    public int prendre(Ressource r, String prod) throws RemoteException {
        int recu = 0;
        try {
            Producteur p = (Producteur) Naming.lookup(prod);
            recu = p.prendre_ressource(r);
            if (recu != 0 && recu != -1) {
                for (Ressource x : m_possession)
                    if (x.m_nom.equals(r.m_nom))
                    {
                        x.ajouter(recu);
                        
                    }
                if (!possede(r)) {
                    if (r.m_nom.equals("Fer"))
                        m_possession.add(new Fer(recu));
                    if (r.m_nom.equals("Petrole"))
                        m_possession.add(new Petrole(recu));
                    if (r.m_nom.equals("Or"))
                        m_possession.add(new Or(recu));
                }
            }
            System.out.println(this.get_nom() + " a pris " + recu + " ressources de " + r.m_nom );
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
        return recu;
    }

    public boolean objectifs_remplis() {
        {
            for (Ressource r : m_objectifs) {
                boolean found = false;
                for (Ressource r2 : m_possession) 
                {
                    if (r.m_nom.equals(r2.m_nom)) 
                    {
                        found = true;
                        if (r.m_disponible <= r2.m_disponible)
                            continue;
                        else
                            return false;
                    }
                }
                if (!found) return false;
            }
        }
        return true;
    }

    public boolean get_finished() {
        return finished;
    }

    public void set_finished(boolean x) throws RemoteException {
        finished = x;
    }

    public Ressource[] get_objectifs() {
        return m_objectifs;
    }

    public Comportement get_comportement() {
        return m_comportement;
    }

    public boolean is_humain() {
        return m_humain;
    }

    public boolean is_done() {
        return m_done;
    }

    public void set_myturn(boolean x) {
        my_turn = x;
    }

}
