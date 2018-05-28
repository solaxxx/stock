package com.rishiqing

import base.User
import grails.transaction.Transactional

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*

/**
 * 交易记录相关controller
 * 基本操作：
 * 两个人之间直接进行买卖操作的交易
 *
 * 说明：
 * 在该交易记录里面进行修改，会同步修改相关的股权池和期权池中信息
 * 但是，如果修改股权池或者期权池中的信息的话，对交易记录会有如下影响：
 * 1、股权池或者期权池的信息是由交易记录生成的（调用这个controller中相关方法生成），修改任意信息，会删除在交易记录下的记录！
 * 目的：原来股权和期权池操作是不同步的，所以有可能只改动一项，这会破坏交易记录的完整性，所以就整个删掉了
 *
 * 2、股权池或者期权池的信息是按原有方法手动添加，则对交易记录没有影响！
 *
 * 2018年5月26日16:06:23
 */
@Transactional(readOnly = true)
class TradingRecordController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {

    }

    /**
     * 获取交易列表
     * 不考虑分页等
     * @param max
     * @return
     */
    def list () {
        //获取所有的交易记录
        List<TradingRecord> list = TradingRecord.list()
        //获取当前股票单价
        String sharePrice = GlobalSystemOptions.getInstance().getByType('share_price') + ""
        //算出每笔交易在现在的总价值
        for(TradingRecord tempTradingRecord : list){
            tempTradingRecord.totalShare = tempTradingRecord.buyShareNum * Double.parseDouble(sharePrice)
        }
        render (view:'list', model:[tradingRecordList:list])
    }

    /**
     * 跳转到添加交易记录页面
     * @return
     */
    def create() {
        //交易记录
        TradingRecord tradingRecord = new TradingRecord()
        //用户列表
        List<User> userList = User.findAllByIsDeleted(false)
        //当前股价
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'create', model: [tradingRecord:tradingRecord, sharePrice:sharePrice, userList:userList])
    }

    /**
     * 保存交易记录信息
     * @param tradingRecord
     * @return
     */
    @Transactional
    def save(TradingRecord tradingRecord) {
        if (tradingRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (tradingRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tradingRecord.errors, view:'create'
            return
        }

        //卖出人
        User sellUser  = User.findById(params.long('sellUserId'))
        //买入人
        User buyUser  = User.findById(params.long('buyUserId'))
        //交易时间
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd")
        Date transactionDate = format.parse(params.transactionDateStr)

        tradingRecord.sellUser = sellUser
        tradingRecord.buyUser = buyUser
        tradingRecord.transactionDate = transactionDate

        //对卖出人进行操作
        //卖出类型
        int sellType = tradingRecord.sellType
        if(sellType == 1){ //卖出的是股票
            ShareRecord shareRecord = new ShareRecord(
                    user: sellUser, //卖出人
                    buyShareNum : 0, //买入数量
                    sellShareNum : tradingRecord.sellShareNum, //卖出数量
                    transactionDate : transactionDate, //交易时间
                    tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                    sharePrice: tradingRecord.sharePrice, //购买时股价
                    turnover :  tradingRecord.turnover, //成交金额
                    remark :  tradingRecord.remark //备注信息
            )
            shareRecord.save(flush: true)
            tradingRecord.sellShareRecord = shareRecord
            tradingRecord.sellOptionsRecord = null
            tradingRecord.turnover = "-" + tradingRecord.sellShareNum * tradingRecord.tradingPrice.toDouble() + ""
        }else if(sellType == 2){ //卖出的是期权
            OptionsRecord optionsRecord = new OptionsRecord(
                    user: sellUser, //卖出人
                    buyShareNum : 0, //买入数量
                    sellShareNum :tradingRecord.sellShareNum, //卖出数量
                    transactionDate : transactionDate, //交易时间
                    tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                    sharePrice: tradingRecord.sharePrice, //当前股价
                    turnover :  tradingRecord.turnover, //成交金额
                    remark :  tradingRecord.remark //备注信息
            )
            optionsRecord.save flush:true
            tradingRecord.sellShareRecord = null
            tradingRecord.sellOptionsRecord = optionsRecord
            tradingRecord.turnover = "-" + tradingRecord.sellShareNum * tradingRecord.tradingPrice.toDouble() + ""
        }

        //对买入人进行操作
        int buyType = tradingRecord.buyType
        if(buyType == 1){ //买入的是股票
            ShareRecord shareRecord = new ShareRecord(
                    user: buyUser, //买入人
                    buyShareNum : tradingRecord.buyShareNum, //买入数量
                    sellShareNum : 0, //卖出数量
                    transactionDate : transactionDate, //交易时间
                    tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                    sharePrice: tradingRecord.sharePrice, //购买时股价
                    turnover :  tradingRecord.turnover, //成交金额
                    remark :  tradingRecord.remark //备注信息
            )
            shareRecord.save(flush: true)
            tradingRecord.buyShareRecord = shareRecord
            tradingRecord.buyOptionsRecord = null
            tradingRecord.turnover = tradingRecord.sellShareNum * tradingRecord.tradingPrice.toDouble() + ""
        }else if(sellType == 2){ //买入的是期权
            OptionsRecord optionsRecord = new OptionsRecord(
                    user: buyUser, //买入人
                    buyShareNum : tradingRecord.buyShareNum, //买入数量
                    sellShareNum :0, //卖出数量
                    transactionDate : transactionDate, //交易时间
                    tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                    sharePrice: tradingRecord.sharePrice, //当前股价
                    turnover :  tradingRecord.turnover, //成交金额
                    remark :  tradingRecord.remark //备注信息
            )
            optionsRecord.save flush:true
            tradingRecord.buyShareRecord = null
            tradingRecord.buyOptionsRecord = optionsRecord
            tradingRecord.turnover = tradingRecord.sellShareNum * tradingRecord.tradingPrice.toDouble() + ""
        }

        tradingRecord.save flush:true
        flash.message = '添加交易成功'
        redirect action: 'list'
    }

    /**
     * 跳转到编辑页面
     * @param id
     * @return
     */
    def edit(Long id) {
        //获取要编辑的信息
        TradingRecord tradingRecord = TradingRecord.findById(id)
        //用户列表
        List<User> userList = User.findAllByIsDeleted(false)
        //当前股价
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'edit', model: [tradingRecord:tradingRecord, sharePrice:sharePrice, userList:userList])
    }

    /**
     * 修改交易信息
     * @param tradingRecord
     * @return
     */
    @Transactional
    def update(TradingRecord tradingRecord) {
        if (tradingRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (tradingRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tradingRecord.errors, view:'edit'
            return
        }

        //通过id获取数据库中的交易信息
        TradingRecord tradingRecord_db = TradingRecord.get(tradingRecord.id)

        //原卖出人
        User sellUser_db = tradingRecord_db.sellUser
        //原卖出人卖出股票信息
        ShareRecord sellShareRecord_db = tradingRecord_db.sellShareRecord
        //原卖出人卖出期权信息
        OptionsRecord sellOptionsRecord_db = tradingRecord_db.sellOptionsRecord

        //原买入人
        User buyUser_db = tradingRecord_db.buyUser
        //原买入人买入股票信息
        ShareRecord buyShareRecord_db = tradingRecord_db.buyShareRecord
        //原买入人买入股票信息
        OptionsRecord buyOptionsRecord_db = tradingRecord_db.buyOptionsRecord

        //卖出人
        User sellUser  = User.findById(params.long('sellUserId'))
        //买入人
        User buyUser  = User.findById(params.long('buyUserId'))
        //交易时间
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd")
        Date transactionDate = format.parse(params.transactionDateStr)

        //对卖出人进行操作
        //卖出类型
        int sellType = tradingRecord.sellType
        if(sellType == 1){ //卖出的是股票
            if(!sellShareRecord_db){ //原卖出股票信息为空
                ShareRecord shareRecord = new ShareRecord(
                        user: sellUser, //卖出人
                        buyShareNum : 0, //买入数量
                        sellShareNum : tradingRecord.sellShareNum, //卖出数量
                        transactionDate : transactionDate, //交易时间
                        tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                        sharePrice: tradingRecord.sharePrice, //购买时股价
                        turnover :  tradingRecord.turnover, //成交金额
                        remark :  tradingRecord.remark //备注信息
                )
                shareRecord.save(flush: true)
                tradingRecord_db.sellShareRecord = shareRecord
                //删除原来卖出期权的信息
                tradingRecord_db.sellOptionsRecord = null
                sellOptionsRecord_db.delete(flush: true)
            }else{
                //判断是否修改卖出人
                if(sellUser_db == sellUser){ //是同一个人
                    //修改交易记录
                    sellShareRecord_db.buyShareNum = 0
                    sellShareRecord_db.sellShareNum = tradingRecord.sellShareNum
                    sellShareRecord_db.transactionDate = transactionDate
                    sellShareRecord_db.tradingPrice = tradingRecord.tradingPrice
                    sellShareRecord_db.sharePrice = tradingRecord.sharePrice
                    sellShareRecord_db.turnover = tradingRecord.turnover
                    sellShareRecord_db.remark = tradingRecord.remark
                    sellShareRecord_db.save(flush: true)
                    tradingRecord_db.sellShareRecord = sellShareRecord_db
                    tradingRecord_db.sellOptionsRecord = null
                }else{ //不是同一个人
                    //删除原来的交易信息
                    sellShareRecord_db.delete(flush: true)
                    //创建新的交易信息
                    ShareRecord shareRecord = new ShareRecord(
                            user: sellUser, //卖出人
                            buyShareNum : 0, //买入数量
                            sellShareNum : tradingRecord.sellShareNum, //卖出数量
                            transactionDate : transactionDate, //交易时间
                            tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                            sharePrice: tradingRecord.sharePrice, //购买时股价
                            turnover :  tradingRecord.turnover, //成交金额
                            remark :  tradingRecord.remark //备注信息
                    )
                    shareRecord.save(flush: true)
                    tradingRecord_db.sellShareRecord = shareRecord
                    tradingRecord_db.sellOptionsRecord = null
                }
            }
        }else if(sellType == 2){ //卖出的是期权
            if(!sellOptionsRecord_db){ //原卖出期权信息为空
                OptionsRecord optionsRecord = new OptionsRecord(
                        user: sellUser, //卖出人
                        buyShareNum : 0, //买入数量
                        sellShareNum : tradingRecord.sellShareNum, //卖出数量
                        transactionDate : transactionDate, //交易时间
                        tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                        sharePrice: tradingRecord.sharePrice, //购买时股价
                        turnover :  tradingRecord.turnover, //成交金额
                        remark :  tradingRecord.remark //备注信息
                )
                optionsRecord.save(flush: true)
                tradingRecord_db.sellOptionsRecord = optionsRecord
                //删除原来卖出股权的信息
                tradingRecord_db.sellShareRecord = null
                sellShareRecord_db.delete(flush: true)
            }else{
                if(sellUser_db == sellUser){ //是同一个人
                    //修改交易记录
                    sellOptionsRecord_db.buyShareNum = 0
                    sellOptionsRecord_db.sellShareNum = tradingRecord.sellShareNum
                    sellOptionsRecord_db.transactionDate = transactionDate
                    sellOptionsRecord_db.tradingPrice = tradingRecord.tradingPrice
                    sellOptionsRecord_db.sharePrice = tradingRecord.sharePrice
                    sellOptionsRecord_db.turnover = tradingRecord.turnover
                    sellOptionsRecord_db.remark = tradingRecord.remark
                    sellOptionsRecord_db.save(flush: true)
                    tradingRecord_db.sellShareRecord = null
                    tradingRecord_db.sellOptionsRecord = sellOptionsRecord_db
                }else{ //不是同一个人
                    //删除原来的交易信息
                    sellOptionsRecord_db.delete(flush: true)
                    //创建新的交易信息
                    OptionsRecord optionsRecord = new OptionsRecord(
                            user: sellUser, //卖出人
                            buyShareNum : 0, //买入数量
                            sellShareNum : tradingRecord.sellShareNum, //卖出数量
                            transactionDate : transactionDate, //交易时间
                            tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                            sharePrice: tradingRecord.sharePrice, //购买时股价
                            turnover :  tradingRecord.turnover, //成交金额
                            remark :  tradingRecord.remark //备注信息
                    )
                    optionsRecord.save(flush: true)
                    tradingRecord_db.sellShareRecord = null
                    tradingRecord_db.sellOptionsRecord = optionsRecord
                }
            }
        }

        //对买入人进行操作
        int buyType = tradingRecord.buyType
        if(buyType == 1){ //买入的是股票
            if(!buyShareRecord_db){ //原买入的股票信息为空
                ShareRecord shareRecord = new ShareRecord(
                        user: buyUser, //买入人
                        buyShareNum : tradingRecord.buyShareNum, //买入数量
                        sellShareNum : 0, //卖出数量
                        transactionDate : transactionDate, //交易时间
                        tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                        sharePrice: tradingRecord.sharePrice, //购买时股价
                        turnover :  tradingRecord.turnover, //成交金额
                        remark :  tradingRecord.remark //备注信息
                )
                shareRecord.save(flush: true)
                tradingRecord_db.buyShareRecord = shareRecord
                //删除原买入期权的信息
                tradingRecord_db.buyOptionsRecord = null
                buyOptionsRecord_db.delete(flush: true)
            }else{
                //判断是否修改卖出人
                User tempUser = buyShareRecord_db.user
                if(tempUser == sellUser){ //是同一个人
                    //修改交易记录
                    buyShareRecord_db.buyShareNum = tradingRecord.buyShareNum
                    buyShareRecord_db.sellShareNum = 0
                    buyShareRecord_db.transactionDate = transactionDate
                    buyShareRecord_db.tradingPrice = tradingRecord.tradingPrice
                    buyShareRecord_db.sharePrice = tradingRecord.sharePrice
                    buyShareRecord_db.turnover = tradingRecord.turnover
                    buyShareRecord_db.remark = tradingRecord.remark
                    buyShareRecord_db.save(flush: true)
                    tradingRecord_db.buyShareRecord = buyShareRecord_db
                    tradingRecord_db.buyOptionsRecord = null
                }else{ //不是同一个人
                    //删除原来的交易信息
                    buyShareRecord_db.delete(flush: true)
                    //创建新的交易信息
                    ShareRecord shareRecord = new ShareRecord(
                            user: buyUser, //买入人
                            buyShareNum : tradingRecord.buyShareNum, //买入数量
                            sellShareNum : 0, //卖出数量
                            transactionDate : transactionDate, //交易时间
                            tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                            sharePrice: tradingRecord.sharePrice, //购买时股价
                            turnover :  tradingRecord.turnover, //成交金额
                            remark :  tradingRecord.remark //备注信息
                    )
                    shareRecord.save(flush: true)
                    tradingRecord_db.buyShareRecord = shareRecord
                    tradingRecord_db.buyOptionsRecord = null
                }
            }
        }else if(sellType == 2){ //买入的是期权
            if(!buyOptionsRecord_db){ //原买入的期权信息为空
                OptionsRecord optionsRecord = new OptionsRecord(
                        user: buyUser, //买入人
                        buyShareNum : tradingRecord.buyShareNum, //买入数量
                        sellShareNum : 0, //卖出数量
                        transactionDate : transactionDate, //交易时间
                        tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                        sharePrice: tradingRecord.sharePrice, //购买时股价
                        turnover :  tradingRecord.turnover, //成交金额
                        remark :  tradingRecord.remark //备注信息
                )
                optionsRecord.save(flush: true)
                tradingRecord_db.buyOptionsRecord = optionsRecord

                tradingRecord_db.buyShareRecord = null
                buyShareRecord_db.delete(flush: true)
            }else{
                //判断是否修改卖出人
                User tempUser = buyOptionsRecord_db.user
                if(tempUser == sellUser){ //是同一个人
                    //修改交易记录
                    buyOptionsRecord_db.buyShareNum = tradingRecord.buyShareNum
                    buyOptionsRecord_db.sellShareNum = 0
                    buyOptionsRecord_db.transactionDate = transactionDate
                    buyOptionsRecord_db.tradingPrice = tradingRecord.tradingPrice
                    buyOptionsRecord_db.sharePrice = tradingRecord.sharePrice
                    buyOptionsRecord_db.turnover = tradingRecord.turnover
                    buyOptionsRecord_db.remark = tradingRecord.remark
                    buyOptionsRecord_db.save(flush: true)
                    tradingRecord_db.buyShareRecord = null
                    tradingRecord_db.buyOptionsRecord = buyOptionsRecord_db
                }else{ //不是同一个人
                    //删除原来的交易信息
                    buyOptionsRecord_db.delete(flush: true)
                    //创建新的交易信息
                    OptionsRecord optionsRecord = new OptionsRecord(
                            user: buyUser, //买入人
                            buyShareNum : tradingRecord.buyShareNum, //买入数量
                            sellShareNum : 0, //卖出数量
                            transactionDate : transactionDate, //交易时间
                            tradingPrice :  tradingRecord.tradingPrice, //买卖单价
                            sharePrice: tradingRecord.sharePrice, //购买时股价
                            turnover :  tradingRecord.turnover, //成交金额
                            remark :  tradingRecord.remark //备注信息
                    )
                    optionsRecord.save(flush: true)
                    tradingRecord_db.buyShareRecord = null
                    tradingRecord_db.buyOptionsRecord = optionsRecord
                }
            }
        }

        flash.message = '修改交易成功'
        redirect action: 'list'
    }

    @Transactional
    def delete(TradingRecord tradingRecord) {
        println('--------------------')
        if (tradingRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        tradingRecord.delete flush:true

        flash.message = '删除交易记录成功'
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'tradingRecord.label', default: 'TradingRecord'), tradingRecord.id])
                /* redirect action:"index", method:"GET"*/
                redirect action:"list", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

}
