/**
 * Created by solax on 2017-1-18.
 * 初始化
 *
 */
var init = {
    systemOptions : null,

    init : function () {
        this.getData();
    },
    getData : function () {
        // 获取系统参数
        this.getSystemData();
    },
    // 获取系统参数
    getSystemData :function () {
        var self = this
        $.ajax({
            url: '/stock/systemOptions/',
            type: 'get',
            success : function (json) {
                self.systemOptions = json;
            /*    $('#systemOptions #sharePrice').val(self.systemOptions['share_price'])*/
            }, error : function () {
                alert('systemOptions')
            }
        })
    }
}

window.systemInit = init;