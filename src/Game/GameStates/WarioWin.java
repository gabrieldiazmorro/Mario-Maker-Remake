package Game.GameStates;

import Display.UI.UIStringButton;
import Main.Handler;
import Resources.Images;
import Display.UI.UIManager;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class WarioWin extends State {

    private UIManager uiManager;

    public WarioWin(Handler handler) {
        super(handler);
        uiManager = new UIManager(handler);

        uiManager.addObjects(new UIStringButton(600, 223-(64+96), 128, 64, "Quit", () -> {
            System.exit(0);
        },handler,Color.BLACK));

        uiManager.addObjects(new UIStringButton(56, (223-(64+96)), 128, 64, "Title", () -> {
            handler.getMouseManager().setUimanager(null);
            handler.setIsInMap(false);
            State.setState(handler.getGame().menuState);
        },handler,Color.BLACK));

    }

    @Override
    public void tick() {
        handler.getMouseManager().setUimanager(uiManager);
        uiManager.tick();
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
            State.setState(handler.getGame().gameState);
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(Images.WarioWins,0,0,handler.getWidth(),handler.getHeight(),null);
        uiManager.Render(g);
    }
}
