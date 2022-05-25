/**
 * Author: tungnd
 * create date: 23/05/2022
 */
package store.checkout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import store.checkout.constant.TypeProduct;
import store.checkout.constant.TypeSales;
import store.checkout.entity.DetailBill;
import store.checkout.entity.Product;

/**
 * 
 * Demo Grocery store checkout counter
 *
 */
public class StoreCheckout {
	private static SessionFactory factory;

	private static final Pattern positiveNumber = Pattern.compile("\\d+");
	private static final Pattern decimal = Pattern.compile("\\d+(\\.\\d+)?");

	public static void main(String[] args) {
		try {
			factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
		} catch (Throwable ex) {
			System.err.println("Failed to create sessionFactory object." + ex);
			throw new ExceptionInInitializerError(ex);
		}
		initData();
		showMenu();
		boolean flgEnter = true;
		int select = 0;
		List<DetailBill> listDetailBill = new ArrayList<DetailBill>();
		do {
			flgEnter = true;
			System.out.println("Please enter your selection:");
			Scanner scanner = new Scanner(System.in);
			String option = scanner.nextLine();
			Matcher matcher = positiveNumber.matcher(option);
			if (!matcher.matches()) {
				flgEnter = false;
			} else {
				select = Integer.parseInt(option);
				switch (select) {
				case 1:
					checkout(listDetailBill, scanner);
					showMenu();
					break;
				case 2:
					printBill(listDetailBill);
					listDetailBill = new ArrayList<DetailBill>();
					showMenu();
					break;
				case 3:
					listDetailBill = new ArrayList<DetailBill>();
					System.out.println("Thanks you used store checkout");
					break;
				default:
					System.out.println("Invalid input parameters");
					showMenu();
				}
			}
		} while (select != 3 || !flgEnter);
	}

	/**
	 * Print out the bill
	 * 
	 * @param listDetailBill
	 */
	private static void printBill(List<DetailBill> listDetailBill) {
		Float totalMoney = 0.0f;
		Integer index = 0;
		System.out.println("\nRECEIPT: ");
		for (DetailBill detailBill : listDetailBill) {
			Product product = detailBill.getProduct();
			String typeProduct = product.getTypeProduct();
			String typeSales = product.getTypeSales();
			// If the product is in category bulk
			if (TypeProduct.BULK_PRODUCT.toString().equals(typeProduct)) {
				typeProduct = "BULK PRODUCT";
				// If the product is in category piece
			} else if (TypeProduct.PIECE_PRODUCT.toString().equals(typeProduct)) {
				typeProduct = "PIECE PRODUCT";
			}
			// set value for promotion type
			// If type promotion is buy one, get one free
			if (TypeSales.BUY_ONE_GET_ONE.toString().equals(typeSales)) {
				typeSales = "BUY ONE, GET ONE FREE";
				// If type promotion is buy tow, get one free
			} else if (TypeSales.BUY_TOW_GET_ONE.toString().equals(typeSales)) {
				typeSales = "BUY TWO, GET ONE FREE";
				// If type promotion is no promotions
			} else {
				typeSales = "NO PROMOTIONS";
			}
			// output console
			System.out.println("Product number: " + ++index);
			System.out.println("Product Id: " + product.getId());
			System.out.println("Name product: " + product.getNameProduct());
			System.out.println("TypeProduct: " + typeProduct);
			System.out.println("Price Product: " + product.getPrice() + "$");
			if (detailBill.getNumberOfProduct() != null) {
				System.out.println("Quantity: " + detailBill.getNumberOfProduct());
			}
			if (detailBill.getWeightofProduct() != null) {
				System.out.println("Weight: " + detailBill.getWeightofProduct() + "kg");
			}
			System.out.println("Type Sales: " + typeSales);
			System.out.println("total Price: " + detailBill.getTotalPriceProduct() + "$");
			totalMoney += detailBill.getTotalPriceProduct();
			System.out.println("");
		}
		System.out.println("Total Money: " + totalMoney + "$");
	}

	/**
	 * Implement logic when paying
	 * 
	 * @param listDetailBill
	 * @param scanner
	 */
	private static void checkout(List<DetailBill> listDetailBill, Scanner scanner) {
		boolean flgEnter = true;
		Matcher matcher = null;
		DetailBill detailBill = null;
		do {
			flgEnter = true;
			System.out.println("Enter Product Code: ");
			String code = scanner.nextLine();
			matcher = positiveNumber.matcher(code);
			if (!matcher.matches()) {
				flgEnter = false;
			} else {
				Integer idProduct = Integer.parseInt(code);
				Product product = getProduct(idProduct);
				if (Objects.isNull(product) || product.getDeleteFlg() == 1) {
					flgEnter = false;
					System.out.println("You have entered the wrong product code");
				} else {
					boolean flgEnter2 = true;
					if (TypeProduct.PIECE_PRODUCT.toString().equals(product.getTypeProduct())) {
						String stringSl = null;
						Integer intSl = 0;
						do {
							flgEnter2 = true;
							System.out.println("Enter product quantity: ");
							stringSl = scanner.nextLine();
							matcher = positiveNumber.matcher(stringSl);
							if (!matcher.matches()) {
								flgEnter2 = false;
							} else {
								intSl = Integer.parseInt(stringSl);
								if (!exitListDetailBill(listDetailBill, product, TypeProduct.PIECE_PRODUCT.toString(),
										intSl, null)) {
									detailBill = new DetailBill();
									Float totalPriceProduct = salesPromotions(product, detailBill,
											TypeProduct.PIECE_PRODUCT.toString(), intSl, null);
									detailBill.setProduct(product);
									detailBill.setTotalPriceProduct(totalPriceProduct);
									listDetailBill.add(detailBill);
								}
							}
						} while (!flgEnter2 || intSl == 0);
					} else if (TypeProduct.BULK_PRODUCT.toString().equals(product.getTypeProduct())) {
						String stWeight = null;
						float fltWeight = 0.0f;
						do {
							flgEnter2 = true;
							System.out.println("Enter product weight: ");
							stWeight = scanner.nextLine();
							matcher = decimal.matcher(stWeight);
							if (!matcher.matches()) {
								flgEnter2 = false;
							} else {
								fltWeight = Float.parseFloat(stWeight);
								if (!exitListDetailBill(listDetailBill, product, TypeProduct.BULK_PRODUCT.toString(),
										null, fltWeight)) {
									detailBill = new DetailBill();
									Float totalPriceProduct = salesPromotions(product, detailBill,
											TypeProduct.BULK_PRODUCT.toString(), null, fltWeight);
									detailBill.setProduct(product);
									detailBill.setTotalPriceProduct(totalPriceProduct);
									listDetailBill.add(detailBill);
								}
							}
						} while (!flgEnter2 || fltWeight == 0);
					}
				}
			}
		} while (!flgEnter);
	}

	/**
	 * Check the product already in the invoice
	 * 
	 * @param listDetailBills
	 * @param product
	 * @param typeProduct
	 * @param intSl
	 * @param fltWeight
	 * @return
	 */
	private static boolean exitListDetailBill(List<DetailBill> listDetailBills, Product product, String typeProduct,
			Integer intSl, Float fltWeight) {
		Float totalPriceProduct = null;
		boolean flg = false;
		for (DetailBill detailBill : listDetailBills) {
			Product pr = detailBill.getProduct();
			if (pr.getId() == product.getId()) {
				if (TypeProduct.PIECE_PRODUCT.toString().equals(typeProduct)) {
					totalPriceProduct = salesPromotions(pr, detailBill, TypeProduct.PIECE_PRODUCT.toString(), intSl,
							null);
				}
				if (TypeProduct.BULK_PRODUCT.toString().equals(typeProduct)) {
					totalPriceProduct = salesPromotions(pr, detailBill, TypeProduct.BULK_PRODUCT.toString(), null,
							fltWeight);
				}
				detailBill.setTotalPriceProduct(detailBill.getTotalPriceProduct() + totalPriceProduct);
				flg = true;
				break;
			}
		}
		return flg;
	}

	/**
	 * Calculate the price of each type of product
	 * 
	 * @param product
	 * @param detailBill
	 * @param typeProduct
	 * @param intSl
	 * @param fltWeight
	 * @return totalPriceProduct
	 */
	private static Float salesPromotions(Product product, DetailBill detailBill, String typeProduct, Integer intSl,
			Float fltWeight) {
		float totalPriceProduct = 0.0f;
		// If the product is sold by piece
		if (TypeProduct.PIECE_PRODUCT.toString().equals(typeProduct)) {
			totalPriceProduct = Float.valueOf(product.getPrice()) * intSl;
			// set number of product with case sales promotions buy one, get one free
			if (TypeSales.BUY_ONE_GET_ONE.toString().equals(product.getTypeSales())) {
				if (detailBill.getNumberOfProduct() != null) {
					detailBill.setNumberOfProduct(detailBill.getNumberOfProduct() + intSl * 2);
				} else {
					detailBill.setNumberOfProduct(intSl * 2);
				}
				// set number of product with case sales promotions buy two, get one free
			} else if (TypeSales.BUY_TOW_GET_ONE.toString().equals(product.getTypeSales())) {
				if (detailBill.getNumberOfProduct() != null) {
					int numberOld = (int) Math.ceil(((double) detailBill.getNumberOfProduct()) * 2 / 3);
					int numberNew = numberOld + intSl;
					detailBill.setNumberOfProduct(numberNew + numberNew / 2);
				} else {
					detailBill.setNumberOfProduct(intSl + intSl / 2);
				}
				// set number of product with case no sales promotions
			} else {
				if (detailBill.getNumberOfProduct() != null) {
					detailBill.setNumberOfProduct(detailBill.getNumberOfProduct() + intSl);
				} else {
					detailBill.setNumberOfProduct(intSl);
				}
			}
			// If the product is sold in bulk
		} else if (TypeProduct.BULK_PRODUCT.toString().equals(typeProduct)) {
			totalPriceProduct = Float.valueOf(product.getPrice()) * fltWeight;
			// set total weight of product with case sales promotions buy one, get one free
			if (TypeSales.BUY_ONE_GET_ONE.toString().equals(product.getTypeSales())) {
				if (detailBill.getWeightofProduct() != null) {
					float weightOld = (float) Math.ceil(Math.floor(detailBill.getWeightofProduct()) / 2)
							+ detailBill.getWeightofProduct() % 1;
					float weightNew = weightOld + fltWeight;
					detailBill.setWeightofProduct(weightNew + (float) Math.floor(weightNew));
				} else {
					detailBill.setWeightofProduct(fltWeight + (float) Math.floor(fltWeight));
				}
				// set total weight of product with case sales promotions buy two, get one free
			} else if (TypeSales.BUY_TOW_GET_ONE.toString().equals(product.getTypeSales())) {
				if (detailBill.getWeightofProduct() != null) {
					float weightOld = (float) Math.ceil(Math.floor(detailBill.getWeightofProduct()) * 2 / 3)
							+ detailBill.getWeightofProduct() % 1;
					float weightNew = weightOld + fltWeight;
					detailBill.setWeightofProduct(weightNew + (float) Math.floor(weightNew / 2));
				} else {
					detailBill.setWeightofProduct(fltWeight + (float) Math.floor(fltWeight / 2));
				}
				// set total weight of product with case no promotions
			} else {
				if (detailBill.getWeightofProduct() != null) {
					detailBill.setWeightofProduct(detailBill.getWeightofProduct() + fltWeight);
				} else {
					detailBill.setWeightofProduct(fltWeight);
				}
			}
		}
		return totalPriceProduct;
	}

	/**
	 * add products to DB
	 * 
	 * @param Product
	 */
	private static Integer addProduct(Product product) {
		Session session = factory.openSession();
		Transaction tx = null;
		Integer productID = null;
		try {
			tx = session.beginTransaction();
			productID = (Integer) session.save(product);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return productID;
	}

	/**
	 * get Product by ID
	 * 
	 * @param productId
	 * @return Product
	 */
	private static Product getProduct(Integer productId) {
		Product product = null;
		Session session = factory.openSession();
		try {
			product = (Product) session.get(Product.class, productId);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return product;
	}

	/**
	 * get List Product
	 * 
	 * @return List<Product>
	 */
	private static List<Product> listProducts() {
		Session session = factory.openSession();
		Transaction tx = null;
		List<Product> listProducts = null;
		try {
			tx = session.beginTransaction();
			Criteria cr = session.createCriteria(Product.class);
			listProducts = cr.list();
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback();
			e.printStackTrace();
		} finally {
			session.close();
		}
		return listProducts;
	}

	/**
	 * method to show the menu
	 */
	private static void showMenu() {
		System.out.println("=========Select the feature below=========");
		System.out.println("== ==");
		System.out.println("== [1] Product payment ==");
		System.out.println("== [2] Print Bill ==");
		System.out.println("== [3] Shutdown ==");
		System.out.println("== ==");
		System.out.println("==========================================");
	}

	/**
	 * Generate data for testing
	 */
	private static void initData() {
		Product product = new Product();
		product.setNameProduct("Rice");
		product.setPrice("2000");
		product.setTypeProduct(TypeProduct.BULK_PRODUCT.toString());
		product.setTypeSales(TypeSales.BUY_TOW_GET_ONE.toString());
		product.setDateProduce(new Date());
		product.setDateExpires(new Date());
		product.setCreateUser(1);
		product.setCreateDate(new Date());
		product.setUpdateUser(1);
		product.setUpdateDate(new Date());
		product.setDeleteFlg(0);
		addProduct(product);

		Product product2 = new Product();
		product2.setNameProduct("Rice cake");
		product2.setPrice("3000");
		product2.setTypeProduct(TypeProduct.PIECE_PRODUCT.toString());
		product2.setTypeSales(TypeSales.BUY_ONE_GET_ONE.toString());
		product2.setDateProduce(new Date());
		product2.setDateExpires(new Date());
		product2.setCreateUser(1);
		product2.setCreateDate(new Date());
		product2.setUpdateUser(1);
		product2.setUpdateDate(new Date());
		product2.setDeleteFlg(0);
		addProduct(product2);

		Product product3 = new Product();
		product3.setNameProduct("Snacks");
		product3.setPrice("4000");
		product3.setTypeProduct(TypeProduct.PIECE_PRODUCT.toString());
		product3.setTypeSales(TypeSales.BUY_TOW_GET_ONE.toString());
		product3.setDateProduce(new Date());
		product3.setDateExpires(new Date());
		product3.setCreateUser(1);
		product3.setCreateDate(new Date());
		product3.setUpdateUser(1);
		product3.setUpdateDate(new Date());
		product3.setDeleteFlg(0);
		addProduct(product3);

		Product product4 = new Product();
		product4.setNameProduct("Sugar");
		product4.setPrice("1000");
		product4.setTypeProduct(TypeProduct.BULK_PRODUCT.toString());
		product4.setTypeSales(TypeSales.BUY_ONE_GET_ONE.toString());
		product4.setDateProduce(new Date());
		product4.setDateExpires(new Date());
		product4.setCreateUser(1);
		product4.setCreateDate(new Date());
		product4.setUpdateUser(1);
		product4.setUpdateDate(new Date());
		product4.setDeleteFlg(0);
		addProduct(product4);

		Product product5 = new Product();
		product5.setNameProduct("CANDY");
		product5.setPrice("500");
		product5.setTypeProduct(TypeProduct.PIECE_PRODUCT.toString());
		product5.setTypeSales(TypeSales.NO_PROMOTIONS.toString());
		product5.setDateProduce(new Date());
		product5.setDateExpires(new Date());
		product5.setCreateUser(1);
		product5.setCreateDate(new Date());
		product5.setUpdateUser(1);
		product5.setUpdateDate(new Date());
		product5.setDeleteFlg(0);
		addProduct(product5);

		Product product6 = new Product();
		product6.setNameProduct("Water");
		product6.setPrice("500");
		product6.setTypeProduct(TypeProduct.PIECE_PRODUCT.toString());
		product6.setTypeSales(TypeSales.BUY_TOW_GET_ONE.toString());
		product6.setDateProduce(new Date());
		product6.setDateExpires(new Date());
		product6.setCreateUser(1);
		product6.setCreateDate(new Date());
		product6.setUpdateUser(1);
		product6.setUpdateDate(new Date());
		product6.setDeleteFlg(0);
		addProduct(product6);

		Product product7 = new Product();
		product7.setNameProduct("clothes");
		product7.setPrice("700");
		product7.setTypeProduct(TypeProduct.PIECE_PRODUCT.toString());
		product7.setTypeSales(TypeSales.BUY_TOW_GET_ONE.toString());
		product7.setDateProduce(new Date());
		product7.setDateExpires(new Date());
		product7.setCreateUser(1);
		product7.setCreateDate(new Date());
		product7.setUpdateUser(1);
		product7.setUpdateDate(new Date());
		product7.setDeleteFlg(0);
		addProduct(product7);

		Product product8 = new Product();
		product.setNameProduct("Salt");
		product.setPrice("1200");
		product.setTypeProduct(TypeProduct.BULK_PRODUCT.toString());
		product.setTypeSales(TypeSales.NO_PROMOTIONS.toString());
		product.setDateProduce(new Date());
		product.setDateExpires(new Date());
		product.setCreateUser(1);
		product.setCreateDate(new Date());
		product.setUpdateUser(1);
		product.setUpdateDate(new Date());
		product.setDeleteFlg(0);
		addProduct(product);
	}
}
