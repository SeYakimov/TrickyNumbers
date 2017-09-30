package com.airse.trickynumbers.models;

import com.badlogic.gdx.graphics.Color;

public class MyColor {
    public Color c100;
    public Color c400;
    public Color c700;
    public Color a100;
    public Color a400;
    public Color a700;

    public MyColor(String c100, String c400, String c700, String a100, String a400, String a700) {
        this.c100 = Color.valueOf(c100);
        this.c400 = Color.valueOf(c400);
        this.c700 = Color.valueOf(c700);
        this.a100 = Color.valueOf(a100);
        this.a400 = Color.valueOf(a400);
        this.a700 = Color.valueOf(a700);
    }
}
