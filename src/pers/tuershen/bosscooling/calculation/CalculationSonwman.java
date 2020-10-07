package pers.tuershen.bosscooling.calculation;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.api.Sonwman;
import pers.tuershen.bosscooling.listener.PlayerSummoningSnowman;

public class CalculationSonwman extends AbstractCalculation implements Sonwman {

    //雪
    public static final String   SNOWMAN          = "SNOWMAN";
    //雪方块
    public static final String   SNOW_BLOCK       = "SNOW_BLOCK";
    //南瓜灯
    private static final String  JACK_O_LANTERN   = "JACK_O_LANTERN";
    //南瓜头
    private static final String  PUMPKIN          = "PUMPKIN";


    /**
     * 注册该事件：雪人
     * @param setting 配置类
     * @param sub this
     * @param <T> extends AbstractCalculation
     */
    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(new PlayerSummoningSnowman(
                        setting,
                        (CalculationSonwman)sub),
                        plugin);
    }


    @Override
    public boolean sonwmanStructure(Block block) {
        Location location = block.getLocation();
        for (int i = 1; i <= 2 ; i++) {
            location.setY(block.getY()- i);
            if (getBlockName(block) && getBlockName(location.getBlock())) return true;
        }
        return false;
    }

    public boolean getBlockName(Block block){
        return SNOW_BLOCK
                .equalsIgnoreCase(block.getType().name());
    }

    public boolean ironPuppet(ItemStack itemStack){
        return  JACK_O_LANTERN
                .equalsIgnoreCase(itemStack.getType().name())
                ||
                PUMPKIN
                        .equalsIgnoreCase(itemStack.getType().name());
    }

    @Override
    public String clickHandItem() {
        return SNOWMAN;
    }

    @Override
    public String clickBlockTypeName() {
        return SNOW_BLOCK;
    }
}
