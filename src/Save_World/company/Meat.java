package Save_World.company;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Meat {

    public final static long timeBetweenNewRockets = Framework.secInNanosec / 4;
    public static long timeOfLastCreatedRocket = 0;

    public static int damagePower = 100;

    public int xCoordinate;
    public int yCoordinate;

    private double movingXspeed;

    public long currentSmokeLifeTime;

    public static BufferedImage meatImg;

    public void Initialize(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate -20;
        this.yCoordinate = yCoordinate;
        
        this.movingXspeed = 23;
        
        this.currentSmokeLifeTime = Framework.secInNanosec / 2;
    }
    
    

    public boolean isItLeftScreen()
    {
        if(xCoordinate > 0 && xCoordinate < Framework.frameWidth)
            return false;
        else
            return true;
    }
    
    

    public void Update() {
        xCoordinate += movingXspeed;
    }
    
    

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(meatImg, xCoordinate, yCoordinate, null);
    }
}
