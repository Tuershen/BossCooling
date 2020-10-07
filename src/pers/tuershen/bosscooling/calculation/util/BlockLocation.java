package pers.tuershen.bosscooling.calculation.util;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by - on 2020/3/25.
 */
@Deprecated
public class BlockLocation {
    private int x;
    private int y;
    private int z;
    static Location location;
    public BlockLocation(int x,int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getZ() {
        return z;
    }

    @Deprecated
    public static Map<Integer, Block> getBlockPos(Block block, BlockLocation blockLocation, double index) {
        Map<Integer, Block> map = new Hashtable();
        location = block.getLocation();
        location.setX(block.getLocation().getX() + index);
        location.setZ(block.getLocation().getZ() + index);
        int thisIndex = 0;
        for (int Y = 0; Y < blockLocation.getY(); Y++)
        {
            location.setY(block.getLocation().getY() - Y + 1.0D);
            for (int Z = 0; Z < blockLocation.getZ(); Z++)
            {
                location.setZ(block.getLocation().getZ() + index - Z);
                for (int X = 0; X < blockLocation.getX(); X++)
                {
                    location.setX(block.getLocation().getX() + index - X);
                    map.put(Integer.valueOf(thisIndex + 1), location.getBlock());
                    thisIndex++;
                }
                location.setX(block.getLocation().getX() + index);
            }
            location.setZ(block.getLocation().getZ() + index);
        }
        return map;
    }
}