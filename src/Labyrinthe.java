import lejos.nxt.ColorSensor.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;

public class Labyrinthe {
    private Noeud entree, tresor; // Contient donc tout le labyrinthe
    private Map<Noeud, Orientation> noeuds_communs = new HashMap<>();

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
        ArrayList<Couloir> temp = new ArrayList<>();
        temp.add(new Couloir(robot.get_orientation()));
        entree = new Noeud(new Color(0, 0, 0, 0, 0), temp, (float) robot.get_x(), (float) robot.get_y());

        // appel de la fonction récursif qui va créer les noeuds
        entree.get_couloirs().get(0).visite_couloir(robot, this);

        // Résolution du chemin si le trésor a été trouvé
        // sinon le robot se situe déjà à l'entrée du labyrinthe
        if (tresor != null) {

        }
    }

    public void set_noeud_tresor(Noeud noeud) {
        this.tresor = noeud;
    }
}
