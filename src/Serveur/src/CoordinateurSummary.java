import java.io.Serializable;

public class CoordinateurSummary implements Serializable {
    String nom;
    String addr;

    public CoordinateurSummary(String nom, String addr) {
        this.nom = nom;
        this.addr = addr;
    }
}
