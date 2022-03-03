package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TaskSquare extends EventSquare {
    protected int back;
    protected Sprite subSprite;

    public TaskSquare(Vector2 pos, int type, int count, String document, TextureAtlas atlas) {
        super(pos, type, count, document, atlas);
        subSprite = atlas.createSprite("normal");
        subSprite.flip(false, true);
        back = MathUtils.random(1, 6);
        if(this.count < back) back = this.count;
    }

    //識別用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        subSprite.setSize(128, 128);
        subSprite.setPosition(pos.x*256+64, pos.y*256+64);
        subSprite.draw(batch);
    }
}
