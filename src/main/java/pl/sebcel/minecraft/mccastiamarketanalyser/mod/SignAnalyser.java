package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISignFoundListener;

public class SignAnalyser implements ISignFoundListener {

    private Set<IShopOfferFoundListener> shopOfferFoundListeners = new HashSet<>();
    private Set<IShopInfoFoundListener> shopInfoFoundListeners = new HashSet<>();

    public void addShopOfferFoundListener(IShopOfferFoundListener shopOfferFoundListener) {
        this.shopOfferFoundListeners.add(shopOfferFoundListener);
    }

    public void addShopInfoFoundListener(IShopInfoFoundListener shopInfoFoundListener) {
        this.shopInfoFoundListeners.add(shopInfoFoundListener);
    }

    public void onSignFound(TileEntitySign sign) {
        ITextComponent[] signText = ((TileEntitySign) sign).signText;
        String[] lines = new String[signText.length];
        for (int i = 0; i < signText.length; i++) {
            lines[i] = signText[i].getUnformattedText();
        }

        try {
            ShopOffer shopOffer = parseShopOffer(lines);
            if (shopOffer != null) {
                for (IShopOfferFoundListener listener : shopOfferFoundListeners) {
                    listener.onShopOfferFound(shopOffer);
                }
            }

            ShopInfo shopInfo = parseShopInfo(lines);
            if (shopInfo != null) {
                for (IShopInfoFoundListener listener : shopInfoFoundListeners) {
                    listener.onShopInfoFound(shopInfo);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ShopOffer parseShopOffer(String[] lines) {
        if (!containsFourNonEmptyStrings(lines)) {
            return null;
        }

        try {
            ShopOffer shopOffer = new ShopOffer();

            String shopOwnerName = lines[0];
            int stockSize = Integer.parseInt(lines[1]);
            String offer = lines[2];
            String productName = lines[3];

            shopOffer.setOwnerName(shopOwnerName);
            shopOffer.setProductName(productName);
            shopOffer.setRawOfferString(offer);
            shopOffer.setStockSize(stockSize);

            return shopOffer;
        } catch (Exception ex) {
            return null;
        }
    }

    // [Rented],shop3,JuumpyMC,01-04 21:35
    public ShopInfo parseShopInfo(String[] lines) {
        if (!containsFourNonEmptyStrings(lines)) {
            return null;
        }

        if (!lines[0].equals("[Rented]")) {
            return null;
        }

        if (!lines[1].startsWith("shop")) {
            return null;
        }

        try {
            String shopName = lines[1];
            String ownerName = lines[2];
            int shopId = Integer.parseInt(shopName.substring(4));
            return new ShopInfo(shopId, ownerName);
        } catch (Exception ex) {
            return null;
        }
    }

    private boolean containsFourNonEmptyStrings(String[] lines) {
        if (lines.length != 4) {
            return false;
        }
        if (lines[0].trim().length() == 0) {
            return false;
        }
        if (lines[1].trim().length() == 0) {
            return false;
        }
        if (lines[2].trim().length() == 0) {
            return false;
        }
        if (lines[3].trim().length() == 0) {
            return false;
        }

        return true;
    }
}