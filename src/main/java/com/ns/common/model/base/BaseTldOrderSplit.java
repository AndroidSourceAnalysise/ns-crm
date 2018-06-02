package com.ns.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTldOrderSplit<M extends BaseTldOrderSplit<M>> extends Model<M> implements IBean {

	public M setID(java.lang.String ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.String getID() {
		return getStr("ID");
	}

	public M setOrderId(java.lang.String orderId) {
		set("ORDER_ID", orderId);
		return (M)this;
	}
	
	public java.lang.String getOrderId() {
		return getStr("ORDER_ID");
	}

	public M setOrderNo(java.lang.String orderNo) {
		set("ORDER_NO", orderNo);
		return (M)this;
	}
	
	public java.lang.String getOrderNo() {
		return getStr("ORDER_NO");
	}

	public M setConId(java.lang.String conId) {
		set("CON_ID", conId);
		return (M)this;
	}
	
	public java.lang.String getConId() {
		return getStr("CON_ID");
	}

	public M setConNo(java.lang.String conNo) {
		set("CON_NO", conNo);
		return (M)this;
	}
	
	public java.lang.String getConNo() {
		return getStr("CON_NO");
	}

	public M setConName(java.lang.String conName) {
		set("CON_NAME", conName);
		return (M)this;
	}
	
	public java.lang.String getConName() {
		return getStr("CON_NAME");
	}

	public M setPntId(java.lang.String pntId) {
		set("PNT_ID", pntId);
		return (M)this;
	}
	
	public java.lang.String getPntId() {
		return getStr("PNT_ID");
	}

	public M setPntName(java.lang.String pntName) {
		set("PNT_NAME", pntName);
		return (M)this;
	}
	
	public java.lang.String getPntName() {
		return getStr("PNT_NAME");
	}

	public M setSkuId(java.lang.String skuId) {
		set("SKU_ID", skuId);
		return (M)this;
	}
	
	public java.lang.String getSkuId() {
		return getStr("SKU_ID");
	}

	public M setSkuName(java.lang.String skuName) {
		set("SKU_NAME", skuName);
		return (M)this;
	}
	
	public java.lang.String getSkuName() {
		return getStr("SKU_NAME");
	}

	public M setSplitNumber(java.lang.Integer splitNumber) {
		set("SPLIT_NUMBER", splitNumber);
		return (M)this;
	}
	
	public java.lang.Integer getSplitNumber() {
		return getInt("SPLIT_NUMBER");
	}

	public M setExpCompanyId(java.lang.String expCompanyId) {
		set("EXP_COMPANY_ID", expCompanyId);
		return (M)this;
	}
	
	public java.lang.String getExpCompanyId() {
		return getStr("EXP_COMPANY_ID");
	}

	public M setExpCompanyName(java.lang.String expCompanyName) {
		set("EXP_COMPANY_NAME", expCompanyName);
		return (M)this;
	}
	
	public java.lang.String getExpCompanyName() {
		return getStr("EXP_COMPANY_NAME");
	}

	public M setWAYBILL(java.lang.String WAYBILL) {
		set("WAYBILL", WAYBILL);
		return (M)this;
	}
	
	public java.lang.String getWAYBILL() {
		return getStr("WAYBILL");
	}

	public M setIsDelivery(java.lang.Integer isDelivery) {
		set("IS_DELIVERY", isDelivery);
		return (M)this;
	}
	
	public java.lang.Integer getIsDelivery() {
		return getInt("IS_DELIVERY");
	}

	public M setCOUNTRY(java.lang.String COUNTRY) {
		set("COUNTRY", COUNTRY);
		return (M)this;
	}
	
	public java.lang.String getCOUNTRY() {
		return getStr("COUNTRY");
	}

	public M setPROVINCE(java.lang.String PROVINCE) {
		set("PROVINCE", PROVINCE);
		return (M)this;
	}
	
	public java.lang.String getPROVINCE() {
		return getStr("PROVINCE");
	}

	public M setCITY(java.lang.String CITY) {
		set("CITY", CITY);
		return (M)this;
	}
	
	public java.lang.String getCITY() {
		return getStr("CITY");
	}

	public M setDISTRICT(java.lang.String DISTRICT) {
		set("DISTRICT", DISTRICT);
		return (M)this;
	}
	
	public java.lang.String getDISTRICT() {
		return getStr("DISTRICT");
	}

	public M setADDRESS(java.lang.String ADDRESS) {
		set("ADDRESS", ADDRESS);
		return (M)this;
	}
	
	public java.lang.String getADDRESS() {
		return getStr("ADDRESS");
	}

	public M setPostalCode(java.lang.String postalCode) {
		set("POSTAL_CODE", postalCode);
		return (M)this;
	}
	
	public java.lang.String getPostalCode() {
		return getStr("POSTAL_CODE");
	}

	public M setMOBILE(java.lang.String MOBILE) {
		set("MOBILE", MOBILE);
		return (M)this;
	}
	
	public java.lang.String getMOBILE() {
		return getStr("MOBILE");
	}

	public M setRECIPIENTS(java.lang.String RECIPIENTS) {
		set("RECIPIENTS", RECIPIENTS);
		return (M)this;
	}
	
	public java.lang.String getRECIPIENTS() {
		return getStr("RECIPIENTS");
	}

	public M setSalePrice(java.math.BigDecimal salePrice) {
		set("SALE_PRICE", salePrice);
		return (M)this;
	}
	
	public java.math.BigDecimal getSalePrice() {
		return get("SALE_PRICE");
	}

	public M setENABLED(java.lang.Integer ENABLED) {
		set("ENABLED", ENABLED);
		return (M)this;
	}
	
	public java.lang.Integer getENABLED() {
		return getInt("ENABLED");
	}

	public M setVERSION(java.lang.Integer VERSION) {
		set("VERSION", VERSION);
		return (M)this;
	}
	
	public java.lang.Integer getVERSION() {
		return getInt("VERSION");
	}

	public M setSTATUS(java.lang.Integer STATUS) {
		set("STATUS", STATUS);
		return (M)this;
	}
	
	public java.lang.Integer getSTATUS() {
		return getInt("STATUS");
	}

	public M setREMARK(java.lang.String REMARK) {
		set("REMARK", REMARK);
		return (M)this;
	}
	
	public java.lang.String getREMARK() {
		return getStr("REMARK");
	}

	public M setCreateBy(java.lang.String createBy) {
		set("CREATE_BY", createBy);
		return (M)this;
	}
	
	public java.lang.String getCreateBy() {
		return getStr("CREATE_BY");
	}

	public M setCreateDt(java.lang.String createDt) {
		set("CREATE_DT", createDt);
		return (M)this;
	}
	
	public java.lang.String getCreateDt() {
		return getStr("CREATE_DT");
	}

	public M setUpdateDt(java.lang.String updateDt) {
		set("UPDATE_DT", updateDt);
		return (M)this;
	}
	
	public java.lang.String getUpdateDt() {
		return getStr("UPDATE_DT");
	}

}