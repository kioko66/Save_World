package Save_World.company;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;


public class Framework extends Canvas {

    public static int frameWidth;
    public static int frameHeight;

    public static final long secInNanosec = 1000000000L;
    public static final long milisecInNanosec = 1000000L;

    private final int GAME_FPS = 60;
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;

    public static enum GameState {STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}

    public static GameState gameState;

    private long gameTime;
    private long lastTime;

    private Game game;
    private Font font;

    private BufferedImage gameTitleImg;
    private BufferedImage menuBorderImg;
    private BufferedImage skyColorImg;
    private BufferedImage cloudLayer1Img;
    private BufferedImage cloudLayer2Img;


    public Framework() {
        super();

        gameState = GameState.VISUALIZING;

        Thread gameThread = new Thread() {
            @Override
            public void run() {
                GameLoop();
            }
        };
        gameThread.start();
    }

    private void Initialize() {
        font = new Font("monospaced", Font.BOLD, 28);
    }

    private void LoadContent() {
        try {
            URL menuBorderImgUrl = this.getClass().getClassLoader().getResource("menu_border.png");
            menuBorderImg = ImageIO.read(menuBorderImgUrl);

            URL skyColorImgUrl = this.getClass().getClassLoader().getResource("sky_color.jpg");
            skyColorImg = ImageIO.read(skyColorImgUrl);

            URL gameTitleImgUrl = this.getClass().getClassLoader().getResource("save_world_title.png");
            gameTitleImg = ImageIO.read(gameTitleImgUrl);

            URL cloudLayer1ImgUrl = this.getClass().getClassLoader().getResource("cloud_layer_1.png");
            cloudLayer1Img = ImageIO.read(cloudLayer1ImgUrl);
            URL cloudLayer2ImgUrl = this.getClass().getClassLoader().getResource("cloud_layer_2.png");
            cloudLayer2Img = ImageIO.read(cloudLayer2ImgUrl);
        } catch (IOException ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void GameLoop() {
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        long beginTime, timeTaken, timeLeft;

        while (true) {
            beginTime = System.nanoTime();

            switch (gameState) {
                case PLAYING:
                    gameTime += System.nanoTime() - lastTime;
                    game.UpdateGame(gameTime, mousePosition());
                    lastTime = System.nanoTime();
                    break;
                case GAMEOVER:
                    break;
                case MAIN_MENU:
                    break;
                case OPTIONS:
                    break;
                case GAME_CONTENT_LOADING:
                    break;
                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                    break;
                case VISUALIZING:
                    if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        gameState = GameState.STARTING;
                    } else {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                    break;
            }

            repaint();

            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;
            if (timeLeft < 10) {
                timeLeft = 10;
            }
            try {
                Thread.sleep(timeLeft);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void Draw(Graphics2D g2d) {
        switch (gameState) {
            case PLAYING:
                game.Draw(g2d, mousePosition(), gameTime);
                break;
            case GAMEOVER:
                drawMenuBackground(g2d);
                g2d.setColor(Color.black);
                g2d.drawString("Press ENTER to restart or ESC to exit.", frameWidth / 2 - 113, frameHeight / 4 + 30);
                game.DrawStatistic(g2d, gameTime);
                g2d.setFont(font);
                g2d.drawString("GAME OVER", frameWidth / 2 - 90, frameHeight / 4);
                break;
            case MAIN_MENU:
                drawMenuBackground(g2d);
                g2d.drawImage(gameTitleImg, frameWidth / 2 - gameTitleImg.getWidth() / 2, frameHeight / 4, null);
                g2d.setColor(Color.black);
                g2d.drawString("Press any key to start the game or ESC to exit.", frameWidth / 2 - 117, frameHeight / 2 - 30);
                break;
            case OPTIONS:
                break;
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.white);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
                break;
        }
    }

    private void newGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game = new Game();
    }

    private void restartGame() {
        gameTime = 0;
        lastTime = System.nanoTime();
        game.RestartGame();
        gameState = GameState.PLAYING;
    }

    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();

            if (mp != null) {
                return this.getMousePosition();
            } else {
                return new Point(0, 0);
            }
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }

    @Override
    public void keyReleasedFramework(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        }

        switch (gameState) {
            case GAMEOVER:
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    restartGame();
                }
                break;
            case MAIN_MENU:
                newGame();
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    private void drawMenuBackground(Graphics2D g2d) {
        g2d.drawImage(skyColorImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(cloudLayer1Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(cloudLayer2Img, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.drawImage(menuBorderImg, 0, 0, Framework.frameWidth, Framework.frameHeight, null);
        g2d.setColor(Color.white);
    }
}
