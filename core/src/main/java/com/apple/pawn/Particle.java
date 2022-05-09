package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * パーティクルの抽象クラス
 * @author fujii
 */
public interface Particle {
    // trueが返ると破棄される
    boolean update();
    void draw(Batch batch, ShapeRenderer renderer);
    void dispose();
}
