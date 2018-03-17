package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

public class ShopOffer {

    private String ownerName;
    private String productName;
    private String rawOfferString;
    private int stockSize;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStockSize() {
        return stockSize;
    }

    public void setStockSize(int stockSize) {
        this.stockSize = stockSize;
    }

    public String getRawOfferString() {
        return rawOfferString;
    }

    public void setRawOfferString(String rawOfferString) {
        this.rawOfferString = rawOfferString;
    }

    
}