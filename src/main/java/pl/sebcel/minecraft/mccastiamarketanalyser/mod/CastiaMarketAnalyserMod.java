package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.List;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IAnalyseMarketCommandListener;

@Mod(modid = CastiaMarketAnalyserMod.MODID, name = CastiaMarketAnalyserMod.NAME, version = CastiaMarketAnalyserMod.VERSION)
public class CastiaMarketAnalyserMod implements IAnalyseMarketCommandListener {

    public static final String MODID = "castiamarketanalyser";
    public static final String NAME = "CastiaMC Market Analyser";
    public static final String VERSION = "1.0";

    private PlayerMoveHandler playerMoveHandler;
    private SignAnalyser signAnalyser;
    private RawSignTextLogger rawSignTextLogger;
    private MarketDataAnalyser marketDataAnalyser;
    private ShopListExporter shopListExporter;
    private AnalyseMarketCommand analyseMarketCommand;
    private MarketDataExporter marketDataExporter;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        System.out.println("FMLPreInitializationEvent");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("FMLInitializationEvent");
        playerMoveHandler = new PlayerMoveHandler();
        signAnalyser = new SignAnalyser();
        rawSignTextLogger = new RawSignTextLogger();
        marketDataAnalyser = new MarketDataAnalyser();
        shopListExporter = new ShopListExporter();
        analyseMarketCommand = new AnalyseMarketCommand();
        marketDataExporter = new MarketDataExporter();

        rawSignTextLogger.initialize();
        marketDataAnalyser.initialize();

//        playerMoveHandler.addSignFoundListener(rawSignTextLogger);
        playerMoveHandler.addSignFoundListener(signAnalyser);
        rawSignTextLogger.setOutputDirectoryName("signs");
        signAnalyser.addShopOfferFoundListener(marketDataAnalyser);
        signAnalyser.addShopInfoFoundListener(shopListExporter);
        analyseMarketCommand.addCommandListener(this);
        signAnalyser.addShopInfoFoundListener(marketDataExporter);

        MinecraftForge.EVENT_BUS.register(playerMoveHandler);
        ClientCommandHandler.instance.registerCommand(analyseMarketCommand);
    }

    @Override
    public void onAnalyseMarketCommand() {
        System.out.println("Analysing market data");
        List<MarketData> marketData = marketDataAnalyser.analyseMarketData();
        System.out.println("Market data analysed for " + marketData.size() + " products. Exporting");
        marketData.stream().forEach(x -> marketDataExporter.exportMarketData(x));
        System.out.println("Export completed");
    }
}