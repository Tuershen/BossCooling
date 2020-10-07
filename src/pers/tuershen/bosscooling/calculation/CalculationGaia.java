package pers.tuershen.bosscooling.calculation;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.Metadatable;
import pers.tuershen.bosscooling.calculation.api.Gaia;
import pers.tuershen.bosscooling.calculation.api.PluginSetting;
import pers.tuershen.bosscooling.calculation.nms.AbstractServer;
import pers.tuershen.bosscooling.calculation.nms.gaia.GaiaHardMode;
import pers.tuershen.bosscooling.calculation.util.Utils;
import pers.tuershen.bosscooling.listener.PlayerSummoningGaia;

import java.util.*;

public class CalculationGaia extends AbstractCalculation implements Gaia {

    //搜寻宽度
    public static final int         RADIUS         = 4;
    //搜寻高度
    public static final int         HEIGHT         = 4;
    //起始周长
    private static final int        PERIMETER      = 8;
    //信标
    public static final String      BEACON         = "BEACON";
    //盖亚水晶
    public static final String      BOTANIA_PYLON  = "BOTANIA_PYLON";
    //盖亚水晶x坐标
    private static final int[]      POS_X          = { 4, -4, 4, -4};
    //盖亚水晶z坐标
    private static final int[]      POS_Z          = { 4, -4, -4, 4};
    //植物魔法的所有boss
    public static List<String>      blockList;
    //
    public static Map<String,String> gaiaEntityMap;

    private AbstractServer          gaiaHardMode;

    public CalculationGaia(){
        gaiaHardMode = new GaiaHardMode();
    }


    static {
        blockList = new ArrayList<>();
        gaiaEntityMap = new HashMap<>();
        //盖亚1 1.7.10-1.12.2
        blockList.add("BOTANIA_MANARESOURCE:4");
        //盖亚二 1.7.10-1.12.2
        blockList.add("BOTANIA_MANARESOURCE:14");
        //盖亚三 1.7.10
        blockList.add("EXTRABOTANY_GAIAWISE");
        //盖亚3 1.12.2 用挑战卷召唤的
        blockList.add("EXTRABOTANY_MATERIAL:6");
        //盖亚3 1.12.2 用自然蕴息宝珠召唤的
        blockList.add("EXTRABOTANY_NATUREORB");
        //空之律者 1.12.2
        blockList.add("EXTRABOTANY_MATERIAL:9");
        //艾尔夫海姆
        blockList.add("BOTANIA:FLUGELEYE");


        //1.7.10-1.12.2 盖亚12
    /*    gaiaEntityMap.put("BOTANIA_BOTANIADOPPLEGANGER", Arrays.asList("BOTANIA_MANARESOURCE:4","BOTANIA_MANARESOURCE:14"));*/
        gaiaEntityMap.put("EXTRABOTANY_GAIA3","EXTRABOTANY_GAIAWISE");
        //空置律者
        gaiaEntityMap.put("EXTRABOTANY_VOIDHERRSCHER","EXTRABOTANY_MATERIAL:9");
        //1.12.2盖亚3
        gaiaEntityMap.put("EXTRABOTANY_GAIAIII","EXTRABOTANY_NATUREORB");


    }

    public boolean getHandItemStack(ItemStack itemStack){
        if (blockList == null || blockList.size() == 0) return false;
        for (int i = 0; i < blockList.size() ; i++) {
            if (blockList.get(i)
                    .equalsIgnoreCase(
                            Utils
                                    .getItemName(itemStack)))
                return true;
        }
        return false;
    }


    @Override
    public <T extends Metadatable> boolean getAltarStructure(T obj) {
        Block block = (Block)obj;
        int num = 0;
        for (int i = 0; i < 4 ; i++) {
            Location location = block.getLocation();
            location.setY(block.getY() + 1.f);
            location.setZ(block.getZ() + POS_Z[i]);
            location.setX(block.getX() + POS_X[i]);
            if (BOTANIA_PYLON.equalsIgnoreCase(
                    location
                            .getBlock()
                            .getType()
                            .name()))
                num++;
        }
        return num == 4;
    }


    @Override
    public <T extends AbstractCalculation> void registerListener(PluginSetting setting, T sub) {
       plugin
               .getServer()
               .getPluginManager()
               .registerEvents(new PlayerSummoningGaia(
                       setting,
                       (CalculationGaia)sub),
                       plugin);
    }

    @Override
    public boolean getGaiaStructure(Block posBlock){
        Location pos = posBlock.getLocation();
        double Y = posBlock.getLocation().getY();
        double X = posBlock.getLocation().getX();
        double Z = posBlock.getLocation().getZ();
        int index=0;
        int h = HEIGHT;
        int r = RADIUS;
        int p = PERIMETER;
        for (int y=0 ; y < h ; y++){
            pos.setY(Y + y + 1.0D);
            for (int o = 0; o < r; o++) {
                int t= o + 1;
                pos.setX(X-t);
                pos.setZ(Z-t);
                int x = 0;
                int z = t * 2;
                int i = (p * r) - (r - (o + 1)) * p;
                for (int k = 0; k < i; k++) {
                    if ((x==t*2 && z==t*2) || (x==t*-2 && z==t*-2)) z=0;
                    int zp = (x==t*2 || x==t*-2
                            ? x > 0
                            ? x==t*2 && z!=t*2
                            ? ++z:0 :x==t*-2 && z!=t*-2
                            ? --z:0 :0) == 0
                            ? 0 : z > 0
                            ? 1:-1;
                    int xp = ((z==t*2 || z==t*-2)
                            ? z > 0
                            ? z==t*2 && x!=t*2
                            ? ++x:0 :z==t*-2 && x!=t*-2
                            ? --x:0 :0) == 0
                            ? 0 : x > 0
                            ? 1:-1;
                    pos.setZ(pos.getZ() + zp);
                    pos.setX(pos.getX() + xp);
                    if (z== t*2 && x==t*2 && k >=((i) / 2)-1){
                        z=t * -2;
                        x=0;
                    }
                    index++;
                    if (pos
                            .getBlock()
                            .getType() != Material.AIR
                            &&
                            breakCheck(index) != -1) return false;
                }
            }
        }
        return true;
    }


    public int breakCheck(int index){
        switch (index){
            case 56:
            case 64:
            case 72:
            case 80:
                return -1;
        }
        return 0;
    }

    public boolean getHandMode(Entity entity){
        return (boolean)gaiaHardMode.getEntityNBTTagCompound(entity);
    }

    @Override
    public boolean hasKey(String entityName) {
        return false;
    }


    @Override
    public String clickHandItem() {
        return null;
    }

    @Override
    public String clickBlockTypeName() {
        return BEACON;
    }


    public boolean getEntityName(String name){
        return gaiaEntityMap.containsKey(name);
    }

    public String getItemSetting(String name){
        return gaiaEntityMap.get(name);
    }




}