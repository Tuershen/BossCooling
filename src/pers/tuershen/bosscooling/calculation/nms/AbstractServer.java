package pers.tuershen.bosscooling.calculation.nms;


import pers.tuershen.bosscooling.BossCoolingPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public abstract class AbstractServer {

    //Bukkit Entity实现类
    private static Class<?>  craftEntity;
    //服务器版本
    private static String    SERVER_VERSION;
    //获取net.minecraft.server.entity.Entity
    private static Method    handle;

    public static void load(){
        //获取服务器版本
        SERVER_VERSION = BossCoolingPlugin.plugin
                .getServer()
                .getClass()
                .getPackage()
                .getName()
                .replace(".", ",")
                .split(",")[3];
        try {
            craftEntity = Class.forName("org.bukkit.craftbukkit." + SERVER_VERSION + ".entity.CraftEntity");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public abstract <T> Object getEntityNBTTagCompound(T obj);

    protected String getServerVersion(){
        return SERVER_VERSION;
    }

    protected <T> Object getHandle(T type) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Object obj = craftEntity.cast(type);
        handle = obj.getClass().getDeclaredMethod("getHandle");
        return handle.invoke(obj);
    }

}
