package com.atlas.crmapp.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by huangyang on 2017/5/1.
 */

public class SpUtil {

    private static SharedPreferences sp;
    private static String xml_name="ATLAS";

    public static final String ICON="ATLAS_ICON";
    public static final String COMPANY="ATLAS_COMPANY";
    public static final String PHONE="ATLAS_PHONE";
    public static final String NICK="ATLAS_NICK";
    public static final String AREA="ATLAS_AREA";
    public static final String ID="ATLAS_ID";
    public static final String TIM="ATLAS_TIM";

    public static final String LANGUAGE="LANGUAGE";



    /**
     * 写入boolean变量至sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值 boolean
     */
    public static void putBoolean(Context ctx, String key, boolean value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }
    /**
     * 读取boolean标示从sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param defValue  没有此节点默认值
     * @return      默认值或者此节点读取到的结果
     */
    public static boolean getBoolean(Context ctx,String key,boolean defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 写入String变量至sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putString(Context ctx,String key,String value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }
    /**
     * 读取Long标示从sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param defValue  没有此节点默认值
     * @return      默认值或者此节点读取到的结果
     */
    public static long getLong(Context ctx,String key, long defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        return sp.getLong(key, defValue);
    }


    /**
     * 写入Long变量至sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值string
     */
    public static void putLong(Context ctx, String key,long value){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().putLong(key, value).commit();
    }
    /**
     * 读取String标示从sp中
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param defValue  没有此节点默认值
     * @return      默认值或者此节点读取到的结果
     */
    public static String getString(Context ctx,String key, String defValue){
        //(存储节点文件名称,读写方式)
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


    /**
     * 从sp中移除指定节点
     * @param ctx   上下文环境
     * @param key   需要移除节点的名称
     */
    public static void remove(Context ctx, String key) {
        if(sp == null){
            sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
        }
        sp.edit().remove(key).commit();
    }

    // 退出登录时要调用
    public static void clean(Context ctx) {
        try {
            if(sp == null){
                sp = ctx.getSharedPreferences(xml_name, Context.MODE_PRIVATE);
            }
            sp.edit().clear().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setGson(Context context, String key, Object obj)
    {
        Gson gson = new Gson();
        String content = gson.toJson(obj);
        putString(context, key, content);
    }

    public static Object getGson(Context context, String key, Type type)
    {
        Gson gson = new Gson();
        String content = getString(context, key, null);
        if (null == content)
        {
            return null;
        }
        return gson.fromJson(content, type);
    }

    public static <T> T getGson(Context context, String key, Class<T> clazz)
    {
        Gson gson = new Gson();
        String content = getString(context, key, null);
        if (null == content)
        {
            return null;
        }
        return gson.fromJson(content, clazz);
    }
}
