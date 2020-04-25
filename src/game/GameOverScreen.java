package game;

import resource.GameInfo;
import resource.GameState;
import resource.Resource;
import resource.SoundPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameOverScreen extends JPanel {
    String winningPlayer = "default";
    public GameState currentState = GameState.GAME_OVER;
    int logoXLocation = GameInfo.SCREEN_WIDTH/2 - 153; // 487
    int buttonXLocation = GameInfo.SCREEN_WIDTH/2 - 105; // 535
    private JFrame jFrame;
    int mouseClickX;
    int mouseClickY;
    int mouseLocationX;
    int mouseLocationY;
    SoundPlayer soundEffectPlayer = new SoundPlayer(2, "menusound2.wav", true);

    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(Resource.buttonFont);
        g.fillRect(0, 0, GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        g.drawImage(Resource.getResourceImage("logo"), logoXLocation, 50, null);
        drawRetryButton(g);
        drawExitButton(g);
        g.setColor(Color.WHITE);
        g.setFont(Resource.gameOverFont);
        g.drawString("GAME OVER!", buttonXLocation, 350);
        g.drawString(winningPlayer + " WINS!", buttonXLocation - 35, 400);
    }


    public void drawRetryButton(Graphics g) {
        g.setColor(Color.white);
        //draw start button outline
        g.drawRoundRect(buttonXLocation, 416, 210, 70, 20, 20);
        // if start button is moused over change color
        if (mouseLocationX >= buttonXLocation && mouseLocationX <= buttonXLocation + 210 && mouseLocationY >= 416 && mouseLocationY <= 506) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(buttonXLocation,416,210,70,20,20);
            g.setColor(Color.GREEN);
        }
        else {
            g.setColor(Color.darkGray);
            g.fillRoundRect(buttonXLocation,416,210,70,20,20);
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("RETRY", buttonXLocation + 50, 470);
    }

    public void drawExitButton(Graphics g) {
        g.setColor(Color.white);
        //draw start button outline
        g.drawRoundRect(buttonXLocation, 416 + 90, 210, 70, 20, 20);
        // if start button is moused over change color
        if (mouseLocationX >= buttonXLocation && mouseLocationX <= buttonXLocation + 210 && mouseLocationY >= 526 && mouseLocationY <= 596) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(buttonXLocation,416 + 90,210,70,20,20);
            g.setColor(Color.RED);
        }
        else {
            g.setColor(Color.darkGray);
            g.fillRoundRect(buttonXLocation,416 + 90,210,70,20,20);
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("EXIT", buttonXLocation + 50, 560);
    }


    public void setCurrentState(GameState stateToSet) {
        //System.out.println("start screen state to be set to " + stateToSet);
        this.currentState = stateToSet;
        //System.out.println("start screen state set to " + currentState);
    }

    public GameState getCurrentState() {
        //System.out.println("current state gotten from startScreen getCurrentState");
        return this.currentState;
    }

    public void setGameOverWinner(String winningPlayer) {
        this.winningPlayer = winningPlayer;
    }


    //new init
    public void init() {
        this.jFrame = new JFrame("Tank Wars - Game Over");
        this.jFrame.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent mouseEvent) {
                //System.out.println("click from start");
                mouseClickX = mouseEvent.getX();
                //System.out.println("x: " + mouseClickX);
                mouseClickY = mouseEvent.getY();
                //System.out.println("y: " + mouseClickY);
            }
        });

        //listen for mouse movement
        this.jFrame.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent mouseEvent) {
                //System.out.println("move from start");
                mouseLocationX = mouseEvent.getX();
                //System.out.println("x: " + mouseLocationX);
                mouseLocationY = mouseEvent.getY();
                //System.out.println("y: " + mouseLocationY);
            }
        });

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT + 20);
        this.jFrame.setResizable(false);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(false);


    }

    public void runGameOverScreen() {
        setCurrentState(GameState.GAME_OVER);
        soundEffectPlayer.play();
        if (currentState == GameState.GAME_OVER) {
            try {
                repaint();
                //if retry button clicked
                if (mouseClickX >= buttonXLocation && mouseClickX <= buttonXLocation + 210 && mouseClickY >= 436 && mouseClickY <= 506) {
            /*game.TankGame.soundEffectPlayer.setSoundFile("menusound2.wav");
            game.TankGame.soundEffectPlayer.play();*/
                    mouseClickX = 0;
                    mouseClickY = 0;
                    setCurrentState(GameState.RESET);
                    //this.jFrame.setVisible(false);
                }

                //if exit button clicked
                if (mouseClickX >= buttonXLocation && mouseClickX <= buttonXLocation + 210 && mouseClickY >= 526 && mouseClickY <= 616) {
            /*game.TankGame.soundEffectPlayer.setSoundFile("menusound2.wav");
            game.TankGame.soundEffectPlayer.play();*/
                    System.exit(0);
                }

            } catch (Exception ignored) {
                System.out.println(ignored);
            }
        }

    }

    public void setJFrameVisible(boolean visible) {
        this.jFrame.setVisible(visible);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        super.paintComponent(g2);
        drawImage(g2);
    }


}






