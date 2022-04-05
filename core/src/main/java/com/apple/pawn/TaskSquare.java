package com.apple.pawn;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class TaskSquare extends EventSquare {

    private final String doc;

    public TaskSquare(Vector2 coo, int type, int count, String document, int move, int back) {
        super(coo, type, count, move);
        this.back = back;
        doc = document;
    }

    @Override
    public void initialize(AssetManager manager, int size) {
        super.initialize(manager, size);
        back = Math.min(back, count);
        document = doc + "\n成功で" + move + "マス進む\n失敗で" + back + "マス戻る";
    }

    //確認用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.draw(batch, "back:" + back, position.x+64, position.y+192);
    }
}
