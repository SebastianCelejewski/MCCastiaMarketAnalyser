package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;

import net.minecraft.tileentity.TileEntitySign;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.events.ISignFoundListener;

public class RawSignTextLogger implements ISignFoundListener {

    private String outputDirectoryName = "signs";
    private String outputFileName = "signs.txt";
    private FileWriter fw;

    public void setOutputDirectoryName(String outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public void initialize() {
        File outputFolder = new File(outputDirectoryName);
        File outputFile = new File(outputDirectoryName + File.separator + outputFileName);
        outputFolder.mkdirs();
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
            System.err.println("Failed to save sing text to " + outputDirectoryName + File.separator + outputFileName + ": " + ex.getMessage());
        }
    }
}