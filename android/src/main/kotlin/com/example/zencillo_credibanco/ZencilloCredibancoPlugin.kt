package com.example.zencillo_credibanco

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.NonNull
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry

/** ZencilloCredibancoPlugin */
class ZencilloCredibancoPlugin :
    FlutterPlugin,
    MethodCallHandler,
    ActivityAware,
    PluginRegistry.ActivityResultListener {

    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private var activity: Activity? = null
    private var pendingResult: Result? = null

    companion object {
        private const val CHANNEL_NAME = "zencillo_credibanco"

        private const val REQUEST_SALE = 1002
        private const val REQUEST_PRINT = 10021
        private const val REQUEST_SCAN = 10022
        private const val REQUEST_NFC = 10023

        private const val CREDIBANCO_PACKAGE = "com.credibanco.smartpos"
        private const val CREDIBANCO_PERIPHERALS_PACKAGE = "com.credibanco.smartposperipherals"

        private const val HASH_CODE = "MkmxGEU="
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.applicationContext
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, CHANNEL_NAME)
        channel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when (call.method) {
            "isInstalled" -> {
                result.success(isPackageInstalled(CREDIBANCO_PACKAGE))
            }

            "isPeripheralsInstalled" -> {
                result.success(isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE))
            }

            "sale" -> {
                if (!isPackageInstalled(CREDIBANCO_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchSale(call)
            }

            "saleQr" -> {
                if (!isPackageInstalled(CREDIBANCO_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchSaleQr(call)
            }

            "print" -> {
                if (!isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de periféricos Credibanco no está instalada.",
                        null
                    )
                    return
                }

                val text = call.argument<String>("text") ?: ""

                pendingResult = result
                launchPrint(text)
            }

            "scan" -> {
                if (!isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de periféricos Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchScan()
            }

            "nfc" -> {
                if (!isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de periféricos Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchNfc()
            }

            else -> result.notImplemented()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        try {
            when (requestCode) {
                REQUEST_SALE -> handleSaleResult(resultCode, data)
                REQUEST_PRINT -> handlePrintResult()
                REQUEST_SCAN -> handleScanResult(resultCode, data)
                REQUEST_NFC -> handleNfcResult(resultCode, data)
                else -> return false
            }
        } catch (e: Exception) {
            pendingResult?.error(
                "FAILED",
                e.message ?: "Error desconocido.",
                null
            )
        } finally {
            pendingResult = null
        }

        return true
    }

    private fun launchSale(call: MethodCall) {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val amount = call.argument<Double>("amount") ?: 0.0
        val taxAmount = call.argument<Double>("taxAmount") ?: 0.0
        val tip = call.argument<String>("tip") ?: "0"
        val iac = call.argument<String>("iac") ?: "0"

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra("amount", amount.toLong())
        sendIntent.putExtra("TAX_AMOUNT", taxAmount.toLong())
        sendIntent.putExtra("tip", tip.toLong())
        sendIntent.putExtra("iac", iac.toLong())
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)

        currentActivity.startActivityForResult(sendIntent, REQUEST_SALE)
    }

    private fun launchSaleQr(call: MethodCall) {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val amount = call.argument<Double>("amount") ?: 0.0
        val taxAmount = call.argument<Double>("taxAmount") ?: 0.0
        val tip = call.argument<String>("tip") ?: "0"
        val iac = call.argument<String>("iac") ?: "0"

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra("amount", amount.toLong())
        sendIntent.putExtra("TAX_AMOUNT", taxAmount.toLong())
        sendIntent.putExtra("tip", tip.toLong())
        sendIntent.putExtra("iac", iac.toLong())
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)

        val component = ComponentName(
            CREDIBANCO_PACKAGE,
            "com.credibanco.smartpos.presentation.activity.ExternalSellQrActivity"
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_SALE)
    }

    private fun launchPrint(text: String) {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val cleanText = text.replace(",", ".")

        val valuesToSend = ArrayList<String>()
        valuesToSend.add("TEXT,$cleanText,FONT_NORMAL,ALIGN_CENTER")

        val sendIntent = Intent(Intent.ACTION_MAIN)
        sendIntent.setPackage(CREDIBANCO_PERIPHERALS_PACKAGE)
        sendIntent.action = Intent.ACTION_SEND_MULTIPLE
        sendIntent.type = "*/*"

        sendIntent.putExtra("TYPEFACE", "TYPEFACE_DEFAULT")
        sendIntent.putExtra("LETTER_SPACING", 6)
        sendIntent.putExtra("GRAY_LEVEL", "GRAY_LEVEL_2")
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putStringArrayListExtra(Intent.EXTRA_STREAM, valuesToSend)

        val component = ComponentName(
            CREDIBANCO_PERIPHERALS_PACKAGE,
            "com.credibanco.smartposperipherals.presentation.activity.ExternalPrintingActivity"
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_PRINT)
    }

    private fun launchScan() {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val sendIntent = Intent(Intent.ACTION_MAIN)
        sendIntent.putExtra("SHOWBAR", true)
        sendIntent.putExtra("SHOWBACK", true)
        sendIntent.putExtra("SHOWTITLE", true)
        sendIntent.putExtra("SHOWSWITCH", true)
        sendIntent.putExtra("SHOWMENU", true)
        sendIntent.putExtra("TITLE", "ZENCILLO SOFTWARE")
        sendIntent.putExtra("TITLESIZE", 10)
        sendIntent.putExtra("TIPSIZE", 10)
        sendIntent.putExtra("SCANTIP", "UBIQUE EL DATAFONO DONDE ESTA EL CODIGO DE BARRAS/QR.")
        sendIntent.putExtra("HASH_CODE", HASH_CODE)

        val component = ComponentName(
            CREDIBANCO_PERIPHERALS_PACKAGE,
            "com.credibanco.smartposperipherals.presentation.activity.ExternalScannerActivity"
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_SCAN)
    }

    private fun launchNfc() {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val sendIntent = Intent(Intent.ACTION_MAIN)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)

        val component = ComponentName(
            CREDIBANCO_PERIPHERALS_PACKAGE,
            "com.credibanco.smartposperipherals.presentation.activity.ExternalNfcReadActivity"
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_NFC)
    }

    private fun handleSaleResult(resultCode: Int, data: Intent?) {
        if (resultCode != 6000 && resultCode != 8000) {
            pendingResult?.error("FAILED", "El pago no se completó.", null)
            return
        }

        val bundle: Bundle? = data?.extras

        val map = mutableMapOf<String, Any>()
        map["success"] = true
        map["response"] = "Operacion Exitosa."
        map["cardNumber"] = bundle?.getString("LAST4_TO_PRINT") ?: ""
        map["approvedNumber"] = bundle?.getString("AUTORIZATION_SELL_APPROVED") ?: ""
        map["transactionNumber"] = bundle?.getString("AUTORIZATION_SELL_APPROVED") ?: ""
        map["terminalId"] = bundle?.getString("TERMINAL_ID") ?: ""
        map["franchise"] = bundle?.getString("FRANCHISE_TO_PRINT") ?: ""
        map["autorizationCode"] = bundle?.getString("AUTORIZATION_SELL_APPROVED") ?: ""
        map["authorizationCode"] = bundle?.getString("AUTORIZATION_SELL_APPROVED") ?: ""
        map["amount"] = bundle?.getString("TOTAL_AMOUNT_APROVED") ?: ""
        map["iva"] = bundle?.getString("IVA_TO_PRINT") ?: ""
        map["receipt"] = bundle?.getString("RECEIPT_TO_PRINT") ?: ""
        map["rrn"] = bundle?.getString("RRN_TO_PRINT") ?: ""
        map["timeDate"] = bundle?.getString("TIMEDATE_TO_PRINT") ?: ""
        map["responseCode"] = bundle?.getString("RESPONSE_CODE") ?: ""
        map["accountType"] = bundle?.getString("ACCTYPE_TO_PRINT") ?: ""
        map["quotas"] = bundle?.getString("QUOTAS_TO_PRINT") ?: ""
        map["merchantPosId"] = bundle?.getString("MERCHANT_POS_ID") ?: ""

        pendingResult?.success(map)
    }

    private fun handlePrintResult() {
        pendingResult?.success("Printer Ok")
    }

    private fun handleScanResult(resultCode: Int, data: Intent?) {
        if (resultCode == 80000) {
            val response = data?.getStringExtra("SCANNER") ?: ""
            pendingResult?.success(response)
        } else {
            pendingResult?.error("FAILED", "El lector falló.", null)
        }
    }

    private fun handleNfcResult(resultCode: Int, data: Intent?) {
        if (resultCode == 60000) {
            val response = data?.getStringExtra("NFC_READ_TAG") ?: ""

            if (response != "NODATA") {
                pendingResult?.success(response)
            } else {
                pendingResult?.error("FAILED", "No se encontraron datos.", null)
            }
        } else {
            pendingResult?.error("FAILED", "El lector NFC falló.", null)
        }
    }

    private fun isPackageInstalled(@NonNull packageName: String): Boolean {
        return try {
            context.packageManager.getApplicationInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}