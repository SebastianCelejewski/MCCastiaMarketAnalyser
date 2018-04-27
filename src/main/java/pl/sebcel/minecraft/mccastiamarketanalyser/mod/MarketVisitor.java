package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IVisitMarketCommandListener;

public class MarketVisitor implements IVisitMarketCommandListener {

    @Override
    public void onMarketVisitRequested(ICommandSender sender) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 1; i < 127; i++) {
                    Minecraft.getMinecraft().player.sendChatMessage("/market shop" + i);
                    try {
                        Thread.sleep(5000);
                    } catch (Exception ex) {
                        // intentional
                    }
                }
            }
        }).start();
    }
}