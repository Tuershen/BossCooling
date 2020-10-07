package pers.tuershen.bosscooling.calculation.nms.gaia;

import pers.tuershen.bosscooling.calculation.nms.AbstractServer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GaiaHardMode extends AbstractServer {

    private Method handle;

    private Field isHandle;

    @Override
    public <T> Object getEntityNBTTagCompound(T obj) {
        Object entity;
        try {
            entity = super.getHandle(obj);
            if (super.getServerVersion().contains("1_7")) {
                handle = entity.getClass().getMethod("isHardMode", new Class[0]);
                boolean isHandMode = (boolean) handle.invoke(entity);
                return isHandMode ;
            }
            isHandle = entity.getClass().getDeclaredField("hardMode");
            isHandle.setAccessible(true);
            boolean hardMode = (boolean)isHandle.get(entity);
            return hardMode;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }
}
