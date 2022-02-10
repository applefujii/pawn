package com.apple.pawn;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.EnumSet;

public enum Flag {
    //---- フラグの定義
    PLAY(0, false, "実行中(ポーズされていない)"),
    UI_INPUT_ENABLE(1, true, "UIを操作可能か"),
    INPUT_ENABLE(2, true, "UI以外を操作可能か"),
    TURN_STANDBY(10, true, "ターン開始フェーズ"),
    ACTION_SELECT(11, true, "行動選択フェーズ"),
    DICE_ROLL(12, true, "さいころを振るフェーズ"),
    PIECE_ADVANCE(13, true, "駒を進めるフェーズ");

/* テスト用
    b(6, true, ""),
    c(7, true, ""),
    d(8, true, ""),
    e(9, true, ""),
    f(10, true, ""),
    g(11, true, ""),
    h(12, true, ""),
    i(13, true, ""),
    j(14, true, ""),
    k(15, true, ""),
    l(16, true, ""),
    m(17, true, ""),
    n(18, true, ""),
    o(19, true, ""),
    p(20, true, ""),
    q(21, true, ""),
    r(22, true, ""),
    s(23, true, ""),
    t(24, true, ""),
    u(25, true, ""),
    v(26, true, ""),
    w(27, true, ""),
    x(28, true, ""),
    y(29, true, ""),
    z(30, true, ""),
    aa(31, true, ""),
    bb(32, true, ""),
    cc(33, true, ""),
    dd(34, true, ""),
    ee(35, true, ""),
    ff(36, true, ""),
    gg(37, true, ""),
    hh(38, true, ""),
    ii(39, true, ""),
    jj(40, true, ""),
    kk(41, true, ""),
    ll(42, true, ""),
    mm(43, true, ""),
    nn(44, true, ""),
    oo(45, true, ""),
    pp(46, true, ""),
    qq(47, true, ""),
    rr(48, true, ""),
    ss(49, true, ""),
    tt(50, true, ""),
    uu(51, true, ""),
    vv(52, true, ""),
    ww(53, true, ""),
    xx(54, true, ""),
    yy(55, true, ""),
    zz(56, true, ""),
    aaa(57, true, ""),
    bbb(58, true, ""),
    ccc(59, true, ""),
    ddd(60, true, ""),
    eee(61, true, ""),
    fff(62, true, ""),
    ggg(63, true, ""),
    hhh(64, true, ""),
    iii(65, true, ""),
    jjj(66, true, ""),
    kkk(67, true, ""),
    lll(68, true, ""),
    mmm(69, true, ""),
    nnn(70, true, ""),
    ooo(71, true, ""),
    ppp(72, true, ""),
    qqq(73, true, ""),
    rrr(74, true, ""),
    sss(75, true, ""),
    ttt(76, true, ""),
    uuu(77, true, ""),
    vvv(78, true, ""),
    www(79, true, ""),
    xxx(80, true, ""),
    yyy(81, true, ""),
    zzz(82, true, "");
*/


    public final int no;            // フラグ番号
    public final boolean isSave;    // セーブするか
    public final String cry;        // 説明

    Flag(final int no, final boolean isSave, final String cry) {
        this.no = no;
        this.isSave = isSave;
        this.cry = cry;
    }

}
