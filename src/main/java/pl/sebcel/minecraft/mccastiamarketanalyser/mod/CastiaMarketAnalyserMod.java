package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CastiaMarketAnalyserMod.MODID, name = CastiaMarketAnalyserMod.NAME, version = CastiaMarketAnalyserMod.VERSION)
public class CastiaMarketAnalyserMod {

    public static final String MODID = "castiamarketanalyser";
    public static final String NAME = "CastiaMC Market Analyser";
    public static final String VERSION = "1.0";

    private PlayerMoveHandler playerMoveHandler;
    private SignAnalyser signAnalyser;
    private RawSignTextLogger rawSignTextLogger;
    private MarketData marketData;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        playerMoveHandler = new PlayerMoveHandler();
        signAnalyser = new SignAnalyser();
        rawSignTextLogger = new RawSignTextLogger();
        marketData = new MarketData();
        
        rawSignTextLogger.initialize();

        playerMoveHandler.addSignFoundListener(rawSignTextLogger);
        playerMoveHandler.addSignFoundListener(signAnalyser);
        rawSignTextLogger.setOutputDirectoryName("signs");
        signAnalyser.addShopOfferFoundListener(marketData);
        signAnalyser.addShopInfoFoundListener(marketData);

        MinecraftForge.EVENT_BUS.register(playerMoveHandler);
    }

}