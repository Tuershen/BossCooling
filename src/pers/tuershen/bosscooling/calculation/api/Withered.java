package pers.tuershen.bosscooling.calculation.api;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.Metadatable;

public interface Withered extends BaseCalculation {

    <T extends Metadatable> boolean witheredStructure(T obj);

    boolean getBlockFace(BlockFace face);

    Block getPosBlock(BlockFace face, Block ock);

}
