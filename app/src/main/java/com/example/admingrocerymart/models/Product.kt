package com.example.admingrocerymart.models

import java.util.UUID

data class Product(
    var prod_id : String? = null,
    var Prodtitle : String ?= null,
    var Prodcat : String ?= null,
    var Prodtype : String ?= null,
    var Produnit : String ?= null,
    var Prodqty : Int ?= null,
    var Prodstock : Int ?= null,
    var Prodprice : Int ?= null,
    var admin_uid  : String ?= null,
    var itemcount  : Int ?= null,
    var Prodimageuris : ArrayList<String?> ?= null,
)
