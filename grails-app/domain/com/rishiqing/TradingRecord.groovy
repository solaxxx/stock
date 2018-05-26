package com.rishiqing

import base.User

class TradingRecord {

    transient def totalShare
    transient def systemSharePrice

    // 交易时间
    Date transactionDate

    // 买入数量
    Long buyShareNum = 0

    // 卖出数量
    Long sellShareNum = 0

    // 买入类型
    int buyType

    //卖出类型
    int sellType

    // 买卖单价
    String tradingPrice

    // 成交金额
    String turnover

    // 购买时股价
    String sharePrice

    // 备注
    String remark

    static mapping = {
        remark type: 'text'
    }
    static constraints = {
        remark nullable: true
    }

    static belongsTo = [
            sellUser : User, //卖出人
            sellOptionsRecord : OptionsRecord, //卖出人卖出期权相关信息
            sellShareRecord : ShareRecord, //卖出人卖出股权相关信息

            buyUser : User,  //买入人
            buyOptionsRecord : OptionsRecord, //买入人买入期权相关信息
            buyShareRecord : ShareRecord //买入人买入股权相关信息
    ]
}
