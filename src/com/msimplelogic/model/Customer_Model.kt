package com.msimplelogic.model

import android.view.View


/**
 * Simple POJO model for example
 */
class Customer_Model {
    var distance:String? = null
    var lati:String? = null
    var longi:String? = null
    var name: String? = null
    var address: String? = null
    var mobile: String? = null
    var landline: String? = null
    var credit_profile : Double? = 0.0
    var outstanding: Double? = 0.0
    var overdue: Double? = 0.0
    var code: String? = ""
    var requestBtnClickListener: View.OnClickListener? = null


    constructor() {}
    constructor(name: String?, address: String?, mobile: String?, landline: String?, credit_profile: Double, outstanding: Double?, overdue: Double?, code: String?,distance: String?,lati: String?,longi: String?) {
        this.name = name
        this.address = address
        this.mobile = mobile
        this.landline = landline
        this.credit_profile = credit_profile
        this.outstanding = outstanding
        this.overdue = overdue
        this.code = code
        this.distance = distance
        this.lati = lati
        this.longi = longi

    }


    companion object {

        fun getRequestBtnClickListener(customerModel: Customer_Model): View.OnClickListener? {
            return customerModel.requestBtnClickListener
        }

//        fun setRequestBtnClickListener(requestBtnClickListener: View.OnClickListener?) {
//            this.requestBtnClickListener = requestBtnClickListener
//        }
    }


}