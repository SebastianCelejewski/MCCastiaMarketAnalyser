package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import net.minecraft.command.ICommandSender;

public interface ISaveDataCommandListener {
    
    public void onDataSaveRequested(ICommandSender sender);
    
}