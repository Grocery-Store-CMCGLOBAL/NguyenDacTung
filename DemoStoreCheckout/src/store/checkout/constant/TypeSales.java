 * Author: tungnd
 * create date: 23/05/2022
 */
package store.checkout.constant;

/**
 * Type of Sales
 */
public enum TypeSales {
	// no promotions
	NO_PROMOTIONS("01"),
	// buy one, get one free
	BUY_ONE_GET_ONE("02"),
	// buy tow, get one free
	BUY_TOW_GET_ONE("03");

	private String value;

	private TypeSales(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static void main(String[] args) {
		System.out.println(TypeSales.NO_PROMOTIONS.toString());
	}
}
