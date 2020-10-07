package pers.tuershen.bosscooling.calculation;

import jdk.nashorn.internal.ir.Block;
import org.bukkit.metadata.Metadatable;
import pers.tuershen.bosscooling.calculation.api.Dragon;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.listener.PlayerSummoingShadowDragon;

public class CalculationDragon extends AbstractCalculation implements Dragon {



    private static int[] posX = {};

    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(new PlayerSummoingShadowDragon(
                        setting,
                        (CalculationDragon)sub),
                        plugin);
    }

    public <T extends Metadatable> boolean isAltar(T obj){
        Block block = (Block)obj;
        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 3 ; j++) {





            }
        }
        return false;
    }



    @Override
    public String clickHandItem() {
        return null;
    }

    @Override
    public String clickBlockTypeName() {
        return null;
    }
}
