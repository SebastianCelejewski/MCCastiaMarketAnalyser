package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;

public class MarketDataExporter implements IShopInfoFoundListener {

    private Map<String, String> shopInfos = new HashMap<>();

    public void exportMarketData(MarketData marketData) {
        try {
            String shortFileName = marketData.getProductName() + ".csv";
            if (marketData.isTradeOpportunity()) {
                shortFileName = "_" + shortFileName;
            }

            File outputFile = new File("market" + File.separator + shortFileName);
            outputFile.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(outputFile);

            fw.write("Buy offers:\n");
            marketData.getBuyOffers().forEach(x -> writeBuyOffer(fw, x));

            fw.write("\nSell offers:\n");
            marketData.getSellOffers().forEach(x -> writeSellOffer(fw, x));

            fw.close();
        } catch (Exception ex) {
            System.err.println("Failed to export market data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void writeBuyOffer(Writer out, ShopOffer offer) {
        try {
            out.write(offer.getOwnerName() + " (" + getShopIdForOwnerName(offer.getOwnerName()) + ")," + offer.getItemBuyPrice() + "\n");
        } catch (Exception ex) {
            // intentional - nothing to do here
        }
    }

    private void writeSellOffer(Writer out, ShopOffer offer) {
        try {
            out.write(offer.getOwnerName() + " (" + getShopIdForOwnerName(offer.getOwnerName()) + ")," + offer.getItemSellPrice() + "\n");
        } catch (Exception ex) {
            // intentional - nothing to do here
        }
    }

    @Override
    public void onShopInfoFound(ShopInfo shopInfo) {
        shopInfos.put(shopInfo.getOwnerName(), "Shop" + Integer.toString(shopInfo.getShopId()));
    }

    private String getShopIdForOwnerName(String ownerName) {
        if (shopInfos.containsKey(ownerName)) {
            return shopInfos.get(ownerName);
        } else {
            return "";
        }
    }
}