package com.msimplelogic.model

class Invoice_model {
    var name:String?=null
    var code:String?=null
    var quantity:String?=null
    var price:String?=null
    var amount:String?=null

    constructor(name: String?, code: String?, quantity: String?, price: String?, amount: String?) {
        this.name = name
        this.code = code
        this.quantity = quantity
        this.price = price
        this.amount = amount
    }
}