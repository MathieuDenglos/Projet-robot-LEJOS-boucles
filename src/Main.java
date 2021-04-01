public class Main {
    public static void main(String[] args) {
        // crée un robot, le calibre,
        Robot robot = new Robot();
        Labyrinthe labyrinthe = new Labyrinthe();
        labyrinthe.resoudre(robot);
        if (robot.get_tresor_trouve()) {
            robot.tourner(360);
            robot.tourner(-360);
        }
    }
}