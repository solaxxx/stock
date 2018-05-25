package com.rishiqing

import base.User

/**
 * 期权记录
 */
class OptionsRecord {

    transient def totalShare
    transient def systemSharePrice

    // 交易时间
    Date transactionDate

    // 买入数量
    Long buyShareNum = 0

    // 卖出数量
    Long sellShareNum = 0

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
            user : User
    ]
}
