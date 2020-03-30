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
public class TankRotationExample extends JPanel  {


    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;
    private BufferedImage world;
    private Graphics2D buffer;
    private JFrame jFrame;
    private Tank tankOne;


    public static void main(String[] args) {
        TankRotationExample tankExample = new TankRotationExample();
        tankExample.init();
        try {

            while (true) {
                tankExample.tankOne.update();
                tankExample.repaint();
              //  System.out.println(tankExample.t1);
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }

    }


    private void init() {
        this.jFrame = new JFrame("Tank Rotation");
        this.world = new BufferedImage(TankRotationExample.SCREEN_WIDTH, TankRotationExample.SCREEN_HEIGHT, BufferedImage.TYPE_INT_RGB);
        BufferedImage tankImage = null;
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
            tankImage = read(TankRotationExample.class.getClassLoader().getResource("tank1.png"));
            //Using file objects to read in resources.
            //tankImage = read(new File("tank1.png"));

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        tankOne = new Tank(200, 200, 0, 0, 0, tankImage);


        TankControl tankOneControl = new TankControl(tankOne, KeyEvent.VK_UP,
                KeyEvent.VK_DOWN,
                KeyEvent.VK_LEFT,
                KeyEvent.VK_RIGHT,
                KeyEvent.VK_ENTER);

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.addKeyListener(tankOneControl);
        this.jFrame.setSize(TankRotationExample.SCREEN_WIDTH, TankRotationExample.SCREEN_HEIGHT + 30);
        this.jFrame.setResizable(false);
        jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        buffer = world.createGraphics();
        //following line avoids image trailing
        buffer.fillRect(0,0,getWidth(),getHeight());
        this.tankOne.drawImage(buffer);
        g2.drawImage(world,0,0,null);

    }
}
