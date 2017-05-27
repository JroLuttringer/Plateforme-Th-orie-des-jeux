import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

// génère la partie suivant les fichiers de conf, attend la fin
// et récupère les logs

public class Client {
    public static void retrieve_logs(Partie p, Setup[] sts) {
        /* Ecriture des logs */
        System.out.println("Getting logs");
        ArrayList<String> logs = new ArrayList<String>();
        for (Setup s : sts) {
            try {
                logs.addAll(s.get_logs());
               
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Wrinting logs");
        String log_file = "../Logs/" + p.getM_nom();
        File dir = new File(log_file);
        dir.mkdirs();
        try {
            for (int i = 0; i < logs.size() / 2; i++) {
                String nom_a = logs.get(i * 2);
                String l = logs.get(i * 2 + 1);
                String nom_logs = (log_file + "/" + nom_a);
                PrintWriter out = new PrintWriter(nom_logs);
                out.write(l);
                out.close();
            }
            PrintWriter out_partie = new PrintWriter(log_file + "/Partie");
            String s = "";
            for (JoueurSummary js : p.getJoueurs()) {
                s = "";
                s += js.nom + "\t";
                int need_fer = 0, need_or = 0, need_petrole = 0;
                for (Ressource r : js.objectifs) {
                    if (r.m_nom.equals("Fer"))
                        need_fer += r.m_disponible;
                    if (r.m_nom.equals("Petrole"))
                        need_petrole += r.m_disponible;
                    if (r.m_nom.equals("Or"))
                        need_or += r.m_disponible;
                }
                s += need_fer + "\t" + need_or + "\t" + need_petrole + "\t" + js.comportement.m_nom + "\n";
                out_partie.write(s);
            }
            out_partie.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
 	for (Setup s : sts) {
            try {
                s.arreter_experience();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
	if (args.length < 2) {
            System.out.println("Usage :  java Client [nom fichier serveur] [configuration partie]");
            return;
        }
        String[] serveur_disponible = Parser.get_serveurs(args[0]);
        Partie p = Parser.get_conf_partie(args[1]);
        System.out.println(serveur_disponible.length / 2 + " serveurs mis à disposition ");
        Setup[] sts = new Setup[serveur_disponible.length/2];
	/* Récupération des setup */
        try {
            for (int i = 0; i<serveur_disponible.length/2; i++) {
                sts[i] = (Setup) Naming.lookup(
                        "rmi://" + serveur_disponible[i * 2] + "/" + serveur_disponible[i * 2 + 1]);
		sts[i].set_partie(p);
            }
        } catch (NotBoundException | RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
 /* On met en place le traitement du SigInt si besoin */
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                ArrayList<String> logs = new ArrayList<String>();
                for (Setup s : sts) {
                    try {
                        logs.addAll(s.get_logs());
                        s.arreter_experience();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                retrieve_logs(p,sts);
		 try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
		
                System.out.println("Programme arrete. Log recuperes");
            }
        });

	/* Lancement des Producteurs */
        for (ProducteurSummary pr : p.getProd()) {
            for (int i = 0; i < serveur_disponible.length / 2; i++) {
                if (pr.addr.equals(serveur_disponible[i * 2] + "/" + serveur_disponible[i * 2 + 1])) {
                    try {
                        sts[i].creer_producteur(p, pr.ressources, pr.intervalle, pr.nom, pr.addr);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
	
	/* Lancement des Joueurs */
        for (JoueurSummary j : p.getJoueurs()) {
            for (int i = 0; i < serveur_disponible.length / 2; i++) {
                if (j.addr.equals(serveur_disponible[i * 2] + "/" + serveur_disponible[i * 2 + 1])) {
                    try {
                        sts[i].creer_joueur(p, j.objectifs, j.nom, j.comportement, j.humain,
                                j.addr);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

	/* Lancement des Coordinateurs */
        for (CoordinateurSummary cr : p.getCoords()) {
            for (int i = 0; i < serveur_disponible.length / 2; i++) {
                if (cr.addr.equals(serveur_disponible[i * 2] + "/" + serveur_disponible[i * 2 + 1])) {
                    try {
                        sts[i].creer_coordinateur(p, cr.nom, cr.addr);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

        }

	// Lancement de la partie 
        System.out.println("Waiting 2 seconds before launching");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Launching");
        for (Setup s : sts) {
            try {
                s.lancer_acteurs();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        // On attend la fin des joueurs
        int nombre_serveurs = 0;
        for (Setup s : sts) {
                try {

                    if (s.contient_joueurs()) nombre_serveurs++;                  
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        boolean working = true;
        int serveurs_done = 0;
        while (working) {
            try {
                serveurs_done = 0;
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (Setup s : sts) {
                try {

                    if (!s.contient_joueurs()) continue;
                    if (s.acteurs_done()) serveurs_done++;
                    // working = working && !s.acteurs_done(); 
                    System.out.println("working = " + working);                   
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            if(p.getM_regle().m_condition_victoire.equals("First"))
            {
                if (serveurs_done>0) working = false;
            }
            else
            {
                if (serveurs_done == nombre_serveurs) working = false;
            }
        }

        if(p.getM_regle().m_condition_victoire.equals("First")) System.out.println("Le premier a termine");
            else System.out.println("Tous les acteurs ont termine");
       
	retrieve_logs(p,sts);	    
}
}
