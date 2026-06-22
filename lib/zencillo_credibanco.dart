import 'package:oxidized/oxidized.dart';
import 'package:zencillo_credibanco/map/map.dart';
import 'package:zencillo_helpers/zencillo_helpers.dart';

import 'models/credibanco_response.dart';
import 'zencillo_credibanco_platform_interface.dart';

class ZencilloCredibanco {
  Future<String?> getPlatformVersion() {
    return ZencilloCredibancoPlatform.instance.getPlatformVersion();
  }

  Future<Result<CredibancoResponse, String>> credibancoPay({
    required String amount,
    required String tax,
  }) {
    return ZencilloCredibancoPlatform.instance.credibanco(
      amount: amount,
      tax: tax,
    );
  }

  Future<Result<FormaPagoDetalleModel, String>> credibancoPayFull({
    required int idTurno,
    required int numeroTurno,
    required int idDocument,
    required double total,
    required double taxTotal,
    required double subTotal,
    required int idFormaPago,
  }) async {
    final result = await ZencilloCredibancoPlatform.instance.credibanco(
      amount: total.toString(),
      tax: taxTotal.toString(),
    );
    if (result.isErr()) {
      return Err(result.unwrapErr());
    }
    final data = result.unwrap();
    return Ok(data.toFormaPagoDetalle(
      idTurno: idTurno,
      numeroTurno: numeroTurno,
      idDocument: idDocument,
      total: total,
      taxTotal: taxTotal,
      subTotal: subTotal,
      idFormaPago: idFormaPago,
    ));
  }

  Future<Result<Unit, String>> credibancoPrint(String text) {
    return ZencilloCredibancoPlatform.instance.credibancoPrint(text);
  }

  Future<Result<String, String>> credibancoScan() {
    return ZencilloCredibancoPlatform.instance.credibancoScan();
  }

  Future<Result<String, String>> credibancoNFC() {
    return ZencilloCredibancoPlatform.instance.credibancoNFC();
  }

  Future<Result<CredibancoResponse, String>> credibancoQr({
    required String amount,
    required String tax,
  }) {
    return ZencilloCredibancoPlatform.instance.credibancoQr(
      amount: amount,
      tax: tax,
    );
  }

  Future<Result<CredibancoResponse, String>> credibancoAnulacion({
    required String receiptId,
  }) {
    return ZencilloCredibancoPlatform.instance.credibancoAnulacion(
      receiptId: receiptId,
    );
  }

  Future<Result<CredibancoResponse, String>> credibancoBreb({
    required String amount,
    required String tax,
  }) {
    return ZencilloCredibancoPlatform.instance.credibancoBreb(
      amount: amount,
      tax: tax,
    );
  }

  Future<Result<Map<String, dynamic>, String>> credibancoOtherFunctions() {
    return ZencilloCredibancoPlatform.instance.credibancoOtherFunctions();
  }

  Future<Result<FormaPagoDetalleModel, String>> credibancoQrPayFull({
    required int idTurno,
    required int numeroTurno,
    required int idDocument,
    required double total,
    required double taxTotal,
    required double subTotal,
    required int idFormaPago,
  }) async {
    final result = await ZencilloCredibancoPlatform.instance.credibancoQr(
      amount: total.toString(),
      tax: taxTotal.toString(),
    );

    if (result.isErr()) {
      return Err(result.unwrapErr());
    }

    final data = result.unwrap();

    return Ok(
      data.toFormaPagoDetalle(
        idTurno: idTurno,
        numeroTurno: numeroTurno,
        idDocument: idDocument,
        total: total,
        taxTotal: taxTotal,
        subTotal: subTotal,
        idFormaPago: idFormaPago,
      ),
    );
  }

  Future<Result<FormaPagoDetalleModel, String>> credibancoBrebPayFull({
    required int idTurno,
    required int numeroTurno,
    required int idDocument,
    required double total,
    required double taxTotal,
    required double subTotal,
    required int idFormaPago,
  }) async {
    final result = await ZencilloCredibancoPlatform.instance.credibancoBreb(
      amount: total.toString(),
      tax: taxTotal.toString(),
    );

    if (result.isErr()) {
      return Err(result.unwrapErr());
    }

    final data = result.unwrap();

    return Ok(
      data.toFormaPagoDetalle(
        idTurno: idTurno,
        numeroTurno: numeroTurno,
        idDocument: idDocument,
        total: total,
        taxTotal: taxTotal,
        subTotal: subTotal,
        idFormaPago: idFormaPago,
      ),
    );
  }
}
