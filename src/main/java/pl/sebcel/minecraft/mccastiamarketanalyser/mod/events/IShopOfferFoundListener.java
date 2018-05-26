package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ChestInfo;
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopOffer;

public interface IShopOfferFoundListener {

    public void onShopOfferFound(ShopOffer shopOffer, ChestInfo chestInfo);
}
