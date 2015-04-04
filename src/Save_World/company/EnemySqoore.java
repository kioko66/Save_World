package Save_World.company;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class EnemySqoore {

    private static final long timeBetweenNewEnemiesInit = Framework.secInNanosec * 3;
    public static long timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
    public static long timeOfLastCreatedEnemy = 0;

    public int health;

    public int xCoordinate;
    public int yCoordinate;

    private static final double movingXspeedInit = -4;
    private static double movingXspeed = movingXspeedInit;

    public static BufferedImage sqooreBodyImg;

    public void Initialize(int xCoordinate, int yCoordinate) {
        health = 100;

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        EnemySqoore.movingXspeed = -4;
    }

    public static void restartEnemy() {
        EnemySqoore.timeBetweenNewEnemies = timeBetweenNewEnemiesInit;
        EnemySqoore.timeOfLastCreatedEnemy = 0;
        EnemySqoore.movingXspeed = movingXspeedInit;
    }

    public static void speedUp() {
        if (EnemySqoore.timeBetweenNewEnemies > Framework.secInNanosec) {
            EnemySqoore.timeBetweenNewEnemies -= Framework.secInNanosec / 10;
        }
        EnemySqoore.movingXspeed -= 0.25;
    }

    public boolean isLeftScreen() {
        if (xCoordinate < 0 - sqooreBodyImg.getWidth()) {
            return true;
        } else {
            return false;
        }
    }

    public void Update() {
        xCoordinate += movingXspeed;
    }


    public void Draw(Graphics2D g2d) {
        g2d.drawImage(sqooreBodyImg, xCoordinate, yCoordinate, null);
    }

}
