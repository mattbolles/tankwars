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
    int tankOneLives = 2;
    int tankTwoLives = 2;
    int tankTwoHealth = 25;
    String tankOneCurrentWeapon = "Bullet";
    String tankTwoCurrentWeapon  = "Bullet";
    String tankOneCurrentPowerUp = "None";
    String TankTwoCurrentPowerUp = "None";
    static long tickCounter = 0;
    SoundPlayer soundPlayer;
    ArrayList<GameObject> gameObjects;

    static CollisionDetection collisionDetector = new CollisionDetection();



    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
        tankGame.init();
        try {

            while (true) {
                Rectangle collision;
                Tank tankOne = (Tank) tankGame.gameObjects.get(tankGame.gameObjects.size() - 2);
                Tank tankTwo = (Tank) tankGame.gameObjects.get(tankGame.gameObjects.size() - 1);
                ArrayList<Bullet> tankOneBulletList = tankOne.getBulletList();
                ArrayList<Bullet> tankTwoBulletList = tankTwo.getBulletList();
                tankGame.gameObjects.forEach(gameObject -> gameObject.update());
                for (GameObject currentGameObject : tankGame.gameObjects) {
                    //System.out.println("Current object: " + object.getObjectType());
                    Rectangle t1 = tankOne.getHitBox();
                    Rectangle t2 = tankTwo.getHitBox();
                    Rectangle objectRect = currentGameObject.getHitBox();

                    if (!currentGameObject.getObjectType().equalsIgnoreCase("tank")) {
                        if (objectRect.intersects(t1)) {
                            /*System.out.println("t1 intersects " + currentGameObject.getObjectType() + " located at " + currentGameObject.getX() +
                                            ", " + currentGameObject.getY() + ". Tank is at " + t1.getX() + ", " + t1.getY());*/
                            tankOne.collide(currentGameObject);

                        }

                        if (objectRect.intersects(t2)) {
                            tankTwo.collide(currentGameObject);
                        }


                        //check all other collisions
                        /*for (GameObject anotherGameObject : tankGame.gameObjects) {
                            if (!currentGameObject.equals(anotherGameObject)) {
                                if (collisionDetector.isCollisionDetected(currentGameObject, anotherGameObject)) {
                                    currentGameObject.collide(anotherGameObject);
                                }
                            }
                        }*/
                    }
                    else {
                        for (Bullet bullet : tankOneBulletList) {
                            if (collisionDetector.isCollisionDetected(bullet, currentGameObject)) {
                                if (!currentGameObject.equals(tankOne)) {
                                    bullet.collide(currentGameObject);
                                }
                            }
                        }
                    }


                }
                tankGame.repaint();
                tickCounter++;
              //  System.out.println(tankGame.t1);
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

        Tank tankOne = new Tank(320, 320, 0, 0, 0, Resource.getResourceImage("tankOne"));
        Tank tankTwo = new Tank(1696, 1696, 0, 0, 180, Resource.getResourceImage("tankTwo"));


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

        this.gameObjects.add(tankOne);
        this.gameObjects.add(tankTwo);

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


    public GameObject getGameObjectFromList(int objectToGet) {
        GameObject returnedObject = this.gameObjects.get(this.gameObjects.size() - objectToGet);
        return returnedObject;
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
        Tank tankOne = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        Tank tankTwo = (Tank) this.gameObjects.get(this.gameObjects.size() - 1);
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
    public int getCameraTankOneX() {
        // get 1st tank
        Tank tankOne = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        // divide by 4 as each player gets half the screen width originally
        int cameraX = tankOne.getX() - (GameInfo.SCREEN_WIDTH / 4);
        if (cameraX > offsetMaxX) {
            cameraX = offsetMaxX;
        }

        else if (cameraX < offsetMinX) {
            cameraX = offsetMinX;
        }
        return cameraX;
    }

    public int getCameraTankOneY() {
        Tank tankOne = (Tank) this.gameObjects.get(this.gameObjects.size() - 2);
        int cameraY = tankOne.getY() - (GameInfo.SCREEN_HEIGHT / 2);
        if (cameraY > offsetMaxY) {
            cameraY = offsetMaxY;
        }

        else if (cameraY < offsetMinY) {
            cameraY = offsetMinY;
        }
        return cameraY;
    }

    public int getCameraTankTwoX() {
        Tank tankTwo = (Tank) this.gameObjects.get(this.gameObjects.size() - 1);
        // divide by 4 as each player gets half the screen width originally
        int cameraX = tankTwo.getX() - (GameInfo.SCREEN_WIDTH / 4);
        if (cameraX > offsetMaxX) {
            cameraX = offsetMaxX;
        }

        else if (cameraX < offsetMinX) {
            cameraX = offsetMinX;
        }
        return cameraX;
    }



    public int getCameraTankTwoY() {
        Tank tankTwo = (Tank) this.gameObjects.get(this.gameObjects.size() - 1); // gets added 2nd
        int cameraY = tankTwo.getY() - (GameInfo.SCREEN_HEIGHT / 2);
        if (cameraY > offsetMaxY) {
            cameraY = offsetMaxY;
        }

        else if (cameraY < offsetMinY) {
            cameraY = offsetMinY;
        }
        return cameraY;
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
        this.gameObjects.forEach(gameObject -> gameObject.drawImage(buffer));
        BufferedImage leftScreenHalf = world.getSubimage(getCameraTankOneX(), getCameraTankOneY(),
                GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
        BufferedImage rightScreenHalf = world.getSubimage(getCameraTankTwoX(), getCameraTankTwoY(),
                GameInfo.SCREEN_WIDTH / 2, GameInfo.SCREEN_HEIGHT);
        BufferedImage miniMap = world.getSubimage(0,0, GameInfo.WORLD_WIDTH, GameInfo.WORLD_HEIGHT);
        /*// draw black split screen border
        buffer.setColor(Color.BLACK);
        buffer.fillRect(TankGame.SCREEN_WIDTH / 2, 0, 4, TankGame.SCREEN_HEIGHT);*/
        g2.drawImage(leftScreenHalf,0,0,null);
        g2.drawImage(rightScreenHalf,GameInfo.SCREEN_WIDTH / 2 + 4,0,null);
        //draw minimap
        g2.scale(.09,.09);
        g2.drawImage(miniMap, 6100, 6400, null);
        g2.drawRect(6100, 6400, miniMap.getWidth(), miniMap.getHeight());
        g2.scale(1.0,1.0);

        //draw lives etc - put in seperate function later
        //p1
        Font infoFontBold = new Font("Helvetica", Font.BOLD, 190);
        Font infoFont = new Font("Helvetica", Font.PLAIN, 180);
        g2.drawRect(400, 400,2000,900);
        g2.fillRect(400,400,2000,900);
        String playerOneLives = "Lives: " + tankOneLives;
        String playerOneHealth = Integer.toString(getTankHealth("tankOne"));
        String playerOneWeapon = "Weapon: " +  tankOneCurrentWeapon;
        String playerOnePowerup = "PowerUp: " + tankOneCurrentPowerUp;
        g2.setColor(Color.WHITE);
        g2.setFont(infoFontBold);
        g2.drawString("Player One", 450, 620);
        g2.setFont(infoFont);
        g2.drawString("Health:", 450, 820);
        if (getTankHealth("tankOne") <= 60) {
            g2.setColor(Color.YELLOW);
        }
        else if (getTankHealth("tankOne") <= 25) {
            g2.setColor(Color.RED);
        }
        g2.drawString(playerOneHealth, 1075, 820);
        g2.setColor(Color.WHITE);
        g2.drawString(playerOneLives, 450, 1020);
        //fix this
        /*if ("Bullet".equalsIgnoreCase(tankOneCurrentWeapon)) {
            g2.drawImage(TankGame.bulletImage, 450, 1220, null);
        }*/
        //p2






    }
}
