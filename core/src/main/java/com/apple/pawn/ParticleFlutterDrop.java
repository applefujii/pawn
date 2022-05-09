package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class ParticleFlutterDrop implements Particle{

    private Vector2 pos, wh, defaultWh, speed, addSpeed, maxSpeed;
    private float life, defaultLife, normalSwitchRate, slowSwitchRate, horizonSwitchRate;
    private boolean isDownSlow, isRight;
    private Color color;

    ParticleFlutterDrop() {
        pos = new Vector2(MathUtils.random(-100f,(float)Pawn.LOGICAL_WIDTH)+100,0);
        wh = new Vector2(MathUtils.random(1,8),MathUtils.random(1,8));
        defaultWh = wh.cpy();
        speed = new Vector2(MathUtils.random(-0.005f,0.005f),MathUtils.random(1.0f,4.0f));
        addSpeed = new Vector2(MathUtils.random(-0.02f,0.002f),MathUtils.random(0.0f,0.02f));
        maxSpeed = new Vector2(3.0f,4.0f);
        life = MathUtils.random(260,420);
        defaultLife = life;
        horizonSwitchRate = 1.0f/30;
        normalSwitchRate = 1.0f/20;
        slowSwitchRate = 1.0f/60;
        isDownSlow = MathUtils.randomBoolean();
        isRight = MathUtils.randomBoolean();
        color = new Color(MathUtils.random(0.0f,1.0f),MathUtils.random(0.0f,1.0f),MathUtils.random(0.0f,1.0f),1);
    }

    @Override
    public boolean update() {
        //-- ゆっくり、左右切り替え
        if((int)(MathUtils.random() * (1/horizonSwitchRate)) == 0) isRight = !isRight;
        if(isDownSlow) {
            if((int)(MathUtils.random() * (1/normalSwitchRate)) == 0) isDownSlow = false;
        } else {
            if((int)(MathUtils.random() * (1/slowSwitchRate)) == 0) isDownSlow = true;
        }
        //-- 切り替え時の加速度調整
        if(isRight) {
            if(addSpeed.x < 0) addSpeed.x += 0.02f;
            else addSpeed.x += 0.002f;
        }
        else {
            if(addSpeed.x > 0) addSpeed.x -= 0.02f;
            else addSpeed.x -= 0.002f;
        }
        if(isDownSlow) {
            if(addSpeed.y > 0) addSpeed.y -= 0.01f;
            else addSpeed.y -= 0.002f;
        }
        else {
            if(addSpeed.y < 0) addSpeed.y += 0.01f;
            else addSpeed.y += 0.002f;
        }

        speed.add(addSpeed);
        //-- 最大速度制限
        if(speed.x > maxSpeed.x) speed.x = maxSpeed.x;
        if(speed.x < -maxSpeed.x) speed.x = -maxSpeed.x;
        if(speed.y < 2.0f) speed.y = 2.0f;
        if(speed.y > maxSpeed.y) speed.y = maxSpeed.y;
        // 移動
        pos.add(speed);
        life--;
        //-- lifeに応じて大きさ変更
        int distance = (int)(defaultLife/defaultWh.x);
        for(int i=(int)defaultWh.x ; i>=1 ; i--) {
            if (life < distance*i) if (wh.x > i) wh.x = i;
        }
        distance = (int)(defaultLife/defaultWh.y);
        for(int i=(int)defaultWh.y ; i>=1 ; i--) {
            if (life < distance*i) if (wh.y > i) wh.y = i;
        }
        if(life <= 0) return true;
        return false;
    }

    @Override
    public void draw(Batch batch, ShapeRenderer renderer) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(color);
        renderer.rect(pos.x, pos.y, wh.x, wh.y);
        renderer.end();
    }

    @Override
    public void dispose() {

    }

}
