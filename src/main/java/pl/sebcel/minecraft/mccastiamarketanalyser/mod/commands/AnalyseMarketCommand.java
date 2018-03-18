package pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IAnalyseMarketCommandListener;

public class AnalyseMarketCommand extends CommandBase {
    
    private Set<IAnalyseMarketCommandListener> listeners = new HashSet<>();

    public void addCommandListener(IAnalyseMarketCommandListener listener) {
        this.listeners.add(listener);
    }
    
    @Override
    public String getName() {
        return "cma:analyse";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "cma:analyse";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (IAnalyseMarketCommandListener listener : listeners) {
            listener.onAnalyseMarketCommand();
        }
    }
    
    public boolean checkPermission(MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}