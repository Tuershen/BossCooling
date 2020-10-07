package pers.tuershen.bosscooling.listener;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.api.Withered;
import pers.tuershen.bosscooling.calculation.util.Utils;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by - on 2020/3/22.
 */
public class PlayerSummoningWitheredBoss implements Listener {

    private PluginSetting setting;

    private Withered      withered;

    public <T extends BaseCalculation> PlayerSummoningWitheredBoss(PluginSetting setting, T sub){
        this.setting = setting;
        this.withered = (Withered)sub;
    }
    /**
     * @param event 玩家
     *
     */
    @EventHandler(priority=EventPriority.HIGHEST)
    public void summmoningWitheredBoss(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (event.getClickedBlock()==null) return;
        String blockTypeName = event.getClickedBlock().getType().name();
        ItemStack handItem = player.getInventory().getItemInMainHand();
        BlockFace blockFace = event.getBlockFace();
        Block block = event.getClickedBlock();
        if (withered.clickHandItem().equalsIgnoreCase(Utils.getItemName(handItem)) && withered.getBlockFace(blockFace)){
            if (withered.witheredStructure(withered.getPosBlock(blockFace,block))){
                Utils.trigger(this.setting,withered.clickHandItem(),player,event);
                return;
            }
        }
        if (withered.clickBlockTypeName().equalsIgnoreCase(blockTypeName)
                && withered.clickHandItem().equalsIgnoreCase(Utils.getItemName(handItem))
                && Action.RIGHT_CLICK_BLOCK == event.getAction()
                && event.getBlockFace() == BlockFace.UP) {
            if (withered.witheredStructure(event.getClickedBlock())){
                Utils.trigger(this.setting,withered.clickHandItem(),player,event);
            }
        }
    }

     @EventHandler
    public void summmoningWitheredBossHealth(CreatureSpawnEvent spawnEvent){
        if (EntityType.WITHER == spawnEvent.getEntityType()){
            double maxHealth = this.setting.getHealth(withered.clickHandItem());
            if (maxHealth <= 0) return;
            Utils.setHealth(spawnEvent.getEntity(),maxHealth,this.setting.getName(withered.clickHandItem()));
        }
    }



}
