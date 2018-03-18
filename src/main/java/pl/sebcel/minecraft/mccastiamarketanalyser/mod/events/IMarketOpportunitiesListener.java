package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import java.util.List;
import java.util.Map;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopOffer;

public interface IMarketOpportunitiesListener {

    public void onMarketOpportunitiesCalculated(Map<String, List<ShopOffer>> buyOffers, List<ShopOffer> sellOffers);

}