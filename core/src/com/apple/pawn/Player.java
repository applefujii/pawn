package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Player extends Actor {
    private Texture img;
    private Rectangle rect;

    public Player() {
        rect = new Rectangle(200,200,256,256);
        img = new Texture("badlogic.jpg");
    }

    public void move( Vector3 pos ) {
        this.rect.x = pos.x - (rect.width/2);
        this.rect.y = pos.y - (rect.height/2);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * 描画
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw (Batch batch, float parentAlpha) {
        Sprite sprite = new Sprite( new TextureRegion(img));
        sprite.setSize(256, 256);
        sprite.setRegion(100,100,100,100);
        sprite.setPosition(rect.x, rect.y);
        sprite.flip(false, true);       // 上下反転 setRegionより後に
        sprite.draw(batch);
    }

    public void dispose () {
        img.dispose();
    }

    public Rectangle getRect() {
        return rect;
    }
}
