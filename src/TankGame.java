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

    public static final int WORLD_WIDTH = 2000;
    public static final int WORLD_HEIGHT = 2000;
   /* // get user screen resolution - come back and add this later
    Dimension userScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public double userScreenWidth = userScreenSize.getWidth();
    public double userScreenHeight = userScreenSize.getHeight();
    */
    public static final int SCREEN_WIDTH = 1280; // 40 columns;
    public static final int SCREEN_HEIGHT = 820; // 25 rows - extra 20 is necessary to prevent blocks being cut off
    // for some reason
    private BufferedImage world;
    private BufferedImage background;
    private Background backgroundTile;
    public static BufferedImage bulletImage;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank tankOne;
    private Tank tankTwo;
    SoundPlayer soundPlayer;
    ArrayList<Wall> walls;



    public static void main(String[] args) {
        TankGame tankGame = new TankGame();
        tankGame.init();
        try {

            while (true) {
                tankGame.tankOne.update();
                tankGame.tankTwo.update();
                tankGame.repaint();
                int bump = 20;
                //make a seperate function for this after - more generic
                if (tankGame.tankOne.getHitBox().intersects(tankGame.tankTwo.getHitBox())) {
                    //tankGame.tankOne.hasCollided(); // add this, maybe to replace the other stuff
                    if (tankGame.tankOne.isDownPressed()) {
                        // + makes the tank not run over the other one
                        tankGame.tankOne.setX(tankGame.tankOne.getX() - tankGame.tankOne.getVx());
                        tankGame.tankOne.setY(tankGame.tankOne.getY() - tankGame.tankOne.getVy());
                    }

                    if (tankGame.tankTwo.isDownPressed()) {
                        tankGame.tankTwo.setX(tankGame.tankTwo.getX() - tankGame.tankTwo.getVx());
                        tankGame.tankTwo.setY(tankGame.tankTwo.getY() - tankGame.tankTwo.getVy());
                    }
                    System.out.println("Collision detected");
                }
              //  System.out.println(tankGame.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }

    }


    private void init() {
        this.jFrame = new JFrame("Tank Wars");
        this.world = new BufferedImage(TankGame.SCREEN_WIDTH, TankGame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage tankImage = null;
        BufferedImage tank2Image = null;
        BufferedImage breakableWall = null;
        BufferedImage unBreakableWall = null;
        BufferedImage background = null;
        walls = new ArrayList<>();
        try {

            /*
             * There is a subtle difference between using class
             * loaders and File objects to read in resources for your
             * tank games. First, File objects when given to input readers
             * will use your project's working directory as the base path for
             * finding the file. Class loaders will use the class path of the project
             * as the base path for finding files. For Intellij, this will be in the out
             * folder. if you expand the out folder, the expand the production folder, you
             * will find a folder that has the same name as your project. This is the folder
             * where your class path points to by default. Resources, will need to be stored
             * in here for class loaders to load resources correctly.
             *
             * Also one important thing to keep in mind, Java input readers given File objects
             * cannot be used to access file in jar files. So when building your jar, if you want
             * all files to be stored inside the jar, you'll need to class loaders and not File
             * objects.
             *
             */
            //Using class loaders to read in resources
            tankImage = read(TankGame.class.getClassLoader().getResource("tank1.png"));
            tank2Image = read(TankGame.class.getClassLoader().getResource("tank2.png"));
            TankGame.bulletImage = read(TankGame.class.getClassLoader().getResource("bullet.gif"));
            background = read(TankGame.class.getClassLoader().getResource("background.png"));
            breakableWall = read(TankGame.class.getClassLoader().getResource("WallBreakable.gif"));
            unBreakableWall = read(TankGame.class.getClassLoader().getResource("WallUnbreakable.gif"));

            //background info:
            int backgroundWidth = background.getWidth();
            int backgroundHeight = background.getHeight();
            backgroundTile = new Background(backgroundWidth, backgroundHeight, background);

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
                            BreakableWall br = new BreakableWall(currentCol*32, currentRow*32, breakableWall);
                            this.walls.add(br);
                            break;
                        case "3":
                        case "9":
                            //unbreakable wall
                            UnBreakableWall ubr = new UnBreakableWall(currentCol*32, currentRow*32, unBreakableWall);
                            this.walls.add(ubr);
                    }
                }
            }

            //Using file objects to read in resources.
            //tankImage = read(new File("tank1.png"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // change these later, add separate tank images
        tankOne = new Tank(200, 100, 0, 0, 0, tankImage);
        tankTwo = new Tank(980, 600, 0, 0, 180, tank2Image);

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
        this.jFrame.setSize(TankGame.SCREEN_WIDTH, TankGame.SCREEN_HEIGHT);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
        // background music
        soundPlayer = new SoundPlayer(1,"crystalraindrops.wav");
    }

    /*public void drawBackground() {
        // move this to a proper location... later
        int backgroundWidth = background.getWidth();
        int backgroundHeight = background.getHeight();
        for (int y = 0; y < getHeight(); y += backgroundHeight) {
            for (int x = 0; x < getWidth(); x += backgroundWidth) {
                //figure out how to get this into a seperate function - pass as parameter, assuming possible

                g2.drawImage(background, x, y, this);
            }
        }
    }*/

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        //following line avoids image trailing
        buffer.fillRect(0,0,TankGame.SCREEN_WIDTH,TankGame.SCREEN_HEIGHT);
        this.backgroundTile.drawImage(buffer);
        this.walls.forEach(wall -> wall.drawImage(buffer));
        this.tankOne.drawImage(buffer);
        this.tankTwo.drawImage(buffer);
        g2.drawImage(world,0,0,null);


    }
}
