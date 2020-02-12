package roborally.game.gameboard;

import com.badlogic.gdx.math.Vector2;
import roborally.game.objects.Robot;
import roborally.game.objects.RobotCore;
import roborally.ui.robot.IUIRobot;

public interface IGameBoard {
    /**
     * Checks if Robot is on flag.
     *
     * @return the Robot on the flag/checkpoint.
     */
    public boolean onFlag(RobotCore robotCore);

    /**
     * Checks if Robot is on hole.
     *
     * @return the Robot on the hole.
     */
    public boolean onHole(RobotCore robotCore);

    /**
     * Check if Robot can move. (Might be a wall or another Robot).
     *
     * @return if the Robot can move.
     */
    public boolean canMove(RobotCore robotCore);
}
