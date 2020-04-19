import java.util.ArrayList;

// class that makes a "camera" - coordinates for the splitscreen to follow

public class Camera {
    int offsetMaxX = GameInfo.WORLD_WIDTH - (GameInfo.SCREEN_WIDTH / 2);
    int offsetMaxY = GameInfo.WORLD_HEIGHT - GameInfo.SCREEN_HEIGHT;
    int offsetMinX = 0;
    int offsetMinY = 0;
    Tank tank;


    public Camera(Tank tank){
        this.tank = tank;
    }

    public void setTank(Tank tank) {
        this.tank = tank;
    }

    public int getCameraX() {
        int cameraX = this.tank.getX() - (GameInfo.SCREEN_WIDTH / 4);
        if (cameraX > offsetMaxX) {
            cameraX = offsetMaxX;
        }

        else if (cameraX < offsetMinX) {
            cameraX = offsetMinX;
        }
        return cameraX;
    }

    public int getCameraY() {
        int cameraY = this.tank.getY() - (GameInfo.SCREEN_HEIGHT / 2);
        if (cameraY > offsetMaxY) {
            cameraY = offsetMaxY;
        }

        else if (cameraY < offsetMinY) {
            cameraY = offsetMinY;
        }
        return cameraY;
    }

}
