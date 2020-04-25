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
    public GameState currentState = GameState.GAME_OVER;
    String winningPlayer = "PLAYER ???";
    int logoXLocation = GameInfo.SCREEN_WIDTH / 2 - 153; // 487
    int buttonXLocation = GameInfo.SCREEN_WIDTH / 2 - 105; // 535
    int mouseClickX;
    int mouseClickY;
    int mouseLocationX;
    int mouseLocationY;
    SoundPlayer soundEffectPlayer = new SoundPlayer(2, "sound/menusound2.wav", true);
    private JFrame jFrame;

    public void drawImage(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(Resource.buttonFont);
        g.fillRect(0, 0, GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        g.drawImage(Resource.getResourceImage("logo"), logoXLocation, 50, null);
        drawRetryButton(g);
        drawExitButton(g);
        g.setColor(Color.WHITE);
        g.setFont(Resource.gameOverFont);
        g.drawString("GAME OVER!", buttonXLocation, 310);
        g.drawString(winningPlayer + " WINS!", buttonXLocation - 50, 350);
        g.setColor(Color.LIGHT_GRAY);
        g.setFont(Resource.creditFont);
        g.drawString("2020 Matt Bolles", GameInfo.SCREEN_WIDTH / 2 - 75, 750);
    }


    public void drawRetryButton(Graphics g) {
        g.setColor(Color.white);
        //draw start button outline
        g.drawRoundRect(buttonXLocation, 416, 210, 70, 20, 20);
        // if start button is moused over change color
        if (mouseLocationX >= buttonXLocation && mouseLocationX <= buttonXLocation + 210 && mouseLocationY >= 416 && mouseLocationY <= 506) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(buttonXLocation, 416, 210, 70, 20, 20);
            g.setColor(Color.GREEN);
        } else {
            g.setColor(Color.darkGray);
            g.fillRoundRect(buttonXLocation, 416, 210, 70, 20, 20);
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("RETRY", buttonXLocation + 20, 470);
    }

    public void drawExitButton(Graphics g) {
        g.setColor(Color.white);
        //draw start button outline
        g.drawRoundRect(buttonXLocation, 416 + 90, 210, 70, 20, 20);
        // if start button is moused over change color
        if (mouseLocationX >= buttonXLocation && mouseLocationX <= buttonXLocation + 210 && mouseLocationY >= 526 && mouseLocationY <= 596) {
            g.setColor(Color.WHITE);
            g.fillRoundRect(buttonXLocation, 416 + 90, 210, 70, 20, 20);
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.darkGray);
            g.fillRoundRect(buttonXLocation, 416 + 90, 210, 70, 20, 20);
            g.setColor(Color.LIGHT_GRAY);
        }
        g.drawString("EXIT", buttonXLocation + 50, 560);
    }

    public GameState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(GameState stateToSet) {
        this.currentState = stateToSet;
    }

    public void setGameOverWinner(String winningPlayer) {
        this.winningPlayer = winningPlayer;
    }


    //new init
    public void init() {
        this.jFrame = new JFrame("Tank Wars - Game Over");
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

        this.jFrame.setLayout(new BorderLayout());
        this.jFrame.add(this);
        this.jFrame.setSize(GameInfo.SCREEN_WIDTH, GameInfo.SCREEN_HEIGHT);
        this.jFrame.setResizable(false);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jFrame.setVisible(false);
        this.jFrame.setUndecorated(true);
        this.jFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
    }

    public void runGameOverScreen() {
        setCurrentState(GameState.GAME_OVER);
        soundEffectPlayer.play();
        if (currentState == GameState.GAME_OVER) {
            try {
                repaint();
                //if retry button clicked
                if (mouseClickX >= buttonXLocation && mouseClickX <= buttonXLocation + 210 && mouseClickY >= 436 && mouseClickY <= 506) {
                    mouseClickX = 0;
                    mouseClickY = 0;
                    setCurrentState(GameState.RESET);
                }

                //if exit button clicked
                if (mouseClickX >= buttonXLocation && mouseClickX <= buttonXLocation + 210 && mouseClickY >= 526 && mouseClickY <= 616) {
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






