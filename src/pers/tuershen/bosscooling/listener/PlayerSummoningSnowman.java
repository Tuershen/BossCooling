package pers.tuershen.bosscooling.listener;
import pers.tuershen.bosscooling.calculation.api.BaseCalculation;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.api.Sonwman;
import pers.tuershen.bosscooling.calculation.util.Utils;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * Created by - on 2020/3/22.
 */
public class PlayerSummoningSnowman implements Listener {

    private PluginSetting setting;
    private Sonwman sonwman;

    public <T extends BaseCalculation> PlayerSummoningSnowman(PluginSetting setting, T sub) {
        this.setting = setting;
        sonwman = (Sonwman)sub;
    }

    @EventHandler
    public void summoningSnowman(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getClickedBlock() == null) return;
        if (sonwman.ironPuppet(player.getInventory().getItemInMainHand())
                && sonwman.clickBlockTypeName().equalsIgnoreCase(event.getClickedBlock().getType().name())
                && Action.RIGHT_CLICK_BLOCK == event.getAction()) {
            if (sonwman.sonwmanStructure(event.getClickedBlock())) {
                Utils.trigger(this.setting, sonwman.clickHandItem(), player, event);
                return;
            }
        }
    }

     @EventHandler
    public void summoningSnowman(CreatureSpawnEvent spawnEvent){
        if (EntityType.SNOWMAN == spawnEvent.getEntityType()){
            double maxHealth = setting.getHealth(sonwman.clickHandItem());
            if (maxHealth <= 0) return;
            Utils.setHealth(spawnEvent.getEntity(),maxHealth,this.setting.getName(sonwman.clickHandItem()));
            return;
        }
    }


}
