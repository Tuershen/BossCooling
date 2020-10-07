package pers.tuershen.bosscooling.calculation.util;
import org.bukkit.entity.LivingEntity;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Date;

/**
 * Created by - on 2020/3/22.
 */
public class Utils {

    private static int time ;

    public static String getItemName(ItemStack itemStack){
        return itemStack.getDurability() == 0
                ? itemStack.getType().name()
                : itemStack.getType().name()+":"+itemStack.getDurability();
    }


    /**
     * 事件处理
     * @param setting 配置类
     * @param note 节点名称
     * @param player 玩家
     * @param playerInteractEvent 交换事件
     */
    public static void trigger(
            PluginSetting setting,
            String note,
            Player player,
            PlayerInteractEvent playerInteractEvent){
        if (!setting.getEnable(note)){
            player.sendMessage(setting.getMessage("SERVER_MESSAGE")
                    .replace("%Boss%", setting.getName(note))
                    .replace('&','§'));
            playerInteractEvent.setCancelled(true);
            return;
        }
        //是否在世界黑名单
        if (setting.isWorld(note,player.getWorld().getName())) {
            player.sendMessage(setting.getMessage("WORLD_MESSAGE")
                    .replace("%Boss%", setting.getName(note))
                    .replace('&','§'));
            playerInteractEvent.setCancelled(true);
            return;
        }
            //获取重置时
        time = ((int)(new Date().getTime() - setting.getDataTime(note)) / 1000);
        double coolingPermissions = setting.getCoolingPermissions(player);
        int cooling = setting.getTime(note);
        int runTime = (int)(cooling * coolingPermissions) / 100;
        if (runTime <= time){
            if (setting.getMotdEnable(note)){
                Bukkit.broadcastMessage(setting.getMotdPrefix(note)
                        .replace("{Boss}",setting.getName(note))
                        .replace("{Player}",player.getDisplayName())
                        .replace("{X}",String.valueOf(playerInteractEvent.getClickedBlock().getLocation().getX()))
                        .replace("{Y}",String.valueOf(playerInteractEvent.getClickedBlock().getLocation().getY()))
                        .replace("{Z}",String.valueOf(playerInteractEvent.getClickedBlock().getLocation().getZ()))
                        .replace("{World}",setting.getWorldName(player.getWorld()))
                        .replace('&','§'));
                setting.updateCooling(note,new Date().getTime());
                return;
            }
            setting.updateCooling(note,new Date().getTime());
            player.sendMessage(setting.getMessage("PLAYERSUMMONINGGAIA_MESSAGE")
                    .replace("%Boss%",setting.getName(note))
                    .replace("%BossCooling%",String.valueOf(setting.getTime(note)))
                    .replace('&','§'));
            return;
        }
        player.sendMessage(setting.getMessage("TIME_MESSAGE")
                .replace("%Boss%",setting.getName(note))
                .replace("%time%",String.valueOf(cooling - time))
                .replace("%cooling%",String.valueOf(coolingPermissions))
                .replace("%coolingTime%",String.valueOf(runTime - time))
                .replace('&','§'));
        playerInteractEvent.setCancelled(true);
        return;
    }


    public static void setHealth(LivingEntity livingEntity, double health, String cusName){
        if (!cusName.equalsIgnoreCase("null")){
            livingEntity.setCustomName(cusName);
        }
        livingEntity.setMaxHealth(health);
        livingEntity.setHealth(health);
        livingEntity.setCustomNameVisible(true);
    }



}
