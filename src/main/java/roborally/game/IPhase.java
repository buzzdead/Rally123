package roborally.game;

import roborally.game.objects.gameboard.IFlag;
import roborally.game.objects.robot.Robot;
import roborally.ui.ILayers;

import java.util.ArrayList;

public interface IPhase {
    void revealProgramCards();
    void playNextRegisterCard();
    void moveAllConveyorBelts(ILayers layers);
    void moveCogs(ILayers layers);
    void fireLasers();
    void updateCheckPoints();
    void registerFlagPositions();
    boolean checkForWinner();
    void run(ILayers layers);
    Robot getWinner();
}
