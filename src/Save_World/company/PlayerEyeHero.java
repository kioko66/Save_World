package Save_World.company;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class PlayerEyeHero {

    private final int healthInit = 100;
    public int health;

    public int xCoordinate;
    public int yCoordinate;

    private double movingXspeed;
    public double movingYspeed;
    private double acceleratingXspeed;
    private double acceleratingYspeed;
    private double stoppingXspeed;
    private double stoppingYspeed;

    private final int numberOfMeatsInit = 80;
    public int numberOfMeats;

    private final int numberOfBreamInit = 1400;
    public int numberOfBream;

    public BufferedImage herobodyImg;

    private int offsetXMeatHolder;
    private int offsetYMeatHolder;

    public int meatHolderXcoordinate;
    public int meatHolderYcoordinate;

    private int offsetXBreamGun;
    private int offsetYBreamGun;

    public int breamGunXcoordinate;
    public int breamGunYcoordinate;


    public PlayerEyeHero(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        LoadContent();
        Initialize();
    }

    private void Initialize() {
        this.health = healthInit;

        this.numberOfMeats = numberOfMeatsInit;
        this.numberOfBream = numberOfBreamInit;

        this.movingXspeed = 0;
        this.movingYspeed = 0;
        this.acceleratingXspeed = 0.2;
        this.acceleratingYspeed = 0.2;
        this.stoppingXspeed = 0.1;
        this.stoppingYspeed = 0.1;

        this.offsetXMeatHolder = 138;
        this.offsetYMeatHolder = 40;
        this.meatHolderXcoordinate = this.xCoordinate + this.offsetXMeatHolder;
        this.meatHolderYcoordinate = this.yCoordinate + this.offsetYMeatHolder;

        this.offsetXBreamGun = herobodyImg.getWidth() - 20;
        this.offsetYBreamGun = herobodyImg.getHeight() - 40;
        this.breamGunXcoordinate = this.xCoordinate + this.offsetXBreamGun;
        this.breamGunYcoordinate = this.yCoordinate + this.offsetYBreamGun;
    }

    private void LoadContent() {
        try {
            URL helicopterBodyImgUrl = this.getClass().getClassLoader().getResource("Oko.png");
            herobodyImg = ImageIO.read(helicopterBodyImgUrl);

        } catch (IOException ex) {
            Logger.getLogger(PlayerEyeHero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Reset(int xCoordinate, int yCoordinate) {
        this.health = healthInit;

        this.numberOfMeats = numberOfMeatsInit;
        this.numberOfBream = numberOfBreamInit;

        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;

        this.breamGunXcoordinate = this.xCoordinate + this.offsetXBreamGun;
        this.breamGunYcoordinate = this.yCoordinate + this.offsetYBreamGun;

        this.movingXspeed = 0;
        this.movingYspeed = 0;
    }

    public boolean isShooting(long gameTime) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON1) &&
                ((gameTime - Bream.timeOfLastCreatedBream) >= Bream.timeBetweenNewBream) && this.numberOfBream > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFiredRocket(long gameTime) {
        if (Canvas.mouseButtonState(MouseEvent.BUTTON3) &&
                ((gameTime - Meat.timeOfLastCreatedRocket) >= Meat.timeBetweenNewRockets) && this.numberOfMeats > 0) {
            return true;
        } else {
            return false;
        }
    }

    public void isMoving() {
        if (Canvas.keyboardKeyState(KeyEvent.VK_D) || Canvas.keyboardKeyState(KeyEvent.VK_RIGHT)) {
            movingXspeed += acceleratingXspeed;
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_A) || Canvas.keyboardKeyState(KeyEvent.VK_LEFT)) {
            movingXspeed -= acceleratingXspeed;
        } else if (movingXspeed < 0) {
            movingXspeed += stoppingXspeed;
        } else if (movingXspeed > 0) {
            movingXspeed -= stoppingXspeed;
        }

        if (Canvas.keyboardKeyState(KeyEvent.VK_W) || Canvas.keyboardKeyState(KeyEvent.VK_UP)) {
            movingYspeed -= acceleratingYspeed;
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_S) || Canvas.keyboardKeyState(KeyEvent.VK_DOWN)) {
            movingYspeed += acceleratingYspeed;
        } else if (movingYspeed < 0) {
            movingYspeed += stoppingYspeed;
        } else if (movingYspeed > 0) {
            movingYspeed -= stoppingYspeed;
        }

    }

    public void Update() {
        xCoordinate += movingXspeed;
        yCoordinate += movingYspeed;

        this.meatHolderXcoordinate = this.xCoordinate + this.offsetXMeatHolder;
        this.meatHolderYcoordinate = this.yCoordinate + this.offsetYMeatHolder;

        this.breamGunXcoordinate = this.xCoordinate + this.offsetXBreamGun;
        this.breamGunYcoordinate = this.yCoordinate + this.offsetYBreamGun;
    }

    public void Draw(Graphics2D g2d) {
        g2d.drawImage(herobodyImg, xCoordinate, yCoordinate, null);
    }

}
