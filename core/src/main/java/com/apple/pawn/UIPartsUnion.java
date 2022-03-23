package com.apple.pawn;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

// ※実装するか迷い中
public class UIPartsUnion {

    private Array<UIParts> uiParts;

    public UIPartsUnion() {
        uiParts = new Array<UIParts>();
    }

    public int update() {
        for(UIParts ui : uiParts) {
            int ret = ui.update();
            if( ret != -1 ) {
                uiParts.removeValue(ui, false);
                return ret;
            }
        }
        return -1;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font) {
        for(UIParts pa : uiParts) {
            pa.draw(batch,renderer,font);
        }
    }

    public void dispose () {
    }
}
