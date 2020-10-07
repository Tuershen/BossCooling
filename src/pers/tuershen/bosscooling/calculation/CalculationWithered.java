package pers.tuershen.bosscooling.calculation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.metadata.Metadatable;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.api.Withered;
import pers.tuershen.bosscooling.listener.PlayerSummoningWitheredBoss;


public class CalculationWithered extends AbstractCalculation implements Withered {

    //灵魂沙
    private final static String  SOUL_SAND_BLOCK
            = "SOUL_SAND";
    //凋零头
    private final static String  SKULL_ITEM_1
            = "SKULL_ITEM:1";

    /**
     *
     * @param obj Block接口
     * @param <T>
     * @return 是否能构建成凋零Boss
     */
    @Override
    public <T extends Metadatable> boolean witheredStructure(T obj) {
        //一共6种情况可以构建成凋零Boss
        for (int i = 0; i < 3 ; i++) {
            //  第1-2种是凋零头在中间  1 代表 凋零头
            //     情况1       情况2       全部结构
            //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 0 0 0 |
            //  0 0 1 0 0 | 0 0 0 0 0 | 0 0 1 0 0 |
            //  0 0 1 0 0 | 0 1 1 1 0 | 0 1 1 1 0 |
            //  0 0 1 0 0 | 0 0 0 0 0 | 0 0 1 0 0 |
            //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 0 0 0 |
            if ( (0x2 & i) == i){
                if (structure((Block) obj))return true;
            }else {
                //  第2-4种是凋零头在边缘
                //     情况3       情况4       情况5       情况6       全部结构
                //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 1 0 0 | 0 0 0 0 0  | 0 0 1 0 0
                //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 1 0 0 | 0 0 0 0 0  | 0 0 1 0 0
                //  1 1 1 0 0 | 0 0 1 1 1 | 0 0 1 0 0 | 0 0 1 0 0  | 1 1 1 1 1
                //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 0 0 0 | 0 0 1 0 0  | 0 0 1 0 0
                //  0 0 0 0 0 | 0 0 0 0 0 | 0 0 0 0 0 | 0 0 1 0 0  | 0 0 1 0 0
                // 像这种结构用位运算是有效的办法
                for (int j = 1; j <= 2 ; j++) {
                    //预期灵魂沙的方块位置
                    Location blockLocation =  ((Block)obj).getLocation();
                    //预期凋零头的方块位置
                    Location skullLocation =  ((Block)obj).getLocation();
                    //获取玩家右键的方块Location
                    double blockX = blockLocation.getX();
                    double blockZ = blockLocation.getZ();
                    //凋零头是在灵魂沙上方的，所以我们需要 y + 1
                    skullLocation.setY(skullLocation.getY() + 1);
                    for (int k = 0; k < 2 ; k++) {
                        // (2 >>> 1 | -(1 & 1 << 1 - 1) ^ 0xFFFFFFFF) + 1 - 1;
                        // (2 >>> 1 | -(2 & 1 << 1 - 1) ^ 0xFFFFFFFF) + 1 - 1;
                        // (2 >>> 2 | -(1 & 2 << 2 - 1) ^ 0xFFFFFFFF) + 2 - 1;
                        // (2 >>> 2 | -(2 & 2 << 2 - 1) ^ 0xFFFFFFFF) + 2 - 1;
                        //  此方法是获取4种情况的x坐标 x(4)= +1 -1 0 0
                        //        int x = (2 >>> j | -(k & j << j - 1) ^ 0xFFFFFFFF) + j - 1;
                        int x = (2 >>> j | ~-(k & j << j - 1)) + j - 1;
                        // (2 >>> (0x1 & 1 + 1) | -(1 >> (0x1 & 1) + 1) ^ 0xFFFFFFFF) + (1 & 0x1)
                        // (2 >>> (0x1 & 1 + 1) | -(1 >> (0x1 & 2) + 1) ^ 0xFFFFFFFF) + (1 & 0x1)
                        // (2 >>> (0x1 & 2 + 1) | -(2 >> (0x1 & 1) + 1) ^ 0xFFFFFFFF) + (2 & 0x1)
                        // (2 >>> (0x1 & 2 + 1) | -(2 >> (0x1 & 2) + 1) ^ 0xFFFFFFFF) + (2 & 0x1)
                        //  此方法是获取4种情况的z坐标 z(4)= 0 0 -1 +1
                        //     int z = (2 >>> (0x1 & j + 1) | -(j >> (0x1 & k) + 1) ^ 0xFFFFFFFF) + (j & 0x1);
                        int z = (2 >>> (0x1 & j + 1) | ~-(j >> (0x1 & k) + 1)) + (j & 0x1);
                        blockLocation.setX(blockX + x);
                        skullLocation.setX(blockX + x);
                        blockLocation.setZ(blockZ + z);
                        skullLocation.setZ(blockZ + z);
                        if (skullLocation
                                .getBlock()
                                .getType() == Material.SKULL
                                &&
                                blockLocation
                                        .getBlock()
                                        .getType() == Material.SOUL_SAND){
                            return structure(blockLocation.getBlock(),x,z);
                        }
                    }
                }
            }
        }
        return false;
    }

    /***
     * 方块是否能搭建实体
     * @param block
     * @return
     */
    public boolean structure(Block block){
        Location locationSoulSand = block.getLocation();
        Location locationSkull    = block.getLocation();
        locationSoulSand.setY(block.getLocation().getY() - 1);
        double pitchX = block.getLocation().getX();
        double pitchZ = block.getLocation().getZ();
        if (locationSoulSand.getBlock().getType() == Material.SOUL_SAND){
            for (int y = 1; y <= 2; y++) {
                locationSoulSand = block.getLocation();
                locationSkull.setY(block.getLocation().getY() + 1);
                int number = 0;
                for (int x = 0; x < 2; x++) {
                    int posX = getPosX(y, x, 2, 1);
                    int posZ = getPosZ(y, x, 2, 1);
                    locationSoulSand.setX(pitchX + posX);
                    locationSoulSand.setZ(pitchZ + posZ);
                    locationSkull.setX(pitchX + posX);
                    locationSkull.setZ(pitchZ + posZ);
                    if (locationSkull
                            .getBlock()
                            .getType() == Material.SKULL
                            &&
                            locationSoulSand
                                    .getBlock()
                                    .getType() == Material.SOUL_SAND){
                        number++;
                    }
                }
                if (number == 2) return true;
            }
        }
        return false;
    }


    public boolean structure(Block block, int locX, int locZ){
        Location locationSoulSand = block.getLocation();
        Location locationSkull = block.getLocation();
        locationSoulSand.setY(block.getLocation().getY() - 1);
        double pitchX = block.getLocation().getX();
        double pitchZ = block.getLocation().getZ();
        if (locationSoulSand.getBlock().getType() == Material.SOUL_SAND){
            locationSoulSand = block.getLocation();
            locationSkull.setY(block.getLocation().getY() + 1);
            locationSoulSand.setX(pitchX + locX);
            locationSkull.setX(pitchX + locX);
            locationSoulSand.setZ(pitchZ + locZ);
            locationSkull.setZ(pitchZ + locZ);
            if (locationSkull
                    .getBlock()
                    .getType() == Material.SKULL
                    &&
                    locationSoulSand
                            .getBlock()
                            .getType() == Material.SOUL_SAND)return true;
        }
        return false;
    }

    /**
     *  * 取正负两极 posX
     *      * -x +0 -0 +0
     *      * -0 +x -0 +0
     *      * -0 +0 -z +0
     *      * -0 +0 -0 +z
     * @param x
     * @param z
     * @return
     */
    public int getPosX(int x, int z, int posX, int posZ){
        return (posX >>> x | ~-(z & x << x - posZ)) + x - posZ;
    }

    /**
     *  * 位倒转得Z坐标 posZ
     *      * -z +0 -0 +0
     *      * -0 +z -0 +0
     *      * -0 +0 -x +0
     *      * -0 +0 -0 +x
     * @param x
     * @param z
     * @return
     */
    public int getPosZ(int x, int z, int posX, int posZ){
        return (posX >>> (posZ & x + posZ) | -(x >> (posZ & z) + posZ) ^ 0xFFFFFFFF) + (x & posZ);
    }

    public boolean getBlockFace(BlockFace blockFace){
        switch (blockFace){
            case EAST:
            case SOUTH:
            case WEST:
            case NORTH:
            case DOWN:
                return true;
        }
        return false;
    }

    @Override
    public Block getPosBlock(BlockFace face, Block ock) {
        Location location = ock.getLocation();
        location.setY(ock.getY() - 1.d);
        switch (face){
            case EAST:
                location.setX(location.getX() + 1.d);
                return location.getBlock();
            case SOUTH:
                location.setZ(location.getZ() +  1.d);
                return location.getBlock();
            case WEST:
                location.setX(location.getX() - 1.d);
                return location.getBlock();
            case NORTH:
                location.setZ(location.getZ() -1.d);
                return location.getBlock();
        }
        return location.getBlock();
    }


    /**
     * 注册该事件：凋零生成
     * @param setting 配置类
     * @param sub this
     * @param <T> extends AbstractCalculation
     */
    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
        plugin
                .getServer()
                .getPluginManager()
                .registerEvents(new PlayerSummoningWitheredBoss(
                        setting,
                        (CalculationWithered)sub),
                        plugin);
    }

    @Override
    public String clickHandItem() {
        return SKULL_ITEM_1;
    }

    @Override
    public String clickBlockTypeName() {
        return SOUL_SAND_BLOCK;
    }
}
