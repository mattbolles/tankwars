package resource;

import java.awt.*;

public class GameInfo {
    // add check for window resolution later - if user resolution too small, display error

    public static final int WORLD_WIDTH = 2016; // 63 col (32x32 tile)
    public static final int WORLD_HEIGHT = 2016; // 63 row
    public static final int SCREEN_WIDTH = 1280; // 40 col
    public static final int SCREEN_HEIGHT = 800; // 25 rows
    public static final int tankOneXSpawnCoord = 320;
    public static final int tankOneYSpawnCoord = 320;
    public static final int tankTwoXSpawnCoord = 1696;
    public static final int tankTwoYSpawnCoord = 1696;
    public static final int offsetMaxX = WORLD_WIDTH - (SCREEN_WIDTH / 2);
    public static final int offsetMaxY = WORLD_HEIGHT - SCREEN_HEIGHT;
    public static final int offsetMinX = 0;
    public static final int offsetMinY = 0;

}
