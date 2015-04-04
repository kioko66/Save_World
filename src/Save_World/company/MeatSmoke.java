package Save_World.company;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class MeatSmoke {

    private int xCoordinate;
    private int yCoordinate;

    public long smokeLifeTime;

    public long timeOfCreation;

    public static BufferedImage smokeImg;

    public float imageTransparency;


    public void Initialize(int xCoordinate, int yCoordinate, long gameTime, long smokeLifeTime) {
        this.timeOfCreation = gameTime;

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate + 20;

        this.smokeLifeTime = smokeLifeTime;

        this.imageTransparency = 1.0f;
    }


    public void updateTransparency(long gameTime) {
        long currentLifeTime = gameTime - timeOfCreation;

        int currentLTInPercentages = (int) (currentLifeTime * 100 / smokeLifeTime);
        currentLTInPercentages = 100 - currentLTInPercentages;
        float rSmokeTransparency = 1.0f * (currentLTInPercentages * 0.01f);

        if (rSmokeTransparency > 0) {
            imageTransparency = rSmokeTransparency;
        }
    }

    public boolean didSmokeDisapper(long gameTime) {
        long currentLifeTime = gameTime - timeOfCreation;

        if (currentLifeTime >= smokeLifeTime) {
            return true;
        } else {
            return false;
        }
    }

    public void Draw(Graphics2D g2d) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageTransparency));

        float imageMultiplier = 2 - imageTransparency;
        int newImageWidth = (int) (smokeImg.getWidth() * imageMultiplier);
        int newImageHeight = (int) (smokeImg.getHeight() * imageMultiplier);
        int newImageYCoordinate = (int) (smokeImg.getHeight() / 2 * (1 - imageTransparency));
        g2d.drawImage(smokeImg, xCoordinate, yCoordinate - newImageYCoordinate, newImageWidth, newImageHeight, null);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
    }
}
