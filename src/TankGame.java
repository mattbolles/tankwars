/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;


import static javax.imageio.ImageIO.read;

/**
 * Main driver class of Tank Example.
 * Class is responsible for loading resources and
 * initializing game objects. Once completed, control will
 * be given to infinite loop which will act as our game loop.
 * A very simple game loop.
 * @author anthony-pc
 */
public class TankGame extends JPanel  {


    private BufferedImage world;
    private Background backgroundTile;
    private Graphics2D buffer;
    private JFrame jFrame;
    int tankOneLives = 3;
    int tankTwoLives = 3;
    String tankOneCurrentWeapon = "Bullet";
    String tankTwoCurrentWeapon  = "Bullet";
    String tankOneCurrentPowerUp = "None";
    String TankTwoCurrentPowerUp = "None";
    static long tickCounter = 0;
    SoundPlayer soundPlayer;
    ArrayList<GameObject> gameObjects;
    Tank tankOne = new Tank(320, 320, 0, 0, 0, Resource.getResourceImage("tankOne"), "tankOne", this);
    Tank tankTwo = new Tank(1696, 1696, 0, 0, 180, Resource.getResourceImage("tankTwo"), "tankTwo", this);



    static CollisionDetection collisionDetector = new CollisionDetection();



    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
        tankGame.init();
        try {

            while (true) {
                Rectangle collision;
                /*ArrayList<Bullet> tankOneBulletList = tankOne.getBulletList();
                ArrayList<Bullet> tankTwoBulletList = tankTwo.getBulletList();*/
                //tankGame.tankOne.update();
                //tankGame.tankTwo.update();

                //tankGame.gameObjects.forEach(gameObject -> gameObject.update());

                for (int currentGameObjectIndex = 0; currentGameObjectIndex < tankGame.gameObjects.size(); currentGameObjectIndex++) {
                    tankGame.repaint();
                    //System.out.println(tankGame.gameObjects.size());
                    //System.out.println(currentGameObjectIndex);
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

                    if (currentGameObject instanceof BreakableWall) {
                        if (((BreakableWall) currentGameObject).getHealth() <= 0) {
                            tankGame.gameObjects.remove(currentGameObjectIndex);
                        }
                    }
                }
                tankGame.gameObjects = collisionDetector.processCollisions(tankGame.gameObjects);

                for (int i = 0; i < tankGame.gameObjects.size(); i++) {
                tankGame.gameObjects.get(i).update();
                }
                tickCounter++;
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }


    private void init() {
        this.jFrame = new JFrame("Tank Wars");
        this.world = new BufferedImage(GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.gameObjects = new ArrayList<>();
        try {
            //background info - generify later:
            int backgroundWidth = Resource.getResourceImage("background").getWidth();
            int backgroundHeight = Resource.getResourceImage("background").getHeight();
            backgroundTile = new Background(backgroundWidth, backgroundHeight, Resource.getResourceImage("background"));

            //for reading maps:
            InputStreamReader inputStreamReader = new InputStreamReader(TankGame.class.getClassLoader().getResourceAsStream("maps/map1"));
            BufferedReader mapReader = new BufferedReader(inputStreamReader);

            String row = mapReader.readLine();
            if (row == null) {
                throw new IOException("Error! No data in map file :(");
            }
            // split by tab as delim
            String mapInfo[] = row.split("\t");
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
        this.jFrame.setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT + 20);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
        // background music
        soundPlayer = new SoundPlayer(1,"crystalraindrops.wav");


    }

    public void addGameObject(GameObject objectToAdd) {
        this.gameObjects.add(objectToAdd);
    }



    // following is to make the split screen follow each player

    int offsetMaxX = GameInfo.WORLD_WIDTH - (GameInfo.SCREEN_WIDTH / 2);
    int offsetMaxY = GameInfo.WORLD_HEIGHT - GameInfo.SCREEN_HEIGHT;
    int offsetMinX = 0;
    int offsetMinY = 0;
    

    public int getTankHealth(String tank) {
        int tankHealth;
        /*Tank tankOne = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        Tank tankTwo = (Tank) this.gameObjects.get(this.gameObjects.size() - 1);*/
        switch (tank) {
            case "tankOne":

                tankHealth =  tankOne.getHealth();
            case "tankTwo":
                tankHealth = tankTwo.getHealth();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tank);
        }
        return tankHealth;
    }


    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        //following line avoids image trailing
        buffer.fillRect(0,0, GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT);
        // draw background tiles
        this.backgroundTile.drawImage(buffer);
        //draw all game objects
        //this.gameObjects.forEach(gameObject -> gameObject.drawImage(buffer));
        for (int i = 0; i < this.gameObjects.size(); i++) {
            this.gameObjects.get(i).drawImage(buffer);
        }
        BufferedImage leftScreenHalf = world.getSubimage(tankOne.getCameraX(), tankOne.getCameraY(),
                GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
        BufferedImage rightScreenHalf = world.getSubimage(tankTwo.getCameraX(), tankTwo.getCameraY(),
                GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
        BufferedImage miniMap = world.getSubimage(0,0, GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT);
        /*// draw black split screen border
        buffer.setColor(Color.BLACK);
        buffer.fillRect(TankGame.SCREEN_WIDTH / 2, 0, 4, TankGame.SCREEN_HEIGHT);*/
        g2.drawImage(leftScreenHalf,0,0,null);
        g2.drawImage(rightScreenHalf,GameInfo.SCREEN_WIDTH / 2 + 4,0,null);
        //draw minimap

        //draw lives etc - put in seperate function later
        //p1
        Font infoFontBold = new Font("Helvetica", Font.BOLD, 19);
        Font infoFont = new Font("Helvetica", Font.PLAIN, 18);
        g2.fillRect(40,40,110,80);
        g2.setColor(Color.CYAN);
        g2.drawRect(39, 39, 112, 82);

        g2.setFont(infoFontBold);
        // change to accurate color when you make this a seperate function
        g2.setColor(Color.CYAN);
        g2.drawString("Player One", 45, 60);
        g2.setColor(Color.WHITE);
        if (tankOne.getHealth() <= 60 && tankOne.getHealth() > 25) {
            g2.setColor(Color.YELLOW);
        }
        else if (tankOne.getHealth() <= 25) {
            g2.setColor(Color.RED);
        }
        g2.fillRect(45, 68, tankOne.getHealth(), 15);
        //g2.drawString(playerOneHealth, 1075, 820);
        g2.setColor(Color.WHITE);
        //g2.drawString(playerOneLives, 450, 1020);
        for (int i = 0; i < tankOneLives; i++) {
                g2.drawImage(Resource.getResourceImage("tank1life"), 45 + (37 * i), 90, null);
        }

        //p2
        g2.setColor(Color.BLACK);
        g2.fillRect(GameInfo.SCREEN_WIDTH - 150, GameInfo.SCREEN_HEIGHT - 130,110,80);
        g2.setColor(Color.WHITE);
        g2.setFont(infoFontBold);
        g2.setColor(Color.PINK);
        g2.drawRect(GameInfo.SCREEN_WIDTH - 151, GameInfo.SCREEN_HEIGHT - 131, 112, 82);
        g2.drawString("Player Two", GameInfo.SCREEN_WIDTH - 145, GameInfo.SCREEN_HEIGHT - 110);
        g2.setColor(Color.WHITE);
        if (tankTwo.getHealth() <= 60 && tankTwo.getHealth() > 25) {
            g2.setColor(Color.YELLOW);
        }
        else if (tankTwo.getHealth() <= 25) {
            g2.setColor(Color.RED);
        }
        g2.fillRect(GameInfo.SCREEN_WIDTH - 145, GameInfo.SCREEN_HEIGHT - 102, tankTwo.getHealth(), 15);
        //g2.drawString(playerOneHealth, 1075, 820);
        g2.setColor(Color.WHITE);
        //g2.drawString(playerOneLives, 450, 1020);
        for (int i = 0; i < tankTwoLives; i++) {
            g2.drawImage(Resource.getResourceImage("tank2life"), GameInfo.SCREEN_WIDTH - 145 + (37 * i),
                    GameInfo.SCREEN_HEIGHT - 80, null);
        }


        // put last so scale doesn't affect rest
        g2.scale(.09,.09);
        g2.drawImage(miniMap, 6100, 6400, null);
        g2.setColor(Color.WHITE);
        g2.drawRect(6090, 6390, miniMap.getWidth() + 10, miniMap.getHeight() + 10);

        //fix this
        /*if ("Bullet".equalsIgnoreCase(tankOneCurrentWeapon)) {
            g2.drawImage(TankGame.bulletImage, 450, 1220, null);
        }*/
        //p2






    }
}
