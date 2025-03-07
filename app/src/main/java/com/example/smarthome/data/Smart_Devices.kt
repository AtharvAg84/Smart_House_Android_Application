package com.example.smarthome.data

import java.io.Serializable

class Smart_Devices : Serializable {
    fun getPort(): Any {
        return port
    }

    var name: String? = null
    var iD: Int = 0
    var iconOff: Int = 0
    var iconOn: Int = 0

    var description: String? = null
    var type: Int = 0
    var signal: Int = 3
    var state: Int = 0
    var port: Int = 0


    constructor()

    constructor(name: String?, ID: Int, type: Int, signal: Int) {
        this.name = name
        this.iD = ID
        this.type = type
        this.signal = signal
    }

    constructor(
        name: String?,
        ID: Int,
        description: String?,
        type: Int,
        signal: Int,
        port: Int,
        state: Int
    ) {
        this.name = name
        this.iD = ID
        this.description = description
        this.type = type
        this.signal = signal
        this.port = port
        this.state = state
    }
}