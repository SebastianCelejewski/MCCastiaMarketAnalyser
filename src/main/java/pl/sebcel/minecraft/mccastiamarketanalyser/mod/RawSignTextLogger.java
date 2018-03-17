package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import java.io.File;
import java.io.FileWriter;

import net.minecraft.tileentity.TileEntitySign;

public class RawSignTextLogger {

    private String outputDirectoryName = "signs";
    private String outputFileName = "signs.txt";

    public void setOutputDirectoryName(String outputDirectoryName) {
        this.outputDirectoryName = outputDirectoryName;
    }

    public void saveSign(TileEntitySign sign) {
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
        File outputFolder = new File(outputDirectoryName);
        File outputFile = new File(outputDirectoryName + File.separator + outputFileName);
        FileWriter fw = null;
        try {
            outputFolder.mkdirs();
            fw = new FileWriter(outputFile, true);
            fw.write(signText + "\n");
            fw.close();
        } catch (Exception ex) {
            System.err.println("Failed to save sing text to " + outputFolder.getAbsolutePath() + File.separator + outputFileName + ": " + ex.getMessage());
        } finally {
            try {
                fw.close();
            } catch (Exception ex) {
                // intentional - nothing to do here
            }
        }

    }
}
