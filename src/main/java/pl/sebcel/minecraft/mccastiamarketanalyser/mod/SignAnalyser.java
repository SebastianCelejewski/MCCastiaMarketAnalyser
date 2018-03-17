package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.text.ITextComponent;

public class SignAnalyser {

    public void analyseSign(TileEntitySign sign) {
        ITextComponent[] signText = ((TileEntitySign) sign).signText;
        String fullSignText = "";
        for (int i = 0; i < signText.length; i++) {
            fullSignText += signText[i].getUnformattedText() + " ";
        }
        System.out.println("(" + sign.getPos().getX() + "," + sign.getPos().getY() + "," + sign.getPos().getZ() + "): " + fullSignText);
    }
}