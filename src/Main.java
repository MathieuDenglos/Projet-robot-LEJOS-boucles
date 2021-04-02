public class Main {
    public static void main(String[] args) {
        // cree un robot, le calibre,
        Robot robot = new Robot();
        Labyrinthe labyrinthe = new Labyrinthe();
        labyrinthe.resoudre(robot);
    }
}