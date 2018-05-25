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
class OptionsRecordController {

    def dataSource

    /*   static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]*/

    // 当前股数，股票总值， 股份占比
    def list (Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def result = []
        User currentUser  = getAuthenticatedUser()
        def query = {
            createAlias('optionsRecord', 'optionsRecord', JoinType.LEFT_OUTER_JOIN)
            eq('isDeleted', false)
            if (currentUser.getAuthorityValue() != ConstantUtil.ROLE_ADMIN) {
                eq('id', currentUser.id)
            }
            projections  {
                property('id' ,'userId')
                property('realName' ,'realName')
                sqlProjection('(SUM(IFNULL(buy_share_num, 0)) - SUM(IFNULL(sell_share_num, 0))) as totalShare', 'totalShare', StandardBasicTypes.BIG_INTEGER)
            }
            /*   order('optionsRecord.totalShare', 'desc')*/
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
/*        def count = OptionsRecord.createCriteria().count() {
            eq('user.id', id)
        }*/
        def list = OptionsRecord.createCriteria().list(params) {
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
        OptionsRecord optionsRecord = OptionsRecord.findById(id)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'show', model: [optionsRecord:optionsRecord, sharePrice:sharePrice, userId:params.userId])
        respond optionsRecord
    }

    def create(Long id) {
        OptionsRecord optionsRecord = new OptionsRecord(params)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'create', model: [optionsRecord:optionsRecord, sharePrice:sharePrice, userId:id])
        //respond new OptionsRecord(params)
    }

    @Transactional
    def save() {
/*        if (optionsRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }*/
        User user  = User.findById(params.long('userId'))
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd")
        OptionsRecord optionsRecord = new OptionsRecord(
                user: user,
                buyShareNum : params.long('buyShareNum'),
                sellShareNum : params.long('sellShareNum'),
                transactionDate : format.parse(params.transactionDate),
                tradingPrice :  params.tradingPrice,
                sharePrice: params.sharePrice,
                turnover :  params.turnover,
                remark :  params.remark
        )

        if (optionsRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond optionsRecord.errors, view:'create',model: [userId:user.id, optionsRecord:optionsRecord, sharePrice:params.sharePrice]
            return
        }

        optionsRecord.save flush:true

/*        request.withFormat {
            form multipartForm {
*//*                flash.message = message(code: 'default.created.message', args: [message(code: 'optionsRecord.label', default: 'OptionsRecord'), optionsRecord.id])
                redirect optionsRecord*//*
                flash.message = '添加交易记录成功'
                redirect action: 'index',  params: [userId:optionsRecord.user.id]
            }
            '*' { respond optionsRecord, [status: CREATED] }
        }*/
        flash.message = '添加交易记录成功'
        redirect action: 'index',  params: [optionsRecord: optionsRecord, id:optionsRecord.user.id]
    }

    def edit(Long id) {
        OptionsRecord optionsRecord = OptionsRecord.findById(id)
        def sharePrice = GlobalSystemOptions.getInstance().getByType('share_price')
        render (view: 'edit', model: [optionsRecord:optionsRecord, sharePrice:sharePrice, userId:params.userId])
        /* respond optionsRecord*/
    }

    @Transactional
    def update() {
        OptionsRecord optionsRecord     =   OptionsRecord.findById(params.id)
        SimpleDateFormat format     =   new SimpleDateFormat("yyyy-MM-dd")
        optionsRecord.buyShareNum     =   params.long('buyShareNum')
        optionsRecord.sellShareNum    =   params.long('sellShareNum')
        optionsRecord.transactionDate =   format.parse(params.transactionDate)
        optionsRecord.tradingPrice    =   params.tradingPrice
        optionsRecord.sharePrice      =   params.sharePrice
        optionsRecord.turnover        =   params.turnover
        optionsRecord.remark          =   params.remark

        if (optionsRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (optionsRecord.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond optionsRecord.errors, view:'edit',model: [userId:params.userId, optionsRecord:optionsRecord, sharePrice:params.sharePrice]
            /*respond optionsRecord.errors, view:'edit'*/
            return
        }

        optionsRecord.save flush:true

/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'optionsRecord.label', default: 'OptionsRecord'), optionsRecord.id])
                redirect optionsRecord
            }
            '*'{ respond optionsRecord, [status: OK] }
        }*/
        flash.message = '修改记录成功'
        redirect action: 'index',  params: [optionsRecord: optionsRecord, id:optionsRecord.user.id]
    }

    @Transactional
    def delete(OptionsRecord optionsRecord) {

        if (optionsRecord == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        optionsRecord.delete flush:true

/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'optionsRecord.label', default: 'OptionsRecord'), optionsRecord.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }*/
        flash.message = '删除交易记录成功'
        redirect action: 'index',  params: [optionsRecord: optionsRecord, id:optionsRecord.user.id]
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'optionsRecord.label', default: 'OptionsRecord'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
