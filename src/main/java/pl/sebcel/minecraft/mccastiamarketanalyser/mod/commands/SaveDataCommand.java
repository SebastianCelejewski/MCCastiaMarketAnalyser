package pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISaveDataCommandListener;

public class SaveDataCommand extends CommandBase {

    private Set<ISaveDataCommandListener> listeners = new HashSet<>();

    public void addCommandListener(ISaveDataCommandListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public String getName() {
        return "cma:save";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "cma:save";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (ISaveDataCommandListener listener : listeners) {
            listener.onDataSaveRequested(sender);
        }
    }
    
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }    
}