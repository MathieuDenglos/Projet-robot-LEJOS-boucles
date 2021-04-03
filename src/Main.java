public class Main {
    public static void main(String[] args) {
        // cree un robot et un labyrinthe
        Robot robot = new Robot();
        Labyrinthe labyrinthe = new Labyrinthe();

        // Resoud le labyrinthe
        labyrinthe.resoudre(robot);
    }
}