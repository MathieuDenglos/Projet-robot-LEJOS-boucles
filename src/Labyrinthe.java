import lejos.nxt.ColorSensor.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

public class Labyrinthe {
    private Noeud entree;
    private Map<Noeud, Orientation> noeuds_communs = new HashMap<>();
    // Potentiellement une série de Node de Lejos pour le Astar

    public Orientation dans_la_map(Noeud comparaison) {
        return noeuds_communs.get(comparaison);
    }

    public void ajout_dans_map(Noeud noeud, Orientation orientation) {
        noeuds_communs.put(noeud, orientation);
    }

    public Noeud verifier_existence(Robot robot) {
        // return entree.();
    }

    public void resoudre(Robot robot) {
        // calibration du robot
        robot.calibration();

        // création du premier lieu avec un couloir dans le sens du robot
        entree = new Noeud(new Color(0, 0, 0, 0, 0),
                new ArrayList<>(Arrays.asList(new Couloir[] { new Couloir(robot.get_orientation()) })));

        // appel de la fonction récursif qui va créer les noeuds
        entree.get_couloirs().get(0).visite_couloir(robot, this);

        // Résolution du chemin si le trésor a été trouvé
        // sinon le robot se situe déjà à l'entrée du labyrinthe
        if (robot.get_tresor_trouve())
            ; // résolution

    }
}
