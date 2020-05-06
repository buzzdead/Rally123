package roborally.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import roborally.game.IGame;
import roborally.game.robot.Robot;
import roborally.gameview.elements.ProgramCardsView;
import roborally.gameview.elements.UIElements;
import roborally.utilities.SettingsUtil;

import java.util.ArrayList;
import java.util.List;

public class AnimateEvent {
    private Events events;
    private ProgramCardsView programCardsView;
    private ProgramCardsView registerCardsView;
    private UIElements uiElements;
    private boolean cardPhase;
    private boolean playPhase;
    private WinEvent winEvent;

    public AnimateEvent(Events events, ProgramCardsView programCardsView, UIElements uiElements) {
        this.events = events;
        this.programCardsView = programCardsView;
        this.uiElements = uiElements;
        this.winEvent = new WinEvent();
    }

    /**
     * Called from UI to draw events in order. If no events are active nothing gets drawn.
     * @param batch the spriteBatch from UI.
     * @param game the game that is running.
     * @param stage the stage from UI.
     */
    public void drawEvents(SpriteBatch batch, IGame game, Stage stage) {
        batch.begin();
        if (cardPhase) {
            drawCardsInHand(game, batch, stage);
            stage.act();
        } else if (playPhase && !game.getGameOptions().inMenu()){
            drawRegisterCards(game, batch, stage);
        }
        if (events.getFadeRobot() && !game.getGameOptions().inMenu())
            events.fadeRobots(batch);
        if (events.hasLaserEvent() && !game.getGameOptions().inMenu())
            for (LaserEvent laserEvent : events.getLaserEvents())
                laserEvent.drawLaserEvent(batch, game.getRobots());
        if (!game.getGameOptions().inMenu())
            drawUIElements(game, batch, stage);
        if(events.hasExplosionEvent()) {
            for(List<Image> list : events.getExplosions()) {
                events.explode(Gdx.graphics.getDeltaTime(), (ArrayList<Image>) list);
                for(Image image : list)
                    image.draw(batch, 1);
            }
        }
        if(events.hasArchiveBorders() && !game.getGameOptions().inMenu()) {
            for(Image image : events.getArchiveBorders().values())
                image.draw(batch, 1);
        }
        batch.end();
    }


    private void drawUIElements(IGame game, SpriteBatch batch, Stage stage) {
        drawUIElement(batch, uiElements.getReboots());
        drawUIElement(batch, uiElements.getDamageTokens());
        drawUIElement(batch, uiElements.getFlags().get());
        for(Group group : uiElements.getLeaderboard())
            group.draw(batch, 1);

        updateMessageLabel(game, batch, stage);

        drawPowerDownButton(batch, stage);
    }

    private void drawUIElement(SpriteBatch batch, ArrayList<Image> uiElementsList) {
        for (Image element : uiElementsList) {
            element.draw(batch, 1);
        }
    }

    private void drawPowerDownButton(SpriteBatch batch, Stage stage) {
        if (cardPhase) {
            stage.addActor(uiElements.getPowerDownButton().get());
            uiElements.getPowerDownButton().get().draw(batch, 1);
        }
    }

    private void updateMessageLabel(IGame game, SpriteBatch batch, Stage stage) {
        for (Robot robot : game.getRobots()) {
            checkRobotStatus(robot.getLogic().isDestroyed(), robot.getName() + " was destroyed!", stage);
            checkRobotStatus(robot.isRobotInHole(), robot.getName() + " went into a hole!", stage);
            checkRobotStatus(!robot.getLogic().isUserRobot() && robot.getLogic().hasWon(), "Sorry, you lost! " + robot.getName() + " won!", stage);
            checkRobotStatus(robot.getLogic().isUserRobot() && robot.getLogic().hasWon(), "You have won!", stage);
        }

        uiElements.getMessage().get().draw(batch, 1);

        if (uiElements.getMessage().get().toString().contains("won")) {
            uiElements.getFlags().set(game.getUserRobot(), stage);
            uiElements.getExitButton().set(game, events, stage, uiElements);
            uiElements.getRestartButton().set(game, uiElements);
            stage.addActor(uiElements.getRestartButton().get());
            stage.addActor(uiElements.getExitButton().get());
            uiElements.getRestartButton().get().draw(batch, 1);
            uiElements.getExitButton().get().draw(batch, 1);
            Gdx.input.setInputProcessor(stage);
            winEvent.fireworks(Gdx.graphics.getDeltaTime(), events);
            events.setWaitMoveEvent(false);
        }
    }

    private void checkRobotStatus(boolean status, String message, Stage stage) {
        if (status) {
            uiElements.getMessage().set(message, stage);
        }
    }

    /**
     * Draws the cards until the user has chosen his or her cards.
     * @param game The game that is running.
     * @param batch The spriteBatch from UI.
     * @param stage The stage from UI.
     */
    private void drawCardsInHand(IGame game, SpriteBatch batch, Stage stage) {
        programCardsView.getTimerLabel().draw(batch, stage.getHeight() / 2);
        programCardsView.updateTimer(Gdx.graphics.getDeltaTime(), game.getUserRobot());
        programCardsView.getDoneButton().draw(batch, stage.getWidth() / 2);
        for (Group group : programCardsView.getGroups()) {
            group.draw(batch, 1);
        }

        if (programCardsView.done()) {
            cardPhase = false;
            stage.clear();

            game.orderTheUserRobotsCards(programCardsView.getOrder()); // TODO: Move to Game
            programCardsView.clear();
            events.setWaitMoveEvent(true);
        } else if (game.getUserRobot().getLogic().getPowerDown() || game.getUserRobot().getLogic().getNumberOfLockedCards() == SettingsUtil.REGISTER_SIZE) {
            cardPhase = false;
            stage.clear();
            programCardsView.clear();
            events.setWaitMoveEvent(true);
        }
    }

    private void drawRegisterCards(IGame game, SpriteBatch batch, Stage stage) {
        for (Group group : registerCardsView.getGroups()) {
            group.draw(batch, 1);
        }
    }

    /**
     * Initializes the cards into fixed positions. Makes a button to click to finish choosing cards.
     *
     * @param stage The stage from UI.
     */
    public void initiateCards(Stage stage, ProgramCardsView programCardsView) {
        this.programCardsView = programCardsView;
        programCardsView.setStage(stage);
        programCardsView.setDoneButton();
        programCardsView.setTimerLabel();

        stage.addActor(programCardsView.getDoneButton());

        float cardsGroupPositionX = stage.getWidth() - programCardsView.getGroups().size() * programCardsView.getCardWidth();
        cardsGroupPositionX = cardsGroupPositionX / 2 - programCardsView.getCardWidth();

        for (Group group : programCardsView.getGroups()) {
            group.setX(cardsGroupPositionX += programCardsView.getCardWidth());
            stage.addActor(group);
        }
        // xShift to the right-side edge of the game board
        float xShift = (stage.getWidth() + SettingsUtil.MAP_WIDTH) / 2f;
        float doneButtonPosX = xShift - programCardsView.getDoneButton().getWidth();
        programCardsView.getDoneButton().setX(doneButtonPosX);

        cardPhase = true;
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Initializes the cards in the register into fixed positions.
     * @param stage the stage from UI
     * @param registerView the ui reprentative of the register cards
     */
    public void initiateRegister(Stage stage, ProgramCardsView registerView) {
        this.registerCardsView = registerView;

        float cardsGroupPositionX = stage.getWidth() - registerCardsView.getGroups().size() * registerCardsView.getCardWidth();
        cardsGroupPositionX = cardsGroupPositionX / 2 - registerCardsView.getCardWidth();

        for (Group group : registerCardsView.getGroups()) {
            group.setX(cardsGroupPositionX += registerCardsView.getCardWidth());
        }

        playPhase = true;
    }

    public boolean getCardPhase() {
        return this.cardPhase;
    }
}
