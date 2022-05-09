package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * パーティクル管理クラス
 * @author fujii
 */
public class ParticleManager {

    private final Array<Particle> aParticle;
    private float dpsFlutterDrop;
    private boolean isFlutterDrop;
    private float preTimer = 0, preFlutterDropTimer = 0, surplus = 0;

    ParticleManager() {
        aParticle = new Array<>();
    }

    /**
     * 動かす
     * @param timer 現在のゲームタイマー
     */
    public void update(float timer) {
        generateParticleFlutterDrop(timer);

        Iterator<Particle> ite = aParticle.iterator();
        while(ite.hasNext()) {
            Particle p = ite.next();
            if( p.update() ) {
                ite.remove();
            }
        }
        preTimer = timer;
    }

    /**
     * 描画
     * @param batch
     * @param renderer
     */
    public void draw(Batch batch, ShapeRenderer renderer) {
        for(Particle p : aParticle) {
            p.draw(batch, renderer);
        }
    }

    public void dispose() {
        for(Particle p : aParticle) {
            p.dispose();
        }
    }

    /**
     * パーティクルを生み出す
     * @param timer 現在のゲームタイマー
     */
    private void generateParticleFlutterDrop(float timer) {
        if(isFlutterDrop) {
//            if(timer >= preFlutterDropTimer+(1/dpsFlutterDrop)) {
            int n = (int)((timer-preFlutterDropTimer)/(1/dpsFlutterDrop)+surplus);
            if( n > 10 ) n = 10;
            if( timer == 0 ) n = 0;
            surplus += ((timer-preFlutterDropTimer)%(1/dpsFlutterDrop));
            surplus %= 1.0f;
            for(int i=0 ; i<n ; i++) {
                aParticle.add(new ParticleFlutterDrop());
            }
            preFlutterDropTimer = timer;
//            }
        }
    }

    public void addParticle(Particle par) {
        aParticle.add(par);
    }

    /**
     * パーティクル生産を開始
     * @param dps 1秒間に何個生み出すか
     */
    public void startParticle(int dps) {
        dpsFlutterDrop = dps;
        isFlutterDrop = true;
    }

    /**
     * パーティクル生産を止める
     */
    public void stopParticle() {
        isFlutterDrop = false;
    }

    /**
     * パーティクルをnフレーム分動かす
     * @param n 動かすフレーム数
     */
    public void skipFrame(int n) {
        float dpf = dpsFlutterDrop/60;
        float m = 0;
        for(int i=0 ; i<n ; i++) {
            for(int j=0 ; j<(int)(dpf+m)-(int)m ; j++) aParticle.add(new ParticleFlutterDrop());
            m += dpf;
            Iterator<Particle> ite = aParticle.iterator();
            while(ite.hasNext()) {
                Particle p = ite.next();
                if( p.update() ) {
                    ite.remove();
                }
            }
        }
    }

}
