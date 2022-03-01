package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.EnumSet;

/**
 * フラグ管理クラス
 * @author 藤井淳一
 */
public class FlagManagement {
    private static EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);

    public FlagManagement() {
//        Gdx.app.debug("info", "flag->str="+this.Encode());
    }

    /**
     * フラグを立てる
     * @param flag 立てるフラグ
     */
    public static void set(Flag flag) {
        if(flag.group != -1) {
            for(Flag f : flags) {
                if(f.group == flag.group) fold(f);
            }
        }
        flags.add(flag);
    }

    /**
     * フラグを立てる
     * @param flag 立てるフラグ
     */
    public static void set(Flag... flag) {
        for(Flag f : flag) set(f);
    }

    /**
     * フラグを折る
     * @param flag 折るフラグ
     */
    public static void fold(Flag flag) {
        flags.remove(flag);
    }

    /**
     * フラグを折る
     * @param flag 折るフラグ
     */
    public static void fold(Flag... flag) {
        for(Flag f : flag) flags.remove(f);
    }

    /**
     * フラグの状態を取得
     * @param flag 確認するフラグ
     * @return boolean フラグの状態
     */
    public static boolean is(Flag flag) {
        return flags.contains(flag);
    }

    /**
     * フラグが全て立っているか
     * @param flag 確認するフラグ
     * @return boolean 全て立っているか
     */
    public static boolean isAllSet(Flag... flag) {
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
    public static boolean isAllFold(Flag... flag) {
        boolean allFold = true;
        for(Flag f : flag){
            if(flags.contains(f)) allFold = false;
        }
        return allFold;
    }

    /**
     * フラグをLong[]で返す
     * @return Long[] 数値化されたフラグ
     */
    public static Long[] Encode() {
        Array<Long> bits = new Array<Long>();
        Long l = 0L;
        int dir = 0;
        for (Flag val : flags) {
            if( dir < (int) (Math.floor(val.no / 64)) ) {
                dir = (int) (Math.floor(val.no / 64));
                bits.add(l);
                l = 0L;
            }
            if(val.isSave) {
                l |= 1L << val.no%64;
//                Gdx.app.debug("info", "save="+String.format("%016x", l));
            }
        }
        bits.add(l);

        //-- 文字列化
        String ret = "";
        Array.ArrayIterator<Long> fi = bits.iterator();
        while( fi.hasNext() ) {
            long lo = fi.next();
            ret += String.format("%016x", lo);
        }
        Gdx.app.debug("info", "flag="+ret);

        Gdx.app.debug("info", "flag="+bits.toArray(Long.class)[0]);
        return bits.toArray(Long.class);
    }

    private static void Decode(Long... data) {
        flags.clear();
        int i = 0;
        for(Long d : data) {
            for(int n=0 ; n>=64 ; n++) {
                Long a = d & 1L<<(n+i*64);
                if(a != 0) {
                    FlagManagement.set(Flag.searchNo(a));
                }
            }
            i++;
        }
    }

}
