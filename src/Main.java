public class Main {
    public static void main(String[] args) {
        // crée un robot, le calibre,
        Robot robot = new Robot();
        Labyrinthe labyrinthe = new Labyrinthe();
        labyrinthe.resoudre(robot);
    }
}