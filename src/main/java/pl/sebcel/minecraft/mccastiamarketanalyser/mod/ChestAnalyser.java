package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.properties.IProperty;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ChestInfo;

public class ChestAnalyser {

    private final static int SINGLE_CHEST_CAPACITY = 3 * 9;

    public ChestInfo getChestInfo(TileEntity sign) {
        System.out.println("Found sign " + sign + " at " + sign.getPos());
        World world = sign.getWorld();
        BlockPos chestLocation = findChestLocation(sign);
        System.out.println("Chest location: " + chestLocation);
        List<TileEntityChest> chests = findChestsForLocation(world, chestLocation);
        if (chests.isEmpty()) {
            System.out.println("Chests not found");
            return null;
        }
        
        System.out.println("Found " + chests.size() + " chests");

        int chestCapacity = 0;
        Map<String, Integer> chestContents = new HashMap<>();

        for (TileEntityChest chest : chests) {
            chestCapacity += SINGLE_CHEST_CAPACITY;

            for (int i = 0; i < SINGLE_CHEST_CAPACITY; i++) {
                String itemName = chest.getStackInSlot(i).getItem().getUnlocalizedName();
                int itemCount = chest.getStackInSlot(i).getCount();
//                if (itemCount > 0) {
                    System.out.println("- Found " + itemCount + " " + itemName);
//                }
                if (!chestContents.containsKey(itemName)) {
                    chestContents.put(itemName, 0);
                }
                chestContents.put(itemName, chestContents.get(itemName) + itemCount);
            }
        }

        return new ChestInfo(chestLocation, chestCapacity, chestContents);
    }

    private BlockPos findChestLocation(TileEntity sign) {
        Map<IProperty<?>, Comparable<?>> properties = sign.getWorld().getBlockState(sign.getPos()).getProperties();
        BlockPos signLocation = sign.getPos();
        BlockPos chestLocation = null;
        if (properties.containsValue(EnumFacing.EAST)) {
            chestLocation = signLocation.add(-1, 0, 0);
        }
        if (properties.containsValue(EnumFacing.WEST)) {
            chestLocation = signLocation.add(1, 0, 0);
        }
        if (properties.containsValue(EnumFacing.NORTH)) {
            chestLocation = signLocation.add(0, 0, 1);
        }
        if (properties.containsValue(EnumFacing.SOUTH)) {
            chestLocation = signLocation.add(0, 0, -1);
        }
        return chestLocation;
    }
    
    private List<TileEntityChest> findChestsForLocation(World world, BlockPos chestLocation) {
        List<TileEntityChest> chests = new ArrayList<>();

        if (chestLocation == null) {
            return chests;
        }

        try {
            if (world.getTileEntity(chestLocation).getClass().equals(TileEntityChest.class)) {
                chests.add((TileEntityChest) world.getTileEntity(chestLocation));
            }
        } catch (Exception ex) {
            // intentional
        }
        if (chests.isEmpty()) {
            return chests;
        }

        BlockPos adjacentChestPosition = findAdjacentChestPosition(chests.get(0));
        if (adjacentChestPosition != null) {
            TileEntityChest adjacentChest = (TileEntityChest) world.getTileEntity(adjacentChestPosition);
            chests.add(adjacentChest);
        }

        return chests;
    }

    private BlockPos findAdjacentChestPosition(TileEntityChest chest) {
        if (chest.adjacentChestXNeg != null) {
            return chest.getPos().add(-1, 0, 0);
        }
        if (chest.adjacentChestXPos != null) {
            return chest.getPos().add(1, 0, 0);
        }
        if (chest.adjacentChestZNeg != null) {
            return chest.getPos().add(0, 0, -1);
        }
        if (chest.adjacentChestZPos != null) {
            return chest.getPos().add(0, 0, 1);
        }
        return null;
    }

    private BlockPos[] __findChestsForSign(TileEntity sign) {
        Map<IProperty<?>, Comparable<?>> properties = sign.getWorld().getBlockState(sign.getPos()).getProperties();
        BlockPos signLocation = sign.getPos();
        BlockPos[] chestLocations = new BlockPos[4];
        if (properties.containsValue(EnumFacing.EAST)) {
            chestLocations[0] = signLocation.add(-1, 0, 0);
            chestLocations[1] = signLocation.add(-1, 0, 1);
            chestLocations[2] = signLocation.add(-1, 0, -1);
            chestLocations[3] = signLocation.add(-2, 0, 0);
        }
        if (properties.containsValue(EnumFacing.WEST)) {
            chestLocations[0] = signLocation.add(1, 0, 0);
            chestLocations[1] = signLocation.add(1, 0, 1);
            chestLocations[2] = signLocation.add(1, 0, -1);
            chestLocations[3] = signLocation.add(2, 0, 0);
        }
        if (properties.containsValue(EnumFacing.NORTH)) {
            chestLocations[0] = signLocation.add(0, 0, 1);
            chestLocations[1] = signLocation.add(1, 0, 1);
            chestLocations[2] = signLocation.add(-1, 0, 1);
            chestLocations[3] = signLocation.add(0, 0, 2);
        }
        if (properties.containsValue(EnumFacing.SOUTH)) {
            chestLocations[0] = signLocation.add(0, 0, -1);
            chestLocations[1] = signLocation.add(1, 0, -1);
            chestLocations[2] = signLocation.add(-1, 0, -1);
            chestLocations[3] = signLocation.add(0, 0, -2);
        }
        return chestLocations;
    }

}