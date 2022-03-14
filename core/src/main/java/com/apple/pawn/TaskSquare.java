package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TaskSquare extends EventSquare {
//    public TaskSquare(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
    public TaskSquare(Vector2 pos, int type, int count, String document) {
//        super(pos, type, count, document, atlas);
        super(pos, type, count, document);
        back = MathUtils.random(1, 6);
        if(this.count < back) back = this.count;
    }

    //確認用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.draw(batch, "back:" + back, pos.x*256+64, pos.y*256+192);
    }
}
