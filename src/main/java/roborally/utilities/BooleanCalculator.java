package roborally.utilities;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import roborally.game.objects.laser.Cannon;
import roborally.ui.ILayers;
import roborally.ui.Layers;
import java.util.HashMap;

// Beep... Robots need to calculate.
public class BooleanCalculator {
    private HashMap<String, Boolean> operations;
    public ILayers layers;
    private int x;
    private int y;
    private int height;
    private int width;
    private Cannon cannon;

    public BooleanCalculator() {
        layers = new Layers();
        width = layers.getRobots().getWidth();
        height = layers.getRobots().getHeight();
        // Advanced calculations for AI, can take multiple conditions to figure out a good move.
        operations = new HashMap<>();
        cannon = new Cannon();
    }

    public Cannon getCannon() {
        return this.cannon;
    }

    /**
     * Checks if the robot is blocked by another robot, true if the robot is on the edge. If not, then bumping.
     * @param x the x position
     * @param y the y position
     * @param dx steps taken in x-direction
     * @param dy steps taken in y-direction
     * @return True if the robot or any robot on a straight line in its direction is facing a wall.
     */
    public boolean checkIfBlocked(int x, int y, int dx, int dy) {
        if(layers.checkForWall(x, y, dx, dy))
            return true;
        int newX = x + dx;
        int newY = y + dy;
        // There is no Robot on the next position.
        if(!layers.assertRobotNotNull(newX, newY))
            return false;
        else {
            if(layers.checkForWall(newX, newY, dx, dy))
                return true;
            if(layers.assertRobotNotNull(newX + dx, newY + dy) && layers.robotNextToRobot(newX, newY, dx, dy))
                return true;

        }
        layers.findCollidingRobot(newX, newY, dx, dy);
        return false;
    }

    /** Creates a new laser instance if there is a laser cell in the position the robot is moving to.
     *  Else it will see if the robot is currently in a laser instance.
     * @param x The x-coordinate the robot is moving to
     * @param y The y-coordinate the robot is moving to
     * @param name The name of the robot
     */

    public void checkForLasers(int x, int y, String name) {
        GridPoint2 pos = new GridPoint2(x, y);
        int id;
        if (layers.assertLaserNotNull(x, y)) {
            id = layers.getLaserID(x, y);
            cannon.createLaser(id, pos, name);
        }
        else
            cannon.updateLaser(name, pos);
    }

    // Check a specific position if it is blocked
    public boolean isBlocked(int x, int y) {
        return layers.assertRobotNotNull(x, y);
    }

    public boolean isOnHole(int x, int y) {
        return layers.assertHoleNotNull(x, y);
    }

    // AI methods
    public void determineFlagPos(Vector2 flagPos) {
        this.x = (int) flagPos.x;
        this.y = (int) flagPos.y;
    }

    public Vector2 getCurrFlagPos() {
        return new Vector2(this.x,this.y);
    }

    private boolean isBelowFlagOnMap(int y){
        return y < this.y;
    }

    private boolean isAboveFlagOnMap(int y){
        return y > this.y;
    }

    private boolean isToTheRightOfFlagOnMap(int x){
        return x > this.x;
    }

    private boolean isToTheLeftOfFlagOnMap(int x){
        return x < this.x;
    }

    public HashMap<String, Boolean> getOperations() {
        return this.operations;
    }

    public void loadAICalc(int x, int y) {
        operations.put("Left", isToTheLeftOfFlagOnMap(x));
        operations.put("Right", isToTheRightOfFlagOnMap(x));
        operations.put("Up", isAboveFlagOnMap(y));
        operations.put("Down", isBelowFlagOnMap(y));
    }
}