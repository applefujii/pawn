package com.apple.pawn;

import com.badlogic.gdx.utils.Array;

//※ ポップアップの制御に必要だが、とりあえず必要なし
public class UIPartsPopupManager {

    private Array<UIPartsPopup> aPopup;
    private int max = 3;
    private int x, y;

    public UIPartsPopupManager(int x, int y) {
        this.x = x;
        this.y = y;
        aPopup = new Array<>();
    }

}
