/**
 * project name: ns-api
 * file name:NodeContentService
 * package name:com.ns.node.service
 * date:2018-03-27 17:16
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.node.service;

import com.ns.common.exception.CustException;
import com.ns.common.model.NodeContent;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.math.BigDecimal;
import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-03-27 17:16
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class NodeContentService {
    private final NodeContent dao = new NodeContent();
    public static final NodeContentService me = new NodeContentService();
    private final String COLUMN = "ID,CATEGORY_ID,CONTENT,PIC1,PIC3,CON_ID,CON_NO,CON_NAME,CON_PIC,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean update(NodeContent content) {
        content.setUpdateDt(DateUtil.getNow());
        return content.update();
    }

    public boolean delete(String id) {
        return dao.deleteById(id);
    }


    public Page<Record> getList(String conNo, Integer pageNumber, Integer pageSize, String categoryId, String startDt, String endDt) {
        String select = "SELECT " + COLUMN;
        StringBuffer sqlExceptSelect = new StringBuffer("from node_content where 1=1 ");
        if (StrKit.notBlank(categoryId)) {
            sqlExceptSelect.append(" and CATEGORY_ID =  '" + categoryId + "'");
        }
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and CON_NO =  '" + conNo + "'");
        }
        if (StrKit.notBlank(startDt)) {
            sqlExceptSelect.append(" and CREATE_DT >= " + "'" + startDt + "'");
        }
        if (StrKit.notBlank(endDt)) {
            sqlExceptSelect.append(" and CREATE_DT <= " + "'" + endDt + "'");
        }
        Page<Record> page = Db.paginate(pageNumber, pageSize, select, sqlExceptSelect.toString());
        for (Record record : page.getList()) {
            String id = record.getStr("ID");
            String categoryName = Db.queryStr("select NAME from node_category where id = ?", record.getStr("CATEGORY_ID"));
            record.set("CATEGORY_NAME", categoryName);
            //收藏数
            int collectionNum = Db.queryInt("select count(ID) from node_collection where ENABLED = 1 AND SOURCE_ID = ?", id);
            record.set("COLLECTION_NUM", collectionNum);

            //点赞数
            int likeNum = Db.queryInt("select count(ID) from node_like where ENABLED = 1 AND SOURCE_ID = ? ", id);
            record.set("LIKE_NUM", likeNum);
            //评论数
            int cmtNum = Db.queryInt("select count(ID) from node_cmt where ENABLED = 1 AND SOURCE_ID = ? ", id);
            record.set("CMT_NUM", cmtNum);
        }
        return page;
    }

    public static void main(String[] args) {
        BigDecimal conversion_rate = new BigDecimal(0).divide((new BigDecimal(0).add(new BigDecimal(0)))).multiply(new BigDecimal(100));

        System.out.println(conversion_rate);
    }
}
