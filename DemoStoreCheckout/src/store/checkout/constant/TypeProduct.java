/**
 * Author: tungnd
 * create date: 23/05/2022
 */
package store.checkout.constant;

/**
 * Type of Product
 */
public enum TypeProduct {
	// products sold by piece
	PIECE_PRODUCT("01"), 
	// products sold in bulk
	BULK_PRODUCT("02");

	private String value;

	private TypeProduct(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}
}
