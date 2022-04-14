package com.apple.pawn;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

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
    private AssetManager manager;
    private UI ui;

    public Achievement(float time) {
        this.time = time;
    }

    public void initialize(AssetManager manager, UI ui) {
        this.manager = manager;
        this.ui = ui;
        try {
            Class.forName(DRIVE_NAME);
            connection = DriverManager.getConnection(DB_URL);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(float time, Player player) {
        float diff = time - this.time;
        this.time = time;
        try {
            Class.forName(DRIVE_NAME);

            stPlayData = connection.prepareStatement("select * from play_data");
            stUpdatePlayData = connection.prepareStatement("update play_data set total_time = ?, total_turn = ? where id = 1");
            stAchievement = connection.prepareStatement("select * from achievement");
            stUpdateAchievement = connection.prepareStatement("update achievement set state = 1, timestamp = ? where id = ?");

            ResultSet rs = stPlayData.executeQuery();
            while (rs.next()) {
                BigDecimal updateTotalTime = rs.getBigDecimal("total_time");
                int updateTotalTurn = rs.getInt("total_turn");
                updateTotalTime = updateTotalTime.add(new BigDecimal(diff));
                updateTotalTurn += 1;
                stUpdatePlayData.setBigDecimal(1, updateTotalTime);
                stUpdatePlayData.setInt(2, updateTotalTurn);
                int st = stUpdatePlayData.executeUpdate();
//                Gdx.app.debug("achievement", "Update" + st);
//                Gdx.app.debug("achievement", "total_time=" + updateTotalTime);
//                Gdx.app.debug("achievement", "total_turn=" + updateTotalTurn);

                //-- 実績を達成したか確認
                Timestamp ts = new Timestamp(System.currentTimeMillis());
                ResultSet rs2 = stAchievement.executeQuery();
                while (rs2.next()) {
                    // 取得済みはスルー
                    if(rs2.getInt("state") == 1) continue;
                    int id = rs2.getInt("id");
                    boolean isGet = false;
                    int[] ret;
                    switch(id) {
                        case 1:
                            if(updateTotalTime.compareTo(new BigDecimal(60*10)) == 1) isGet = true;
                            break;
                        case 2:
                            if(updateTotalTime.compareTo(new BigDecimal(60*30)) == 1) isGet = true;
                            break;
                        case 3:
                            if(updateTotalTime.compareTo(new BigDecimal(60*60*1)) == 1) isGet = true;
                            break;
                        case 4:
                            if(updateTotalTime.compareTo(new BigDecimal(60*60*3)) == 1) isGet = true;
                            break;
                        case 5:
                            if(updateTotalTime.compareTo(new BigDecimal(60*60*5)) == 1) isGet = true;
                            break;
                        case 6:
                            if(updateTotalTime.compareTo(new BigDecimal(60*60*10)) == 1) isGet = true;
                            break;
                        case 7:
                            if(updateTotalTurn >= 1) isGet = true;
                            break;
                        case 8:
                            if(updateTotalTurn >= 10) isGet = true;
                            break;
                        case 9:
                            if(updateTotalTurn >= 25) isGet = true;
                            break;
                        case 10:
                            if(updateTotalTurn >= 50) isGet = true;
                            break;
                        case 11:
                            if(updateTotalTurn >= 100) isGet = true;
                            break;
                        case 12:
                            if(updateTotalTurn >= 300) isGet = true;
                            break;
                        case 13:
                            if(updateTotalTurn >= 1000) isGet = true;
                            break;
                        case 14:
                            ret = dicedSerialCheck(player.getaDiceNo());
                            if(ret[0] == 6  &&  ret[1] == 2) isGet = true;
                            break;
                        case 15:
                            ret = dicedSerialCheck(player.getaDiceNo());
                            if(ret[0] == 6  &&  ret[1] == 3) isGet = true;
                            break;
                        case 16:
                            ret = dicedSerialCheck(player.getaDiceNo());
                            if(ret[0] == 1  &&  ret[1] == 2) isGet = true;
                            break;
                    }
                    if(isGet) {
                        ui.add(new UIPartsPopup("achievement", manager, 50, 50, 300, 100, rs2.getString("title") + "\n\n" + rs2.getString("detail"), 10));
                        stUpdateAchievement.setTimestamp(1, ts);
                        stUpdateAchievement.setInt(2, id);
                        stUpdateAchievement.executeUpdate();
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
        }
    }

    public void dispose() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同じ目が何回連続で出たか返す
     * @param aDicedNo
     * @return [0]何の目が [1]何回
     */
    private int[] dicedSerialCheck(Array<Integer> aDicedNo) {
        Iterator<Integer> ite = new Array.ArrayIterator<>(aDicedNo);
        int count = 1, dd = -1;
        while(ite.hasNext()) {
            int d = ite.next();
            if(dd == -1) dd = d;
            else if(dd == d) count++;
            else break;
        }
        return new int[]{dd, count};
    }

}
