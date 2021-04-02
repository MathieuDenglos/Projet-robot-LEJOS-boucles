import java.util.ArrayList;
import lejos.robotics.pathfinding.Node;

public class Noeud extends Node {

    private Orientation orientation;
    private ArrayList<Noeud> noeuds = new ArrayList<Noeud>();
    private int couleur;

    public void add(Noeud noeud) {
        noeuds.add(noeud);
    }

    public Noeud(Orientation orientation) {
        super(0, 0);
        this.orientation = orientation;
    }

    public void set_valeurs(int couleur, ArrayList<Noeud> noeuds, float x, float y) {
        // Verifie que la couleur envoyee correspond bien a une couleur de noeud
        if (couleur != TypeNoeud.tresor && couleur != TypeNoeud.embranchement && couleur != TypeNoeud.cul_de_sac) {
            System.out.println("ERREUR RENTREE DANS LE CONSTRUCTEUR DE NOEUD");
        } else {
            this.couleur = couleur;
        }
        this.x = x;
        this.y = y;
        this.noeuds = noeuds;
    }

    public Noeud verifier_existence(Robot robot) {
        if (this.get_couleur() == TypeNoeud.cul_de_sac)
            return null;

        if (this.est_proche(robot))
            return this;
        else {
            for (Noeud noeud : noeuds) {
                Noeud temp = noeud.verifier_existence(robot);
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
        return couleur;
    }

    public float get_x() {
        return this.x;
    }

    public float get_y() {
        return this.y;
    }

    public Orientation get_orientation() {
        return this.orientation;
    }

    public void afficherNoeud() {
        // afficher la couleur du noeud et le nombre de couloirs qui en sortent
        System.out.println("La couleur du noeud est : " + couleur);
        if (couleur == TypeNoeud.embranchement)
            System.out.println("Le nombre de chemin est :" + noeuds.size());
    }

    /**
     * Fonction recursive permettant au robot de visiter le labyrinthe et y sort des
     * que le tresor a ete trouve ou le labyrinthe entierement visite
     * 
     * @param robot Le robot envoye dans le couloir
     */

    public void visite_noeud(Robot robot, Labyrinthe labyrinthe, Noeud noeud_precedent) {
        // oriente le robot et le fait avancer jusqu'au prochain noeud
        robot.avancer_au_noeud(this.orientation);

        // recupere le noeud avec sa couleur et ses potentiels chemins
        robot.decouvrir(labyrinthe, noeud_precedent, this);

        // Si le noeud est un carrefour, analyse le carrefour pour de potentiel chemins
        // a emprunter
        if (this.get_couleur() == TypeNoeud.embranchement) {
            // explore tous les embranchements et sous-embranchements du noeud
            for (Noeud noeud : noeuds) {
                if (labyrinthe.chercher_noeud_commun(this) != noeud.get_orientation()) {
                    System.out.println("parcours d'un chemin, " + noeud.orientation.toString());
                    noeud.visite_noeud(robot, labyrinthe, this);
                }

                // Si le tresor a ete trouve plus loins dans le parcours, retourne sans verifier
                // les autres chemins
                if (labyrinthe.get_noeud_tresor() != null) {
                    return;
                }
            }
            // Si aucun des chemins ne vont vers le tresor retourne au noeud precedent
            robot.avancer_au_noeud(this.orientation.droite().droite());
        }
        // Si le noeud contient le tresor, indique au robot que le tresor a ete trouve
        // et retourne
        else if (this.get_couleur() == TypeNoeud.tresor) {
            System.out.println("tresor recupere, retour au depart");
            labyrinthe.set_noeud_tresor(this);
            return;
        }
        // Si le noeud est un cul de sac ou qu'aucune des branches ne mene au tresor,
        // fait demi tour
        else if (this.get_couleur() == TypeNoeud.cul_de_sac) {
            System.out.println("cul_de_sac : demi tour");
            robot.avancer_au_noeud(this.orientation.droite().droite());
            return;
        }
    }

    public boolean verifier_orientation(Robot robot) {
        return this.orientation == robot.get_orientation();
    }
}
