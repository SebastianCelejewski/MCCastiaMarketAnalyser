package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;

public class MarketData implements IShopOfferFoundListener, IShopInfoFoundListener {

    private Map<String, Map<String, ShopOffer>> offers = new HashMap<>();
    private Map<String, Integer> shops = new HashMap<>();

    public MarketData() {
        Thread marketSaveThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    try {

                        String superOffers = "";

                        for (String productName : offers.keySet()) {
                            File outputFile = new File("market" + File.separator + productName + ".csv");
                            outputFile.getParentFile().mkdirs();
                            FileWriter fw = new FileWriter(outputFile, false);

                            ShopOffer bestBuyOffer = null;
                            ShopOffer bestSellOffer = null;

                            for (String shopOwner : offers.get(productName).keySet()) {
                                ShopOffer offerInfo = offers.get(productName).get(shopOwner);
                                String shopIdStr = "";
                                Integer shopId = shops.get(shopOwner);
                                if (shopId != null) {
                                    shopIdStr = "Shop" + shopId;
                                }

                                if (offerInfo.getItemBuyPrice() != null) {
                                    if (bestBuyOffer == null || bestBuyOffer.getItemBuyPrice() > offerInfo.getItemBuyPrice()) {
                                        bestBuyOffer = offerInfo;
                                    }
                                }

                                if (offerInfo.getItemSellPrice() != null) {
                                    if (bestSellOffer == null || bestSellOffer.getItemSellPrice() < offerInfo.getItemSellPrice()) {
                                        bestSellOffer = offerInfo;
                                    }
                                }

                                fw.write(shopIdStr + "," + shopOwner + "," + offerInfo.getStockSize() + "," + offerInfo.getRawOfferString() + "," + offerInfo.getStockBuyPrice() + "," + offerInfo.getStockSellPrice() + "," + offerInfo.getItemBuyPrice() + "," + offerInfo.getItemSellPrice() + "\n");
                            }

                            fw.write("\n");
                            if (bestBuyOffer != null) {
                                fw.write("Best buy offer: " + bestBuyOffer.getItemBuyPrice() + ", " + bestBuyOffer.getOwnerName() + "\n");
                            }
                            if (bestSellOffer != null) {
                                fw.write("Best sell offer: " + bestSellOffer.getItemSellPrice() + ", " + bestSellOffer.getOwnerName() + "\n");
                            }
                            if (bestBuyOffer != null && bestSellOffer != null && bestBuyOffer.getItemBuyPrice() < bestSellOffer.getItemSellPrice()) {
                                String buyShopId = getShopIdByOwnerName(bestBuyOffer.getOwnerName());
                                String sellShopId = getShopIdByOwnerName(bestSellOffer.getOwnerName());

                                superOffers += bestBuyOffer.getProductName() + " from " + bestBuyOffer.getOwnerName() + " (" + buyShopId + ") " + bestBuyOffer.getItemBuyPrice() + " to " + bestSellOffer.getOwnerName() + " " + bestSellOffer.getItemSellPrice() + " (" + sellShopId + ")\n";
                            }

                            fw.close();
                        }

                        File outputFile = new File("market" + File.separator + "_superoffers.txt");
                        outputFile.getParentFile().mkdirs();
                        FileWriter fw = new FileWriter(outputFile);
                        fw.write(superOffers);
                        fw.close();

                        outputFile = new File("market" + File.separator + "_shops.csv");
                        outputFile.getParentFile().mkdirs();
                        Comparator<? super Entry<String, Integer>> comparator = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
                        final StringBuilder sb = new StringBuilder();
                        shops.entrySet().stream().sorted(comparator).forEach(x -> sb.append(x.getValue() + "," + x.getKey() + "\n"));
                        fw = new FileWriter(outputFile, false);
                        fw.write(sb.toString());
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

    private String getShopIdByOwnerName(String ownerName) {
        if (ownerName == null) {
            return "";
        }
        Integer shopId = shops.get(ownerName);
        if (shopId == null) {
            return null;
        }
        return "Shop" + shopId;
    }

    @Override
    public void onShopOfferFound(ShopOffer shopOffer) {
        String productName = shopOffer.getProductName();
        if (!offers.containsKey(productName)) {
            offers.put(productName, new HashMap<>());
        }
        Map<String, ShopOffer> pricesForProduct = offers.get(productName);

        String ownerName = shopOffer.getOwnerName();
        pricesForProduct.put(ownerName, shopOffer);
    }

    @Override
    public void onShopInfoFound(ShopInfo shopInfo) {
        shops.put(shopInfo.getOwnerName(), shopInfo.getShopId());
    }
}