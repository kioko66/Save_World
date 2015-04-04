package Save_World.company;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


public class MovingBackground {

    private BufferedImage image;

    private double speed;

    private double xPositions[];
    private int yPosition;


    public void Initialize(BufferedImage image, double speed, int yPosition) {
        this.image = image;
        this.speed = speed;

        this.yPosition = yPosition;

        int numberOfPositions = (Framework.frameWidth / this.image.getWidth()) + 2;
        xPositions = new double[numberOfPositions];

        for (int i = 0; i < xPositions.length; i++) {
            xPositions[i] = i * image.getWidth();
        }
    }

    private void Update() {
        for (int i = 0; i < xPositions.length; i++) {
            xPositions[i] += speed;

            if (speed < 0) {
                if (xPositions[i] <= -image.getWidth()) {
                    xPositions[i] = image.getWidth() * (xPositions.length - 1);
                }
            } else {
                if (xPositions[i] >= image.getWidth() * (xPositions.length - 1)) {
                    xPositions[i] = -image.getWidth();
                }
            }
        }
    }

    public void Draw(Graphics2D g2d) {
        this.Update();

        for (int i = 0; i < xPositions.length; i++) {
            g2d.drawImage(image, (int) xPositions[i], yPosition, null);
        }
    }
}