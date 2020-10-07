package pers.tuershen.bosscooling.listener;

import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.Dragon;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import pers.tuershen.bosscooling.calculation.util.Utils;

/**
 * Created by - on 2020/3/22.
 */
public class PlayerSummoingShadowDragon implements Listener{

    private PluginSetting setting;

    private Dragon dragon;

    public <T extends BaseCalculation> PlayerSummoingShadowDragon(PluginSetting setting, T sub){
        this.setting=setting;
        dragon = (Dragon)sub;
    }





    @EventHandler (priority= EventPriority.HIGHEST)
    public void SummoingShadowDragon(CreatureSpawnEvent event){
        if (event.getEntityType() == EntityType.ENDER_DRAGON){
            double maxHealth =  setting.getHealth("Dragon");
            if (maxHealth <= 0) return;
            Utils.setHealth(event.getEntity(),maxHealth,dragon.clickHandItem());
            return;
        }
    }











}
