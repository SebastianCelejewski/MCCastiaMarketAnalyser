package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.command.ICommandSender;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.MarketData;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopInfo;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopOffer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ILoadDataCommandListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISaveDataCommandListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;

public class MarketDataExporter implements IShopInfoFoundListener, ILoadDataCommandListener, ISaveDataCommandListener {

    private final static String SHOP_INFO_FILE_NAME = "market" + File.separator + "shopInfo.dta";

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

    @Override
    public void onDataSaveRequested(ICommandSender sender) {
        try {
            File outputFile = new File(SHOP_INFO_FILE_NAME);
            outputFile.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(outputFile));
            out.writeObject(shopInfos);
            out.close();
            System.out.println("Successfully saved shop info for " + shopInfos.size() + " shops");
        } catch (Exception ex) {
            System.err.println("Failed to save shop info: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDataLoadRequested(ICommandSender sender) {
        try {
            File inputFile = new File(SHOP_INFO_FILE_NAME);
            if (inputFile.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(inputFile));
                shopInfos = (Map<String, String>) in.readObject();
                in.close();
                System.out.println("Successfully loaded shop info for " + shopInfos.size() + " shops");
            } else {
                System.out.println("Not loaded shop infos - data file not found");
            }
        } catch (Exception ex) {
            System.err.println("Failed to load shop info: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}