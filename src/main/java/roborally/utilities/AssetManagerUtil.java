package roborally.utilities;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSets;
import roborally.game.objects.robot.AI;
import roborally.game.objects.robot.Robot;

import java.util.*;

public class AssetManagerUtil {
    public static float volume = 1;
    public static final com.badlogic.gdx.assets.AssetManager manager = new com.badlogic.gdx.assets.AssetManager();
    // Sounds
    public static final AssetDescriptor<Sound> SHOOT_LASER
            = new AssetDescriptor<>("sounds/fireLaser.mp3", Sound.class);
    public static final AssetDescriptor<Sound> STEPIN_LASER
            = new AssetDescriptor<>("sounds/stepIntoLaser.wav", Sound.class);
    public static final AssetDescriptor<Sound> ROBOT_HIT
            = new AssetDescriptor<>("sounds/robotHit.mp3", Sound.class);
    public static final AssetDescriptor<Sound> STEP1
            = new AssetDescriptor<>("sounds/step1.mp3", Sound.class);
    public static final AssetDescriptor<Sound> STEP2
            = new AssetDescriptor<>("sounds/step2.mp3", Sound.class);
    public static final AssetDescriptor<Sound> STEP3
            = new AssetDescriptor<>("sounds/step3.mp3", Sound.class);
    public static final AssetDescriptor<Music> SOUNDTRACK
            = new AssetDescriptor<>("sounds/soundTrack.mp3", Music.class);
    //Maps
    private static final AssetDescriptor<TiledMap> MAP_LASER_TEST
            = new AssetDescriptor<>("maps/testMap001.tmx", TiledMap.class);
    private static final AssetDescriptor<TiledMap> MAP_TEST
            = new AssetDescriptor<>("maps/riskyExchangeBeginnerWithStartArea.tmx", TiledMap.class);
    private static final AssetDescriptor<TiledMap> MAP_TEST3
            = new AssetDescriptor<>("maps/testMapSingleLasers.tmx", TiledMap.class);
    private static final AssetDescriptor<TiledMap> MAP_TEST2
            = new AssetDescriptor<>("maps/riskyExchangeBeginnerWithStartAreaVertical.tmx", TiledMap.class);

    //Other
    private static final AssetDescriptor<Texture> MENU
            = new AssetDescriptor<>("menu/menu.png", Texture.class);
    private static final AssetDescriptor<Texture> BUTTONS
            = new AssetDescriptor<>("menu/buttons.png", Texture.class);
    private static final AssetDescriptor<Texture> MAP_BUTTON
            = new AssetDescriptor<>("menu/mapButton.png", Texture.class);
    private static final AssetDescriptor<Texture> BACKUP
            = new AssetDescriptor<>("cards/new/backup.png", Texture.class);
    private static final AssetDescriptor<Texture> ROTATELEFT
            = new AssetDescriptor<>("cards/new/rotatel.png", Texture.class);
    private static final AssetDescriptor<Texture> ROTATERIGHT
            = new AssetDescriptor<>("cards/new/rotater.png", Texture.class);
    private static final AssetDescriptor<Texture> MOVE_1
            = new AssetDescriptor<>("cards/new/move1.png", Texture.class);
    private static final AssetDescriptor<Texture> MOVE_2
            = new AssetDescriptor<>("cards/new/move2.png", Texture.class);
    private static final AssetDescriptor<Texture> MOVE_3
            = new AssetDescriptor<>("cards/new/move3.png", Texture.class);
    private static final AssetDescriptor<Texture> U_TURN
            = new AssetDescriptor<>("cards/new/u-turn.png", Texture.class);

    //Robots
    private static final AssetDescriptor<Texture> ANGRY
            = new AssetDescriptor<>("robots/new/Angry.png", Texture.class);
    private static final AssetDescriptor<Texture> BLUE
            = new AssetDescriptor<>("robots/new/Blue.png", Texture.class);
    private static final AssetDescriptor<Texture> GREEN
            = new AssetDescriptor<>("robots/new/Green.png", Texture.class);
    private static final AssetDescriptor<Texture> ORANGE
            = new AssetDescriptor<>("robots/new/Orange.png", Texture.class);
    private static final AssetDescriptor<Texture> PINK
            = new AssetDescriptor<>("robots/new/Pink.png", Texture.class);
    private static final AssetDescriptor<Texture> PURPLE
            = new AssetDescriptor<>("robots/new/Purple.png", Texture.class);
    private static final AssetDescriptor<Texture> RED
            = new AssetDescriptor<>("robots/new/Red.png", Texture.class);
    private static final AssetDescriptor<Texture> YELLOW
            = new AssetDescriptor<>("robots/new/Yellow.png", Texture.class);
    public static AI[] ai;
    public static ArrayList<Robot> robots;
    private static TiledMap loadedMap;
    private static HashMap<String, TiledMapTileLayer> layers;
    private static Stack<String> robotNames;

    public static void load() {
        //Maps
        manager.load(MAP_TEST);
        manager.load(MAP_TEST2);
        manager.load(MAP_TEST3);
        manager.load(MAP_LASER_TEST);

        //Robots
        manager.load(ANGRY);
        manager.load(BACKUP);
        manager.load(BLUE);
        manager.load(GREEN);
        manager.load(ORANGE);
        manager.load(PINK);
        manager.load(PURPLE);
        manager.load(RED);
        manager.load(YELLOW);

        //Sounds
        manager.load(SHOOT_LASER);
        manager.load(STEPIN_LASER);
        manager.load(STEP1);
        manager.load(STEP2);
        manager.load(STEP3);
        manager.load(ROBOT_HIT);
        manager.load(SOUNDTRACK);

        //Menu
        manager.load(MENU);
        manager.load(BUTTONS);
        manager.load(MAP_BUTTON);

        //Cards
        manager.load(ROTATERIGHT);
        manager.load(ROTATELEFT);
        manager.load(MOVE_1);
        manager.load(MOVE_2);
        manager.load(MOVE_3);
        manager.load(U_TURN);

    }

    public static Texture getMenu() {
        return manager.get(MENU);
    }
    public static Texture getButtons() {
        return manager.get(BUTTONS);
    }
    public static Texture getMapButton() {return manager.get(MAP_BUTTON);}

    // Only one map so far, but can add more and return a list.
    public static TiledMap getMap(int map) {
        TiledMap[] tiledMaps = {manager.get(MAP_TEST), manager.get(MAP_TEST2),
                manager.get(MAP_LASER_TEST), manager.get(MAP_TEST3)};
        List<TiledMap> maps = Arrays.asList(tiledMaps);
        loadedMap = maps.get(map);
        return loadedMap;
    }

    public static Texture getCardTexture(String card) {
        HashMap<String, Texture> map = new HashMap<>();
        map.put("RotateRight", manager.get(ROTATERIGHT));
        map.put("RotateLeft", manager.get(ROTATELEFT));
        map.put("Move1", manager.get(MOVE_1));
        map.put("Move2", manager.get(MOVE_2));
        map.put("Move3", manager.get(MOVE_3));
        map.put("Uturn", manager.get(U_TURN));
        map.put("Backup", manager.get(BACKUP));
        return map.get(card);
    }

    /**
     * Returns the robotTexture in position i, in chronological order
     */
    public static Texture getRobotTexture(int i) {
        Texture[] robotTexture = new Texture[8];
        robotTexture[0] = manager.get(ANGRY);
        robotTexture[1] = manager.get(BLUE);
        robotTexture[2] = manager.get(GREEN);
        robotTexture[3] = manager.get(ORANGE);
        robotTexture[4] = manager.get(PINK);
        robotTexture[5] = manager.get(PURPLE);
        robotTexture[6] = manager.get(RED);
        robotTexture[7] = manager.get(YELLOW);
        return robotTexture[i];
    }

    public static void dispose() {
        manager.clear();
    }

    /**
     * Returns the map that is loaded into the current game.
     */
    public static TiledMap getLoadedMap() {
        if (loadedMap == null) {
            loadedMap = manager.get(MAP_TEST2);
        }
        return loadedMap;
    }

    /**
     * Returns a HashMap with the layers of the current TiledMap.
     */
    public static HashMap<String, TiledMapTileLayer> getLoadedLayers() {

        ReadAndWriteLayers readAndWriteLayers = new ReadAndWriteLayers();
        layers = readAndWriteLayers.createLayers(getLoadedMap());

        return layers;
    }

    public static TiledMapTileSets getTileSets() {
        return loadedMap.getTileSets();
    }

    // Default AI robots
    public static void makeAIRobots() {
        ai = new AI[8];
        ai[0] = new AI(3, 0, 0);
        ai[1] = new AI(0, 1, 1);
        ai[2] = new AI(3, 2, 2);
        ai[3] = new AI(8, 3, 3);
        ai[4] = new AI(3, 3, 4);
        ai[5] = new AI(4, 4, 5);
        ai[6] = new AI(4, 5, 6);
        ai[7] = new AI(6, 4, 7);
    }

    public static ArrayList<Robot> getRobots() {
        return robots;
    }

    /**
     * Sets the robots array to be something different for other game modes
     */
    public static void setRobots(ArrayList<Robot> robots) {
        AssetManagerUtil.robots = robots;
    }

    public static AI[] getAIRobots() {
        return ai;
    }

    // Default Robots
    public static ArrayList<Robot> makeRobots() {
        robots = new ArrayList<>();
        robots.add(new Robot(5, 0, 0));
        robots.add(new Robot(6, 0, 1));
        robots.add(new Robot(3, 1, 2));
        robots.add(new Robot(8, 1, 3));
        robots.add(new Robot(1, 2, 4));
        robots.add(new Robot(5, 6, 5));
        robots.add(new Robot(5, 4, 6));
        robots.add(new Robot(5, 5, 7));

        // Texture region is not automatically set in constructor (for now).
        return robots;
    }


    // Default names for the robots
    public static void makeRobotNames() {
        robotNames = new Stack<>();
        robotNames.add("Yellow");
        robotNames.add("Red");
        robotNames.add("Purple");
        robotNames.add("Pink");
        robotNames.add("Orange");
        robotNames.add("Green");
        robotNames.add("Blue");
        robotNames.add("Angry");
    }

    public static String getRobotName() {
        if (robotNames == null || robotNames.isEmpty()) {
            robotNames = new Stack<>();
            makeRobotNames();
        }
        return robotNames.pop();
    }
}
