package com.rishiqing

import base.User
import grails.converters.JSON
import groovy.sql.Sql
import org.hibernate.criterion.CriteriaSpecification
import org.hibernate.sql.JoinType
import grails.gorm.CriteriaBuilder
import org.hibernate.type.StandardBasicTypes
import util.CommonUtil
import util.ConstantUtil

import java.text.SimpleDateFormat

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ShareRecordController {

    def dataSource

 /*   static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]*/

    // 当前股数，股票总值， 股份占比
    def list (Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = []
        User currentUser  = getAuthenticatedUser()
        def query = {
            createAlias('shareRecord', 'shareRecord', JoinType.LEFT_OUTER_JOIN)
            eq('isDeleted', false)
            if (currentUser.getAuthorityValue() != ConstantUtil.ROLE_ADMIN) {
                eq('id', currentUser.id)
            }
            projections  {
                property('id' ,'userId')
                property('realName' ,'realName')
                sqlProjection('(SUM(IFNULL(buy_share_num, 0)) - SUM(IFNULL(sell_share_num, 0))) as totalShare', 'totalShare', StandardBasicTypes.BIG_INTEGER)
            }
         /*   order('shareRecord.totalShare', 'desc')*/
            sqlRestriction('1=1 group by this_.id order by totalShare desc, this_.id')
            setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
        }
        // 数据总数
/*        def count = 0
        if (currentUser.getAuthorityValue() != ConstantUtil.ROLE_ADMIN) {
            count = User.countByIdAndIsDeleted(currentUser.id, false)
        } else {
            count = User.countByIsDeleted(false)
        }*/
        // 获取数据
        def list = User.createCriteria().list(query)
        // 获取当前股票单价
        def shareValue = GlobalSystemOptions.getInstance().getByType('share_price')
        // 获得当前总股份数
        def stockList = StockHistory.findAll()
        long totalStock = 0
        stockList.each { it ->
            totalStock += it.addShares
        }
        //  股票单价转换为double
        Double shareDoubleValue
        try {
              shareDoubleValue =  Double.parseDouble(shareValue)
        } catch (Exception e) {
            flash.message = '当前股价异常，请修正数值'
        }
        list.each { it ->
            it ['totalPrice']   = CommonUtil.formatLong(it.totalShare * shareDoubleValue) // 股票总值 = 当前成员总股数 * 当前股票单价
            it ['sharePercent'] = CommonUtil.toPercent(it.totalShare / totalStock)      // 股份占比 = 当前成员总股数 / 公司总股数
            it ['sharePrice'] = shareValue
        }
        //render (list as JSON)
        render (view:'list', model:[resultList: list])
    }

    def index(Long id) {
        //params.max = 10
/*        def count = ShareRecord.createCriteria().count() {
            eq('user.id', id)
        }*/
        def list = ShareRecord.createCriteria().list(params) {
            eq('user.id', id)
        }
        Double shareValue =  Double.parseDouble(GlobalSystemOptions.getInstance().getByType('share_price'))
        list.each { it ->
            it.totalShare =   CommonUtil.formatLong((it.buyShareNum - it.sellShareNum) * shareValue)
            it.systemSharePrice = shareValue
            it.turnover = CommonUtil.formatLong(it.turnover)
        }
        respond list, model:[userId:id]
    }

    def show(Long id) {
        ShareRecord shareRecord = ShareRecord.findById(id)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'show', model: [shareRecord:shareRecord, sharePrice:sharePrice, userId:params.userId])
        respond shareRecord
    }

    def create(Long id) {
        ShareRecord shareRecord = new ShareRecord(params)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'create', model: [shareRecord:shareRecord, sharePrice:sharePrice, userId:id])
        //respond new ShareRecord(params)
    }

    @Transactional
    def save() {
/*        if (shareRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }*/
        User user  = User.findById(params.long('userId'))
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd")
        ShareRecord shareRecord = new ShareRecord(
                user: user,
                buyShareNum : params.long('buyShareNum'),
                sellShareNum : params.long('sellShareNum'),
                transactionDate : format.parse(params.transactionDate),
                tradingPrice :  params.tradingPrice,
                sharePrice: params.sharePrice,
                turnover :  params.turnover,
                remark :  params.remark
        )

        if (shareRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond shareRecord.errors, view:'create',model: [userId:user.id, shareRecord:shareRecord, sharePrice:params.sharePrice]
            return
        }

        shareRecord.save flush:true

/*        request.withFormat {
            form multipartForm {
*//*                flash.message = message(code: 'default.created.message', args: [message(code: 'shareRecord.label', default: 'ShareRecord'), shareRecord.id])
                redirect shareRecord*//*
                flash.message = '添加交易记录成功'
                redirect action: 'index',  params: [userId:shareRecord.user.id]
            }
            '*' { respond shareRecord, [status: CREATED] }
        }*/
        flash.message = '添加交易记录成功'
        redirect action: 'index',  params: [shareRecord: shareRecord, id:shareRecord.user.id]
    }

    def edit(Long id) {
        ShareRecord shareRecord = ShareRecord.findById(id)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'edit', model: [shareRecord:shareRecord, sharePrice:sharePrice, userId:params.userId])
       /* respond shareRecord*/
    }

    @Transactional
    def update() {
        ShareRecord shareRecord     =   ShareRecord.findById(params.id)
        SimpleDateFormat format     =   new SimpleDateFormat("yyyy-MM-dd")
        shareRecord.buyShareNum     =   params.long('buyShareNum')
        shareRecord.sellShareNum    =   params.long('sellShareNum')
        shareRecord.transactionDate =   format.parse(params.transactionDate)
        shareRecord.tradingPrice    =   params.tradingPrice
        shareRecord.sharePrice      =   params.sharePrice
        shareRecord.turnover        =   params.turnover
        shareRecord.remark          =   params.remark

        if (shareRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (shareRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond shareRecord.errors, view:'edit',model: [userId:params.userId, shareRecord:shareRecord, sharePrice:params.sharePrice]
            /*respond shareRecord.errors, view:'edit'*/
            return
        }

        shareRecord.save flush:true

        //判断是否有交易记录相关信息，如果有，删除相关交易信息(不考虑大数据和速度!)
        List<TradingRecord> tradingRecordList = TradingRecord.findAllByBuyShareRecordOrSellShareRecord(shareRecord, shareRecord)
        if(tradingRecordList && tradingRecordList.size() > 0){
            for(TradingRecord tradingRecord : tradingRecordList){
                tradingRecord.delete(flush: true)
            }
        }

/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'shareRecord.label', default: 'ShareRecord'), shareRecord.id])
                redirect shareRecord
            }
            '*'{ respond shareRecord, [status: OK] }
        }*/
        flash.message = '修改记录成功'
        redirect action: 'index',  params: [shareRecord: shareRecord, id:shareRecord.user.id]
    }

    @Transactional
    def delete(ShareRecord shareRecord) {

        if (shareRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        shareRecord.delete flush:true

/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'shareRecord.label', default: 'ShareRecord'), shareRecord.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }*/
        flash.message = '删除交易记录成功'
        redirect action: 'index',  params: [shareRecord: shareRecord, id:shareRecord.user.id]
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'shareRecord.label', default: 'ShareRecord'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
