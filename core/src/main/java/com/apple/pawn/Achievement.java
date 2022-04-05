package com.apple.pawn;

import java.math.BigDecimal;
import java.sql.*;
import com.badlogic.gdx.Gdx;

public class Achievement {

    private static final String DB_URL = "jdbc:sqlite:save\\achievement.sqlite3";
    private static final String DRIVE_NAME = "org.sqlite.JDBC";

    private Connection connection = null;
    private PreparedStatement stPlayData = null;
    private PreparedStatement stUpdatePlayData = null;
    private PreparedStatement stAchievement = null;
    private PreparedStatement stUpdateAchievement = null;

    private float time;

    //-- 参照
    private UI ui;

    public Achievement(float time) {
        this.time = time;
    }

    public void initialize(UI ui) {
        this.ui = ui;
    }

    public void update(float time) {
        float diff = time - this.time;
        this.time = time;
        try {
            Class.forName(DRIVE_NAME);

            Connection connection = DriverManager.getConnection(DB_URL);
            stPlayData = connection.prepareStatement("select * from play_data");
            stUpdatePlayData = connection.prepareStatement("update play_data set total_time = ?, total_turn = ? where id = 1");
            stAchievement = connection.prepareStatement("select * from achievement");
            stUpdateAchievement = connection.prepareStatement("update achievement set state = 1 where id = ?");

            ResultSet rs = stPlayData.executeQuery();
            while (rs.next()) {
                BigDecimal updateTotalTime = rs.getBigDecimal("total_time");
                int updateTotalTurn = rs.getInt("total_turn");
                updateTotalTime = updateTotalTime.add(new BigDecimal(diff));
                updateTotalTurn += 1;
                stUpdatePlayData.setBigDecimal(1, updateTotalTime);
                stUpdatePlayData.setInt(2, updateTotalTurn);
                int st = stUpdatePlayData.executeUpdate();
                Gdx.app.debug("achievement", "Update" + st);
                Gdx.app.debug("achievement", "total_time=" + updateTotalTime);
                Gdx.app.debug("achievement", "total_turn=" + updateTotalTurn);

                //-- 実績を達成したか確認
                ResultSet rs2 = stAchievement.executeQuery();
                while (rs2.next()) {
                    // 取得済みはスルー
                    if(rs2.getInt("state") == 1) continue;
                    int id = rs2.getInt("id");
                    Gdx.app.debug("achievement", "id = " + id);
                    switch(id) {
                        case 1:
                            if(updateTotalTime.compareTo(new BigDecimal(60*60)) == 1) {
                                Gdx.app.debug("achievement", "get id = " + id);
                                ui.add(new UIPartsPopup("achievement", 50,50,300,100, rs2.getString("title")+"\n"+rs2.getString("detail"), 10));
                                stUpdateAchievement.setInt(1, id);
                                stUpdateAchievement.executeUpdate();
                            }
                            break;
                        case 2:
                            if(updateTotalTurn >= 1) {
                                Gdx.app.debug("achievement", "get id = " + id);
                                ui.add(new UIPartsPopup("achievement", 50,50,300,100, rs2.getString("title")+"\n"+rs2.getString("detail"), 10));
                                stUpdateAchievement.setInt(1, id);
                                stUpdateAchievement.executeUpdate();
                            }
                            break;
                        case 3:
                            if(updateTotalTurn >= 10) {
                                Gdx.app.debug("achievement", "get id = " + id);
                                ui.add(new UIPartsPopup("achievement", 50,50,300,100, rs2.getString("title")+"\n"+rs2.getString("detail"), 10));
                                stUpdateAchievement.setInt(1, id);
                                stUpdateAchievement.executeUpdate();
                            }
                            break;
                    }
                }
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (stPlayData != null) {
                    stPlayData.close();
                }
                if (stUpdatePlayData != null) {
                    stUpdatePlayData.close();
                }
                if (stAchievement != null) {
                    stAchievement.close();
                }
                if (stUpdateAchievement != null) {
                    stUpdateAchievement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
