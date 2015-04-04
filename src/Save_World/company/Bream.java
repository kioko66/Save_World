package Save_World.company;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;


public class Bream {

    public final static long timeBetweenNewBream = Framework.secInNanosec / 10;
    public static long timeOfLastCreatedBream = 0;

    public static int damagePower = 20;

    public double xCoordinate;
    public double yCoordinate;

    private static int breamSpeed = 20;
    private double movingXspeed;
    private double movingYspeed;

    public static BufferedImage breamImg;

    public Bream(int xCoordinate, int yCoordinate, Point mousePosition) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        setDirectionAndSpeed(mousePosition);
    }

    private void setDirectionAndSpeed(Point mousePosition) {
        double directionVx = mousePosition.x - this.xCoordinate;
        double directionVy = mousePosition.y - this.yCoordinate;
        double lengthOfVector = Math.sqrt(directionVx * directionVx + directionVy * directionVy);
        directionVx = directionVx / lengthOfVector;
        directionVy = directionVy / lengthOfVector;

        this.movingXspeed = breamSpeed * directionVx;
        this.movingYspeed = breamSpeed * directionVy;
    }

    public boolean isItLeftScreen() {
        if (xCoordinate > 0 && xCoordinate < Framework.frameWidth &&
                yCoordinate > 0 && yCoordinate < Framework.frameHeight) {
            return false;
        } else {
            return true;
        }
    }

    public void Update() {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(breamImg, (int) xCoordinate, (int) yCoordinate, null);
    }
}
