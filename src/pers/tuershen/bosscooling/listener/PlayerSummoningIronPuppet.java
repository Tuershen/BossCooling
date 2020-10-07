package pers.tuershen.bosscooling.listener;
import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.IronPuppet;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.CalculationIron;
import pers.tuershen.bosscooling.calculation.util.Utils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by - on 2020/3/23.
 */
public class PlayerSummoningIronPuppet implements Listener{

    private PluginSetting setting;

    private IronPuppet ironPuppet;

    public <T extends BaseCalculation> PlayerSummoningIronPuppet(PluginSetting setting, T sub){
        this.setting = setting;
        ironPuppet = (CalculationIron) sub;
    }

    @EventHandler
    public void summoningIronPuppte(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        if (ironPuppet.isPumpkin(player.getItemInHand())
                && ironPuppet.clickBlockTypeName().equalsIgnoreCase(event.getClickedBlock().getType().name())
                && Action.RIGHT_CLICK_BLOCK == event.getAction()){
            if(ironPuppet.ironPuppetStructure(event.getClickedBlock())){
                Utils.trigger(this.setting, ironPuppet.clickHandItem(), player,event);
                return;
            }
        }
    }


    @EventHandler
    public void summoningIronPuppet(CreatureSpawnEvent spawnEvent){
        if (EntityType.IRON_GOLEM == spawnEvent.getEntityType()){
            double maxHealth = setting.getHealth(ironPuppet.clickHandItem());
            if (maxHealth<=0) return;
            Utils.setHealth(spawnEvent.getEntity(),maxHealth,this.setting.getName(ironPuppet.clickHandItem()));
            return;
        }
    }
}
