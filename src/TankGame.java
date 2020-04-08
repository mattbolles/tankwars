/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;


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
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;
    private BufferedImage world;
    private BufferedImage background;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank tankOne;
    private Tank tankTwo;
    SoundPlayer soundPlayer;


    public static void main(String[] args) {
        TankGame tankExample = new TankGame();
        tankExample.init();
        try {

            while (true) {
                tankExample.tankOne.update();
                tankExample.tankTwo.update();
                tankExample.repaint();
              //  System.out.println(tankExample.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }

    }


    private void init() {
        this.jFrame = new JFrame("Tank Wars");
        this.world = new BufferedImage(TankGame.SCREEN_WIDTH, TankGame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        this.background = new BufferedImage(TankGame.SCREEN_WIDTH, TankGame.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage tankImage = null;
        BufferedImage tank2Image = null;
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
            background = read(TankGame.class.getClassLoader().getResource("background.bmp"));
            //Using file objects to read in resources.
            //tankImage = read(new File("tank1.png"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // change these later, add separate tank images
        tankOne = new Tank(200, 200, 0, 0, 0, tankImage);
        tankTwo = new Tank(980, 400, 0, 0, 180, tank2Image);


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

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        //following line avoids image trailing
        buffer.fillRect(0,0,TankGame.SCREEN_WIDTH,TankGame.SCREEN_HEIGHT);
        int backgroundWidth = background.getWidth();
        int backgroundHeight = background.getHeight();

        this.tankOne.drawImage(buffer);
        this.tankTwo.drawImage(buffer);
        for (int y = 0; y < getHeight(); y += backgroundHeight) {
            for (int x = 0; x < getWidth(); x += backgroundWidth) {
                g2.drawImage(background, x, y, this);
            }
        }
        g2.drawImage(world,0,0,null);


    }
}
