package com.atlas.crmapp.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/2.
 */

public class EmoUtil {
    public static int[] emojiint = {
            0x1F601,
            0x1F602,
            0x1F603,
            0x1F604,
            0x1F605,
            0x1F606,
            0x1F609,
            0x1F60A,
            0x1F60B,
            0x1F60C,
            0x1F60D,
            0x1F60E,
            0x1F60F,
            0x1F612,
            0x1F613,
            0x1F614,
            0x1F616,
            0x1F618,
            0x1F61A,
            0x1F61C,
            0x1F61D,
            0x1F61E,
            0x1F620,
            0x1F621,
            0x1F622,
            0x1F623,
            0x1F624,
            0x1F625,
            0x1F628,
            0x1F629,
            0x1F62A,
            0x1F62B,
            0x1F62D,
            0x1F630,
            0x1F631,
            0x1F632,
            0x1F633,
            0x1F634,
            0x1F635,
            0x1F637,
            0x1F638,
            0x1F639,
            0x1F63A,
            0x1F63B,
            0x1F63C,
            0x1F63D,
            0x1F63E,
            0x1F63F,
    };

    public static List<Integer> getEmoList(){
        List<Integer> list = new ArrayList<>();
        for (int i : emojiint )
            list.add(i);
        return list;
    }
}
