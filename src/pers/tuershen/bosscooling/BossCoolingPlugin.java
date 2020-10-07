package pers.tuershen.bosscooling;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.nms.AbstractServer;
import pers.tuershen.bosscooling.command.BossCoolingCommand;
import pers.tuershen.bosscooling.io.PluginConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by - on 2020/3/22.
 */
public class BossCoolingPlugin extends JavaPlugin {
    public static Plugin plugin ;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Boss冷却插件已加载!");
        getLogger().info("作者QQ:1208465031");
        getLogger().info("发现bug请及时联系我!谢谢!");
        plugin = this ;
        AbstractServer.load();
        PluginSetting setting = new PluginConfiguration();
        setting.init();
        getCommand("ts").setExecutor(new BossCoolingCommand(setting));
    }

    @Override
    public void onDisable() {
        getLogger().info("Boss冷却插件已卸载!");
    }
}
