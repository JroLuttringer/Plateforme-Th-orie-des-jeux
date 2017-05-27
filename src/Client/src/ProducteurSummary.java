import java.io.Serializable;

/* Résumé des informations des producteurs
Diffusé aux acteurs en début de partie */
public class ProducteurSummary implements Serializable {
    public Ressource[] ressources;
    public String addr;
    public String nom;
    public int intervalle;

    public ProducteurSummary(Ressource[] ressources, String addr, String nom, int intervalle) {
        this.ressources = ressources;
        this.addr = addr;
        this.nom = nom;
        this.intervalle = intervalle;
    }
}
