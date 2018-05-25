package com.rishiqing

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class StockHistoryController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
/*        params.max = Math.min(max ?: 10, 100)
        def list = StockHistory.list(params)
        render (view:'index', model:[stockHistoryList:list, stockHistoryCount: StockHistory.count()])*/
/*        respond StockHistory.list(params), model:[stockHistoryCount: StockHistory.count()]*/

    }

    def list (Integer max) {
        params.max = Math.min(max ?: 10, 100)
        def list = StockHistory.list(params)
        render (view:'list', model:[stockHistoryList:list, stockHistoryCount: StockHistory.count()])
    }

    def show(StockHistory stockHistory) {
        respond stockHistory
    }

    def create() {
        respond new StockHistory(params)
    }

    @Transactional
    def save(StockHistory stockHistory) {
        if (stockHistory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stockHistory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stockHistory.errors, view:'create'
            return
        }

        stockHistory.save flush:true
        //flash.message = message(code: 'default.created.message', args: ['创建成功'])
        flash.message = '创建成功'
/*        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'stockHistory.label', default: 'StockHistory'), stockHistory.id])
                redirect stockHistory
            }
            '*' { respond stockHistory, [status: CREATED] }
        }*/
        redirect action: 'list'
    }

    def edit(StockHistory stockHistory) {
        respond stockHistory
    }

    @Transactional
    def update(StockHistory stockHistory) {
        if (stockHistory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (stockHistory.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond stockHistory.errors, view:'edit'
            return
        }

        stockHistory.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'stockHistory.label', default: 'StockHistory'), stockHistory.id])
                /*redirect  action:'index' stockHistory*/
                redirect  action:'list'
            }
            '*'{ respond stockHistory, [status: OK] }
        }
    }

    @Transactional
    def delete(StockHistory stockHistory) {
        println('--------------------')
        if (stockHistory == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        stockHistory.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'stockHistory.label', default: 'StockHistory'), stockHistory.id])
               /* redirect action:"index", method:"GET"*/
                redirect action:"list", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'stockHistory.label', default: 'StockHistory'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
