/**
 * 简单封装分页组件
 * vertion 2.0
 * TODO 后期需要更改css
 * Created by RyanChou on 2017-04-11.
 */
// var div_page_id = "";
// var table_data_url = "";
// var table_param_form_id = "";

var myPaging = function (config) {
    this.page_id = config.page_id;
    this.div_pagination_id = config.div_pagination_id;
    this.form_id = config.form_id;
    this.url = config.url;
    this.pageNumber = Number(config.pageNumber);
    this.totalPage = Number(config.totalPage);
    this.params = config.params;
    this.pageSize = Number(config.pageSize);

    this.init = function () {
        var pageId = this.page_id;
        var formId = this.form_id;
        var pageUrl = this.url;
        var divPaginationId = this.div_pagination_id;
        // 首页按钮
        var first = '<div class="am-u-sm-12 am-u-md-8 am-u-lg-8"><ul class="am-pagination">';
        var up = '';
        if (this.pageNumber <= 1) {
            first += '<li><a class="am-btn am-disabled" href="javascript:void(0);">首页</a></li>';
            up += '<li><a class="am-btn am-disabled" href="javascript:void(0);">&laquo; 上页</a></li>';
        } else {
            first += '<li><a href="javascript:void(0);" onclick="goto(1,\'' + this.pageSize + '\',\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\',\'' + divPaginationId + '\');" >首页</a></li>';
            up += '<li><a href="javascript:void(0);" onclick="goto(' + (this.pageNumber - 1) + ',\'' + this.pageSize + '\',\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\',\'' + divPaginationId + '\');">&laquo;上页</a></li>';
        }
        var select = '<select class="page_select" style="margin-right: 5px;padding: 0.5rem 0;margin-bottom: 5px;" data-am-selected="" onchange="goto(this.value,\'' + this.pageSize + '\',\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\',\'' + divPaginationId + '\');">';
        for (var i = 1; i <= this.totalPage; i++) {
            var check = "";
            if (this.pageNumber == i) {
                check += "selected";
            }
            select += '<option value="' + i + '" ' + check + '>' + i + ' / ' + this.totalPage + '</option>';
            // select += '<option value="' + i + '" ' + check + '>' + i + '</option>';
        }
        select += "</select>"
        // 末页按钮
        var down = "";
        var last = "";
        if (this.pageNumber >= this.totalPage) {
            down = '<li><a class="am-btn am-disabled" href="javascript:void(0);">下页&raquo;</a></li>';
            last += '<li><a class="am-btn am-disabled" href="javascript:void(0);">末页</a></li>';
        } else {
            down = '<li><a href="javascript:void(0);" onclick="goto(' + (this.pageNumber + 1) + ',\'' + this.pageSize + '\',\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\',\'' + divPaginationId + '\');">下页&raquo;</a></li>';
            last += '<li><a href="javascript:void(0);" onclick="goto(' + this.totalPage + ',\'' + this.pageSize + '\',\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\',\'' + divPaginationId + '\');">末页</a></li>';
        }
        last += '</ul></div>';
        // var size10 = "";
        // var size20 = "";
        // var size50 = "";
        // if (this.pageSize == 10) {
        //     size10 = "selected";
        // }
        // if (this.pageSize == 20) {
        //     size20 = "selected";
        // }
        // if (this.pageSize == 50) {
        //     size50 = "selected";
        // }
        // var pageSize = '<div class="am-u-sm-12 am-u-md-4 am-u-lg-4" style="padding-left: 0;margin: 1.5rem 0;list-style: none;text-align: left;"><span style="color: #0e90d2;">页容量：</span>' +
        //     '<select class="pageSizeCss" onchange="changePageSize(this.value,\'' + formId + '\',\'' + pageUrl + '\',\'' + pageId + '\')">' +
        //     '<option value="10" ' + size10 + '>10</option>' +
        //     '<option value="20" ' + size20 + '>20</option>' +
        //     '<option value="50" ' + size50 + '>50</option>' +
        //     '</select>' +
        //     '</div>';
        // $("#" + this.div_pagination_id).html(first + up + select + down + last + pageSize);
        $("#" + this.div_pagination_id).html(first + up + select + down + last);

        AMUI.plugin('pageNumberSelect', AMUI.selected);
        $('.pageSizeCss').pageNumberSelect({btnWidth: '20%', maxHeight: 150, dropUp: 1});
    }
}

function goto(targetNo, pageSize, formId, url, pageId,divPaginationId) {
    $("#pageSize").val(pageSize);
    $("#pageNumber").val(targetNo);
    var data = $("#" + formId).serialize();
    data = data.replace(/=-1/g,"=");
    $.ajax({
        url: url,
        type: "post",
        async: false,
        data: data,
        success: function (result) {
            $("#" + pageId).html(result);
            if (result.result == '0') {
                showDataList(result.data);
            }
            var pageObj = new myPaging({
                page_id: pageId,
                div_pagination_id: divPaginationId,
                form_id: formId,
                url: url,
                pageNumber: result.data.pageNumber,
                totalPage: result.data.totalPage,
                pageSize: result.data.pageSize
            });
            pageObj.init();
        }
    });
}

function changePageSize(pageSize, formId, url, pageId) {
    $("#pageSize").val(pageSize);
    var data = $("#" + formId).serialize();
    data = data.replace(/=-1/g,"=");
    $.ajax({
        url: url,
        type: "post",
        async: false,
        data: data,
        success: function (result) {
            $("#" + pageId).html(result);

            if (result.result == '0') {
                showConList(result.data);
            }

            var pageObj = new myPaging({
                page_id: pageId,
                div_pagination_id: "myPaging",
                form_id: formId,
                url: url,
                pageNumber: result.data.pageNumber,
                totalPage: result.data.totalPage,
                pageSize: result.data.pageSize
            });
            pageObj.init();
        }
    });
}
