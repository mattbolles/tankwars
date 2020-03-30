/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 *
 * @author anthony-pc
 */
public class TankControl implements KeyListener {

    private Tank tank;
    private final int up;
    private final int down;
    private final int right;
    private final int left;
    private final int shoot;
    
    public TankControl(Tank tank, int up, int down, int left, int right, int shoot) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            this.tank.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.tank.toggleDownPressed();
        }
        if (keyPressed == left) {
            this.tank.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.tank.toggleRightPressed();
        }
        // shift for shoot (for right side player? maybe use space for left side player?)
        if (keyPressed == KeyEvent.VK_SHIFT) {
            this.tank.toggleShiftPressed();
        }
        

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        int keyReleased = keyEvent.getKeyCode();
        if (keyReleased  == up) {
            this.tank.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.tank.unToggleDownPressed();
        }
        if (keyReleased  == left) {
            this.tank.unToggleLeftPressed();
        }
        if (keyReleased  == right) {
            this.tank.unToggleRightPressed();
        }

    }
}
