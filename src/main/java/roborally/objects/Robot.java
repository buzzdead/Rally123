package roborally.objects;


import roborally.gameboard.GameBoard;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector2;
import roborally.gameboard.Layers;
import roborally.tools.AssMan;

public class Robot implements IRobot {

    private TiledMapTileLayer.Cell robotCell;
    private TiledMapTileLayer.Cell robotWonCell;
    private TiledMapTileLayer.Cell robotLostCell;
    private Vector2 robotPosition;
    private Texture robotTexture;
    private TextureRegion[][] robotTextureRegion;
    private Layers layers;

    public Robot() {
        this.robotTexture = AssMan.getRobotTexture();
        robotTextureRegion = TextureRegion.split(robotTexture, GameBoard.TILE_SIZE, GameBoard.TILE_SIZE);
        this.robotPosition = new Vector2(0,0);
        this.layers = new Layers();
    }

    @Override
    public Texture getTexture() { return this.robotTexture;}

    @Override
    public TiledMapTileLayer.Cell getWinCell() {
        if (this.robotWonCell==null) {
            this.robotWonCell = new TiledMapTileLayer.Cell();
            this.robotWonCell.setTile(new StaticTiledMapTile(robotTextureRegion[0][2]));
        }
        return this.robotWonCell;
    }

    @Override
    public TiledMapTileLayer.Cell getLostCell() {
        if (this.robotLostCell==null) {
            this.robotLostCell = new TiledMapTileLayer.Cell();
            this.robotLostCell.setTile(new StaticTiledMapTile(robotTextureRegion[0][1]));
        }
        return this.robotLostCell;
    }

    @Override
    public TiledMapTileLayer.Cell getCell() {
        if (this.robotCell==null) {
            this.robotCell = new TiledMapTileLayer.Cell();
            this.robotCell.setTile(new StaticTiledMapTile(robotTextureRegion[0][0]));
        }
        return this.robotCell;
    }

    @Override
    public void setPosition(float x, float y) {
        this.robotPosition.x = x; this.robotPosition.y = y;
    }

    @Override
    public int getPositionX() {
        return (int)this.robotPosition.x;
    }

    @Override
    public int getPositionY() {
        return (int)this.robotPosition.y;
    }

    @Override
    public boolean moveRobot(int dx, int dy) {
        layers.getRobot().setCell(this.getPositionX(), this.getPositionY(), null);
        this.setPosition(this.getPositionX()+dx,this.getPositionY()+dy);
        layers.getRobot().setCell(this.getPositionX(), this.getPositionY(), getCell());
        return this.getPositionX() >= 0 && this.getPositionY() >= 0
                && this.getPositionX() < layers.getRobot().getWidth()
                && this.getPositionY() < layers.getRobot().getHeight();
    }
}