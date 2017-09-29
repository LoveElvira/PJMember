package com.humming.pjmember.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by Elvira on 2017/5/31.
 */

public class SharePrefUtil {

    public static void putBoolean(String fileName, String key, boolean value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String fileName, String key, boolean defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static void putString(String fileName, String key, String value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String fileName, String key, String defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(String fileName, String key, int value, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String fileName, String key, int defValue, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }

    public static void remove(String fileName, String key, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        sp.edit().remove(key).commit();
    }

    /**
     * 针对复杂类型存储<对象>
     *
     * @param key
     * @param object
     */
    public static void setObject(String fileName, String key, Object object, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);

        //创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //创建字节对象输出流
        ObjectOutputStream out = null;
        try {
            //然后通过将字对象进行64转码，写入key值为key的sp中
            out = new ObjectOutputStream(baos);
            out.writeObject(object);
            String objectVal = new String(Base64.encode(baos.toByteArray(), Base64.DEFAULT));
            SharedPreferences.Editor editor = sp.edit();
            editor.putString(key, objectVal);
            editor.commit();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static <T> T getObject(String fileName, String key, Class<T> clazz, Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(fileName,
                Context.MODE_PRIVATE);
        if (sp.contains(key)) {
            String objectVal = sp.getString(key, null);
            byte[] buffer = Base64.decode(objectVal, Base64.DEFAULT);
            //一样通过读取字节流，创建字节流输入流，写入对象并作强制转换
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = null;
            try {
                ois = new ObjectInputStream(bais);
                T t = (T) ois.readObject();
                return t;
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bais != null) {
                        bais.close();
                    }
                    if (ois != null) {
                        ois.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
