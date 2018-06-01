/**
 * Created by Ryan Chou on 2017-05-19.
 */
// 丑先生的js工具

/**
 * 我的画板
 * @param config
 */
var panel = function (config) {
    this.url = config.url;
    this.data = config.data;
    this.target = config.target;
    this.load = function () {
        var jquery_target = $(this.target);
        $.ajax({
            url: this.url,
            type: "get",
            async: false,
            data: this.data,
            dataType: "html",
            success: function (html) {
                jquery_target.html(html);
            }
        });
    };
}

// 建立一个抽象对象, 可根据每个页面所需进行设定
var back = new Object();