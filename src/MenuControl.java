
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


    /**
     *
     * @author anthony-pc
     */
    public class MenuControl implements KeyListener {

        private final int up;
        private final int down;
        private final int right;
        private final int left;
        private final int select;
        private StartScreen startScreen;

        public MenuControl(StartScreen startScreen, int up, int down, int left, int right, int select) {
            this.startScreen = startScreen;
            this.up = up;
            this.down = down;
            this.right = right;
            this.left = left;
            this.select = select;

        }

        @Override
        public void keyTyped(KeyEvent ke) {

        }

        @Override
        public void keyPressed(KeyEvent ke) {
            int keyPressed = ke.getKeyCode();
            int location = ke.getKeyLocation();
            /*if (keyPressed == up) {
                this.startScreen.toggleUpPressed();
            }
            if (keyPressed == down) {
                this.startScreen.toggleDownPressed();
            }
            if (keyPressed == left) {
                this.startScreen.toggleLeftPressed();
            }
            if (keyPressed == right) {
                this.startScreen.toggleRightPressed();
            }
            if (keyPressed == select) {
                this.startScreen.toggleSelectPressed();
            }*/


        }

        @Override
        public void keyReleased(KeyEvent ke) {
            int keyReleased = ke.getKeyCode();
            int location = ke.getKeyLocation();
            /*if (keyReleased  == up) {
                this.startScreen.unToggleUpPressed();
            }
            if (keyReleased == down) {
                this.startScreen.unToggleDownPressed();
            }
            if (keyReleased  == left) {
                this.startScreen.unToggleLeftPressed();
            }
            if (keyReleased  == right) {
                this.startScreen.unToggleRightPressed();
            }

            if (keyReleased == select) {
                this.startScreen.selectPressed();
            }
*/
        }
    }

