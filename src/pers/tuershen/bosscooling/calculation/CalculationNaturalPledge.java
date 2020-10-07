package pers.tuershen.bosscooling.calculation;

import org.bukkit.Location;
import org.bukkit.block.Block;
import pers.tuershen.bosscooling.calculation.api.NaturalPledge;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.listener.PlayerSummoningNaturalPledge;

public class CalculationNaturalPledge extends AbstractCalculation implements NaturalPledge {

    private int pos[][] = {
            { 4, -4,  4,  -4 },
            { 4, -4,  -4,  4 },
            { 1, -1,  1,  -1 },
            { 1, -1,  -1,  1 }
    };

    public static final String  BOTANIA_PYLON  = "BOTANIA_PYLON";

    public static final String  Naturalpledge_Divine_Core = "NATURALPEDGE_DIVINE_CORE";

    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
        plugin.getServer().getPluginManager().registerEvents(
                new PlayerSummoningNaturalPledge(
                        setting,
                        (CalculationNaturalPledge)sub),
                plugin);
    }


    @Override
    public String clickHandItem() {
        return "BOTANIA_MANARESOURCE:14";
    }

    @Override
    public String clickBlockTypeName() {
        return "BEACON";
    }

    @Override
    public boolean isAltar(Block block) {
        int num = 0;
        for (int i = 0; i < 4 ; i++) {
            Location location = block.getLocation();
            location.setY(block.getY() + 1.f);
            location.setZ(block.getZ() + pos[0][i]);
            location.setX(block.getX() + pos[1][i]);
            if (BOTANIA_PYLON.equalsIgnoreCase(
                    location
                            .getBlock()
                            .getType()
                            .name()))
                num++;
        }
        for (int i = 0; i < 4 ; i++) {
            Location location = block.getLocation();
            location.setY(block.getY() + 2.f);
            location.setZ(block.getZ() + pos[2][i]);
            location.setX(block.getX() + pos[3][i]);
            if (Naturalpledge_Divine_Core.equalsIgnoreCase(
                    location
                            .getBlock()
                            .getType()
                            .name()))
                num++;
        }
        return num == 8;
    }

    @Override
    public String getSettingNode() {
        return "NaturalPledge";
    }

    @Override
    public String getEntityName() {
        return "BOTANIA_BOTANIADOPPLEGANGER";
    }
}
