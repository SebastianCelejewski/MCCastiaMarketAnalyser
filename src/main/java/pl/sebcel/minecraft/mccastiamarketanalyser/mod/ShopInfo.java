package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

public class ShopInfo {

    private int shopId;
    private String ownerName;

    public ShopInfo(int shopId, String ownerName) {
        this.shopId = shopId;
        this.ownerName = ownerName;
    }

    public int getShopId() {
        return shopId;
    }

    public String getOwnerName() {
        return ownerName;
    }

}