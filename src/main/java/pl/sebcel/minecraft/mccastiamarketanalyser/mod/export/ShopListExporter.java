package pl.sebcel.minecraft.mccastiamarketanalyser.mod.export;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.CastiaMarketAnalyserMod;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopInfo;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;

public class ShopListExporter implements IShopInfoFoundListener {

    private final static String SHOP_LIST_FILE_NAME = CastiaMarketAnalyserMod.PLUGIN_DIRECTORY_NAME + File.separator + "__shops.csv";

    private Map<Integer, String> shops = new HashMap<>();

    @Override
    public void onShopInfoFound(ShopInfo shopInfo) {
        shops.put(shopInfo.getShopId(), shopInfo.getOwnerName());
        exportShopInfo();
    }

    private void exportShopInfo() {
        try {
            File outputFile = new File(SHOP_LIST_FILE_NAME);
            outputFile.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(outputFile, false);
            fw.write(shops.entrySet().stream().map(x -> x.getKey() + "," + x.getValue()).collect(Collectors.joining("\n")));
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}