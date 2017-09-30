package com.airse.trickynumbers.models;

import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.Random;

/**
 * Created by qwert on 30.09.2017.
 */

public class RColor {

    public static MyColor red = new MyColor("ffcdd2", "ef5350", "d32f2f", "ff8a80", "ff17440", "d50000");
    public static MyColor pink = new MyColor("f8bbd0", "ec407a", "c2185b", "ff80ab", "f50057", "c51162");
    public static MyColor purple = new MyColor("e1bee7", "ab47bc", "7b1fa2", "ea80fc", "d500f9", "aa00ff");
    public static MyColor indigo = new MyColor("c5cae9", "5c6bc0", "303f9f", "8c9eff", "3d5afe", "304ffe");
    public static MyColor light_blue = new MyColor("b3e5fc", "29b6f6", "0288d1", "80d8ff", "00b0ff", "0091ea");
    public static MyColor cyan = new MyColor("b2ebf2", "26c6da", "0097a7", "84ffff", "00e5ff", "00b8d4");
    public static MyColor teal = new MyColor("b2dfdb", "26a69a", "00796b", "a7ffeb", "1de9b6", "00bfa5");
    //public static MyColor light_green = new MyColor("dcedc8", "9ccc65", "689f38", "ccff90", "76ff03", "64dd17");
    //public static MyColor lime = new MyColor("f0f4c3", "d4e157", "afb42b", "f4ff81", "c6ff00", "aeea00"); // ???
    public static MyColor yellow = new MyColor("fff9c4", "ffee58", "fbc02d", "ffff8d", "ffea00", "ffd600");
    public static MyColor amber = new MyColor("ffecb3", "ffca28", "ffa000", "ffe57f", "ffc400", "ffab00");
    public static MyColor orange = new MyColor("ffe0b2", "ffa726", "f57c00", "ffd180", "ff9100", "ff6d00");

    public static MyColor getColor(){
        Array<MyColor> colors = new Array<MyColor>();
        colors.addAll(red, pink, purple, indigo, light_blue, cyan, teal, yellow, amber, orange);
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size));
    }
}
