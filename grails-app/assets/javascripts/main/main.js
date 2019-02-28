/**
 * Created by solax on 2017-1-17.
 */
var SHARE_PRICE = 'share_price'

$(function () {
    // 初始化
    window.systemInit.init()

/*    setTimeout(function () {
        $('#bulletinManager').modal('show');
    }, 3000);*/

    // 左按钮
    var $buttonLeft = $('.left .btn1');

    // 右按钮
    var $buttonRight = $('.right .btn1');

    var $center = $('.center');

    var $page1 =  $('.page1');

    var $page2 = $('.page2');

    var $systemOptionsTip = $("[data-target='#systemOptions']");

    var $sharePriceInput = $('#systemOptions #sharePrice');

    var $systemOptionsSaveBtn = $("#systemOptions .save");

    loader.init();

    $buttonLeft.click(function (event) {
        loader.moveLeft();
    });

    $buttonRight.click(function (event) {
        loader.moveRight();
    });

    $systemOptionsTip.click(function () {
        $sharePriceInput.val(window.systemInit.systemOptions[SHARE_PRICE])
    })

    // 保存系统信息
    $systemOptionsSaveBtn.click(function () {
        var sharePrice = $sharePriceInput.val();
        if (!sharePrice) {
            alertify.error('当前股价不能为空');
            return;
        }
        $.ajax({
            url: '/systemOptions/update',
            type: 'POST',
            data:{
                type : SHARE_PRICE,
                value : sharePrice
            },
            success : function (json) {
                window.systemInit.systemOptions = json;
                alertify.success('保存成功');
            }, error : function () {
                alertify.error('保存失败');
            }
        })
    })

    // 修改密码
    $('#passwordManager .save').click(function () {
        var old1 = $('#oldPassword').val();
        var new1 = $('#newPassword').val();
        var new2 = $('#newPassword-repeat').val();
        if (!new1) {
            alertify.error('请输入新密码');
            return;
        } else if (!new2) {
            alertify.error('请再次输入新密码');
            return;
        }else if (new1 !== new2) {
            alertify.error('两次密码输入不匹配');
            return;
        } else if (new1.length < 6) {
            alertify.error('密码长度必须大于6');
            return;
        }
        $.ajax({
            url: '/user/changePassword',
            type: 'POST',
            data:{
                newPassword : new1
            },
            success : function (json) {
                if (json.success) {
                    alertify.success(json.message);
                    $('#passwordManager').modal('hide');
                } else {
                    alertify.error(json.message);
                }
            }, error : function () {
                alertify.error('保存失败');
            }
        })
    });

    // 左右大按钮
    $('.left.btn1').mouseover(function (event) {
        $(event.target).attr('src', '/stock/assets/button/btn-left2.png')
    });
    $('.left.btn1').mouseout(function (event) {
        $(event.target).attr('src', '/stock/assets/button/btn-left1.png')
    });
    $('.right.btn1').mouseover(function (event) {
        $(event.target).attr('src', '/stock/assets/button/btn-right2.png')
    });
    $('.right.btn1').mouseout(function (event) {
        $(event.target).attr('src', '/stock/assets/button/btn-right1.png')
    });

    // 公告
    $('.bulletin').click(function () {
        $('#bulletinManager').modal('show');
    })
})

/**
 * 菜单加载对象
 * @type {{menuIndex: number, functionMenuArgs: string[], systemMenuArgs: Array, init: loader.init}}
 */
var loader = {
    menuIndex         :      0,
    pageClass         :     '.index-page',
    functionMenu      :     '.functionMenu',
    functionMenuArgs :      ['.page1', '.page2', '.page3', '.page4'],
    systemMenuArgs    :      [],
    init : function () {
        var self  = this;
        $(this.functionMenu).click(function (event) {
            var index = $(this).index();
            self.movePage(index);
        })
    },
    getActive : function (index) {
        return $(this.functionMenuArgs[index])
    },
    lastActive : function () {
        return $(this.functionMenuArgs[this.menuIndex])
    },
    setActive : function (index) {
        this.menuIndex = index;
    },
    setMenuActive : function ($obj) {
        $(this.functionMenu).removeClass('active');
        $obj.addClass('active')
    },
    leftOut : function ($obj) {
        this.removeMoveClass($obj);
        $obj.addClass('animated fadeOutLeft');
    },
    leftIn : function ($obj) {
        this.removeMoveClass($obj);
        $obj.addClass('animated fadeInLeft');
        $obj.show();
    },
    rightOut : function ($obj) {
        this.removeMoveClass($obj);
        $obj.addClass('animated fadeOutRight');
    },
    rightIn : function ($obj) {
        this.removeMoveClass($obj);
        $obj.addClass('animated fadeInRight');
        $obj.show();
    },
    removeMoveClass : function ($obj) {
        $obj.removeClass('animated fadeInLeft fadeOutLeft fadeInRight fadeOutRight');
    },
    moveLeft : function () {
        var minIndex  =  0
        if (minIndex < this.menuIndex) {
            this.movePage(this.menuIndex - 1);
        }
    },
    moveRight : function () {
        var maxIndex  =  this.functionMenuArgs.length - 1;
        if (this.menuIndex < maxIndex) {
            this.movePage(this.menuIndex + 1);
        }
    },
    movePage : function (index) {
        var self = this;
        var obj   = self.getActive(index)
        var lastActive = self.lastActive();
        if (index > self.menuIndex) { // 下一个
            // 向左移走上一个
            self.leftOut(lastActive);
            // 从右移进这个
            self.rightIn(obj);
            // 重置active对象
            self.setActive(index)
            // 菜单激活状态修改
            self.setMenuActive($($(this.functionMenu)[index]));
        } else if (index < self.menuIndex) { // 上一个
            // 向右移走下一个
            self.rightOut(lastActive);
            // 从左移进这个
            self.leftIn(obj)
            // 重置active对象
            self.setActive(index)
            // 菜单激活状态修改
            self.setMenuActive($($(this.functionMenu)[index]));
        }
    }
}