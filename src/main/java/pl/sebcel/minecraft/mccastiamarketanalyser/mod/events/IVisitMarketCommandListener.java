package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import net.minecraft.command.ICommandSender;

public interface IVisitMarketCommandListener {

    public void onMarketVisitRequested(ICommandSender sender);

}