package pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IVisitMarketCommandListener;

public class VisitMarketCommand extends CommandBase {
    
    private Set<IVisitMarketCommandListener> listeners = new HashSet<>();

    public void addCommandListener(IVisitMarketCommandListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public String getName() {
        return "cma:visit";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "cma:visit";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (IVisitMarketCommandListener listener : listeners) {
            listener.onMarketVisitRequested(sender);
        }
    }
    
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}