package com.msimplelogic.model

class AttendanceModel {
    var date:String?=null
    var shift:String?=null
    var intime:String?=null
    var outtime:String?=null
    var hours:String?=null

    constructor(date: String, shift: String?, intime: String?, outtime: String?, hours: String?) {
        this.date = date
        this.shift = shift
        this.intime = intime
        this.outtime = outtime
        this.hours = hours
    }
}