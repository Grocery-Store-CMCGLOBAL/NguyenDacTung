/**
 * Author: tungnd
 * create date: 23/05/2022
 */
package store.checkout.constant;

/**
 * Type of Sales
 */
public enum TypeSales {
	// no promotions
	NORMAL("01"), 
	// buy one, get one free 
	// only products sold by piece
	TYPE_02("02"), 
	// buy tow, get one free
	// only products sold by piece
	TYPE_03("03"),
	// discount 25%
	TYPE_04("04");

	private String value;

	private TypeSales(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return value;
	}

	public static void main(String[] args) {
		System.out.println(TypeSales.NORMAL.toString());
	}
}
