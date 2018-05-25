/**
 * Created by solax on 2017-1-17.
 */
var ACTIVE  =   'active';
var NEW     =   'new';
var LIST    =   'list';

$(function () {
    // tip 对象
    var $tip = $(".nav.nav-tabs li");

    // 列表对象
    var $table = $(".iframe-list");

    // 新建对象
    var $create = $(".iframe-create");

    // tips的点击事件
    $tip.click(function (event) {
        $tip.removeClass(ACTIVE);
        var $li = $(event.target).parent();
        $li.addClass(ACTIVE);
        if ($li.hasClass(LIST)) {
            $create.fadeOut(300, function () {
                $table.fadeIn(300);
            });
        } else {
            $table.fadeOut(300, function () {
                $create.fadeIn(300);
            });
        }
    })
    //  股权交易段
    $('.date-show').datepicker({
        format:'yyyy-mm-dd',
        autoclose:true,
        language:'zh-CN'
    });
    $('#buyShareNum,#sellShareNum,#tradingPrice').blur(function (event) {
        var buyNum  = $('#buyShareNum').val();
        var sellNum = $('#sellShareNum').val();
        var price   = $('#tradingPrice').val();
        var turnover = (buyNum - sellNum) * price;
        $('#turnover').val(parseFloat(turnover.toFixed(2)));
    })
})