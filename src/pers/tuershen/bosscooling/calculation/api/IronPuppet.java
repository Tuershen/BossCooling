package pers.tuershen.bosscooling.calculation.api;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public interface IronPuppet extends BaseCalculation {

    boolean ironPuppetStructure(Block block);

    boolean isPumpkin(ItemStack type);


}
