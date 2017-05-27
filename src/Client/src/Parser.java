import java.io.*;


public class Parser {

    /* Parse les fichiers serveurs et configuration pour déterminer
comment lancer la partie demandée */
    public static String[] get_serveurs(String fichier) {
        int nb_serveur = 0;
        String[] serveur_disponible = null;
        LineNumberReader lnr;
        try {
            lnr = new LineNumberReader(new FileReader(new File(fichier)));
            lnr.skip(Long.MAX_VALUE);
            lnr.close();
            nb_serveur = lnr.getLineNumber();
            if (nb_serveur == 0) nb_serveur++;
            serveur_disponible = new String[nb_serveur * 2];
            String line;
            BufferedReader br;
            br = new BufferedReader(new FileReader(fichier));
            int i = 0;
            while ((line = br.readLine()) != null) {
                String[] serveur = line.split(",");
                serveur_disponible[i] = serveur[0];
                serveur_disponible[i + 1] = serveur[1];
                i += 2;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serveur_disponible;
    }

    public static ProducteurSummary read_prod(BufferedReader br) {
        String line;
        Ressource[] r = null;
        String nom = null;
        String intervalle = null;
        String addr = null;
        try {
            line = br.readLine();
            nom = line.split("=")[1];
            line = br.readLine();
            String[] ressources = line.split("=")[1].split(",");
            line = br.readLine();
            intervalle = line.split("=")[1];
            line = br.readLine();
            addr = line.split("=")[1];
            r = new Ressource[ressources.length / 2];
            for (int i = 0; i < ressources.length / 2; i++) {
                if (ressources[2 * i].equals("Fer"))
                    r[i] = new Fer(Integer.parseInt(ressources[2 * i + 1]));
                if (ressources[2 * i].equals("Petrole"))
                    r[i] = new Petrole(Integer.parseInt(ressources[2 * i + 1]));
                if (ressources[2 * i].equals("Or"))
                    r[i] = new Or(Integer.parseInt(ressources[2 * i + 1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ProducteurSummary(r, addr, nom, Integer.parseInt(intervalle));
    }

    public static JoueurSummary read_joueur(BufferedReader br, boolean humain) {
        String line;
        Ressource[] r = null;
        String nom = null;
        Comportement comportement = null;
        String addr = null;
        try {
            line = br.readLine();
            nom = line.split("=")[1];
            line = br.readLine();
            String[] ressources = line.split("=")[1].split(",");
            line = br.readLine();
            addr = line.split("=")[1];
            line = br.readLine();
            String cprt = line.split("=")[1];
            if (cprt.equals("Average"))
                comportement = new Average();
            if (cprt.equals("Agressif"))
                comportement = new Agressif();
            if (cprt.equals("Gentil"))
                comportement = new Gentil();
            if (cprt.equals("Humain"))
                comportement = new Humain();
            r = new Ressource[ressources.length / 2];
            for (int i = 0; i < ressources.length / 2; i++) {
                if (ressources[2 * i].equals("Fer"))
                    r[i] = new Fer(Integer.parseInt(ressources[2 * i + 1]));
                if (ressources[2 * i].equals("Petrole"))
                    r[i] = new Petrole(Integer.parseInt(ressources[2 * i + 1]));
                if (ressources[2 * i].equals("Or"))
                    r[i] = new Or(Integer.parseInt(ressources[2 * i + 1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new JoueurSummary(addr, nom, r, comportement, humain);
    }

    public static CoordinateurSummary read_coord(BufferedReader br) {
        String line;
        String nom = null;
        String addr = null;
        try {
            line = br.readLine();
            nom = line.split("=")[1];
            line = br.readLine();
            addr = line.split("=")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new CoordinateurSummary(nom, addr);
    }


    public static void read_partie(BufferedReader br, Partie p) {
        String line;
        String nom = null;
        int max_take = 0;
        int pun_vol = 0;
        String victoire_cond = "";
        boolean tourpartour = false;
        int nb_ia, nb_j, nb_p, nb_c;
        nb_ia = nb_j = nb_p = nb_c = 0;
        try {
            line = br.readLine();
            nom = line.split("=")[1];
            line = br.readLine();
            max_take = Integer.parseInt(line.split("=")[1]);
            line = br.readLine();
            pun_vol = Integer.parseInt(line.split("=")[1]);
            line = br.readLine();
            victoire_cond = line.split("=")[1];
            line = br.readLine();
            tourpartour = Boolean.parseBoolean(line.split("=")[1]);
            line = br.readLine();
            nb_ia = Integer.parseInt(line.split("=")[1]);
            line = br.readLine();
            nb_j = Integer.parseInt(line.split("=")[1]);
            line = br.readLine();
            nb_p = Integer.parseInt(line.split("=")[1]);
            line = br.readLine();
            nb_c = Integer.parseInt(line.split("=")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.setM_nom(nom);
        p.setM_regle(new Regle(max_take, pun_vol, victoire_cond, tourpartour));
        p.setM_nb_ias(nb_ia);
        p.setM_nb_joueurs(nb_j);
        p.setM_nb_prod(nb_p);
        p.setM_nb_coord(nb_c);
    }

    public static Partie get_conf_partie(String fichier) {
        LineNumberReader lnr;
        Partie p = new Partie();
        try {
            lnr = new LineNumberReader(new FileReader(new File(fichier)));
            lnr.skip(Long.MAX_VALUE);
            lnr.close();
            String line;
            BufferedReader br;
            br = new BufferedReader(new FileReader(fichier));
            while ((line = br.readLine()) != null) {
                String[] ligne_split = line.split("=");
                if (ligne_split == null) continue;
                if (ligne_split.length != 2) continue;
                if (!ligne_split[0].equals("TYPE")) continue;
                if (ligne_split[1].equals("PROD")) p.getProd().add(read_prod(br));
                if (ligne_split[1].equals("JOUEUR")) p.getJoueurs().add(read_joueur(br, true));
                if (ligne_split[1].equals("IA")) p.getJoueurs().add(read_joueur(br, false));
                if (ligne_split[1].equals("COORD")) p.getCoords().add(read_coord(br));
                if (ligne_split[1].equals("PARTIE")) read_partie(br, p);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return p;
    }

}
