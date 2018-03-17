package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import net.minecraft.tileentity.TileEntitySign;

public interface ISignFoundListener {
    
    public void onSignFound(TileEntitySign sign);

}
