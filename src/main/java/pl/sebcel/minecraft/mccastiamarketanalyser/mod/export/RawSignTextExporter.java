package pl.sebcel.minecraft.mccastiamarketanalyser.mod.export;

import java.io.File;
import java.io.FileWriter;

import net.minecraft.tileentity.TileEntitySign;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.CastiaMarketAnalyserMod;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISignFoundListener;

public class RawSignTextExporter implements ISignFoundListener {

    private final static String SIGNS_CONTENT_FILE_NAME = CastiaMarketAnalyserMod.PLUGIN_DIRECTORY_NAME + File.separator + "__signs.log";

    private FileWriter fw;

    public void initialize() {
        File outputFile = new File(SIGNS_CONTENT_FILE_NAME);
        outputFile.getParentFile().mkdirs();
        try {
            fw = new FileWriter(outputFile, true);
        } catch (Exception ex) {
            System.err.println("Failed to initialize RawSignTextLogger: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void onSignFound(TileEntitySign sign) {
        int x = sign.getPos().getX();
        int y = sign.getPos().getY();
        int z = sign.getPos().getZ();
        String line = x + "," + y + "," + z;
        for (int i = 0; i < sign.signText.length; i++) {
            line += "," + sign.signText[i].getUnformattedText();
        }
        saveSignTextToFile(line);
    }

    private void saveSignTextToFile(String signText) {
        try {
            if (fw != null) {
                fw.write(signText + "\n");
                fw.flush();
            }
        } catch (Exception ex) {
            System.err.println("Failed to save sing text to " + SIGNS_CONTENT_FILE_NAME + ": " + ex.getMessage());
        }
    }
}