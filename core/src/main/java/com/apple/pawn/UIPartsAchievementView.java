package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UIPartsAchievementView extends UIParts{

    private static final int UNIT_HEIGHT = 60;
    private static final int POS_X[] = {40,300,840,960};
    private static final String DB_URL = "jdbc:sqlite:save\\achievement.sqlite3";
    private static final String DRIVE_NAME = "org.sqlite.JDBC";

    private OrthographicCamera camera;		// カメラ

    private int count;
    private String[][] result;

    public UIPartsAchievementView(String name, int x, int y, int width, int height) {
        super(name,x,y,width,height);

        camera = new OrthographicCamera();
        camera.setToOrtho(true, width, height);

        //---- データベースから取得
        Connection connection = null;
        PreparedStatement stAchievementCount = null;
        PreparedStatement stAchievement = null;
        try {
            Class.forName(DRIVE_NAME);
            connection = DriverManager.getConnection(DB_URL);
            stAchievementCount = connection.prepareStatement("select count(*) from achievement");
            stAchievement = connection.prepareStatement("select * from achievement");

            ResultSet rs;
            rs = stAchievementCount.executeQuery();
            count = rs.getInt(1);
            result = new String[count][4];
            rs = stAchievement.executeQuery();
            int i = 0;
            while (rs.next()) {
                result[i][0] = rs.getString("title");
                result[i][1] = rs.getString("detail");
                if(rs.getInt("state") == 0) {
                    result[i][2] = "";
                    result[i][3] = "";
                } else {
                    result[i][2] = "取得済み";
                    result[i][3] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Timestamp(Long.valueOf(rs.getString("timestamp"))));
                }
                i++;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stAchievement != null) {
                    stAchievement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public int update(){
        //---- スクロール
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            camera.translate(0,-8.0f);
            int po = height/2;
            if(camera.position.y < po)
                camera.position.y = po;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            camera.translate(0,8.0f);
            int po = (80+UNIT_HEIGHT*count+10) - (height/2);
            if(camera.position.y > po)
                camera.position.y = po;
        }
        return 0;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font){
        //-- 描画範囲を変える
        GL20 gl = Gdx.graphics.getGL20();
        gl.glViewport(x, y, width, height);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        renderer.setProjectionMatrix(camera.combined);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.4f,0.4f,0.4f,1);
        renderer.box(0,0,0,width,80+UNIT_HEIGHT*count+10,0);
        renderer.setColor(0.6f,0.6f,0.6f,1);
        renderer.box(8,80,0,width-16,UNIT_HEIGHT*count,0);
        renderer.setColor(0.7f,0.6f,0.6f,1);
        for(int i=0 ; i<count ; i+=2) {
            renderer.box(8,80+UNIT_HEIGHT*i,0,width-16,UNIT_HEIGHT,0);
        }
        renderer.end();

        batch.begin();
        font.getData().setScale(2.0f);
        font.draw(batch, "実績一覧", 50, 30);
        font.getData().setScale(1.0f);
        for(int i=0 ; i<count ; i++) {
            for(int j=0 ; j<4 ; j++) {
                font.draw(batch, result[i][j], POS_X[j], 80+24+UNIT_HEIGHT*i);
            }
        }
        batch.end();

        // 描画範囲を戻す
        gl.glViewport(0, 0, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
    }

    public void dispose (){

    }

}
