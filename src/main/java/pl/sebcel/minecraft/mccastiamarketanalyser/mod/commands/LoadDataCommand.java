package pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ILoadDataCommandListener;

public class LoadDataCommand extends CommandBase {
    
    private Set<ILoadDataCommandListener> listeners = new HashSet<>();

    public void addCommandListener(ILoadDataCommandListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public String getName() {
        return "cma:load";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "cma:load";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (ILoadDataCommandListener listener : listeners) {
            listener.onDataLoadRequested(sender);
        }
    }
    
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }    
}