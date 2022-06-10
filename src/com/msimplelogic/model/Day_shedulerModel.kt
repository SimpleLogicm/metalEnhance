package com.msimplelogic.model

class Day_shedulerModel{
    var image:String?=null
    var shopname:String?=null
    var time:String?=null
    var distance:String?=null
    var lat:String?=null
    var long:String?=null
    var call:String?=null


    constructor(image: String?, shopname: String?, time: String?, distance: String?, lat: String?, long: String?, call: String?) {
        this.image = image
        this.shopname = shopname
        this.time = time
        this.distance = distance
        this.lat = lat
        this.long = long
        this.call = call
    }
}
