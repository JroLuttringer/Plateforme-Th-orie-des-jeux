import java.io.Serializable;
// Résumé des info du coordinateur
// Information partagé entre tous les acteurs
public class CoordinateurSummary implements Serializable {
    String nom;
    String addr;

    public CoordinateurSummary(String nom, String addr) {
        this.nom = nom;
        this.addr = addr;
    }
}
