package pers.tuershen.bosscooling.io;

import org.bukkit.World;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import pers.tuershen.bosscooling.BossCoolingPlugin;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.AbstractCalculation;

import java.util.*;

public class PluginConfiguration implements PluginSetting {

    private Map<String,Map<String,Object>> bossMap;

    private Map<String,String> messagesMap;

    private Map<String,Long> cooling;

    private Map<String,Double> coolingPermissions;

    private Map<String,String> worldMap;

    private static long systemData = new Date().getTime();

    public PluginConfiguration(){
        this.bossMap = new HashMap<>();
        this.messagesMap = new HashMap<>();
        this.cooling = new HashMap<>();
        this.coolingPermissions = new HashMap<>();
        this.worldMap = new HashMap<>();
    }

    public void init(){
        BossCoolingPlugin.plugin.saveDefaultConfig();
        intiBoss();
        initMessages();
        intiPermissions();
        initWorld();
        AbstractCalculation.inti(this);
    }

    public void reload(){
        BossCoolingPlugin.plugin.saveDefaultConfig();
        BossCoolingPlugin.plugin.reloadConfig();
        this.bossMap = new HashMap<>();
        this.messagesMap = new HashMap<>();
        this.coolingPermissions = new HashMap<>();
        this.worldMap = new HashMap<>();
        AbstractCalculation.newSet();
        intiBoss();
        initMessages();
        intiPermissions();
        initWorld();
        HandlerList.unregisterAll(BossCoolingPlugin.plugin);
        AbstractCalculation.inti(this);
    }


    public void intiBoss(){
        FileConfiguration fileConfiguration = BossCoolingPlugin.plugin.getConfig();
        MemorySection memoryConfiguration = (MemorySection)fileConfiguration.get("Boss");
        Map<String,Object> node;
        Iterator<String> itr = memoryConfiguration.getKeys(false).iterator();
        while (itr.hasNext()){
            String key  = itr.next();
            node = new HashMap<>();
            node.put("enable",fileConfiguration.getBoolean("Boss."+key+".enable"));
            node.put("time",fileConfiguration.getInt("Boss."+key+".time"));
            node.put("motdEnable",fileConfiguration.getBoolean("Boss."+key+".motdEnable"));
            node.put("motdPrefix",fileConfiguration.getString("Boss."+key+".motdPrefix"));
            node.put("health",fileConfiguration.getInt("Boss."+key+".health"));
            node.put("name",fileConfiguration.getString("Boss."+key+".name"));
            node.put("world",fileConfiguration.getList("Boss."+key+".world"));
            boolean enable = fileConfiguration.getBoolean("Boss."+key+".listener");
            String eventName = fileConfiguration.getString("Boss."+key+".event");
            node.put("event",eventName);
            node.put("listener",enable);
            if (enable){
                AbstractCalculation.putSet(eventName);
            }
            int time = ((Integer)node.get("time")).intValue() * 1000;
            long cooling = systemData - (long)time;
            this.cooling.put(key,cooling);
            this.bossMap.put(key,node);
        }
    }

    public void initWorld(){
        FileConfiguration fileConfiguration = BossCoolingPlugin.plugin.getConfig();
        MemorySection memoryConfiguration = (MemorySection)fileConfiguration.get("world");
        Iterator<String> itr = memoryConfiguration.getKeys(false).iterator();
        while (itr.hasNext()){
            String key  = itr.next();
            this.worldMap.put(key,fileConfiguration.getString("world."+key) .replace('&','§'));
        }

    }

    public void initMessages(){
        FileConfiguration fileConfiguration = BossCoolingPlugin.plugin.getConfig();
        MemorySection memoryConfiguration = (MemorySection) fileConfiguration.get("messages");
        Iterator<String> itr = memoryConfiguration.getKeys(false).iterator();
        while (itr.hasNext()){
            String key = itr.next();
            this.messagesMap.put(key,fileConfiguration.getString("messages."+key));

        }
    }

    //加载权限组
    public void intiPermissions(){
        FileConfiguration fileConfiguration = BossCoolingPlugin.plugin.getConfig();
        MemorySection memoryConfiguration = (MemorySection)fileConfiguration.get("permissions.Cooling.time");
        Iterator<String> itr = memoryConfiguration.getKeys(false).iterator();
        while (itr.hasNext()){
            String key = itr.next();
            this.coolingPermissions.put(key,fileConfiguration.getDouble("permissions.Cooling.time."+key));
        }
    }



    //是否开启
    public boolean getEnable(String key){
       return  (boolean)this.bossMap.get(key).get("enable");
    }

    //获取冷却时间
    public int getTime(String key){
        return (int)this.bossMap.get(key).get("time");
    }

    //是否开启全服提升
    public boolean getMotdEnable(String key){
        return (boolean)this.bossMap.get(key).get("motdEnable");
    }

    //获取全服提升信息
    public String getMotdPrefix(String key){
        return this.bossMap.get(key).get("motdPrefix").toString();
    }

    //获取设置Boss的最大生命值
    public int getHealth(String key){
        return (int)this.bossMap.get(key).get("health");
    }

    //获取Boss的名称
    public String getName(String key){
        return this.bossMap.get(key).get("name").toString();
    }

    //是否在世界黑名单中
    public boolean isWorld(String key, String worldName){

        for (String item : (List<String>)this.bossMap.get(key).get("world")){
            if (worldName.equalsIgnoreCase(item))return true;
        }
        return false;
    }

    //获取提升信息
    public String getMessage(String key){
        String message = this.messagesMap.get(key);
        if (message != null){
            return message;
        }
        return " ";
    }


    //该Boss是否存在？
    public boolean hasKey(String key){
        Iterator<String> keySet = this.bossMap.keySet().iterator();
        while (keySet.hasNext()){
            String next = keySet.next();
            if (key.equalsIgnoreCase(next))return true;
        }
        return false;
    }

    //获取Boss的冷却时间
    @Override
    public long getDataTime(String key) {
        Long time = this.cooling.get(key);
        if (time == null) return 0;
        return time;
    }

    //更新冷却Boss冷却时间
    @Override
    public void updateCooling(String key, Long time) {
        this.cooling.put(key,time);
    }


    //获取冷却缩减的权限组值
    public double getCoolingPermissions(Player player){
        Iterator<String> keySet = this.coolingPermissions.keySet().iterator();
        if (player.isOp()) return 0.0;
        while (keySet.hasNext()){
            String next = keySet.next();
            if (player.hasPermission(next)){
                return this.coolingPermissions.get(next);
            }
        }
        return 100.0d;
    }

    //获取世界名称
    public String getWorldName(World world){
        Iterator<String> keySet = this.worldMap.keySet().iterator();
        while (keySet.hasNext()){
            String next = keySet.next();
            if (world.getName().equalsIgnoreCase(next))return this.worldMap.get(next);
        }
        return world.getName();
    }

}
