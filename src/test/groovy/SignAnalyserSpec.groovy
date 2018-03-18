import spock.lang.*
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.*
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.domain.*

class SignAnalyserSpec extends Specification {

	def cut = new SignAnalyser()
	
	def "ShopOffer parsing: when sign contains no text, it must not be recognized as a shop offer"() {
		String[] lines = ["", "", "", ""]
		expect: cut.parseShopOffer(lines) == null
	}
	
	def "ShopOffer parsing: when sign contains less than four lines, it must not be recognized as a shop offer"() {
		String[] lines = ["A", "B", "C", ""]
		expect: cut.parseShopOffer(lines) == null
	}
	
	def "ShopOffer parsing: when sign contains owner, amount, price, and product name, it should be recognized as a shop offer"() {
		String[] lines = [ "owner-name", "64", "B 1500", "Sulphur" ]
		ShopOffer offer = cut.parseShopOffer(lines)
		expect:
			offer.ownerName == "owner-name"
			offer.stockSize == 64
			offer.productName == "Sulphur"
			offer.rawOfferString == "B 1500"
	}
	
	def "ShopOffer parsing: when sign contains owner, amount, price, and product name, it should extract buy and sell prices"() {
		String[] lines = [ "owner-name", "64", "S 1200 : B 1500", "Sulphur" ]
		ShopOffer offer = cut.parseShopOffer(lines)
		expect:
			offer.stockBuyPrice == 1500
			offer.stockSellPrice == 1200
	}
	
	def "Shop info parsing: when sign contains no text, it must not be recognized as a shop info"() {
		String[] lines = ["", "", "", ""]
		expect: cut.parseShopInfo(lines) == null                                                   
	}

	def "Shop info parsing: when sign contains less than four lines, it must not be recognized as a shop info"() {
		String[] lines = ["A", "B", "C", ""]
		expect: cut.parseShopInfo(lines) == null
	}

	def "Shop info parsing: when first line is not '[Rented]', it must not be recognized as a shop info"() {
		String[] lines = ["A", "B", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "Shop info parsing: when second line does not start with 'shop', it must not be recognized as a shop info"() {
		String[] lines = ["[Rented]", "B", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "Shop info parsing: when second line does not end with a number, it must not be recognized as a shop info"() {
		String[] lines = ["[Rented]", "shophop", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "Shop info parsing: when sign contains shop info, it should extract shop owner and shop ID"() {
		String[] lines = ["[Rented]", "shop7", "shop-owner", "D"]
		ShopInfo shopInfo = cut.parseShopInfo(lines)
		expect:
			shopInfo != null
			shopInfo.shopId == 7
			shopInfo.ownerName == "shop-owner"
	}
	
	def "Buy price parsing: when raw offer text is null, stock buy price is null"() {
		expect: cut.parseStockBuyPrice(null) == null
	}
	
	def "Buy price parsing: when raw offer text is empty, stock buy price is null"() {
		expect: cut.parseStockBuyPrice("") == null
	}
	
	def "Buy price parsing: when raw offer text does not contain letter 'B', stock buy price is null"() {
		expect:
			cut.parseStockBuyPrice("1500") == null
			cut.parseStockBuyPrice("S 1500") == null
			cut.parseStockBuyPrice("1500 S") == null
	}
	
	def "Buy price parsing: when raw offer text contains buy and sell prices, stock buy price should come from the 'B' fragmet"() {
		expect:
			cut.parseStockBuyPrice("B1500:S1200") == 1500
			cut.parseStockBuyPrice("B 1500:S 1200") == 1500
			cut.parseStockBuyPrice("1500 B : 1200 S") == 1500
			cut.parseStockBuyPrice("S1500:B1200") == 1200
			cut.parseStockBuyPrice("S 1500:B 1200") == 1200
			cut.parseStockBuyPrice("1500 S : 1200 B") == 1200
	}
	
	def "Buy price parsing: when raw offer text contains buy price only, stock buy price should be parsed from the whole line"() {
		expect:
			cut.parseStockBuyPrice("B1500") == 1500
			cut.parseStockBuyPrice("B 1500") == 1500
			cut.parseStockBuyPrice("1500 B") == 1500
	}

	def "Sell price parsing: when raw offer text is null, stock sell price is null"() {
		expect: cut.parseStockSellPrice(null) == null
	}
	
	def "Sell price parsing: when raw offer text is empty, stock sell price is null"() {
		expect: cut.parseStockSellPrice("") == null
	}
	
	def "Sell price parsing: when raw offer text does not contain letter 'S', stock sell price is null"() {
		expect:
			cut.parseStockSellPrice("1500") == null
			cut.parseStockSellPrice("B 1500") == null
			cut.parseStockSellPrice("1500 B") == null
	}
	
	def "Sell price parsing: when raw offer text contains buy and sell prices, stock sell price should come from the 'S' fragmet"() {
		expect:
			cut.parseStockSellPrice("B1500:S1200") == 1200
			cut.parseStockSellPrice("B 1500:S 1200") == 1200
			cut.parseStockSellPrice("1500 B : 1200 S") == 1200
			cut.parseStockSellPrice("S1500:B1200") == 1500
			cut.parseStockSellPrice("S 1500:B 1200") == 1500
			cut.parseStockSellPrice("1500 S : 1200 B") == 1500
	}
	
	def "Sell price parsing: when raw offer text contains sell price only, sell buy price should be parsed from the whole line"() {
		expect:
			cut.parseStockSellPrice("S1500") == 1500
			cut.parseStockSellPrice("S 1500") == 1500
			cut.parseStockSellPrice("1500 S") == 1500
	}
}