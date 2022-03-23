package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class TaskSquare extends EventSquare {
    public TaskSquare(Vector2 pos, int type, int count, String document) {
        this(pos, type, count, document, MathUtils.random(1, 6), MathUtils.random(1, 6));

    }

    public TaskSquare(Vector2 pos, int type, int count, String document, int move, int back) {
        super(pos, type, count, move);
        this.back = Math.min(this.count, back);

        this.document = document+"\n成功で"+this.move+"マス進む\n失敗で"+this.back+"マス戻る";
    }

    //確認用の仮描画
    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        font.draw(batch, "back:" + back, pos.x*256+64, pos.y*256+192);
    }
}
