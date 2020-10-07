package pers.tuershen.bosscooling.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.NaturalPledge;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.util.Utils;

public class PlayerSummoningNaturalPledge implements Listener {


    private PluginSetting setting;

    private NaturalPledge naturalPledge;

    public <T extends BaseCalculation> PlayerSummoningNaturalPledge(PluginSetting setting, T sub){
        this.setting = setting;
        naturalPledge = (NaturalPledge) sub;
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void summoningIronPuppte(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) return;
        if (event.getClickedBlock() == null) return;
        String itemStack = Utils.getItemName(player.getInventory().getItemInMainHand());
        if (naturalPledge.clickHandItem().equalsIgnoreCase(itemStack)
                && naturalPledge.clickBlockTypeName().equalsIgnoreCase(event.getClickedBlock().getType().name())
                && player.isSneaking()
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)){
            //是否是自然誓约
            if (naturalPledge.isAltar(event.getClickedBlock())){
                Utils.trigger(this.setting, naturalPledge.getSettingNode(), player, event);
            }
            return;
        }
    }

    @EventHandler
    public void createGaia(CreatureSpawnEvent spawnEvent){
        if (naturalPledge.getEntityName().equalsIgnoreCase(spawnEvent.getEntityType().name())){
            double maxHealth = setting.getHealth(naturalPledge.getSettingNode());
            if (maxHealth <= 0 ) return;
            Utils.setHealth(spawnEvent.getEntity(),maxHealth,this.setting.getName(naturalPledge.getSettingNode()));
            return;
        }
    }



}
