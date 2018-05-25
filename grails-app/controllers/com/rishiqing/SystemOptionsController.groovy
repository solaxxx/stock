package com.rishiqing

import grails.converters.JSON

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

/*@Transactional(readOnly = true)*/
class SystemOptionsController {

   /* static allowedMethods = [get: "GET", save: "POST", update: "PUT", delete: "DELETE"]*/

    def index() {
        println('index')
        def result = GlobalSystemOptions.getInstance().getData()
        render(result as JSON)
    }

    def  save () {

    }

    def update () {
        println('update')
        def type = params.type
        def value = params.value
        GlobalSystemOptions systemOptions =  GlobalSystemOptions.getInstance()
        systemOptions.save(type, value)
        render (systemOptions.getData() as JSON)
    }

    def delete () {

    }

}
