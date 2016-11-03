package com.bwf.yibao.framwork.utils;

import java.util.Random;

/**
 * Created by nicholas on 2016/9/13.
 */
public class RandomUtil {
    public static int getRandomInt(int range){
        return new Random().nextInt(range) + 1;
    }
}
