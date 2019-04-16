package Game.Entities.StaticEntities;

import Main.Handler;
import Resources.Images;

public class StarBlock extends BaseStaticEntity {

    public StarBlock(int x, int y, int width, int height, Handler handler) {
        super(x, y, width, height,handler, Images.starblock);
    }

}
