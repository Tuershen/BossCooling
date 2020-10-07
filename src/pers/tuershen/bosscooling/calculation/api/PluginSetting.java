package pers.tuershen.bosscooling.calculation.api;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface PluginSetting{

    void init();

    void reload();

    boolean getEnable(String key);

    int getTime(String key);

    boolean getMotdEnable(String key);

    String getMotdPrefix(String key);

    int getHealth(String key);

    String getName(String key);

    boolean isWorld(String key, String worldName);

    String getMessage(String key);

    boolean hasKey(String key);

    long getDataTime(String key);

    void updateCooling(String key,Long time);

    double getCoolingPermissions(Player player);

    String getWorldName(World world);
}
