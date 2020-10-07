package pers.tuershen.bosscooling.listener;
import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.Gaia;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.util.Utils;
import org.bukkit.entity.Entity;
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
public class PlayerSummoningGaia implements Listener {

    private Gaia gaia;
    private PluginSetting setting;

    public <T extends BaseCalculation> PlayerSummoningGaia(PluginSetting setting, T sub){
        gaia = (Gaia) sub;
        this.setting = setting;

    }

    @EventHandler (priority= EventPriority.HIGHEST)
    public void onSummoningGaia(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (player.getInventory().getItemInMainHand() == null) return;
        if (event.getClickedBlock() == null) return;
        String itemStack = Utils.getItemName(player.getInventory().getItemInMainHand());
        if (gaia.getHandItemStack(player.getInventory().getItemInMainHand())
                && gaia.clickBlockTypeName().equalsIgnoreCase(event.getClickedBlock().getType().name())
                && player.isSneaking()
                && (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)){
                //是否是盖亚祭坛结构体
                if (gaia.getAltarStructure(event.getClickedBlock())) {
                    Utils.trigger(this.setting, itemStack, player, event);
                }
                return;
        }
    }

    /*  if (gaia.getGaiaStructure(event.getClickedBlock())) {*/
                   /*     return;
                    }
                    player.sendMessage(this.setting.getMessage("GAIASTRUCTURE").replace('&','§'));
                    event.setCancelled(true);
                    return;*/

    @EventHandler
    public void createGaia(CreatureSpawnEvent spawnEvent){
        if ("BOTANIA_BOTANIADOPPLEGANGER".equalsIgnoreCase(spawnEvent.getEntityType().name())){
            Entity entity = spawnEvent.getEntity();
            double maxHealth = setting.getHealth(gaia.getHandMode(entity)
                    ? "BOTANIA_MANARESOURCE:14"
                    : "BOTANIA_MANARESOURCE:4");
            if (maxHealth <= 0 ) return;
            spawnEvent.getEntity().setMaxHealth(maxHealth);
            spawnEvent.getEntity().setHealth(maxHealth);
            return;
        }

        if (gaia.getEntityName(spawnEvent.getEntityType().name().toUpperCase())){
            double maxHealth = setting.getHealth(gaia.getItemSetting(spawnEvent.getEntityType().name().toUpperCase()));
            if (maxHealth <= 0) return;
            spawnEvent.getEntity().setMaxHealth(maxHealth);
            spawnEvent.getEntity().setHealth(maxHealth);
            return;
        }
    }
}
