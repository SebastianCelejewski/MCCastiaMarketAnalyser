package pl.sebcel.minecraft.mccastiamarketanalyser.mod;

public class ShopOffer {

    private String ownerName;
    private String productName;
    private String rawOfferString;
    private int stockSize;
    private Integer stockBuyPrice;
    private Integer stockSellPrice;

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

    public Integer getStockBuyPrice() {
        return stockBuyPrice;
    }

    public void setStockBuyPrice(Integer stockBuyPrice) {
        this.stockBuyPrice = stockBuyPrice;
    }

    public Integer getStockSellPrice() {
        return stockSellPrice;
    }

    public void setStockSellPrice(Integer stockSellPrice) {
        this.stockSellPrice = stockSellPrice;
    }
    
    public Double getItemSellPrice() {
        if (stockSellPrice != null) {
            return stockSellPrice.doubleValue() / stockSize;
        } else {
            return null;
        }
    }
    
    public Double getItemBuyPrice() {
        if (stockBuyPrice != null) {
            return stockBuyPrice.doubleValue() / stockSize;
        } else {
            return null;
        }
    }
}