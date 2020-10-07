package pers.tuershen.bosscooling.calculation;

import org.bukkit.plugin.Plugin;
import pers.tuershen.bosscooling.BossCoolingPlugin;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;

import java.util.*;

public abstract class AbstractCalculation {

    //插件
    protected static  Plugin                plugin;

    //所有监听类
    private  static   Map<String,Class<?>>  subCalculationMap;

    //监听类
    private  static   Set<String>           event;

    static {
        plugin = BossCoolingPlugin.plugin;
        event = new HashSet<>();
        subCalculationMap = new HashMap<>();
        subCalculationMap.put("gaia",CalculationGaia.class); //盖亚
        subCalculationMap.put("withered",CalculationWithered.class); //凋零
        subCalculationMap.put("sonwman",CalculationSonwman.class); //雪人
        subCalculationMap.put("ironman",CalculationIron.class); //铁人
        subCalculationMap.put("Dragon" ,CalculationDragon.class); //末影龙
        subCalculationMap.put("NaturalPledge" ,CalculationNaturalPledge.class); //自然誓约
    }

    public abstract <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub);

    public static void putSet(String eventString){
        event.add(eventString);
    }

    public static <T extends AbstractCalculation> void inti(PluginSetting setting){
        Iterator<String> itr = event.iterator();
        while (itr.hasNext()){
            String next = itr.next();
            Class<?> clazz = subCalculationMap.get(next);
            if (clazz != null){
                try {
                    T subClazz =(T) clazz.newInstance();
                    subClazz.registerListener(setting,subClazz);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void newSet(){
        event = new HashSet<>();
    }

}
