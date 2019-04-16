package Game.GameStates;

import Display.UI.UIPointer;
import Game.Entities.DynamicEntities.BaseDynamicEntity;
import Game.Entities.StaticEntities.BaseStaticEntity;
import Game.World.MapBuilder;
import Main.Handler;

import java.awt.*;
import java.awt.event.KeyEvent;

import Display.UI.UIListener;

/**
 * Created by AlexVR on 7/1/2018.
 */
public class GameState extends State {

    public GameState(Handler handler){
        super(handler);
        handler.getGame().pointer = new UIPointer(28 * MapBuilder.pixelMultiplier,197 * MapBuilder.pixelMultiplier,128,128,handler);

    }

    @Override
    public void tick() {
        if(handler.getKeyManager().keyJustPressed(KeyEvent.VK_ESCAPE)){
            State.setState(handler.getGame().pauseState);
        }
        if(PlayerState.player2Activate) {
        	if(handler.getMario().getHit()|| handler.getWario().getWin()){
               State.setState(handler.getGame().wariowin); //wario wins state
        		//System.out.println("wario wins");
            }
        	if(handler.getWario().getHit() || handler.getMario().getWin() ){
            State.setState(handler.getGame().mariowin);// Mario wins state
        		//System.out.println("Mario wins!");
            }
        	
        }
        if(!PlayerState.player2Activate && handler.getMario().getHit()){
            State.setState(handler.getGame().deathState);
        }
        
        handler.getMario().tick();
        
        if (PlayerState.player2Activate) {
			handler.getWario().tick();
		}
        if(handler.getMap().getListener() != null && MapBuilder.mapDone) {
        	handler.getMap().getListener().tick();
        	handler.getMap().getHand().tick();
        	handler.getMap().getWalls().tick();
        }
        for (BaseDynamicEntity entity:handler.getMap().getEnemiesOnMap()) {
            entity.tick();
        }
    }

    @Override
    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        handler.getMap().drawMap(g2);
        
    }

}
