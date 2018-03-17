package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;

public class MarketData implements IShopOfferFoundListener, IShopInfoFoundListener {

    private Map<String, Map<String, String>> offers = new HashMap<>();
    private Map<String, Integer> shops = new HashMap<>();

    public MarketData() {
        Thread marketSaveThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        for (String productName : offers.keySet()) {
                            File outputFile = new File("market" + File.separator + "prices-" + productName + ".csv");
                            outputFile.getParentFile().mkdirs();
                            FileWriter fw = new FileWriter(outputFile, false);
                            for (String shopOwner : offers.get(productName).keySet()) {
                                String offer = offers.get(productName).get(shopOwner);
                                fw.write(shopOwner + "," + offer + "\n");
                            }

                            fw.close();
                        }

                        File outputFile = new File("market" + File.separator + "shops.csv");
                        outputFile.getParentFile().mkdirs();
                        FileWriter fw = new FileWriter(outputFile, false);
                        for (Map.Entry<String, Integer> entry : shops.entrySet()) {
                            fw.write(entry.getValue() + "," + entry.getKey() + "\n");
                        }

                        fw.close();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (Exception ex) {
                        // intentional - nothing to do here
                    }
                }

            }
        });
        marketSaveThread.start();
    }

    @Override
    public void onShopOfferFound(ShopOffer shopOffer) {
        String productName = shopOffer.getProductName();
        if (!offers.containsKey(productName)) {
            offers.put(productName, new HashMap<>());
        }
        Map<String, String> pricesForProduct = offers.get(productName);

        String ownerName = shopOffer.getOwnerName();
        String offerValue = shopOffer.getStockSize() + "/" + shopOffer.getRawOfferString();
        pricesForProduct.put(ownerName, offerValue);
    }

    @Override
    public void onShopInfoFound(ShopInfo shopInfo) {
        shops.put(shopInfo.getOwnerName(), shopInfo.getShopId());
    }
}