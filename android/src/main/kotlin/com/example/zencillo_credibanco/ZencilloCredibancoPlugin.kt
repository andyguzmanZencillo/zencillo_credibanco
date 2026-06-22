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
        private const val REQUEST_CANCEL = 10024
        private const val REQUEST_BREB = 10025
        private const val REQUEST_OTHER_FUNCTIONS = 10026

        private const val CREDIBANCO_PACKAGE = "com.credibanco.smartpos"
        private const val CREDIBANCO_PERIPHERALS_PACKAGE = "com.credibanco.smartposperipherals"

        private const val CREDIBANCO_QR_ACTIVITY =
            "com.credibanco.smartpos.presentation.activity.ExternalSellQrActivity"

        private const val CREDIBANCO_CANCEL_ACTIVITY =
            "com.credibanco.smartpos.presentation.activity.ExternalCancelSellActivity"

        private const val CREDIBANCO_BREB_ACTIVITY =
            "com.credibanco.smartpos.presentation.activity.ExternalSellBreBActivity"

        private const val CREDIBANCO_OTHER_FUNCTIONS_ACTIVITY =
            "com.credibanco.smartpos.presentation.activity.ExternalOtherFunctionsActivity"

        private const val CREDIBANCO_PRINT_ACTIVITY =
            "com.credibanco.smartposperipherals.presentation.activity.ExternalPrintingActivity"

        private const val CREDIBANCO_SCAN_ACTIVITY =
            "com.credibanco.smartposperipherals.presentation.activity.ExternalScannerActivity"

        private const val CREDIBANCO_NFC_ACTIVITY =
            "com.credibanco.smartposperipherals.presentation.activity.ExternalNfcReadActivity"

        private const val HASH_CODE = "MkmxGEU="

        private const val PASSWORD_PROVIDER_TITLE = "title"
        private const val RECEIPT_ID = "RECEIPT_ID"
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
            "getPlatformVersion" -> {
                result.success("Android ${android.os.Build.VERSION.RELEASE}")
            }

            "isInstalled" -> {
                result.success(isPackageInstalled(CREDIBANCO_PACKAGE))
            }

            "isPeripheralsInstalled" -> {
                result.success(isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE))
            }

            "sale", "credibanco" -> {
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

            "saleQr", "credibancoqr" -> {
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

            "credibancoAnulacion" -> {
                if (!isPackageInstalled(CREDIBANCO_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result

                val receiptId = call.argument<String>("receiptId")
                    ?: call.argument<String>("receipt")
                    ?: call.argument<String>("numeroRecibo")
                    ?: ""

                launchCredibancoAnulacion(receiptId)
            }

            "credibancoBreb", "breb" -> {
                if (!isPackageInstalled(CREDIBANCO_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchCredibancoBreb(call)
            }

            "credibancoOtherFunctions", "otherFunctions" -> {
                if (!isPackageInstalled(CREDIBANCO_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                launchCredibancoOtherFunctions()
            }

            "print", "credibancoPrint" -> {
                if (!isPackageInstalled(CREDIBANCO_PERIPHERALS_PACKAGE)) {
                    result.error(
                        "APP_NOT_INSTALLED",
                        "La aplicación de periféricos Credibanco no está instalada.",
                        null
                    )
                    return
                }

                pendingResult = result
                val text = call.argument<String>("text") ?: ""
                launchPrint(text)
            }

            "scan", "credibancoScan" -> {
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

            "nfc", "credibancoNFC" -> {
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

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ): Boolean {
        try {
            when (requestCode) {
                REQUEST_SALE -> handleTransactionResult(
                    resultCode = resultCode,
                    data = data,
                    responseMessage = "Operacion Exitosa."
                )

                REQUEST_CANCEL -> handleTransactionResult(
                    resultCode = resultCode,
                    data = data,
                    responseMessage = "Anulación Exitosa."
                )

                REQUEST_BREB -> handleTransactionResult(
                    resultCode = resultCode,
                    data = data,
                    responseMessage = "Operacion Bre-B Exitosa."
                )

                REQUEST_OTHER_FUNCTIONS -> handleOtherFunctionsResult(resultCode, data)

                REQUEST_PRINT -> handlePrintResult(resultCode, data)

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

        val amount = getDoubleArgument(call, "amount").toLong()
        val taxAmount = getDoubleArgument(call, "TAX_AMOUNT", "taxAmount").toLong()
        val tip = getLongArgument(call, "tip")
        val iac = getLongArgument(call, "iac")

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra("amount", amount)
        sendIntent.putExtra("TAX_AMOUNT", taxAmount)
        sendIntent.putExtra("tip", tip)
        sendIntent.putExtra("iac", iac)
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

        val amount = getDoubleArgument(call, "amount").toLong()
        val taxAmount = getDoubleArgument(call, "TAX_AMOUNT", "taxAmount").toLong()
        val tip = getLongArgument(call, "tip")
        val iac = getLongArgument(call, "iac")

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra("amount", amount)
        sendIntent.putExtra("TAX_AMOUNT", taxAmount)
        sendIntent.putExtra("tip", tip)
        sendIntent.putExtra("iac", iac)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)

        val component = ComponentName(
            CREDIBANCO_PACKAGE,
            CREDIBANCO_QR_ACTIVITY
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_SALE)
    }

    private fun launchCredibancoAnulacion(receiptId: String) {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra(PASSWORD_PROVIDER_TITLE, true)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)
        sendIntent.putExtra(RECEIPT_ID, receiptId)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)

        val component = ComponentName(
            CREDIBANCO_PACKAGE,
            CREDIBANCO_CANCEL_ACTIVITY
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_CANCEL)
    }

    private fun launchCredibancoBreb(call: MethodCall) {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val amount = getDoubleArgument(call, "amount").toLong()
        val taxAmount = getDoubleArgument(call, "TAX_AMOUNT", "taxAmount").toLong()
        val tip = getLongArgument(call, "tip")
        val iac = getLongArgument(call, "iac")

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra("amount", amount)
        sendIntent.putExtra("TAX_AMOUNT", taxAmount)
        sendIntent.putExtra("tip", tip)
        sendIntent.putExtra("iac", iac)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)

        val component = ComponentName(
            CREDIBANCO_PACKAGE,
            CREDIBANCO_BREB_ACTIVITY
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_BREB)
    }

    private fun launchCredibancoOtherFunctions() {
        val currentActivity = activity

        if (currentActivity == null) {
            pendingResult?.error("NO_ACTIVITY", "No hay Activity disponible.", null)
            return
        }

        val sendIntent = Intent()
        sendIntent.setPackage(CREDIBANCO_PACKAGE)
        sendIntent.action = Intent.ACTION_SENDTO
        sendIntent.type = "text/plain"

        sendIntent.putExtra(PASSWORD_PROVIDER_TITLE, true)
        sendIntent.putExtra("HASH_CODE", HASH_CODE)
        sendIntent.putExtra("PACKAGE_NAME", currentActivity.packageName)

        val component = ComponentName(
            CREDIBANCO_PACKAGE,
            CREDIBANCO_OTHER_FUNCTIONS_ACTIVITY
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_OTHER_FUNCTIONS)
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
            CREDIBANCO_PRINT_ACTIVITY
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
            CREDIBANCO_SCAN_ACTIVITY
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
            CREDIBANCO_NFC_ACTIVITY
        )

        sendIntent.component = component
        currentActivity.startActivityForResult(sendIntent, REQUEST_NFC)
    }

    private fun handleTransactionResult(
        resultCode: Int,
        data: Intent?,
        responseMessage: String
    ) {
        val bundle: Bundle? = data?.extras

        if (resultCode == 6000) {
            val map = buildCredibancoTransactionMap(
                bundle = bundle,
                responseMessage = responseMessage,
                resultCode = resultCode
            )

            pendingResult?.success(map)
        } else {
            pendingResult?.error(
                "FAILED",
                getCredibancoErrorMessage(resultCode, bundle),
                buildErrorDetails(resultCode, bundle)
            )
        }
    }

    private fun handleOtherFunctionsResult(resultCode: Int, data: Intent?) {
        val bundle: Bundle? = data?.extras

        if (resultCode == 6000) {
            val map = mutableMapOf<String, Any>()

            map["success"] = true
            map["resultCode"] = resultCode
            map["response"] = "Operación de cierre/reportería exitosa."
            map["isCloseCorrect"] = getBooleanFromBundle(bundle, "IS_CLOSING_PROCESS")
            map["dateClose"] = getStringFromBundle(bundle, "FINAL_CLOSING_DATE")
            map["timeClose"] = getStringFromBundle(bundle, "FINAL_CLOSING_TIME")
            map["typeReport"] = getStringFromBundle(
                bundle,
                "TYPE_ACTION_TITLE",
                "TYPE_REPORT_TITLE"
            )
            map["rawExtras"] = bundleToStringMap(bundle)

            pendingResult?.success(map)
        } else {
            pendingResult?.error(
                "FAILED",
                getCredibancoErrorMessage(resultCode, bundle),
                buildErrorDetails(resultCode, bundle)
            )
        }
    }

    private fun handlePrintResult(resultCode: Int, data: Intent?) {
        if (resultCode == 40000 || resultCode == 0 || resultCode == 6000) {
            pendingResult?.success("Printer Ok")
        } else {
            pendingResult?.error(
                "FAILED",
                "La impresión no se completó. Código: $resultCode",
                buildErrorDetails(resultCode, data?.extras)
            )
        }
    }

    private fun handleScanResult(resultCode: Int, data: Intent?) {
        if (resultCode == 80000) {
            val response = data?.getStringExtra("SCANNER") ?: ""
            pendingResult?.success(response)
        } else {
            pendingResult?.error(
                "FAILED",
                "El lector falló. Código: $resultCode",
                buildErrorDetails(resultCode, data?.extras)
            )
        }
    }

    private fun handleNfcResult(resultCode: Int, data: Intent?) {
        if (resultCode == 60000) {
            val response = data?.getStringExtra("NFC_READ_TAG") ?: ""

            if (response != "NODATA") {
                pendingResult?.success(response)
            } else {
                pendingResult?.error(
                    "FAILED",
                    "No se encontraron datos.",
                    buildErrorDetails(resultCode, data?.extras)
                )
            }
        } else {
            pendingResult?.error(
                "FAILED",
                "El lector NFC falló. Código: $resultCode",
                buildErrorDetails(resultCode, data?.extras)
            )
        }
    }

    private fun buildCredibancoTransactionMap(
        bundle: Bundle?,
        responseMessage: String,
        resultCode: Int
    ): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()

        map["success"] = true
        map["resultCode"] = resultCode
        map["response"] = responseMessage

        map["cardNumber"] = getStringFromBundle(bundle, "LAST4_TO_PRINT")
        map["lastFourDigitsCard"] = getStringFromBundle(bundle, "LAST4_TO_PRINT")

        map["approvedNumber"] = getStringFromBundle(bundle, "AUTORIZATION_SELL_APPROVED")
        map["transactionNumber"] = getStringFromBundle(bundle, "AUTORIZATION_SELL_APPROVED")

        map["autorizationCode"] = getStringFromBundle(bundle, "AUTORIZATION_SELL_APPROVED")
        map["authorizationCode"] = getStringFromBundle(bundle, "AUTORIZATION_SELL_APPROVED")

        map["amount"] = getStringFromBundle(
            bundle,
            "TOTAL_AMOUNT_APROVED",
            "TOTAL_AMOUNT_APPROVED"
        )

        map["monto"] = getStringFromBundle(
            bundle,
            "TOTAL_AMOUNT_APROVED",
            "TOTAL_AMOUNT_APPROVED"
        )

        map["iva"] = getStringFromBundle(bundle, "IVA_TO_PRINT")
        map["receipt"] = getStringFromBundle(bundle, "RECEIPT_TO_PRINT")
        map["rrn"] = getStringFromBundle(bundle, "RRN_TO_PRINT")
        map["terminalId"] = getStringFromBundle(bundle, "TERMINAL_ID")
        map["timeDate"] = getStringFromBundle(bundle, "TIMEDATE_TO_PRINT")
        map["responseCode"] = getStringFromBundle(bundle, "RESPONSE_CODE")
        map["franchise"] = getStringFromBundle(bundle, "FRANCHISE_TO_PRINT")
        map["accountType"] = getStringFromBundle(bundle, "ACCTYPE_TO_PRINT")
        map["quotas"] = getStringFromBundle(bundle, "QUOTAS_TO_PRINT")
        map["merchantPosId"] = getStringFromBundle(bundle, "MERCHANT_POS_ID")

        map["apiResult"] = getStringFromBundle(bundle, "API_RESULT")
        map["extraInformation"] = getStringFromBundle(
            bundle,
            "extra_information",
            "EXTRA_INFORMATION"
        )

        map["rawExtras"] = bundleToStringMap(bundle)

        return map
    }

    private fun buildErrorDetails(
        resultCode: Int,
        bundle: Bundle?
    ): MutableMap<String, Any> {
        val map = mutableMapOf<String, Any>()

        map["success"] = false
        map["resultCode"] = resultCode
        map["message"] = getCredibancoErrorMessage(resultCode, bundle)
        map["apiResult"] = getStringFromBundle(bundle, "API_RESULT")
        map["rawExtras"] = bundleToStringMap(bundle)

        return map
    }

    private fun getCredibancoErrorMessage(resultCode: Int, bundle: Bundle?): String {
        val apiResult = getStringFromBundle(bundle, "API_RESULT")

        if (apiResult.isNotEmpty()) {
            return apiResult
        }

        return when (resultCode) {
            0 -> "Proceso transaccional cancelado."
            4010 -> "Error en lectura o error en el proceso transaccional."
            6010 -> "Proceso venta/anulación con tarjeta rechazado o declinado."
            8010 -> "Proceso venta/anulación con QR rechazado o declinado."
            else -> "La operación no se completó. Código: $resultCode"
        }
    }

    private fun getStringFromBundle(
        bundle: Bundle?,
        vararg keys: String
    ): String {
        if (bundle == null) return ""

        for (key in keys) {
            val value = bundle.get(key)
            if (value != null) return value.toString()
        }

        return ""
    }

    private fun getBooleanFromBundle(
        bundle: Bundle?,
        key: String
    ): Boolean {
        if (bundle == null) return false

        val value = bundle.get(key)

        return when (value) {
            is Boolean -> value
            is String -> value.equals("true", ignoreCase = true)
            is Int -> value == 1
            else -> false
        }
    }

    private fun bundleToStringMap(bundle: Bundle?): MutableMap<String, String> {
        val map = mutableMapOf<String, String>()

        if (bundle == null) return map

        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            map[key] = value?.toString() ?: ""
        }

        return map
    }

    private fun getDoubleArgument(
        call: MethodCall,
        vararg keys: String
    ): Double {
        for (key in keys) {
            val value = call.argument<Any>(key)

            when (value) {
                is Double -> return value
                is Float -> return value.toDouble()
                is Int -> return value.toDouble()
                is Long -> return value.toDouble()
                is String -> return value.toDoubleOrNull() ?: 0.0
            }
        }

        return 0.0
    }

    private fun getLongArgument(
        call: MethodCall,
        key: String
    ): Long {
        val value = call.argument<Any>(key)

        return when (value) {
            is Double -> value.toLong()
            is Float -> value.toLong()
            is Int -> value.toLong()
            is Long -> value
            is String -> value.toDoubleOrNull()?.toLong() ?: 0L
            else -> 0L
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