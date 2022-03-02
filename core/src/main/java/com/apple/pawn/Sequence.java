package com.apple.pawn;

/**
 * @author fujii
 */
public enum Sequence {
    TURN_STANDBY(10, "ターン開始待ち"),
    ACTION_SELECT(20, "アクションの選択"),
    DICE_ROLL(30, "サイコロを振る"),
    PIECE_ADVANCE(40, "駒を進める"),
    TASK_DO(50, "タスクの確認"),
    RESULT(60, "リザルト表示");

    public final int no;            // シークエンス番号
    public final String cry;        // 説明

    Sequence(final int no, final String cry) {
        this.no = no;
        this.cry = cry;
    }
}
