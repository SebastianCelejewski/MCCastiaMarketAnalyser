package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import net.minecraft.command.ICommandSender;

public interface ILoadDataCommandListener {

    public void onDataLoadRequested(ICommandSender sender);
}
