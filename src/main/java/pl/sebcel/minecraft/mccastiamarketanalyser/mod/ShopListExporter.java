package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopInfo;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IShopInfoFoundListener;

public class ShopListExporter implements IShopInfoFoundListener {

    private Map<Integer, String> shops = new HashMap<>();

    @Override
    public void onShopInfoFound(ShopInfo shopInfo) {
        shops.put(shopInfo.getShopId(), shopInfo.getOwnerName());
        exportShopInfo();
    }

    private void exportShopInfo() {
        try {
            File outputFile = new File(CastiaMarketAnalyserMod.PLUGIN_DIRECTORY_NAME + File.separator + "_shops.csv");
            outputFile.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(outputFile, false);
            fw.write(shops.entrySet().stream().map(x -> x.getKey() + "," + x.getValue()).collect(Collectors.joining("\n")));
            fw.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}