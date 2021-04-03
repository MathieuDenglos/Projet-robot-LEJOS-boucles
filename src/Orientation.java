public enum Orientation {

    // Les differentes valeurs
    NORD, EST, SUD, OUEST;

    // tableau qui contient les valeurs de l'enum {NORD, EST, SUD, OUEST}
    private static final Orientation[] valeurs = values();

    /** @return l'orientation a droite */
    public final Orientation droite() {
        return valeurs[(this.ordinal() + 1) % valeurs.length];
    }

    /** @return l'orientation a gauche */
    public final Orientation gauche() {
        return valeurs[(this.ordinal() - 1 + valeurs.length) % valeurs.length];
    }

    /**
     * compare deux directions
     * 
     * @param comparaison L'orientation visee
     * @return Le nombre de quarts de tours le plus faible vers la direction
     */
    public final int difference(Orientation comparaison) {
        int temp = this.ordinal() - comparaison.ordinal();
        if (temp == -3)
            return 1;
        if (temp == 3)
            return -1;
        else
            return temp;
    }

    /**
     * A partir des coordonees de deux noeuds (A et B), retourn l'orientation a
     * emprunte pour aller du noeud A au noeud B
     * 
     * @param x_A coordonee en x du noeud A
     * @param y_A coordonee en y du noeud A
     * @param x_B coordonee en x du noeud B
     * @param y_B coordonee en y du noeud B
     * @return l'orientation a prendre pour aller de A a B
     */
    public Orientation trouver_orientation(float x_A, float y_A, float x_B, float y_B) {
        if (x_A - x_B > 0 && x_A - x_B > y_A - y_B)
            return Orientation.SUD;
        else if (x_A - x_B < 0 && x_A - x_B < y_A - y_B)
            return Orientation.NORD;
        else if (y_A - y_B > 0 && x_A - x_B < y_A - y_B)
            return Orientation.OUEST;
        else
            return Orientation.EST;
    }
};