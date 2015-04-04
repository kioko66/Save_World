package Save_World.company;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class Game {
    private Random random;

    private Robot robot;

    private PlayerEyeHero player;

    private ArrayList<EnemySqoore> enemySqooreArrayList = new ArrayList<EnemySqoore>();

    private ArrayList<Animation> explosionsList;
    private BufferedImage explosionAnimImg;

    private ArrayList<Bream> breamArrayList;

    private ArrayList<Meat> meatArrayList;
    private ArrayList<MeatSmoke> meatSmokeArrayList;

    private BufferedImage skyColorImg;

    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;
    private BufferedImage groundImg;

    private MovingBackground cloudLayer1Moving;
    private MovingBackground cloudLayer2Moving;

    private MovingBackground groundMoving;

    private Font font;

    private int runAwayEnemies;
    private int destroyedEnemies;


    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;

        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
                Initialize();
                LoadContent();
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }

    private void Initialize() {
        random = new Random();

        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        player = new PlayerEyeHero(Framework.frameWidth / 4, Framework.frameHeight / 4);

        enemySqooreArrayList = new ArrayList<EnemySqoore>();

        explosionsList = new ArrayList<Animation>();

        breamArrayList = new ArrayList<Bream>();

        meatArrayList = new ArrayList<Meat>();
        meatSmokeArrayList = new ArrayList<MeatSmoke>();

        cloudLayer1Moving = new MovingBackground();
        cloudLayer2Moving = new MovingBackground();
        groundMoving = new MovingBackground();

        font = new Font("monospaced", Font.BOLD, 18);

        runAwayEnemies = 0;
        destroyedEnemies = 0;
    }


    private void LoadContent() {
        try {
            URL skyColorImgUrl = this.getClass().getClassLoader().getResource("sky_color.jpg");
            skyColorImg = ImageIO.read(skyColorImgUrl);
            URL cloudLayer1ImgUrl = this.getClass().getClassLoader().getResource("cloud_layer_1.png");
            cloudLayer1Img = ImageIO.read(cloudLayer1ImgUrl);
            URL cloudLayer2ImgUrl = this.getClass().getClassLoader().getResource("cloud_layer_2.png");
            cloudLayer2Img = ImageIO.read(cloudLayer2ImgUrl);

            URL groundImgUrl = this.getClass().getClassLoader().getResource("ground.png");
            groundImg = ImageIO.read(groundImgUrl);

            URL sqooreImgUrl = this.getClass().getClassLoader().getResource("belka.png");
            EnemySqoore.sqooreBodyImg = ImageIO.read(sqooreImgUrl);

            URL meatImgUrl = this.getClass().getClassLoader().getResource("BoolMeet.png");
            Meat.meatImg = ImageIO.read(meatImgUrl);
            URL meatSmokeImgUrl = this.getClass().getClassLoader().getResource("meat_smoke.png");
            MeatSmoke.smokeImg = ImageIO.read(meatSmokeImgUrl);

            URL explosionAnimImgUrl = this.getClass().getClassLoader().getResource("explosion_anim.png");
            explosionAnimImg = ImageIO.read(explosionAnimImgUrl);

            URL breamImgUrl = this.getClass().getClassLoader().getResource("Bream.png");
            Bream.breamImg = ImageIO.read(breamImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }

        cloudLayer1Moving.Initialize(cloudLayer1Img, -6, 0);
        cloudLayer2Moving.Initialize(cloudLayer2Img, -2, 0);
        groundMoving.Initialize(groundImg, -1.2, Framework.frameHeight - groundImg.getHeight());
    }

    public void RestartGame() {
        player.Reset(Framework.frameWidth / 4, Framework.frameHeight / 4);

        EnemySqoore.restartEnemy();

        Bream.timeOfLastCreatedBream = 0;
        Meat.timeOfLastCreatedRocket = 0;

        enemySqooreArrayList.clear();
        breamArrayList.clear();
        meatArrayList.clear();
        meatSmokeArrayList.clear();
        explosionsList.clear();

        runAwayEnemies = 0;
        destroyedEnemies = 0;
    }

    public void UpdateGame(long gameTime, Point mousePosition) {

        if (!isPlayerAlive() && explosionsList.isEmpty()) {
            Framework.gameState = Framework.GameState.GAMEOVER;
            return;
        }

        if (player.numberOfBream <= 0 && player.numberOfMeats <= 0 &&
                breamArrayList.isEmpty() && meatArrayList.isEmpty() && explosionsList.isEmpty()) {
            Framework.gameState = Framework.GameState.GAMEOVER;
            return;
        }

        if (isPlayerAlive()) {
            isPlayerShooting(gameTime, mousePosition);
            didPlayerFiredMeat(gameTime);
            player.isMoving();
            player.Update();
        }

        limitMousePosition(mousePosition);

        updateBream();

        updateMeat(gameTime);
        updateMeatSmoke(gameTime);

        createEnemySqoore(gameTime);
        updateEnemies();

        updateExplosions();
    }

    public void Draw(Graphics2D g2d, Point mousePosition, long gameTime) {
        g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);

        groundMoving.Draw(g2d);
        cloudLayer2Moving.Draw(g2d);

        if (isPlayerAlive()) {
            player.Draw(g2d);
        }

        for (int i = 0; i < enemySqooreArrayList.size(); i++) {
            enemySqooreArrayList.get(i).Draw(g2d);
        }

        for (int i = 0; i < breamArrayList.size(); i++) {
            breamArrayList.get(i).Draw(g2d);
        }

        for (int i = 0; i < meatArrayList.size(); i++) {
            meatArrayList.get(i).Draw(g2d);
        }

        for (int i = 0; i < meatSmokeArrayList.size(); i++) {
            meatSmokeArrayList.get(i).Draw(g2d);
        }

        for (int i = 0; i < explosionsList.size(); i++) {
            explosionsList.get(i).Draw(g2d);
        }

        g2d.setFont(font);
        g2d.setColor(Color.darkGray);

        g2d.drawString(formatTime(gameTime), Framework.frameWidth / 2 - 45, 21);
        g2d.drawString("DESTROYED: " + destroyedEnemies, 10, 21);
        g2d.drawString("RUNAWAY: " + runAwayEnemies, 10, 41);
        g2d.drawString("Meat: " + player.numberOfMeats, 10, 81);
        g2d.drawString("Bream: " + player.numberOfBream, 10, 101);

        cloudLayer1Moving.Draw(g2d);
    }

    public void DrawStatistic(Graphics2D g2d, long gameTime) {
        g2d.drawString("Time: " + formatTime(gameTime), Framework.frameWidth / 2 - 50, Framework.frameHeight / 3 + 80);
        g2d.drawString("Meats left: " + player.numberOfMeats, Framework.frameWidth / 2 - 55, Framework.frameHeight / 3 + 105);
        g2d.drawString("Bream left: " + player.numberOfBream, Framework.frameWidth / 2 - 55, Framework.frameHeight / 3 + 125);
        g2d.drawString("Destroyed enemies: " + destroyedEnemies, Framework.frameWidth / 2 - 65, Framework.frameHeight / 3 + 150);
        g2d.drawString("Runaway enemies: " + runAwayEnemies, Framework.frameWidth / 2 - 65, Framework.frameHeight / 3 + 170);
        g2d.setFont(font);
        g2d.drawString("Statistics: ", Framework.frameWidth / 2 - 75, Framework.frameHeight / 3 + 60);
    }

    private static String formatTime(long time) {
        int sec = (int) (time / Framework.milisecInNanosec / 1000);

        int min = sec / 60;
        sec = sec - (min * 60);

        String minString, secString;

        if (min <= 9) {
            minString = "0" + Integer.toString(min);
        } else {
            minString = "" + Integer.toString(min);
        }

        if (sec <= 9) {
            secString = "0" + Integer.toString(sec);
        } else {
            secString = "" + Integer.toString(sec);
        }

        return minString + ":" + secString;
    }

    private boolean isPlayerAlive() {
        if (player.health <= 0) {
            return false;
        }
        return true;
    }

    private void isPlayerShooting(long gameTime, Point mousePosition) {
        if (player.isShooting(gameTime)) {
            Bream.timeOfLastCreatedBream = gameTime;
            player.numberOfBream--;

            Bream b = new Bream(player.breamGunXcoordinate, player.breamGunYcoordinate, mousePosition);
            breamArrayList.add(b);
        }
    }

    private void didPlayerFiredMeat(long gameTime) {
        if (player.isFiredRocket(gameTime)) {
            Meat.timeOfLastCreatedRocket = gameTime;
            player.numberOfMeats--;

            Meat m = new Meat();
            m.Initialize(player.meatHolderXcoordinate, player.meatHolderYcoordinate);
            meatArrayList.add(m);
        }
    }

    private void createEnemySqoore(long gameTime) {
        if (gameTime - EnemySqoore.timeOfLastCreatedEnemy >= EnemySqoore.timeBetweenNewEnemies) {
            EnemySqoore es = new EnemySqoore();
            int xCoordinate = Framework.frameWidth;
            int yCoordinate = random.nextInt(Framework.frameHeight - EnemySqoore.sqooreBodyImg.getHeight());
            es.Initialize(xCoordinate, yCoordinate);
            enemySqooreArrayList.add(es);

            EnemySqoore.speedUp();

            EnemySqoore.timeOfLastCreatedEnemy = gameTime;
        }
    }

    private void updateEnemies() {
        for (int i = 0; i < enemySqooreArrayList.size(); i++) {
            EnemySqoore es = enemySqooreArrayList.get(i);

            es.Update();

            Rectangle playerRectangel = new Rectangle(player.xCoordinate, player.yCoordinate, player.herobodyImg.getWidth(), player.herobodyImg.getHeight());
            Rectangle enemyRectangel = new Rectangle(es.xCoordinate, es.yCoordinate, EnemySqoore.sqooreBodyImg.getWidth(), EnemySqoore.sqooreBodyImg.getHeight());
            if (playerRectangel.intersects(enemyRectangel)) {
                player.health = 0;

                enemySqooreArrayList.remove(i);

                for (int exNum = 0; exNum < 3; exNum++) {
                    Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, player.xCoordinate + exNum * 60, player.yCoordinate - random.nextInt(100), exNum * 200 + random.nextInt(100));
                    explosionsList.add(expAnim);
                }
                for (int exNum = 0; exNum < 3; exNum++) {
                    Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, es.xCoordinate + exNum * 60, es.yCoordinate - random.nextInt(100), exNum * 200 + random.nextInt(100));
                    explosionsList.add(expAnim);
                }
                break;
            }

            if (es.health <= 0) {
                Animation expAnim = new Animation(explosionAnimImg, 134, 134, 12, 45, false, es.xCoordinate, es.yCoordinate - explosionAnimImg.getHeight() / 3, 0); // Substring 1/3 explosion image height (explosionAnimImg.getHeight()/3) so that explosion is drawn more at the center of the helicopter.
                explosionsList.add(expAnim);
                destroyedEnemies++;
                enemySqooreArrayList.remove(i);
                continue;
            }
            if (es.isLeftScreen()) {
                enemySqooreArrayList.remove(i);
                runAwayEnemies++;
            }
        }
    }

    private void updateBream() {
        for (int i = 0; i < breamArrayList.size(); i++) {
            Bream bream = breamArrayList.get(i);

            bream.Update();

            if (bream.isItLeftScreen()) {
                breamArrayList.remove(i);
                continue;
            }

            Rectangle bulletRectangle = new Rectangle((int) bream.xCoordinate, (int) bream.yCoordinate, Bream.breamImg.getWidth(), Bream.breamImg.getHeight());
            for (int j = 0; j < enemySqooreArrayList.size(); j++) {
                EnemySqoore es = enemySqooreArrayList.get(j);

                Rectangle enemyRectangel = new Rectangle(es.xCoordinate, es.yCoordinate, EnemySqoore.sqooreBodyImg.getWidth(), EnemySqoore.sqooreBodyImg.getHeight());

                if (bulletRectangle.intersects(enemyRectangel)) {
                    es.health -= Bream.damagePower;
                    breamArrayList.remove(i);
                    break;
                }
            }
        }
    }

    private void updateMeat(long gameTime) {
        for (int i = 0; i < meatArrayList.size(); i++) {
            Meat meat = meatArrayList.get(i);

            meat.Update();

            if (meat.isItLeftScreen()) {
                meatArrayList.remove(i);
                continue;
            }

            MeatSmoke ms = new MeatSmoke();

            int xCoordinate = meat.xCoordinate - MeatSmoke.smokeImg.getWidth();
            int yCoordinte = meat.yCoordinate - 5 + random.nextInt(6);
            ms.Initialize(xCoordinate, yCoordinte, gameTime, meat.currentSmokeLifeTime);
            meatSmokeArrayList.add(ms);

            int smokePositionX = 5 + random.nextInt(8);
            ms = new MeatSmoke();
            xCoordinate = meat.xCoordinate - MeatSmoke.smokeImg.getWidth() + smokePositionX;
            yCoordinte = meat.yCoordinate - 5 + random.nextInt(6);
            ms.Initialize(xCoordinate, yCoordinte, gameTime, meat.currentSmokeLifeTime);
            meatSmokeArrayList.add(ms);
            meat.currentSmokeLifeTime *= 1.02;

            if (checkIfMeatHitEnemy(meat)) {
                meatArrayList.remove(i);
            }
        }
    }

    private boolean checkIfMeatHitEnemy(Meat meat) {
        boolean didItHitEnemy = false;
        Rectangle rocketRectangle = new Rectangle(meat.xCoordinate, meat.yCoordinate, 2, Meat.meatImg.getHeight());

        for (int j = 0; j < enemySqooreArrayList.size(); j++) {
            EnemySqoore es = enemySqooreArrayList.get(j);
            Rectangle enemyRectangel = new Rectangle(es.xCoordinate, es.yCoordinate, EnemySqoore.sqooreBodyImg.getWidth(), EnemySqoore.sqooreBodyImg.getHeight());

            if (rocketRectangle.intersects(enemyRectangel)) {
                didItHitEnemy = true;
                es.health -= Meat.damagePower;
                break;
            }
        }
        return didItHitEnemy;
    }

    private void updateMeatSmoke(long gameTime) {
        for (int i = 0; i < meatSmokeArrayList.size(); i++) {
            MeatSmoke rs = meatSmokeArrayList.get(i);

            if (rs.didSmokeDisapper(gameTime)) {
                meatSmokeArrayList.remove(i);
            }
            rs.updateTransparency(gameTime);
        }
    }

    private void updateExplosions() {
        for (int i = 0; i < explosionsList.size(); i++) {
            if (!explosionsList.get(i).active) {
                explosionsList.remove(i);
            }
        }
    }

    private void limitMousePosition(Point mousePosition) {
        int maxYcoordinateDistanceFromPlayer_top = 120;
        int maxYcoordinateDistanceFromPlayer_bottom = 120;

        int mouseXcoordinate = player.breamGunXcoordinate + 250;

        int mouseYcoordinate = mousePosition.y;
        if (mousePosition.y < player.breamGunYcoordinate) {
            if (mousePosition.y < player.breamGunYcoordinate - maxYcoordinateDistanceFromPlayer_top) {
                mouseYcoordinate = player.breamGunYcoordinate - maxYcoordinateDistanceFromPlayer_top;
            }
        } else {
            if (mousePosition.y > player.breamGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom) {
                mouseYcoordinate = player.breamGunYcoordinate + maxYcoordinateDistanceFromPlayer_bottom;
            }
        }

        mouseYcoordinate += player.movingYspeed;

        robot.mouseMove(mouseXcoordinate, mouseYcoordinate);
    }
}
