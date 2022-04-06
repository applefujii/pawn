package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class UIPartsAchievementView extends UIParts{

    private static final int UNIT_HEIGHT = 60;
    private static final int POS_X[] = {0,300,740,880};
    private static final String DB_URL = "jdbc:sqlite:save\\achievement.sqlite3";
    private static final String DRIVE_NAME = "org.sqlite.JDBC";

    private int count;
    private String[][] result;

    public UIPartsAchievementView(String name, int x, int y, int width, int height) {
        super(name,x,y,width,height);

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
        return 0;
    }

    public void draw (Batch batch, ShapeRenderer renderer, BitmapFont font){
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(0.4f,0.4f,0.4f,1);
        renderer.box(x,y,0,width,height,0);
        renderer.setColor(0.6f,0.6f,0.6f,1);
        renderer.box(x+30,y+80,0,width-60,UNIT_HEIGHT*count,0);
        renderer.setColor(0.7f,0.6f,0.6f,1);
        for(int i=0 ; i<count ; i+=2) {
            renderer.box(x+30,y+80+UNIT_HEIGHT*i,0,width-60,UNIT_HEIGHT,0);
        }
        renderer.end();

        batch.begin();
        font.getData().setScale(2.0f);
        font.draw(batch, "実績一覧", x+50, y+25);
        font.getData().setScale(1.0f);
        for(int i=0 ; i<count ; i++) {
            for(int j=0 ; j<4 ; j++) {
                font.draw(batch, result[i][j], 100+POS_X[j], y+80+23+UNIT_HEIGHT*i);
            }
        }
        batch.end();
    }

    public void dispose (){

    }

}
