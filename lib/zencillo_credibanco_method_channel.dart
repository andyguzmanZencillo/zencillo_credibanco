import 'dart:developer';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:oxidized/oxidized.dart';

import 'models/credibanco_response.dart';
import 'zencillo_credibanco_platform_interface.dart';

class MethodChannelZencilloCredibanco extends ZencilloCredibancoPlatform {
  @visibleForTesting
  final method = const MethodChannel('zencillo_credibanco');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await method.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  @override
  Future<Result<CredibancoResponse, String>> credibanco({
    required String amount,
    required String tax,
  }) async {
    try {
      final response = await method.invokeMethod<dynamic>(
        'credibanco',
        {
          'amount': double.parse(amount),
          'TAX_AMOUNT': double.parse(tax),
          'tip': '0',
          'iac': '0',
        },
      );

      if (response == null) {
        return const Result.err(
          'No se recibió respuesta del método credibanco',
        );
      }

      final mapped = Map<String, dynamic>.from(response as Map);
      final operacion = CredibancoResponse.fromJson(mapped);

      log(mapped.toString());

      return Result.ok(operacion);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO PAYMENT FAILED ===> $e');
      log('CREDIBANCO PAYMENT FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<Unit, String>> credibancoPrint(String text) async {
    try {
      await method.invokeMethod<String>(
        'credibancoPrint',
        {'text': text},
      );

      return const Result.ok(unit);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO PRINT FAILED ===> $e');
      log('CREDIBANCO PRINT FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<String, String>> credibancoScan() async {
    try {
      final response = await method.invokeMethod<String>('credibancoScan');
      return Result.ok(response ?? '');
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO SCAN FAILED ===> $e');
      log('CREDIBANCO SCAN FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<String, String>> credibancoNFC() async {
    try {
      final response = await method.invokeMethod<String>('credibancoNFC');

      final reduced = response?.substring(0, 16);
      final replaced = reduced?.replaceRange(6, 8, '');

      return Result.ok(replaced ?? '');
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO NFC FAILED ===> $e');
      log('CREDIBANCO NFC FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<CredibancoResponse, String>> credibancoQr({
    required String amount,
    required String tax,
  }) async {
    try {
      final response = await method.invokeMethod<dynamic>(
        'credibancoqr',
        {
          'amount': double.parse(amount),
          'TAX_AMOUNT': double.parse(tax),
          'tip': '0',
          'iac': '0',
        },
      );

      if (response == null) {
        return const Result.err(
          'No se recibió respuesta del método credibancoqr',
        );
      }

      final mapped = Map<String, dynamic>.from(response as Map);
      final operacion = CredibancoResponse.fromJson(mapped);

      log(mapped.toString());

      return Result.ok(operacion);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO QR PAYMENT FAILED ===> $e');
      log('CREDIBANCO QR PAYMENT FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<CredibancoResponse, String>> credibancoAnulacion({
    required String receiptId,
  }) async {
    try {
      final response = await method.invokeMethod<dynamic>(
        'credibancoAnulacion',
        {
          'receiptId': receiptId,
        },
      );

      if (response == null) {
        return const Result.err(
          'No se recibió respuesta del método credibancoAnulacion',
        );
      }

      final mapped = Map<String, dynamic>.from(response as Map);
      final operacion = CredibancoResponse.fromJson(mapped);

      log(mapped.toString());

      return Result.ok(operacion);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO ANULACION FAILED ===> $e');
      log('CREDIBANCO ANULACION FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<CredibancoResponse, String>> credibancoBreb({
    required String amount,
    required String tax,
  }) async {
    try {
      final response = await method.invokeMethod<dynamic>(
        'credibancoBreb',
        {
          'amount': double.parse(amount),
          'TAX_AMOUNT': double.parse(tax),
          'tip': '0',
          'iac': '0',
        },
      );

      if (response == null) {
        return const Result.err(
          'No se recibió respuesta del método credibancoBreb',
        );
      }

      final mapped = Map<String, dynamic>.from(response as Map);
      final operacion = CredibancoResponse.fromJson(mapped);

      log(mapped.toString());

      return Result.ok(operacion);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO BREB PAYMENT FAILED ===> $e');
      log('CREDIBANCO BREB PAYMENT FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<Map<String, dynamic>, String>>
      credibancoOtherFunctions() async {
    try {
      final response = await method.invokeMethod<dynamic>(
        'credibancoOtherFunctions',
      );

      if (response == null) {
        return const Result.err(
          'No se recibió respuesta del método credibancoOtherFunctions',
        );
      }

      final mapped = Map<String, dynamic>.from(response as Map);

      log(mapped.toString());

      return Result.ok(mapped);
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO OTHER FUNCTIONS FAILED ===> $e');
      log('CREDIBANCO OTHER FUNCTIONS FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }

  @override
  Future<Result<String, String>> credibancoBluetooth() async {
    try {
      final response = await method.invokeMethod<String>('credibancoBt');

      return Result.ok(response ?? 'Bluetooth configurado');
    } on PlatformException catch (e) {
      return Result.err(e.message ?? 'Error de plataforma.');
    } catch (e, stacktrace) {
      log('CREDIBANCO BLUETOOTH FAILED ===> $e');
      log('CREDIBANCO BLUETOOTH FAILED ===> $stacktrace');
      return const Result.err('Algo falló!');
    }
  }
}
