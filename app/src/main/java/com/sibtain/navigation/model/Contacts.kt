package com.sibtain.navigation.model

class Contacts {
    var name = ""
    var number = ""
    var contactId =""
    var contactThumbnail : String?=null

    constructor(name: String, number: String) {
        this.name = name
        this.number = number
    }
    constructor(name: String, number: String,contactId: String) {
        this.name = name
        this.number = number
        this.contactId= contactId
    }
    constructor(name: String, number: String,contactId: String,thum : String) {
        this.name = name
        this.number = number
        this.contactId= contactId
        this.contactThumbnail=thum
    }

}