import java.util.ArrayList;
import lejos.nxt.ColorSensor.Color;
import lejos.robotics.pathfinding.Node;

public class Noeud extends Node {
    private ArrayList<Couloir> couloirs = new ArrayList<>();
    private Color couleur;

    public Noeud(Color couleur, ArrayList<Couloir> couloirs, float x, float y) {
        // Vérifie que la couleur envoyée correspond bien à une couleur de noeud
        super(x, y);
        if (couleur.getColor() != TypeNoeud.tresor && couleur.getColor() != TypeNoeud.embranchement
                && couleur.getColor() != TypeNoeud.cul_de_sac) {
            System.out.println("ERREUR RENTREE DANS LE CONSTRUCTEUR DE NOEUD");
        } else {
            this.couleur = couleur;
        }
        this.couloirs = couloirs;
    }

    public Noeud verifier_existence(Robot robot) {
        if (this == null || this.get_couleur() == TypeNoeud.cul_de_sac)
            return null;

        if (this.est_proche(robot))
            return this;
        else {
            for (int i = 0; i < couloirs.size(); i++) {
                Noeud temp = couloirs.get(i).get_noeud().verifier_existence(robot);
                if (temp != null)
                    return temp;
            }
        }
        return null;
    }

    public boolean est_proche(Robot robot) {
        return ((robot.get_x() - this.get_x()) * (robot.get_x() - this.get_x())
                + (robot.get_y() - this.get_y()) * (robot.get_y() - this.get_y()) < Robot.get_erreur_position());
    }

    public int get_couleur() {
        return couleur.getColor();
    }

    public ArrayList<Couloir> get_couloirs() {
        return couloirs;
    }

    public float get_x() {
        return this.x;
    }

    public float get_y() {
        return this.y;
    }

    public void afficherNoeud() {
        // afficher la couleur du noeud et le nombre de couloirs qui en sortent
        System.out.println("La couleur du noeud est : " + couleur);
        if (couleur.getColor() == TypeNoeud.embranchement)
            System.out.println("Le nombre de chemin est :" + couloirs.size());
    }
}
