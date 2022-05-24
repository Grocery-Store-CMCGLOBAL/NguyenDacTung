package store.checkout.entity;

public class DetailBill {
	private Product product;

	private Integer numberOfProduct;

	private Float totalPriceProduct;

	private Float weightofProduct;

	/**
	 * @return the product
	 */
	public Product getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		this.product = product;
	}

	/**
	 * @return the numberOfProduct
	 */
	public Integer getNumberOfProduct() {
		return numberOfProduct;
	}

	/**
	 * @param numberOfProduct the numberOfProduct to set
	 */
	public void setNumberOfProduct(Integer numberOfProduct) {
		this.numberOfProduct = numberOfProduct;
	}

	/**
	 * @return the totalPriceProduct
	 */
	public Float getTotalPriceProduct() {
		return totalPriceProduct;
	}

	/**
	 * @param totalPriceProduct the totalPriceProduct to set
	 */
	public void setTotalPriceProduct(Float totalPriceProduct) {
		this.totalPriceProduct = totalPriceProduct;
	}

	/**
	 * @return the weightofProduct
	 */
	public Float getWeightofProduct() {
		return weightofProduct;
	}

	/**
	 * @param weightofProduct the weightofProduct to set
	 */
	public void setWeightofProduct(Float weightofProduct) {
		this.weightofProduct = weightofProduct;
	}

}
