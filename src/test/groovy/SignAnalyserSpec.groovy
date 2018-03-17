import spock.lang.*
import pl.sebcel.minecraft.mccastiamarketanalyser.mod.*

class SignAnalyserSpec extends Specification {

	def cut = new SignAnalyser()

	def "when sign contains no text it is not shop offer"() {
		String[] lines = ["", "", "", ""]
		expect: cut.parseShopOffer(lines) == null
	}
	
	def "when sign contains less than four lines it is not shop offer"() {
		String[] lines = ["A", "B", "C", ""]
		expect: cut.parseShopOffer(lines) == null
	}
	
	def "owner, amount, price, product pattern"() {
		String[] lines = [ "owner-name", "64", "B 1500", "Sulphur" ]
		ShopOffer offer = cut.parseShopOffer(lines)
		expect:
			offer.ownerName == "owner-name"
			offer.stockSize == 64
			offer.productName == "Sulphur"
			offer.rawOfferString == "B 1500"
	}
	
	def "when sign contains no text it is not shop info"() {
		String[] lines = ["", "", "", ""]
		expect: cut.parseShopInfo(lines) == null                                                   
	}

	def "when sign contains less than four lines it is not shop info"() {
		String[] lines = ["A", "B", "C", ""]
		expect: cut.parseShopInfo(lines) == null
	}

	def "when first line is not [Rented] it is not shop info"() {
		String[] lines = ["A", "B", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "when second line does not start with 'shop' it is not shop info"() {
		String[] lines = ["[Rented]", "B", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "when second line does not end with a number it is not shop info"() {
		String[] lines = ["[Rented]", "shophop", "C", "D"]
		expect: cut.parseShopInfo(lines) == null
	}
	
	def "should extract shop info"() {
		String[] lines = ["[Rented]", "shop7", "shop-owner", "D"]
		ShopInfo shopInfo = cut.parseShopInfo(lines)
		expect:
			shopInfo != null
			shopInfo.shopId == 7
			shopInfo.ownerName == "shop-owner"
	}
}