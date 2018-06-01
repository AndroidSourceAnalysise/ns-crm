package com.ns.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBasCustomerExt<M extends BaseBasCustomerExt<M>> extends Model<M> implements IBean {

	public M setID(java.lang.String ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.String getID() {
		return getStr("ID");
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

	public M setREVENUES(java.math.BigDecimal REVENUES) {
		set("REVENUES", REVENUES);
		return (M)this;
	}
	
	public java.math.BigDecimal getREVENUES() {
		return get("REVENUES");
	}

	public M setCONSUMPTIONS(java.math.BigDecimal CONSUMPTIONS) {
		set("CONSUMPTIONS", CONSUMPTIONS);
		return (M)this;
	}
	
	public java.math.BigDecimal getCONSUMPTIONS() {
		return get("CONSUMPTIONS");
	}

	public M setReConsumptions(java.math.BigDecimal reConsumptions) {
		set("RE_CONSUMPTIONS", reConsumptions);
		return (M)this;
	}
	
	public java.math.BigDecimal getReConsumptions() {
		return get("RE_CONSUMPTIONS");
	}

	public M setPuredCustQty(java.lang.Integer puredCustQty) {
		set("PURED_CUST_QTY", puredCustQty);
		return (M)this;
	}
	
	public java.lang.Integer getPuredCustQty() {
		return getInt("PURED_CUST_QTY");
	}

	public M setUnpuredCustQty(java.lang.Integer unpuredCustQty) {
		set("UNPURED_CUST_QTY", unpuredCustQty);
		return (M)this;
	}
	
	public java.lang.Integer getUnpuredCustQty() {
		return getInt("UNPURED_CUST_QTY");
	}

	public M setOrdersTotal(java.lang.Integer ordersTotal) {
		set("ORDERS_TOTAL", ordersTotal);
		return (M)this;
	}
	
	public java.lang.Integer getOrdersTotal() {
		return getInt("ORDERS_TOTAL");
	}

	public M setOrdersProm(java.lang.Integer ordersProm) {
		set("ORDERS_PROM", ordersProm);
		return (M)this;
	}
	
	public java.lang.Integer getOrdersProm() {
		return getInt("ORDERS_PROM");
	}

	public M setPointsEnabled(java.lang.Integer pointsEnabled) {
		set("POINTS_ENABLED", pointsEnabled);
		return (M)this;
	}
	
	public java.lang.Integer getPointsEnabled() {
		return getInt("POINTS_ENABLED");
	}

	public M setPointsTotal(java.lang.Integer pointsTotal) {
		set("POINTS_TOTAL", pointsTotal);
		return (M)this;
	}
	
	public java.lang.Integer getPointsTotal() {
		return getInt("POINTS_TOTAL");
	}

	public M setPointsCfmd(java.lang.Integer pointsCfmd) {
		set("POINTS_CFMD", pointsCfmd);
		return (M)this;
	}
	
	public java.lang.Integer getPointsCfmd() {
		return getInt("POINTS_CFMD");
	}

	public M setPointsUncfmd(java.lang.Integer pointsUncfmd) {
		set("POINTS_UNCFMD", pointsUncfmd);
		return (M)this;
	}
	
	public java.lang.Integer getPointsUncfmd() {
		return getInt("POINTS_UNCFMD");
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
