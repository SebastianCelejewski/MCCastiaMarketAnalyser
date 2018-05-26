package pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain;

import java.util.Map;

import net.minecraft.util.math.BlockPos;

public class ChestInfo {
    
    private BlockPos location;
    
    private int capacity;
    
    private Map<String, Integer> contents;

    public ChestInfo(BlockPos location, int capacity, Map<String, Integer> contents) {
        this.location = location;
        this.capacity = capacity;
        this.contents = contents;
    }

    public BlockPos getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public Map<String, Integer> getContents() {
        return contents;
    }
    
    @Override
    public String toString() {
        String result = "Chest at " + location.getX() + "," + location.getY() + "," + location.getZ() + " capacity: " + capacity;
        if (contents != null) {
            for (String itemName : contents.keySet()) {
                result += ", " + itemName + ":" + contents.get(itemName);
            }
        } else {
            result += ", no contents";
        }
        return result;
    }

}