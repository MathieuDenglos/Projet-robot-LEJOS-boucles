import java.util.HashMap;
import java.util.Map;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.Path;

public class Labyrinthe {
    private Noeud entree, tresor; // entree contient tout le labyrinthe
    private Map<Noeud, Orientation> noeuds_communs = new HashMap<Noeud, Orientation>();
    // Map contenant les noeuds par lesquels le robot est repassé dessus, ainsi que
    // l'orientation par laquelle il vient ; évite au robot de réexplorer un bout

    /**
     * Permet de résoudre le labyrinthe
     * 
     * @param robot Le robot qui explore le labyrinthe
     */
    public void resoudre(Robot robot) {
        // calibration du robot
        robot.calibration();

        // creation du premier lieu avec un couloir dans le sens du robot
        this.entree = new Noeud(robot.get_orientation());
        this.entree.set_valeurs(TypeNoeud.debut, 0, 0);
        this.entree.ajouter_noeud(new Noeud(robot.get_orientation()));

        // appel de la fonction recursif qui va creer les noeuds
        entree.get_noeuds().get(0).visite_noeud(robot, this, entree);

        // Resolution du chemin si le tresor a ete trouve
        // sinon le robot se situe deja a l'entree du labyrinthe
        if (tresor != null) {
            AstarSearchAlgorithm astar = new AstarSearchAlgorithm();
            Path path = astar.findPath(tresor, entree);
            Orientation retour = Orientation.NORD;
            for (int i = 1; i < path.size(); i++) {
                robot.avancer_au_noeud(
                        retour.trouver_orientation(path.get(i - 1).x, path.get(i - 1).y, path.get(i).x, path.get(i).y));
                // Potentielle celebration
            }
        }
    }

    /**
     * Permet de vérifier si le robot a déjà visité une branche du noeud
     * <p>
     * Permet de s'assurer que le robot ne repasse pas sur une même branche
     * 
     * @param noeud Le noeud à chercher
     * @return la direction déjà observé sinon null
     */
    public Orientation chercher_noeud_commun(Noeud noeud) {
        return noeuds_communs.get(noeud);
    }

    /**
     * Indique que le labyrinthe a une boucle
     * <p>
     * A appeler lorsque le robot revient sur un embranchement déjà scanné
     * 
     * @param noeud       Le noeud sur lequel le robot est revenu
     * @param orientation L'inverse de l'orientation depuis lequel le robot vient
     */
    public void ajout_noeud_commun(Noeud noeud, Orientation orientation) {
        noeuds_communs.put(noeud, orientation);
    }

    /**
     * Programme comparant les coordonées du robot avec tous les noeuds du
     * labyrinthe
     * 
     * @param robot Le robot
     * @return le noeud où le robot se trouve, sinon null
     */
    public Noeud verifier_existence(Robot robot) {
        return entree.verifier_existence(robot);
    }

    /**
     * Permet de définir le noeud avec le trésor, une fois celui-ci trouvé
     * 
     * @param noeud Le noeud du trésor
     */
    public void set_noeud_tresor(Noeud noeud) {
        this.tresor = noeud;
    }

    /** @return le noeud avec le tresor sinon null */
    public Noeud get_noeud_tresor() {
        return this.tresor;
    }
}