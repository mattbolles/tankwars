package game;

import gameobject.*;
import gameobject.powerup.SpeedBoost;
import gameobject.wall.BreakableWall;
import gameobject.wall.UnBreakableWall;
import gameobject.weapon.Bullet;
import resource.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Main tank game driver.
 */
public class TankGame extends JPanel  {


    private BufferedImage world;
    private Background backgroundTile;
    private Graphics2D buffer;
    private JFrame jFrame;
    private JFrame newJFrame;
    static int mouseClickX;
    static int mouseClickY;
    static int mouseLocationX;
    static int mouseLocationY;
    static boolean gamePaused = false;
    public static long tickCounter = 0;
    static GameState currentState;
    static StartScreen startScreen;
    static GameOverScreen gameOverScreen;
    static SoundPlayer continuousMusicPlayer = new SoundPlayer(1, "sound/music.wav", false);
    static CollisionDetection collisionDetector = new CollisionDetection();
    ArrayList<GameObject> gameObjects;
    Tank tankOne = new Tank(GameInfo.tankOneXSpawnCoord, GameInfo.tankOneYSpawnCoord, 0, 0, 0, Resource.getResourceImage("tankOne"),
            "tankOne", this);
    Tank tankTwo = new Tank(GameInfo.tankTwoXSpawnCoord, GameInfo.tankTwoYSpawnCoord,0, 0, 180,
            Resource.getResourceImage("tankTwo"), "tankTwo", this);



    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
        tankGame.init();
        startScreen = new StartScreen();
        startScreen.init();
        gameOverScreen = new GameOverScreen();
        gameOverScreen.init();
        TankGame.currentState = GameState.START;
        while (true) {
            // switching to if statements breaks this
            switch (currentState) {
                case START:
                    TankGame.continuousMusicPlayer.setStarted(false);
                    TankGame.continuousMusicPlayer.stop();
                    tankGame.setJFrameVisible(false);
                    gameOverScreen.setJFrameVisible(false);
                    startScreen.setJFrameVisible(true);
                    while (currentState == GameState.START) {
                        currentState = startScreen.getCurrentState();
                        startScreen.runStartScreen();
                    }
                    break;

                case RUNNING:
                    TankGame.continuousMusicPlayer.setStarted(true);
                    TankGame.continuousMusicPlayer.play();
                    startScreen.setJFrameVisible(false);
                    gameOverScreen.setJFrameVisible(false);
                    tankGame.jFrame.setVisible(true);
                    while (currentState == GameState.RUNNING) {
                        currentState = tankGame.getCurrentState();
                        tankGame.runGame(tankGame);
                    }
                    break;

                case GAME_OVER:
                    TankGame.continuousMusicPlayer.setStarted(false);
                    TankGame.continuousMusicPlayer.stop();
                    startScreen.setJFrameVisible(false);
                    tankGame.setJFrameVisible(false);
                    gameOverScreen.setJFrameVisible(true);
                    while (currentState == GameState.GAME_OVER) {
                        currentState = gameOverScreen.getCurrentState();
                        gameOverScreen.runGameOverScreen();
                    }
                    break;

                case RESET:
                    tankGame.resetTanks();
                    tankGame.resetJFrame();
                    tankGame.init();
                    tankGame.setCurrentState(GameState.RUNNING);
                    break;
            }
        }
    }


    private void init() {
        this.jFrame = new JFrame("Tank Wars");
        this.world = new BufferedImage(GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.gameObjects = new ArrayList<>();
        //listen for mouse click
        this.jFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                    mouseClickX = mouseEvent.getX();
                    mouseClickY = mouseEvent.getY();
            }
        });

        //listen for mouse movement
        this.jFrame.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent mouseEvent) {
                    mouseLocationX = mouseEvent.getX();
                    mouseLocationY = mouseEvent.getY();
            }
        });
        try {
            //background info
            int backgroundWidth = Resource.getResourceImage("background").getWidth();
            int backgroundHeight = Resource.getResourceImage("background").getHeight();
            backgroundTile = new Background(backgroundWidth, backgroundHeight, Resource.getResourceImage("background"));

            //for reading maps - make into seperate function
            InputStreamReader inputStreamReader = new InputStreamReader(TankGame.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader = new BufferedReader(inputStreamReader);

            String row = mapReader.readLine();
            if (row == null) {
                throw new IOException("Error! No data in map file :(");
            }
            // split by tab as delim
            String[] mapInfo = row.split("\t");
            int numOfCols = Integer.parseInt(mapInfo[0]);
            int numOfRows = Integer.parseInt(mapInfo[1]);
            // get data from map file
            // go row by row to get each data for each row
            for (int currentRow = 0; currentRow < numOfRows; currentRow++) {
                row = mapReader.readLine();
                mapInfo = row.split("\t");
                //go column by column for each row
                for (int currentCol = 0; currentCol < numOfCols; currentCol++) {
                    switch(mapInfo[currentCol]) {
                        case "4":
                            //speed boost
                            this.gameObjects.add(new SpeedBoost(currentCol*32, currentRow*32,
                                    Resource.getResourceImage("speedBoost"), 6000));
                            break;
                        case "2":
                            //breakable wall
                            //mult by 32 since wall tile is 32x32 pixels
                            this.gameObjects.add(new BreakableWall(currentCol*32, currentRow*32,
                                    Resource.getResourceImage("breakableWall")));
                            break;
                        case "3":
                        case "9":
                            //unbreakable wall
                            this.gameObjects.add(new UnBreakableWall(currentCol*32, currentRow*32,
                                    Resource.getResourceImage("unBreakableWall")));

                        default:
                            break;
                    }
                }
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        this.gameObjects.add(tankOne);
        tankOne.setTankGame(this);
        this.gameObjects.add(tankTwo);
        tankOne.setTankGame(this);
        // left side control, uses WASD to move and space to shoot
        TankControl tankOneControl = new TankControl(tankOne, KeyEvent.VK_W,
                KeyEvent.VK_S,
                KeyEvent.VK_A,
                KeyEvent.VK_D,
                KeyEvent.VK_SPACE);

        // right side control, uses arrows to move and enter/return to shoot
        TankControl tankTwoControl = new TankControl(tankTwo, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.addKeyListener(tankTwoControl);
        this.jFrame.setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.jFrame.setResizable(false);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(false);
        this.jFrame.setUndecorated(true);
        this.jFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }

    public void resetJFrame() {
        // create temp jframe to avoid JVM closing
        newJFrame = new JFrame("Loading...");
        this.jFrame.dispose();
        this.gameObjects.clear();
        init();
        this.newJFrame.dispose();
    }

    public void resetTanks() {
        tankOne.setHealth(100);
        tankOne.setLives(3);
        tankOne.setX(GameInfo.tankOneXSpawnCoord);
        tankOne.setY(GameInfo.tankOneYSpawnCoord);
        tankOne.setVx(0);
        tankOne.setVy(0);
        tankOne.setAngle(0);
        tankOne.setCurrentPowerUpTickCount(0);
        tankOne.setCurrentPowerUp("none");
        tankOne.hasPowerUp = false;
        tankOne.setSpeed(3);
        tankOne.unToggleShootPressed();
        tankOne.setCompletelyKilled(false);
        tankOne.setTankGame(this);
        tankTwo.setHealth(100);
        tankTwo.setLives(3);
        tankTwo.setX(GameInfo.tankTwoXSpawnCoord);
        tankTwo.setY(GameInfo.tankTwoYSpawnCoord);
        tankTwo.setVx(0);
        tankTwo.setVy(0);
        tankTwo.setAngle(180);
        tankTwo.setCurrentPowerUpTickCount(0);
        tankTwo.setCurrentPowerUp("none");
        tankTwo.hasPowerUp = false;
        tankTwo.setSpeed(3);
        tankTwo.unToggleShootPressed();
        tankTwo.setCompletelyKilled(false);
        tankOne.setTankGame(this);
    }

    public void setJFrameVisible(boolean visible) {
        this.jFrame.setVisible(visible);
    }


    public void runGame(TankGame tankGame) {
        if (!gamePaused) {
            try {
                tankGame.repaint();
                // this updating only has to do with objects being visible on map
                for (int currentGameObjectIndex = 0; currentGameObjectIndex < tankGame.gameObjects.size(); currentGameObjectIndex++) {
                    GameObject currentGameObject = tankGame.gameObjects.get(currentGameObjectIndex);
                    if (currentGameObject instanceof Bullet) {
                        // if bullet is exploded already
                        if (((Bullet) currentGameObject).isExploded()) {
                            tankGame.gameObjects.remove(currentGameObjectIndex);
                            currentGameObjectIndex--;
                        } else {
                            currentGameObject.update();
                        }
                    }

                    else if (currentGameObject instanceof BreakableWall) {
                        if (((BreakableWall) currentGameObject).getHealth() <= 0) {
                            tankGame.gameObjects.remove(currentGameObjectIndex);
                        }
                    }

                    else if (currentGameObject instanceof Tank) {
                        if (((Tank) currentGameObject).getHealth() <= 0) {
                            // if tank is not dead
                            if (!((Tank) currentGameObject).completelyKilled) {
                                ((Tank) currentGameObject).killTank();
                            } else {
                                // game over
                                if ("tankTwo".equals(((Tank) currentGameObject).getOwner())) {
                                    gameOverScreen.setGameOverWinner("PLAYER ONE");
                                } else {
                                    gameOverScreen.setGameOverWinner("PLAYER TWO");
                                }
                                setCurrentState(GameState.GAME_OVER);
                            }
                        }
                    }

                    else if (currentGameObject instanceof SpeedBoost) {
                        //if not visible and not active, remove
                        if (((SpeedBoost) currentGameObject).isDone()) {
                            tankGame.gameObjects.remove(currentGameObject);
                        }
                    }
                }

                tankGame.gameObjects = collisionDetector.processCollisions(tankGame.gameObjects);
                for (int i = 0; i < tankGame.gameObjects.size(); i++) {
                    tankGame.gameObjects.get(i).update();
                }

                tickCounter++;
                Thread.sleep(1000 / 144);
            } catch (InterruptedException ignored) {
                System.out.println(ignored);
            }
        }
    }


    public void addGameObject(GameObject objectToAdd) {
        this.gameObjects.add(objectToAdd);
    }

    public void setCurrentState(GameState state) {
        currentState = state;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    private void drawPlayerOneInfo(Graphics g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(40, 40, 110, 80);
        //draw power up if tank has one
        if (tankOne.hasPowerUp) {
            g2.fillRect(40,120, 110, 35);
            if ("speedBoost".equals(tankOne.currentPowerUp)) {
                g2.drawImage(Resource.getResourceImage("speedBoostIcon"), 45, 125, null);
                g2.setColor(Color.WHITE);
                g2.fillRect(75, 130, 60 - (tankOne.currentPowerUpTickCount / 33), 15);
                g2.setColor(Color.CYAN);
                g2.drawRect(39, 39, 112, 117);
            }
        }
        else if ("none".equals(tankOne.currentPowerUp)) {
            g2.setColor(Color.CYAN);
            g2.drawRect(39, 39, 112, 82);
        }
        g2.setFont(Resource.infoFontBold);
        g2.setColor(Color.CYAN);
        g2.drawString("Player One", 45, 60);
        g2.setColor(Color.WHITE);
        if (tankOne.getHealth() <= 60 && tankOne.getHealth() > 25) {
            g2.setColor(Color.YELLOW);
        } else if (tankOne.getHealth() <= 25) {
            g2.setColor(Color.RED);
        }
        //health bar
        g2.fillRect(45, 68, tankOne.getHealth(), 15);
        //draw lives
        g2.setColor(Color.WHITE);
        for (int i = 0; i < tankOne.getLives(); i++) {
            g2.drawImage(Resource.getResourceImage("tank1life"), 45 + (37 * i), 90, null);
        }
    }

    private void drawPlayerTwoInfo(Graphics g2) {
        g2.setColor(Color.BLACK);
        g2.fillRect(GameInfo.SCREEN_WIDTH - 150, GameInfo.SCREEN_HEIGHT - 130, 110, 80);
        //power up status
        if (tankTwo.hasPowerUp) {
            g2.fillRect(GameInfo.SCREEN_WIDTH - 150, GameInfo.SCREEN_HEIGHT - 165, 110, 35);
            if ("speedBoost".equals(tankTwo.currentPowerUp)) {
                g2.drawImage(Resource.getResourceImage("speedBoostIcon"), GameInfo.SCREEN_WIDTH - 145,
                        GameInfo.SCREEN_HEIGHT - 160, null);
                g2.setColor(Color.WHITE);
                g2.fillRect(GameInfo.SCREEN_WIDTH - 115, GameInfo.SCREEN_HEIGHT - 155,
                        60 - (tankTwo.currentPowerUpTickCount / 33), 15);
                g2.setColor(Color.PINK);
                g2.drawRect(GameInfo.SCREEN_WIDTH - 151, GameInfo.SCREEN_HEIGHT - 166, 112, 117);
            }
        }
        else if ("none".equals(tankTwo.currentPowerUp)) {
            g2.setColor(Color.PINK);
            g2.drawRect(GameInfo.SCREEN_WIDTH - 151, GameInfo.SCREEN_HEIGHT - 131, 112, 82);
        }
        g2.setFont(Resource.infoFontBold);
        g2.setColor(Color.PINK);
        g2.drawString("Player Two", GameInfo.SCREEN_WIDTH - 145, GameInfo.SCREEN_HEIGHT - 110);
        g2.setColor(Color.WHITE);
        if (tankTwo.getHealth() <= 60 && tankTwo.getHealth() > 25) {
            g2.setColor(Color.YELLOW);
        } else if (tankTwo.getHealth() <= 25) {
            g2.setColor(Color.RED);
        }
        g2.fillRect(GameInfo.SCREEN_WIDTH - 145, GameInfo.SCREEN_HEIGHT - 102, tankTwo.getHealth(), 15);
        g2.setColor(Color.WHITE);
        for (int i = 0; i < tankTwo.getLives(); i++) {
            g2.drawImage(Resource.getResourceImage("tank2life"), GameInfo.SCREEN_WIDTH - 145 + (37 * i),
                    GameInfo.SCREEN_HEIGHT - 80, null);
        }
    }
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();

        if (currentState == GameState.RUNNING) {
            buffer.setColor(Color.BLACK);
            //following line avoids image trailing
            buffer.fillRect(0, 0, GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT);
            // draw background tiles
            this.backgroundTile.drawImage(buffer);
            //draw all game objects
            for (int i = 0; i < gameObjects.size(); i++) {
                gameObjects.get(i).drawImage(buffer);
            }
            //split screen
            BufferedImage leftScreenHalf = world.getSubimage(tankOne.getCameraX(), tankOne.getCameraY(),
                    GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
            BufferedImage rightScreenHalf = world.getSubimage(tankTwo.getCameraX(), tankTwo.getCameraY(),
                    GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
            g2.drawImage(leftScreenHalf, 0, 0, null);
            g2.drawImage(rightScreenHalf, GameInfo.SCREEN_WIDTH / 2 + 4, 0, null);
            //mini map

            //player info - lives etc
            drawPlayerOneInfo(g2);
            drawPlayerTwoInfo(g2);

            //draw minimap
            // put last so scale doesn't affect rest
            BufferedImage miniMap = world.getSubimage(0, 0, GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT);
            g2.scale(.09, .09);
            g2.drawImage(miniMap, 6100, 6400, null);
            g2.setColor(Color.WHITE);
            g2.drawRect(6090, 6390, miniMap.getWidth() + 10, miniMap.getHeight() + 10);
        }
    }
}