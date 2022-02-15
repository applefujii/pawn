package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.EnumSet;

/**
 * フラグ管理クラス
 * @author 藤井淳一
 */
public class FlagManagement {
    EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);

    public FlagManagement() {
        Gdx.app.debug("info", "flag->str="+this.Encode());
    }

    /**
     * フラグを立てる
     * @param flag 立てるフラグ
     */
    public void set(Flag flag) {
        flags.add(flag);
    }

    /**
     * フラグを折る
     * @param flag 折るフラグ
     */
    public void fold(Flag flag) {
        flags.remove(flag);
    }

    /**
     * フラグの状態を取得
     * @param flag 確認するフラグ
     * @return boolean フラグの状態
     */
    public boolean is(Flag flag) {
        return flags.contains(flag);
    }

    /**
     * フラグが全て立っているか
     * @param flag 確認するフラグ
     * @return boolean 全て立っているか
     */
    public boolean isAllSet(Flag... flag) {
        boolean allSet = true;
        for(Flag f : flag){
            if(!flags.contains(f)) allSet = false;
        }
        return allSet;
    }

    /**
     * フラグが全て折れているか
     * @param flag 確認するフラグ
     * @return boolean 全て折れているか
     */
    public boolean isAllFold(Flag... flag) {
        boolean allFold = true;
        for(Flag f : flag){
            if(flags.contains(f)) allFold = false;
        }
        return allFold;
    }

    /**
     * フラグを文字列に変換
     * @return String 16進数で変換した文字列
     */
    public String Encode() {
        String ret = "";

        Array<Long> bits = new Array<Long>();
        Long l = 0L;
        int dir = 0;
        for (Flag val : flags) {
            if( dir < (int) (Math.floor(val.no / 64)) ) {
                dir = (int) (Math.floor(val.no / 64));
                bits.insert(0,l);
                l = 0L;
            }
            if(val.isSave) {
                l |= 1L << val.no%64;
//                Gdx.app.debug("info", "save="+String.format("%016x", l));
            }
        }
        bits.insert(0,l);

        //-- 文字列化
        Array.ArrayIterator<Long> fi = bits.iterator();
        while( fi.hasNext() ) {
            long lo = fi.next();
            ret += String.format("%016x", lo);
        }

        return ret;
    }

    /**
     * ※未完成 文字列から読み取る
     * @param str 変換する文字列
     */
    private void Decode(String str) {
        try {
            int cnt = str.length() / 16;
            for (int i = 0; i < cnt; i++) {
                str.substring(i * 16, i * 16 + 15);
            }
        } catch(Exception e) {
            Gdx.app.debug("error", "セーブデータの読み込みに失敗しました。");
        }
    }

}
