package pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain;

import java.io.Serializable;

public class ShopInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
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