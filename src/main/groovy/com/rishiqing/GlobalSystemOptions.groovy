package com.rishiqing

import grails.transaction.Transactional

/**
 * Created by solax on 2017-1-18.
 */
class GlobalSystemOptions {

    static GlobalSystemOptions systemOptions = null

    def dataMap = [:]


    private GlobalSystemOptions () {
        this.initialization()
    }

    static GlobalSystemOptions getInstance () {
        if (systemOptions) {
            return systemOptions
        } else {
            return systemOptions = new GlobalSystemOptions()
        }
    }

    def initialization () {
        List<SystemOptions> list =   SystemOptions.findAll()
        this.format(list)
    }

    def format (def list) {
        list.each { it ->
            dataMap[it.type] = it.value
        }
    }

    def getByType (def type) {
        return dataMap[type]
    }

    def getData () {
        return dataMap
    }

    private void setByType (String type, String systemOptions) {
        dataMap[type] = systemOptions
    }


    def save (String type, String value) {
        def systemOption  = SystemOptions.findByType(type)
        if (systemOption) { // 修改
            systemOption.value = value
        } else { // 新建
            systemOption = new SystemOptions(type:type, value:value)
        }
        if (systemOption.save(flush:true)) {
            this.setByType(type, systemOption.value)
            return true
        } else {
            return false
        }

    }
}
