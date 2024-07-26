package com.guuri11.isi_wear.domain

data class Command (
    var id: String,
    var log: String,
    var chat: Chat,
    var content: String,
    var messageType: String
) {

}