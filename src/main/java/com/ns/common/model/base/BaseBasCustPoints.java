package com.ns.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseBasCustPoints<M extends BaseBasCustPoints<M>> extends Model<M> implements IBean {

	public M setID(java.lang.String ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.String getID() {
		return getStr("ID");
	}

	public M setPointsType(java.lang.Integer pointsType) {
		set("POINTS_TYPE", pointsType);
		return (M)this;
	}
	
	public java.lang.Integer getPointsType() {
		return getInt("POINTS_TYPE");
	}

	public M setPointsQty(java.lang.Integer pointsQty) {
		set("POINTS_QTY", pointsQty);
		return (M)this;
	}
	
	public java.lang.Integer getPointsQty() {
		return getInt("POINTS_QTY");
	}

	public M setPointsDesc(java.lang.String pointsDesc) {
		set("POINTS_DESC", pointsDesc);
		return (M)this;
	}
	
	public java.lang.String getPointsDesc() {
		return getStr("POINTS_DESC");
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