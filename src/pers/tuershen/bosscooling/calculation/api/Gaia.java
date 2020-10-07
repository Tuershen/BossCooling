package pers.tuershen.bosscooling.calculation.api;

import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;

public interface Gaia extends BaseCalculation {

    boolean getGaiaStructure(Block block);

    boolean getHandItemStack(ItemStack itemStack);

    <T extends Metadatable> boolean getAltarStructure(T obj);

    boolean getHandMode(Entity entity);

    boolean hasKey(String entityName);

    boolean getEntityName(String name);

    String getItemSetting(String name);



}
