import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.Path;

public class Labyrinthe {
    private Noeud entree, tresor; // Contient donc tout le labyrinthe
    private Map<Noeud, Orientation> noeuds_communs = new HashMap<Noeud, Orientation>();

    public Orientation chercher_noeud_commun(Noeud comparaison) {
        return noeuds_communs.get(comparaison);
    }

    public void ajout_noeud_commun(Noeud noeud, Orientation orientation) {
        noeuds_communs.put(noeud, orientation);
    }

    public Noeud verifier_existence(Robot robot) {
        return entree.verifier_existence(robot);
    }

    public void resoudre(Robot robot) {
        // calibration du robot
        robot.calibration();

        // création du premier lieu avec un couloir dans le sens du robot
        ArrayList<Noeud> premier_noeud = new ArrayList<Noeud>();
        this.entree = new Noeud(robot.get_orientation());
        premier_noeud.add(new Noeud(robot.get_orientation()));
        this.entree.set_valeurs(TypeNoeud.debut, premier_noeud, 0, 0);

        // appel de la fonction récursif qui va créer les noeuds
        premier_noeud.get(0).visite_noeud(robot, this, entree);

        // Résolution du chemin si le trésor a été trouvé
        // sinon le robot se situe déjà à l'entrée du labyrinthe
        if (tresor != null) {
            AstarSearchAlgorithm astar = new AstarSearchAlgorithm();
            Path path = astar.findPath(tresor, entree);
            Orientation retour = Orientation.NORD;
            for (int i = 1; i < path.size(); i++) {
                robot.avancer_au_noeud(
                        retour.trouver_orientation(path.get(i - 1).x, path.get(i - 1).y, path.get(i).x, path.get(i).y));
                // Potentielle célébration
            }
        }
    }

    public void set_noeud_tresor(Noeud noeud) {
        this.tresor = noeud;
    }

    public Noeud get_noeud_tresor() {
        return this.tresor;
    }
}