package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.util.List;

import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands.AnalyseMarketCommand;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands.LoadDataCommand;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.commands.SaveDataCommand;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.MarketData;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.IAnalyseMarketCommandListener;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.export.MarketDataExporter;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.export.RawSignTextExporter;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.export.ShopListExporter;

@Mod(modid = CastiaMarketAnalyserMod.MODID, name = CastiaMarketAnalyserMod.NAME, version = CastiaMarketAnalyserMod.VERSION)
public class CastiaMarketAnalyserMod implements IAnalyseMarketCommandListener {

    public static final String MODID = "castiamarketanalyser";
    public static final String NAME = "CastiaMC Market Analyser";
    public static final String VERSION = "1.0";

    public static final String PLUGIN_DIRECTORY_NAME = "market";

    private PlayerMoveHandler playerMoveHandler = new PlayerMoveHandler();
    private SignAnalyser signAnalyser = new SignAnalyser();
    private RawSignTextExporter rawSignTextLogger = new RawSignTextExporter();
    private MarketDataAnalyser marketDataAnalyser = new MarketDataAnalyser();
    private ShopListExporter shopListExporter = new ShopListExporter();
    private MarketDataExporter marketDataExporter = new MarketDataExporter();

    private AnalyseMarketCommand analyseMarketCommand = new AnalyseMarketCommand();
    private LoadDataCommand loadDataCommand = new LoadDataCommand();
    private SaveDataCommand saveDataCommand = new SaveDataCommand();

    @EventHandler
    public void init(FMLInitializationEvent event) {
        System.out.println("Initializing CastiaMarketAnalyser mod");

        rawSignTextLogger.initialize();

        loadDataCommand.addCommandListener(marketDataAnalyser);
        loadDataCommand.addCommandListener(marketDataExporter);
        saveDataCommand.addCommandListener(marketDataAnalyser);
        saveDataCommand.addCommandListener(marketDataExporter);
        playerMoveHandler.addSignFoundListener(rawSignTextLogger);
        playerMoveHandler.addSignFoundListener(signAnalyser);
        signAnalyser.addShopOfferFoundListener(marketDataAnalyser);
        signAnalyser.addShopInfoFoundListener(shopListExporter);
        analyseMarketCommand.addCommandListener(this);
        signAnalyser.addShopInfoFoundListener(marketDataExporter);

        MinecraftForge.EVENT_BUS.register(playerMoveHandler);
        ClientCommandHandler.instance.registerCommand(analyseMarketCommand);
        ClientCommandHandler.instance.registerCommand(loadDataCommand);
        ClientCommandHandler.instance.registerCommand(saveDataCommand);
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