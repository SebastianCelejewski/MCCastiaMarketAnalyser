package pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain;

import java.util.List;

public class MarketData {

    private String productName;

    private List<ShopOffer> buyOffers;

    private List<ShopOffer> sellOffers;

    private boolean tradeOpportunity;

    public MarketData(String productName, List<ShopOffer> buyOffers, List<ShopOffer> sellOffers, boolean tradeOpportunity) {
        this.productName = productName;
        this.buyOffers = buyOffers;
        this.sellOffers = sellOffers;
        this.tradeOpportunity = tradeOpportunity;
    }

    public String getProductName() {
        return productName;
    }

    public List<ShopOffer> getBuyOffers() {
        return buyOffers;
    }

    public List<ShopOffer> getSellOffers() {
        return sellOffers;
    }

    public boolean isTradeOpportunity() {
        return tradeOpportunity;
    }

}