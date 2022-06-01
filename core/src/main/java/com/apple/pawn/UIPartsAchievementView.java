package com.apple.pawn;

import android.support.annotation.NonNull;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 実績表示UI
 * @author fujii
 */
public class UIPartsAchievementView extends UIParts{

    private static final int UNIT_HEIGHT = 60;
    private static final int[] POS_X = {40,300,840,960};
    private static final String DB_URL = "jdbc:sqlite:save\\achievement.sqlite3";
    private static final String DRIVE_NAME = "org.sqlite.JDBC";


    private int count;
    private String[][] result;
    private float scrollY = 0;

    // 参照
    private OrthographicCamera camera;		// カメラ

    public UIPartsAchievementView(String name, int x, int y, int width, int height, int group) {
        super(name,x,y,width,height, group);

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
                    result[i][3] = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Timestamp(Long.parseLong(rs.getString("timestamp"))));
                }
                i++;
            }
        } catch (ClassNotFoundException | SQLException e) {
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

    public void initialize(OrthographicCamera camera) {
        this.camera = camera;
    }

    public int update(){
        //---- スクロール
        if (Gdx.input.isKeyPressed(Input.Keys.UP) | Gdx.input.isKeyPressed(Input.Keys.W)) {
            scrollY -= 8.0f;
            if(scrollY < 0) scrollY = 0;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) | Gdx.input.isKeyPressed(Input.Keys.S)) {
            scrollY += 8.0f;
            float po = UNIT_HEIGHT*count-UNIT_HEIGHT*9;
            if(scrollY > po) scrollY = po;
        }
        return 0;
    }

    public void draw (@NonNull SpriteBatch batch, @NonNull ShapeRenderer renderer, BitmapFont font){
        //-- 描画範囲を変える
//        GL20 gl = Gdx.graphics.getGL20();
//        gl.glViewport(x, y, width, height);
//        camera.update();
//        batch.setProjectionMatrix(camera.combined);
//        renderer.setProjectionMatrix(camera.combined);

        batch.flush();
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle(x,y,width,height);
        ScissorStack.calculateScissors(camera, batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.4f,0.4f,0.4f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.setColor(0.6f,0.6f,0.6f,1);
        float yy = y+80-scrollY;
        if(yy < y) yy = y;
        renderer.box(x+8,yy,0,width-16,height,0);
        renderer.setColor(0.7f,0.6f,0.6f,1);
        for(int i=0 ; i<count+1 ; i+=2) {
            yy = y+80-scrollY+UNIT_HEIGHT*i;
            if(yy>-100  &&  yy<= Pawn.LOGICAL_HEIGHT)
                renderer.box(x+8,y+80-scrollY+UNIT_HEIGHT*i,0,width-16,UNIT_HEIGHT,0);
        }
        renderer.end();

        batch.begin();
        font.getData().setScale(2.0f);
        if(scrollY < 100)
            font.draw(batch, "実績一覧", x+50, y+30-scrollY);
        font.getData().setScale(1.0f);
        for(int i=0 ; i<count ; i++) {
            for(int j=0 ; j<4 ; j++) {
                yy = y-scrollY+80+24+UNIT_HEIGHT*i;
                if(yy>-100  &&  yy<= Pawn.LOGICAL_HEIGHT)
                    font.draw(batch, result[i][j], x+POS_X[j], y-scrollY+80+24+UNIT_HEIGHT*i);
            }
        }
        batch.end();

        // 描画範囲を戻す
//        gl.glViewport(0, 0, Pawn.LOGICAL_WIDTH, Pawn.LOGICAL_HEIGHT);
        batch.flush();
        ScissorStack.popScissors();
    }

    public void dispose (){ }

}
