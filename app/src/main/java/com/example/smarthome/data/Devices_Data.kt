package com.example.smarthome.data

import com.example.smarthome.R

class Devices_Data {
    private var devicesList: MutableList<Smart_Devices> = ArrayList()

    fun addDevice(smart_devices: Smart_Devices) {
        devicesList.add(smart_devices)
    }

    fun getDevicesList(): List<Smart_Devices> {
        val Led = Smart_Devices()
        Led.iD = 0
        Led.name = "Led"
        Led.type = 0
        Led.signal = 0
        Led.iconOff = R.drawable.outline_light_
        Led.iconOn = R.drawable.device_11

        devicesList.add(0, Led)

        val GazSensor = Smart_Devices()
        GazSensor.iD = 1
        GazSensor.name = "GazSensor"
        GazSensor.signal = 1
        GazSensor.type = 0
        GazSensor.iconOff = R.drawable.baseline_smartphone_24
        devicesList.add(1, GazSensor)

        val SmokeSensor = Smart_Devices()
        SmokeSensor.iD = 2
        SmokeSensor.name = "SmokeSensor"
        SmokeSensor.type = 0
        SmokeSensor.signal = 1
        SmokeSensor.iconOff = R.drawable.baseline_smartphone_24

        devicesList.add(2, SmokeSensor)

        val PresenceSensor = Smart_Devices()
        PresenceSensor.name = "PresenceSensor"
        PresenceSensor.iD = 3
        PresenceSensor.type = 0
        PresenceSensor.signal = 0
        PresenceSensor.iconOff = R.drawable.baseline_smartphone_24
        devicesList.add(3, PresenceSensor)

        val ServoMotor = Smart_Devices()
        ServoMotor.iD = 4
        ServoMotor.name = "ServoMotor"
        ServoMotor.type = 1
        ServoMotor.signal = 0
        ServoMotor.iconOff = R.drawable.baseline_smartphone_24
        devicesList.add(4, ServoMotor)

        val Other = Smart_Devices()
        Other.iD = 5
        Other.name = "Other"
        Other.type = 3
        Other.iconOff = R.drawable.google_icon
        devicesList.add(5, Other)


        return devicesList
    }
}