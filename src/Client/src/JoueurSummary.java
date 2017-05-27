import java.io.Serializable;

 /* Résumé des infos des joueurs 
Partagé entre tous les acteurs au débug de la partie
*/
public class JoueurSummary implements Serializable {
    public String addr;
    public String nom;
    public Ressource[] objectifs;
    public Comportement comportement;
    public boolean humain;

    public JoueurSummary(String addr, String nom, Ressource[] objectifs, Comportement comportement, boolean humain) {
        this.addr = addr;
        this.nom = nom;
        this.objectifs = objectifs;
        this.comportement = comportement;
        this.humain = humain;
    }
}
