package com.ns.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {
	
	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("bas_cust_address", "ID", BasCustAddress.class);
		arp.addMapping("bas_cust_like", "ID", BasCustLike.class);
		arp.addMapping("bas_cust_point_trans", "ID", BasCustPointTrans.class);
		arp.addMapping("bas_cust_points", "ID", BasCustPoints.class);
		arp.addMapping("bas_cust_qrcode", "ID", BasCustQrcode.class);
		arp.addMapping("bas_cust_relation", "ID", BasCustRelation.class);
		arp.addMapping("bas_customer", "ID", BasCustomer.class);
		arp.addMapping("bas_customer_ext", "ID", BasCustomerExt.class);
		arp.addMapping("bas_customer_request", "ID", BasCustomerRequest.class);
		arp.addMapping("node_category", "ID", NodeCategory.class);
		arp.addMapping("node_cmt", "ID", NodeCmt.class);
		arp.addMapping("node_collection", "ID", NodeCollection.class);
		arp.addMapping("node_content", "ID", NodeContent.class);
		arp.addMapping("node_focus", "ID", NodeFocus.class);
		arp.addMapping("node_like", "ID", NodeLike.class);
		arp.addMapping("pnt_categories", "ID", PntCategories.class);
		arp.addMapping("pnt_menu", "ID", PntMenu.class);
		arp.addMapping("pnt_product", "ID", PntProduct.class);
		arp.addMapping("pnt_product_cmt", "ID", PntProductCmt.class);
		arp.addMapping("pnt_sku", "ID", PntSku.class);
		arp.addMapping("sys_dict", "ID", SysDict.class);
		arp.addMapping("sys_user", "ID", SysUser.class);
		arp.addMapping("tld_coupon", "ID", TldCoupon.class);
		arp.addMapping("tld_coupon_grant", "ID", TldCouponGrant.class);
		arp.addMapping("tld_identify_code", "ID", TldIdentifyCode.class);
		arp.addMapping("tld_order_items", "ID", TldOrderItems.class);
		arp.addMapping("tld_order_split", "ID", TldOrderSplit.class);
		arp.addMapping("tld_orders", "ID", TldOrders.class);
		arp.addMapping("tld_photos", "ID", TldPhotos.class);
		arp.addMapping("tld_qrbgm_params", "ID", TldQrbgmParams.class);
		arp.addMapping("tld_site_msg", "ID", TldSiteMsg.class);
		arp.addMapping("treenodes", "id", Treenodes.class);
	}
}

