package pers.tuershen.bosscooling.calculation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import pers.tuershen.bosscooling.calculation.api.IronPuppet;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.listener.PlayerSummoningIronPuppet;


public class CalculationIron extends AbstractCalculation implements IronPuppet {

    //南瓜灯
    private static final String    JACK_O_LANTERN  = "JACK_O_LANTERN";
    //南瓜头
    private static final String    PUMPKIN         = "PUMPKIN";
    //铁方块
    private static final String    IRON_BLOCK      = "IRON_BLOCK";

    /**
     * 注册该事件
     * @param setting
     * @param sub
     * @param <T>
     */
    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
        plugin.getServer().getPluginManager().registerEvents(
                new PlayerSummoningIronPuppet(
                        setting,
                        (CalculationIron)sub),
                plugin);
    }

    @Override
    public boolean ironPuppetStructure(Block block) {
        Location ironLocation = block.getLocation();
        ironLocation.setY(block.getLocation().getY() - 1);
        if (ironLocation.getBlock().getType() == Material.IRON_BLOCK){
            double posX = block.getLocation().getX();
            double posZ = block.getLocation().getZ();
            for (int i = 1; i <= 2 ; i++) {
                ironLocation = block.getLocation();
                int number = 0;
                for (int j = 0; j < 2 ; j++) {
                    int x = (0B10 >>> i | ~-(j & i << i - 0B1)) + ((i - 0B1) & 0xFFFF);
                    int z = (0B10 >>> (0B1 & i + 0B1) | ~-(i >> (0B1 & j) + 0B1)) + (i & 0B1);
                    ironLocation.setX(posX + x);
                    ironLocation.setZ(posZ + z);
                    if (ironLocation.getBlock().getType() == Material.IRON_BLOCK) number++;
                }
                if (number == 2)return true;
            }
        }
        return false;
    }

    public boolean isPumpkin(ItemStack itemStack){
        return JACK_O_LANTERN
                .equalsIgnoreCase(itemStack.getType().name())
                ||
                PUMPKIN
                        .equalsIgnoreCase(itemStack.getType().name());
    }

    @Override
    public String clickHandItem() {
        return IRON_BLOCK;
    }

    @Override
    public String clickBlockTypeName() {
        return IRON_BLOCK;
    }
}
