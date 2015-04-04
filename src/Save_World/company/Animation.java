package Save_World.company;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class Animation {

    private BufferedImage animImage;

    private int frameWidth;
    private int frameHeight;

    private int numberOfFrames;
    private long frameTime;

    private long startingFrameTime;
    private long timeForNextFrame;

    private int currentFrameNumber;

    private boolean loop;

    public int x;
    public int y;

    private int startingXOfFrameInImage;

    private int endingXOfFrameInImage;

    public boolean active;

    private long showDelay;

    private long timeOfAnimationCration;


    public Animation(BufferedImage animImage, int frameWidth, int frameHeight, int numberOfFrames, long frameTime, boolean loop, int x, int y, long showDelay) {
        this.animImage = animImage;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.numberOfFrames = numberOfFrames;
        this.frameTime = frameTime;
        this.loop = loop;

        this.x = x;
        this.y = y;

        this.showDelay = showDelay;

        timeOfAnimationCration = System.currentTimeMillis();

        startingXOfFrameInImage = 0;
        endingXOfFrameInImage = frameWidth;

        startingFrameTime = System.currentTimeMillis() + showDelay;
        timeForNextFrame = startingFrameTime + this.frameTime;
        currentFrameNumber = 0;
        active = true;
    }

    public void changeCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private void Update() {
        if (timeForNextFrame <= System.currentTimeMillis()) {
            currentFrameNumber++;
            if (currentFrameNumber >= numberOfFrames) {
                currentFrameNumber = 0;
                if (!loop) {
                    active = false;
                }
            }

            startingXOfFrameInImage = currentFrameNumber * frameWidth;
            endingXOfFrameInImage = startingXOfFrameInImage + frameWidth;

            startingFrameTime = System.currentTimeMillis();
            timeForNextFrame = startingFrameTime + frameTime;
        }
    }


    public void Draw(Graphics2D g2d) {
        this.Update();
        if (this.timeOfAnimationCration + this.showDelay <= System.currentTimeMillis()) {
            g2d.drawImage(animImage, x, y, x + frameWidth, y + frameHeight, startingXOfFrameInImage, 0, endingXOfFrameInImage, frameHeight, null);
        }
    }
}