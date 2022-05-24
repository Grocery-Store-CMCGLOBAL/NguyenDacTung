/**
 * Author: tungnd
 * create date: 23/05/2022
 */
package store.checkout.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Entity contain Product
 */
@Entity
@Table(name = "PRODUCT")
public class Product {
	@Id
	@Column(name = "id_product")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProduct;

	@Column(name = "name_product")
	private String nameProduct;

	@Column(name = "price")
	private String price;

	@Column(name = "type_product")
	private String typeProduct;

	@Column(name = "type_sales")
	private String typeSales;

	@Column(name = "date_produce")
	private Date dateProduce;

	@Column(name = "date_expires")
	private Date dateExpires;

	@Column(name = "create_user")
	private Integer createUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "update_user")
	private Integer updateUser;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "update_date")
	private Date updateDate;

	/**
	 * deleteFlg = 0. exist in DB
	 * deleteFlg = 1. Physically deleted
	 */
	@Column(name = "delete_flg")
	private Integer deleteFlg;

	/**
	 * @return the Integer
	 */
	public Integer getId() {
		return idProduct;
	}

	/**
	 * @param idProduct the Integer to set
	 */
	public void setId(Integer idProduct) {
		this.idProduct = idProduct;
	}

	/**
	 * @return the nameProduct
	 */
	public String getNameProduct() {
		return nameProduct;
	}

	/**
	 * @param nameProduct the nameProduct to set
	 */
	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}

	/**
	 * @return the price
	 */
	public String getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(String price) {
		this.price = price;
	}

	/**
	 * @return the typeProduct
	 */
	public String getTypeProduct() {
		return typeProduct;
	}

	/**
	 * @param typeProduct the typeProduct to set
	 */
	public void setTypeProduct(String typeProduct) {
		this.typeProduct = typeProduct;
	}

	/**
	 * @return the typeSales
	 */
	public String getTypeSales() {
		return typeSales;
	}

	/**
	 * @param typeSales the typeSales to set
	 */
	public void setTypeSales(String typeSales) {
		this.typeSales = typeSales;
	}

	/**
	 * @return the dateProduce
	 */
	public Date getDateProduce() {
		return dateProduce;
	}

	/**
	 * @param dateProduce the dateProduce to set
	 */
	public void setDateProduce(Date dateProduce) {
		this.dateProduce = dateProduce;
	}

	/**
	 * @return the dateExpires
	 */
	public Date getDateExpires() {
		return dateExpires;
	}

	/**
	 * @param dateExpires the dateExpires to set
	 */
	public void setDateExpires(Date dateExpires) {
		this.dateExpires = dateExpires;
	}

	/**
	 * @return the createUser
	 */
	public Integer getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser the createUser to set
	 */
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the updateUser
	 */
	public Integer getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(Integer updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the deleteFlg
	 */
	public Integer getDeleteFlg() {
		return deleteFlg;
	}

	/**
	 * @param deleteFlg the deleteFlg to set
	 */
	public void setDeleteFlg(Integer deleteFlg) {
		this.deleteFlg = deleteFlg;
	}
}
