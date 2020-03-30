package roborally.ui.gdx.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import roborally.game.IGame;
import roborally.game.objects.robot.Robot;
import roborally.utilities.SettingsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Events {
    public static final float unitScale = 300 * 3f / 16f;
    private boolean waitEvent;
    private float dt;
    private boolean robotFadeOrder;
    private ArrayList<Alpha> fadeableRobots;
    private int fadeCounter;
    private ArrayList<LaserEvent> laserEvents;
    private int currentPhaseIndex;
    private double gameSpeed;
    private int factor;
    private int robotPlayedCounter;

    public Events() {
        this.waitEvent = false;
        this.dt = 0f;
        this.robotFadeOrder = false;
        this.fadeableRobots = new ArrayList<>();
        this.fadeCounter = 0;
        this.laserEvents = new ArrayList<>();
        this.currentPhaseIndex = 0;
        this.gameSpeed = 0.2;
        this.setLaserSpeed("normal");

    }

    /**
     * Updates the time limit to play the next card in the current phase of the round.
     * (Calculated by time between frames).
     *
     * @param gameSpeed the limit.
     */
    public void setGameSpeed(String gameSpeed) {
        if ("fastest".equals(gameSpeed))
            this.gameSpeed = 0.01;
        else if ("fast".equals(gameSpeed))
            this.gameSpeed = 0.2;
        else if ("normal".equals(gameSpeed))
            this.gameSpeed = 0.5;
    }

    /** Sets the factor used in {@link LaserEvent} to increment the image of the laser relative to its direction */
    public void setLaserSpeed(String laserSpeed) {
        if ("slow".equals(laserSpeed))
            this.factor = 400;
        else if ("normal".equals(laserSpeed))
            this.factor = 800;
        else if ("fast".equals(laserSpeed))
            this.factor = 1200;
    }

    /**
     * Adds the time lapsed between frames to the variable, plays the next card when it reaches the gameSpeed.
     *
     * @param dt   Time lapsed between frames.
     * @param game The game.
     */
    public void waitMoveEvent(float dt, IGame game) {
        this.dt += dt;
        if (this.dt >= gameSpeed) {
            game.getRound().getPhase().playNextRegisterCard();
            this.dt = 0;
            this.robotPlayedCounter++;
        }

        if (robotPlayedCounter == game.getRobots().size()) {
            game.getRound().getPhase().run(game.getLayers());
            currentPhaseIndex++;
            robotPlayedCounter = 0;
        }

        // If last phase
        if (currentPhaseIndex == SettingsUtil.NUMBER_OF_PHASES) {
            this.dt = 0f;
            this.currentPhaseIndex = 0;
            setWaitMoveEvent(false);
            game.getRound().run(game.getLayers());
        }
    }

    public boolean hasWaitEvent() {
        return waitEvent;
    }

    // Lets the UI know wether or not there are robots ready to move.
    public void setWaitMoveEvent(boolean value) {
        this.waitEvent = value;
    }

    /**
     * Replaces the robots texture with an image and fades it.
     *
     * @param pos     The robots position.
     * @param texture The robots texture.
     */
    public void fadeRobot(GridPoint2 pos, TextureRegion[][] texture) {
        Image image = new Image(texture[0][1]);
        image.setX(pos.x * unitScale);
        image.setY(pos.y * unitScale);
        image.setSize(unitScale, unitScale);
        this.fadeableRobots.add(new Alpha(1f, image));
    }

    // Returns true if there are robots to be faded.
    public boolean getFadeRobot() {
        return this.robotFadeOrder;
    }

    public void setFadeRobot(boolean value) {
        this.robotFadeOrder = value;
    }

    // Fades the robots, clears the list of robots to be faded if all subjects have fully faded.
    public void fadeRobots(SpriteBatch batch) {

        for (Alpha alpha : this.fadeableRobots) {
            alpha.image.draw(batch, alpha.update(Gdx.graphics.getDeltaTime()));
            if (alpha.dt <= 0)
                fadeCounter++;
        }

        if (fadeCounter == this.fadeableRobots.size()) {
            this.fadeableRobots.clear();
            setFadeRobot(false);
        }
        fadeCounter = 0;
    }


    // Returns true if there are lasers on the screen.
    public boolean hasLaserEvent() {
        return !this.laserEvents.isEmpty();
    }

    /**
     * Removes lasers that have served their purpose. Returns and sets the list excluding these lasers.
     */
    public ArrayList<LaserEvent> getLaserEvents() {
        List<LaserEvent> temp = this.laserEvents.stream()
                .filter(LaserEvent::hasLaserEvent)
                .collect(Collectors.toList());
        this.laserEvents.removeAll(temp);
        for (LaserEvent laserEvent : this.laserEvents) {
            if (laserEvent.getRobot() != null) { // Fades and removes robots shot by lasers interactively.
                if (("Destroyed".equals(laserEvent.getRobot().getLogic().getStatus()))) {
                    GridPoint2 pos = laserEvent.getRobot().getPosition();
                    fadeRobot(pos, laserEvent.getRobot().getTexture());
                    laserEvent.getRobot().deleteRobot();
                    setFadeRobot(true);
                }
            }
        }
        this.laserEvents = (ArrayList<LaserEvent>) temp;
        return this.laserEvents;
    }

    /**
     * Creates a new Laser event for the laser that has been fired.
     *
     * @param origin The position the laser was fired from.
     * @param pos    The endpoint of the laser.
     */
    public void createNewLaserEvent(GridPoint2 origin, GridPoint2 pos) {
        this.laserEvents.add(new LaserEvent(factor));
        this.laserEvents.get(this.laserEvents.size() - 1).laserEvent(origin, pos);
    }

    public void dispose() {
        this.laserEvents.clear();
        this.fadeableRobots.clear();
    }

    /**
     * Class to take individual images of robots and fade them separately, (subtracts alpha until invisible).
     */
    private static class Alpha {
        private float dt;
        private Image image;

        private Alpha(float dt, Image image) {
            this.dt = dt;
            this.image = image;
        }

        private float update(float dt) {
            this.dt -= (0.5f * dt);
            return this.dt;
        }
    }

    public void checkForDestroyedRobots(ArrayList<Robot> robots) {
        for (Robot robot : robots) {
            if (("Destroyed").equals(robot.getLogic().getStatus())) {
                System.out.println(robot.getName() + " was destroyed");
                removeFromUI(robot);
            }
        }
    }

    public void removeFromUI(Robot robot) {
        fadeRobot(robot.getPosition(), robot.getTexture());
        robot.deleteRobot();
        System.out.println("Removed " + robot.getName() + " from UI");
        setFadeRobot(true);
    }

}
