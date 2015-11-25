package com.lex.gamelib;


import org.andengine.entity.scene.Scene;

/**
 * Created by Oleksiy on 11/24/2015.
 */
public class MyTestClass {
    public static String getHello(){
        Scene s = new Scene();
        return "Hello from lib!";
    }
    public static String getGoodbye() {
        return "Goodbye from lib!";
    }
}
