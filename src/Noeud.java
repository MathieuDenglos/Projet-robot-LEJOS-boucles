import java.util.ArrayList;
import lejos.robotics.pathfinding.Node;

public class Noeud extends Node {
    private Orientation orientation; // Orientation du noeud precedent a ce noeud
    private ArrayList<Noeud> noeuds = new ArrayList<Noeud>(); // List des noeuds relies
    private int couleur; // Couleur du noeud (voir class TypeNoeud)

    /**
     * Constructeur de la classe noeud
     * <p>
     * Tous les autres raguments sont a definir ulterierement dans set_valeurs ou
     * ajouter_noeud
     * 
     * @param orientation Orientation pour aller du noeud precedent a ce noeud
     */
    public Noeud(Orientation orientation) {
        super(0, 0);
        this.orientation = orientation;
    }

    /**
     * Permet de definir les differentes valeurs du noeud
     * <p>
     * L'orientation du noeud est a definir lors de la creation de celui-ci et les
     * noeuds connectes doivent êtes ajoutes avec la methode ajouter_noeud
     * 
     * @param couleur la couleur du noeud
     * @param x       La coordonee x du noeud
     * @param y       La coordonee y du noeud
     */
    public void set_valeurs(int couleur, float x, float y) {
        // Verifie que la couleur envoyee correspond bien a une couleur de noeud
        if (couleur != TypeNoeud.tresor && couleur != TypeNoeud.embranchement && couleur != TypeNoeud.cul_de_sac) {
            System.out.println("ERREUR RENTREE DANS LE CONSTRUCTEUR DE NOEUD");
        } else {
            this.couleur = couleur;
        }
        this.x = x;
        this.y = y;
    }

    /**
     * Rajoute un noeud connecte au noeud actuel
     * 
     * @param noeud noeud connecte au noeud actuel
     */
    public void ajouter_noeud(Noeud noeud) {
        noeuds.add(noeud);
    }

    /**
     * Fonction recursive permettant au robot de visiter et cartographier le
     * labyrinthe, en ressort des que le tresor a ete trouve.
     * <p>
     * Commence l'exploration a partir du noeud qui est envoye.
     * 
     * @param robot           Le robot parcourrant le labyrinthe
     * @param labyrinthe      Le labyrinthe que le robot visite
     * @param noeud_precedent Le noeud d'où viens le
     */
    public void visite_noeud(Robot robot, Labyrinthe labyrinthe, Noeud noeud_precedent) {
        // Fait avancer le robot jusqu'au noeud
        robot.avancer_au_noeud(this.orientation);

        // decouvre le noeud et le complete (couleur, noeuds connectes, coordonees...)
        robot.decouvrir(labyrinthe, noeud_precedent, this);

        // Si le noeud est un embranchement, explore tous les noeuds qui y sont
        // connectes jusqu'a trouver le tresor ou avoir visite tous les noeuds
        if (this.get_couleur() == TypeNoeud.embranchement) {
            for (Noeud noeud : noeuds) {
                // Verifie si la branche que le robot est sur le point d'explorer n'a pas deja
                // ete explore precedement. Sinon l'explore
                if (labyrinthe.chercher_noeud_commun(this) != noeud.orientation) {
                    noeud.visite_noeud(robot, labyrinthe, this);
                }
                // Si le tresor a ete trouve, plus besoins d'explorer le reste du labyrinthe
                if (labyrinthe.get_noeud_tresor() != null)
                    return;
            }
            // Si aucun des chemins ne menent vers le tresor retourne au noeud precedent
            robot.avancer_au_noeud(this.orientation.droite().droite());
        }
        // Si le noeud contient le tresor sort de la phase exploration
        else if (this.get_couleur() == TypeNoeud.tresor) {
            labyrinthe.set_noeud_tresor(this);
            return;
        }
        // Si le noeud est un cul de sac fait demi tour
        else if (this.get_couleur() == TypeNoeud.cul_de_sac) {
            robot.avancer_au_noeud(this.orientation.droite().droite());
            return;
        }
    }

    /**
     * Programme recursif permettant de comparer les coordonees du robot avec celles
     * des differents noeuds a partir duquel la methode est appelee
     * 
     * @param robot Le robot
     * @return le noeud où le robot se trouve, sinon null
     */
    public Noeud verifier_existence(Robot robot) {

        // Si le noeud verifie est un cul de sac, alors le robot ne peut pas revenir
        // dessus, retourne null
        if (this.get_couleur() == TypeNoeud.cul_de_sac)
            return null;

        // Si le robot est proche du noeud, cela signifie qu'il est sur le noeud
        if (this.est_proche(robot))
            return this;
        // Sinon refait la même verification pour tous les noeuds connectes
        else {
            for (Noeud noeud : noeuds) {
                Noeud temp = noeud.verifier_existence(robot);
                // Si le noeud a ete trouve, le retourne sans verifier les autres noeuds
                if (temp != null)
                    return temp;
            }
        }
        return null;
    }

    /**
     * Compare les coordonees du robot avec celles du noeud
     * 
     * @param robot Le robot
     * @return True si le noeud est a moins de "erreur position" mm du robot
     */
    public boolean est_proche(Robot robot) {
        return ((robot.get_x() - this.get_x()) * (robot.get_x() - this.get_x()) + (robot.get_y() - this.get_y())
                * (robot.get_y() - this.get_y()) < Robot.get_erreur_position() * Robot.get_erreur_position());
    }

    /** @return les differents noeuds connectes a ce noeud */
    public ArrayList<Noeud> get_noeuds() {
        return this.noeuds;
    }

    /** @return la couleur du noeud */
    public int get_couleur() {
        return couleur;
    }

    /** @return la coordonee x du noeud */
    public float get_x() {
        return this.x;
    }

    /** @return la coordonee y du noeud */
    public float get_y() {
        return this.y;
    }

    /** @return l'orientation pour aller du noeud precedent a ce noeud */
    public Orientation get_orientation() {
        return this.orientation;
    }
}
