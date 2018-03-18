package pl.sebcel.minecraft.mccastiamarketanalyser.mod.events;

import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.ShopInfo;

public interface IShopInfoFoundListener {
    
    public void onShopInfoFound(ShopInfo shopInfo);

}
