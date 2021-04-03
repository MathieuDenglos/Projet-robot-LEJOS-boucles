import java.util.ArrayList;
import lejos.robotics.pathfinding.Node;

public class Noeud extends Node {
    private Orientation orientation; // Orientation du noeud précédent à ce noeud
    private ArrayList<Noeud> noeuds = new ArrayList<Noeud>(); // List des noeuds reliés
    private int couleur; // Couleur du noeud (voir class TypeNoeud)

    /**
     * Constructeur de la classe noeud
     * <p>
     * Tous les autres raguments sont à définir ultèrierement dans set_valeurs ou
     * ajouter_noeud
     * 
     * @param orientation Orientation pour aller du noeud précédent à ce noeud
     */
    public Noeud(Orientation orientation) {
        super(0, 0);
        this.orientation = orientation;
    }

    /**
     * Permet de définir les différentes valeurs du noeud
     * <p>
     * L'orientation du noeud est à définir lors de la création de celui-ci et les
     * noeuds connectés doivent êtes ajoutés avec la méthode ajouter_noeud
     * 
     * @param couleur la couleur du noeud
     * @param x       La coordonée x du noeud
     * @param y       La coordonée y du noeud
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
     * Rajoute un noeud connecté au noeud actuel
     * 
     * @param noeud noeud connecté au noeud actuel
     */
    public void ajouter_noeud(Noeud noeud) {
        noeuds.add(noeud);
    }

    /**
     * Fonction recursive permettant au robot de visiter et cartographier le
     * labyrinthe, en ressort dés que le trésor a été trouvé.
     * <p>
     * Commence l'exploration à partir du noeud qui est envoyé.
     * 
     * @param robot           Le robot parcourrant le labyrinthe
     * @param labyrinthe      Le labyrinthe que le robot visite
     * @param noeud_precedent Le noeud d'où viens le
     */
    public void visite_noeud(Robot robot, Labyrinthe labyrinthe, Noeud noeud_precedent) {
        // Fait avancer le robot jusqu'au noeud
        robot.avancer_au_noeud(this.orientation);

        // découvre le noeud et le complète (couleur, noeuds connectés, coordonées...)
        robot.decouvrir(labyrinthe, noeud_precedent, this);

        // Si le noeud est un embranchement, explore tous les noeuds qui y sont
        // connectés jusqu'à trouver le trésor ou avoir visité tous les noeuds
        if (this.get_couleur() == TypeNoeud.embranchement) {
            for (Noeud noeud : noeuds) {
                // Vérifie si la branche que le robot est sur le point d'explorer n'a pas déjà
                // été exploré précédement. Sinon l'explore
                if (labyrinthe.chercher_noeud_commun(this) != noeud.orientation) {
                    System.out.println("parcours d'un chemin, " + noeud.orientation.toString());
                    noeud.visite_noeud(robot, labyrinthe, this);
                }
                // Si le trésor a été trouvé, plus besoins d'explorer le reste du labyrinthe
                if (labyrinthe.get_noeud_tresor() != null)
                    return;
            }
            // Si aucun des chemins ne mènent vers le tresor retourne au noeud precedent
            robot.avancer_au_noeud(this.orientation.droite().droite());
        }
        // Si le noeud contient le tresor sort de la phase exploration
        else if (this.get_couleur() == TypeNoeud.tresor) {
            System.out.println("tresor recupere, analyse du labyrinthe");
            labyrinthe.set_noeud_tresor(this);
            return;
        }
        // Si le noeud est un cul de sac fait demi tour
        else if (this.get_couleur() == TypeNoeud.cul_de_sac) {
            System.out.println("cul_de_sac : demi tour");
            robot.avancer_au_noeud(this.orientation.droite().droite());
            return;
        }
    }

    /**
     * Programme récursif permettant de comparer les coordonées du robot avec celles
     * des différents noeuds à partir duquel la méthode est appelée
     * 
     * @param robot Le robot
     * @return le noeud où le robot se trouve, sinon null
     */
    public Noeud verifier_existence(Robot robot) {

        // Si le noeud vérifié est un cul de sac, alors le robot ne peut pas revenir
        // dessus, retourne null
        if (this.get_couleur() == TypeNoeud.cul_de_sac)
            return null;

        // Si le robot est proche du noeud, cela signifie qu'il est sur le noeud
        if (this.est_proche(robot))
            return this;
        // Sinon refait la même vérification pour tous les noeuds connectés
        else {
            for (Noeud noeud : noeuds) {
                Noeud temp = noeud.verifier_existence(robot);
                // Si le noeud a été trouvé, le retourne sans vérifier les autres noeuds
                if (temp != null)
                    return temp;
            }
        }
        return null;
    }

    /**
     * Compare les coordonées du robot avec celles du noeud
     * 
     * @param robot Le robot
     * @return True si le noeud est à moins de "erreur position" mm du robot
     */
    public boolean est_proche(Robot robot) {
        return ((robot.get_x() - this.get_x()) * (robot.get_x() - this.get_x()) + (robot.get_y() - this.get_y())
                * (robot.get_y() - this.get_y()) < Robot.get_erreur_position() * Robot.get_erreur_position());
    }

    /** retourne les différents noeuds connectés à ce noeud */
    public ArrayList<Noeud> get_noeuds() {
        return this.noeuds;
    }

    /** retourne la couleur du noeud */
    public int get_couleur() {
        return couleur;
    }

    /** retourne la coordonée x du noeud */
    public float get_x() {
        return this.x;
    }

    /** retourne la coordonée y du noeud */
    public float get_y() {
        return this.y;
    }

    /** retourne l'orientation pour aller du noeud précédent à ce noeud */
    public Orientation get_orientation() {
        return this.orientation;
    }
}
