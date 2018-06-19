var SERVER_ROUTE = "http://m.nashengbuy.com";
var SERVER_ROUTE = "http://192.168.1.77:8082";
var SERVER_BASE_URL = SERVER_ROUTE + "/ns-crm/";
var WS_SERVER_ROUTE = SERVER_ROUTE + "/ns-crm/crm/";
var ORDER_STATUS_CHN = [{"code":"1","value":"新增订单"},{"code":"2","value":"已付款"},{"code":"3","value":"申请取消"},{"code":"4","value":"已取消"},{"code":"5","value":"已打印"},{"code":"6","value":"配送中"},{"code":"7","value":"已收货"},{"code":"8","value":"申请退货"},{"code":"9","value":"已退货"},{"code":"10","value":"已退款"},{"code":"11","value":"已关闭"},{"code":"12","value":"已删除"},{"code":"13","value":"已评价"}];
var ORDER_SOURCE_CHN = [{"code":"1","value":"微信"},{"code":"2","value":"APP安卓"},{"code":"3","value":"APP苹果"}];
var POINTS_TYPE_CHN = [{"code":"1","value":"推广会员"},{"code":"2","value":"订单积分"},{"code":"3","value":"积分抵扣"}];
var REQUEST_STATUS_CHN = [{"code":"0","value":"申请中"},{"code":"1","value":"通过"},{"code":"2","value":"拒绝"}];
var FINANCE_STATUS_CHN = [{"code":"0","value":"待审核"},{"code":"1","value":"通过"},{"code":"2","value":"拒绝"}];
var REFUND_TYPE_CHN = [{"code":"0","value":"微信钱包"},{"code":"1","value":"支付宝钱包"},{"code":"2","value":"银行卡"},{"code":"3","value":"线下支付"}];
/**
 * 根据参数名称获取地址对应参数值
 * 若没找到对应值则返回null
 * @method   getQueryString
 * @Author   chenguangchao
 * @DateTime 2018-02-09
 * @param    string       name [参数名]
 * @return   string       value [参数值]
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		return unescape(r[2]);
	} else {
		return null;
	}
}

//判断手机号
function checkPhone(phone) {
	var reg = /^1[3|4|5|6|7|8|9][0-9]\d{8}$/;
	if (!reg.test(phone.trim())) {
		return false;
	} else {
		return true;
	}
}

//商品分类数据结构
var proCategoryObj = {
	"treeData":[{"categoryName":"奶粉辅食","id":"FAE77ED129A54DFDAFFF8FCC0C4F7C8C","parentId":"-1"},{"categoryName":"喂养用品","id":"636F529E5E4C4AF3E053F401A8C003C2","parentId":"-1"},{"categoryName":"纸尿裤","id":"38EF1C63A0854A8992C05905D2DFA2F6","parentId":"-1"},{"categoryName":"家居生活","id":"9DBB083E46AE49A69B90F2AAE4DDC15C","parentId":"-1"}],
	"selectNodeIds":"",
	"selectNodeNames":"",
}

