package com.east71.trickynumbers.models;

import com.badlogic.gdx.graphics.Color;

public class MyColor {
    public Color light;
    public Color normal;
    public Color dark;
    public String name;

    public MyColor(String light, String normal, String dark, String name) {
        this.light = Color.valueOf(light);
        this.normal = Color.valueOf(normal);
        this.dark = Color.valueOf(dark);
        this.name = name;
    }
}
