package com.rishiqing

/**
 * 融资历程表
 */
class StockHistory {

    // 增发事由
    String description

    // 增发数量
    Long addShares

    // 总股数
    String shares

    Date dateCreated

    Date lastUpdated

    static constraints = {
        shares nullable: true
    }
}
