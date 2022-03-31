package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TaskSquare extends EventSquare {
    public TaskSquare(Vector2 coo, int type, int count, String document) {
        this(coo, type, count, document, MathUtils.random(1, 6));

    }

    public TaskSquare(Vector2 coo, int type, int count, String document, int move) {
        this(coo, type, count, document, move, MathUtils.random(1, 6));
    }

    public TaskSquare(Vector2 coo, int type, int count, String document, int move, int back) {
        super(coo, type, count, move);
        this.back = back;
        this.document = document;
    }

    @Override
    public void initialize(TextureAtlas atlas, int size) {
        super.initialize(atlas, size);
        back = Math.min(back, count);
        document = document + "\n成功で" + move + "マス進む\n失敗で" + back + "マス戻る";
    }

    //確認用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.draw(batch, "back:" + back, position.x+64, position.y+192);
    }
}
