package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.ICommandSender;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.MarketData;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopOffer;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ILoadDataCommandListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISaveDataCommandListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopOfferFoundListener;

public class MarketDataAnalyser implements IShopOfferFoundListener, ILoadDataCommandListener, ISaveDataCommandListener {

    private final static String PRODUCT_NAMES_FILE_NAME = "market" + File.separator + "products.dta";
    private final static String OFFERS_FILE_NAME = "market" + File.separator + "offers.dta";

    private Set<String> productNames = new HashSet<>();
    private Map<String, Map<String, ShopOffer>> offers = new HashMap<>();

    private Predicate<? super ShopOffer> isBuyOffer = x -> x.getItemBuyPrice() != null;
    private Predicate<? super ShopOffer> isSellOffer = x -> x.getItemSellPrice() != null;
    private Comparator<? super ShopOffer> buyPriceAscending = (o1, o2) -> o1.getItemBuyPrice().compareTo(o2.getItemBuyPrice());
    private Comparator<? super ShopOffer> sellPriceDescending = (o1, o2) -> -o1.getItemSellPrice().compareTo(o2.getItemSellPrice());

    @Override
    public void onShopOfferFound(ShopOffer shopOffer) {
        String productName = shopOffer.getProductName();
        String ownerName = shopOffer.getOwnerName();
        productNames.add(productName);
        if (!offers.containsKey(productName)) {
            offers.put(productName, new HashMap<>());
        }
        offers.get(productName).put(ownerName, shopOffer);
    }

    public List<MarketData> analyseMarketData() {
        List<MarketData> result = new ArrayList<>();

        for (String productName : productNames) {

            List<ShopOffer> buyOffers = offers.getOrDefault(productName, new HashMap<String, ShopOffer>()).values().stream().filter(isBuyOffer).sorted(buyPriceAscending).collect(Collectors.toList());

            List<ShopOffer> sellOffers = offers.getOrDefault(productName, new HashMap<String, ShopOffer>()).values().stream().filter(isSellOffer).sorted(sellPriceDescending).collect(Collectors.toList());

            System.out.println("Found " + buyOffers.size() + " buy offers and " + sellOffers.size() + " sell offers for " + productName);
            if (offers.containsKey(productName)) {
                System.out.println("All offers for " + productName + ": " + offers.get(productName).size());
            }

            boolean tradeOpportunity = buyOffers.size() > 0 && sellOffers.size() > 0 && buyOffers.get(0).getItemBuyPrice() < sellOffers.get(0).getItemSellPrice();

            result.add(new MarketData(productName, buyOffers, sellOffers, tradeOpportunity));
        }

        return result;
    }

    @Override
    public void onDataSaveRequested(ICommandSender sender) {
        try {
            File productNamesFile = new File(PRODUCT_NAMES_FILE_NAME);
            productNamesFile.getParentFile().mkdirs();
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(productNamesFile));
            out.writeObject(productNames);
            out.close();

            File offersFile = new File(OFFERS_FILE_NAME);
            offersFile.getParentFile().mkdirs();
            out = new ObjectOutputStream(new FileOutputStream(offersFile));
            out.writeObject(offers);
            out.close();

            System.out.println("Successfully saved " + productNames.size() + " product names and " + offers.values().size() + " offers");
        } catch (Exception ex) {
            System.err.println("Failed to save market data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void onDataLoadRequested(ICommandSender sender) {
        try {
            File productNamesFile = new File(PRODUCT_NAMES_FILE_NAME);
            File offersFile = new File(OFFERS_FILE_NAME);

            if (productNamesFile.exists() && offersFile.exists()) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(productNamesFile));
                productNames = (Set<String>) in.readObject();
                in.close();

                in = new ObjectInputStream(new FileInputStream(offersFile));
                offers = (Map<String, Map<String, ShopOffer>>) in.readObject();
                in.close();

                System.out.println("Successfully loaded " + productNames.size() + " product names and " + offers.values().size() + " offers");
            } else {
                System.out.println("Data files not found");
            }

        } catch (Exception ex) {
            System.err.println("Failed to load market data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}