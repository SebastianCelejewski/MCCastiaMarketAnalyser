package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;

public class MarketData implements IShopOfferFoundListener {

    private Map<String, String> offers = new HashMap<>();

    public MarketData() {
        Thread marketSaveThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {
                        File outputFile = new File("market" + File.separator + "prices.csv");
                        outputFile.getParentFile().mkdirs();
                        FileWriter fw = new FileWriter(outputFile, false);
                        for (Map.Entry<String, String> entry : offers.entrySet()) {
                            fw.write(entry.getKey() + "    " + entry.getValue() + "\n");
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
        String offerKey = shopOffer.getOwnerName() + "-" + shopOffer.getProductName() + "-" + shopOffer.getStockSize();
        String offerValue = shopOffer.getOffer();
        offers.put(offerKey, offerValue);
        System.out.println(offerKey + ": " + offerValue);
    }
}