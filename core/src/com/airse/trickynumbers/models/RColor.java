package com.airse.trickynumbers.models;

import com.badlogic.gdx.utils.Array;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by qwert on 30.09.2017.
 */

public class RColor {

//    public static MyColor red = new MyColor("ff8a80", "ff1744", "d50000", "red");
//    public static MyColor pink = new MyColor("ff80ab", "f50057", "c51162", "pink");
//    public static MyColor purple = new MyColor("ea80fc", "d500f9", "aa00ff", "purple");
//    public static MyColor indigo = new MyColor("8c9eff", "3d5afe", "304ffe", "indigo");
//    public static MyColor light_blue = new MyColor("80d8ff", "00b0ff", "0091ea", "light_blue");
//    public static MyColor cyan = new MyColor("84ffff", "00e5ff", "00b8d4", "cyan");
//    public static MyColor teal = new MyColor("a7ffeb", "1de9b6", "00bfa5", "teal");
//    //public static MyColor light_green = new MyColor("ccff90", "76ff03", "64dd17", "light_green");
//    //public static MyColor lime = new MyColor("f4ff81", "c6ff00", "aeea00", "lime"); // ???
//    public static MyColor yellow = new MyColor("ffff8d", "ffea00", "ffd600", "yellow");
//    public static MyColor amber = new MyColor("ffe57f", "ffc400", "ffab00", "amber");
//    public static MyColor orange = new MyColor("ffd180", "ff9100", "ff6d00", "orange");
    public static MyColor red = new MyColor("f54e58", "ff1744", "c00730", "red");
    public static MyColor pink = new MyColor("fa4372", "f50057", "c30350", "pink");
    public static MyColor purple = new MyColor("a12dc0", "8e24aa", "72138b", "purple");
    public static MyColor indigo = new MyColor("4e60ce", "3949ab", "28368c", "indigo");
    public static MyColor light_blue = new MyColor("0e8fdc", "0277bd", "025e95", "light_blue");
    public static MyColor cyan = new MyColor("02adbf", "0097a7", "02808d", "cyan");
    public static MyColor teal = new MyColor("01b1a1", "009688", "00786d", "teal");
    //public static MyColor light_green = new MyColor("ccff90", "76ff03", "64dd17", "light_green");
    //public static MyColor lime = new MyColor("f4ff81", "c6ff00", "aeea00", "lime"); // ???
    public static MyColor yellow = new MyColor("fff159", "ffea00", "e4d207", "yellow");
    public static MyColor amber = new MyColor("fcc648", "ffb300", "d89801", "amber");
    public static MyColor orange = new MyColor("faa83d", "ff9100", "db7d00", "orange");
    public static MyColor deep_orange = new MyColor("f65c2b", "ff3d00", "ca3101", "deep_orange");

    public static MyColor getColor(){
        Array<MyColor> colors = new Array<MyColor>();
        colors.addAll(red, pink, purple, indigo, light_blue, cyan, teal, yellow, amber, orange, deep_orange);
        Random rand = new Random();
        return colors.get(rand.nextInt(colors.size));
    }

    public static MyColor getColor(String colorName){
        Array<MyColor> colors = new Array<MyColor>();
        colors.addAll(red, pink, purple, indigo, light_blue, cyan, teal, yellow, amber, orange, deep_orange);
        for (int i = 0; i < colors.size; i++){
            if (colors.get(i).name == colorName)
                return colors.get(i);
        }
        return null;
    }


}
