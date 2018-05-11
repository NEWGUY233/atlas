package com.atlas.crmapp.network;

/**
 * Created by Harry on 2017-05-25.
 */

public class LoadImageUtils {

    public static String loadSmallImage(String url) {
        return loadImage(url, "-sj");
    }
    public static String loadMiddleImage(String url) {
        return loadImage(url, "-mj");
    }
    public static String loadLargeImage(String url) {
        return loadImage(url, "-lj");
    }

    public static String smallWebP(String url) {return loadImage(url, "-sp");}
    public static String middleWebP(String url) {return loadImage(url, "-mp");}
    public static String largeWebP(String url) {return loadImage(url, "-lp");}

    public static String small(String url) {return loadImage(url, "-s");}
    public static String middle(String url) {return loadImage(url, "-m");}
    public static String large(String url) {return loadImage(url, "-l");}

    public static String loadImage(String url, String ext) {
        return url + ext;
    }

}
