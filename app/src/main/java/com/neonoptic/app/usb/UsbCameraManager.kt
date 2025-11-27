package com.neonoptic.app.usb

import android.content.Context
import android.hardware.usb.UsbManager
import com.jiangdg.usb.USBMonitor
import javax.inject.Inject
import javax.inject.Singleton

data class UsbCameraDevice(
    val deviceId: String,
    val vendorId: Int,
    val productId: Int,
    val serialNumber: String?
)

@Singleton
class UsbCameraManager @Inject constructor(
    private val context: Context
) {
    private val usbManager: UsbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    fun listUsbDevices(): List<UsbCameraDevice> {
        return usbManager.deviceList.values.map { device ->
            UsbCameraDevice(
                deviceId = device.deviceId.toString(),
                vendorId = device.vendorId,
                productId = device.productId,
                serialNumber = device.serialNumber
            )
        }
    }

    fun createMonitor(listener: USBMonitor.OnDeviceConnectListener): USBMonitor {
        return USBMonitor(context, listener)
    }
}
