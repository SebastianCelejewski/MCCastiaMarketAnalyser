package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISignFoundListener;

public class SignAnalyser implements ISignFoundListener {

    private Set<IShopOfferFoundListener> shopOfferFoundListeners = new HashSet<>();

    public void addShopOfferFoundListener(IShopOfferFoundListener shopOfferFoundListener) {
        this.shopOfferFoundListeners.add(shopOfferFoundListener);
    }

    public void onSignFound(TileEntitySign sign) {
        ITextComponent[] signText = ((TileEntitySign) sign).signText;
        String[] lines = new String[signText.length];
        for (int i = 0; i < signText.length; i++) {
            lines[i] = signText[i].getUnformattedText();
        }

        ShopOffer shopOffer = new ShopOffer();

        try {
            // Ron21,64,B1050,Coal Ore
            for (int i = 0; i < lines.length; i++) {
                System.out.println("Line " + i + ": " + lines[i]);
            }
            String shopOwnerName = lines[0];
            int stockSize = Integer.parseInt(lines[1]);
            String offer = lines[2];
            String productName = lines[3];

            shopOffer.setOwnerName(shopOwnerName);
            shopOffer.setProductName(productName);
            shopOffer.setOffer(offer);
            shopOffer.setStockSize(stockSize);

            for (IShopOfferFoundListener listener : shopOfferFoundListeners) {
                listener.onShopOfferFound(shopOffer);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}