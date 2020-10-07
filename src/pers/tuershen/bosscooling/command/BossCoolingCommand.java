package pers.tuershen.bosscooling.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;

/**
 * Created by - on 2020/3/22.
 */
public class BossCoolingCommand implements CommandExecutor{
    private final String COMMAND= "ts";
    private PluginSetting setting;

    public BossCoolingCommand(PluginSetting setting){
        this.setting = setting;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String laber, String[] args) {
        if (sender.isOp()){
            if (COMMAND.equalsIgnoreCase(cmd.getName()) && args.length!=0){
                if (args[0].equalsIgnoreCase("reload")){
                    this.setting.reload();
                    sender.sendMessage("§a已重载插件§dBossCooling§a版本§c1.20");
                    sender.sendMessage("§7插件版本1.20 -插件反馈群，如果功能不全可以加群反馈:978420514");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reloadTime")){
                    this.setting.reload();
                    sender.sendMessage("[BossCooling] §a已刷新所有boss的冷却时间");
                    return true;
                }
                commandsHelp(sender);
                return true;
            }
            commandsHelp(sender);
            return true;
        }
        sender.sendMessage("§d§7插件版本1.20");
        return false;
    }
    public void commandsHelp(CommandSender sender){
        sender.sendMessage("§c/ts reloadTime -刷新所有Boss的冷却时间");
        sender.sendMessage("§c/ts reload -重载配置文件");
    }
}
