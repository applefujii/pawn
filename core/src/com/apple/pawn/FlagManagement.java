package com.apple.pawn;

import com.badlogic.gdx.Gdx;

import java.util.EnumSet;

public class FlagManagement {
    EnumSet<Flag> flags = EnumSet.noneOf(Flag.class);

    public FlagManagement() {
        Gdx.app.debug("info", "flag->str="+Flag.Encode(flags));
    }

    public void set(Flag f) {
    }

    public void fold(Flag f) {
    }

}
