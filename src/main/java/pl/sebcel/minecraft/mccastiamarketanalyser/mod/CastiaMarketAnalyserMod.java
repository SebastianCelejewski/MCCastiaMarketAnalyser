package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = CastiaMarketAnalyserMod.MODID, name = CastiaMarketAnalyserMod.NAME, version = CastiaMarketAnalyserMod.VERSION)
public class CastiaMarketAnalyserMod implements ISignFoundListener {

    public static final String MODID = "castiamarketanalyser";
    public static final String NAME = "CastiaMC Market Analyser";
    public static final String VERSION = "1.0";
    
    private PlayerMoveHandler playerMoveHandler;
    private SignAnalyser signAnalyser;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        playerMoveHandler = new PlayerMoveHandler();
        signAnalyser = new SignAnalyser();
        playerMoveHandler.setSignFoundListener(this);
        
        MinecraftForge.EVENT_BUS.register(playerMoveHandler);
    }
    
    public void onSignFound(TileEntitySign sign) {
        signAnalyser.analyseSign(sign);
    }

}