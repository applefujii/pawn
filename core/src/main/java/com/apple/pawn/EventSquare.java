package com.apple.pawn;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EventSquare extends Square {
    public static Array<String> DOCUMENTS;

    private final String document;
    private int move;

    static {
        DOCUMENTS = new Array<>();
        DOCUMENTS.add("あああああ", "いいいいい", "ううううう", "えええええ");
        DOCUMENTS.add("おおおおお", "かかかかか", "ききききき", "くくくくく");
        DOCUMENTS.add("けけけけけ", "こここここ", "さささささ", "ししししし");
        DOCUMENTS.add("すすすすす", "せせせせせ", "そそそそそ", "たたたたた");
        DOCUMENTS.add("ちちちちち", "つつつつつ", "ててててて", "ととととと");
        DOCUMENTS.add("ななななな", "ににににに", "ぬぬぬぬぬ", "ねねねねね");
        DOCUMENTS.add("ののののの", "ははははは", "ひひひひひ", "ふふふふふ");
        DOCUMENTS.add("へへへへへ", "ほほほほほ", "ままままま", "みみみみみ");
        DOCUMENTS.add("むむむむむ", "めめめめめ", "ももももも", "ややややや");
        DOCUMENTS.add("ゆゆゆゆゆ", "よよよよよ", "ららららら", "りりりりり");
        DOCUMENTS.add("るるるるる", "れれれれれ", "ろろろろろ", "わわわわわ");
        DOCUMENTS.add("ををををを", "んんんんん");
    }

    public EventSquare(int x, int y, int type, int count, TextureAtlas atlas) {
        super(x, y, type, count, atlas);
        move = MathUtils.random(1, 6);
        if(move > BoardSurface.SQUARE_COUNT - this.count) move = BoardSurface.SQUARE_COUNT - this.count;
        int index = MathUtils.random(0, DOCUMENTS.size - 1);
        document = DOCUMENTS.get(index);
        DOCUMENTS.removeIndex(index);
    }

    @Override
    public void update(Player turnPlayer) {
        if(FlagManagement.is(Flag.INPUT_ENABLE)) {
            if (type == 1) turnPlayer.goal();
            else if (FlagManagement.is(Flag.DICE_MOVE)) {
                turnPlayer.getPiece().setMove(move);
                FlagManagement.fold(Flag.DICE_MOVE);
            }
        }
    }
}
