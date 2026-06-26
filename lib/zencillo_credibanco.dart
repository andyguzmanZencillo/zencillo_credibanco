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

  Future<Result<FormaPagoDetalleModel, String>> credibancoPayComplete({
    required double total,
    required double taxTotal,
    required double subTotal,
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
      idTurno: 0,
      numeroTurno: 0,
      idDocument: 0,
      total: total,
      taxTotal: taxTotal,
      subTotal: subTotal,
      idFormaPago: 0,
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

  Future<Result<FormaPagoDetalleModel, String>> credibancoQr({
    required double total,
    required double taxTotal,
    required double subTotal,
  }) async {
    final result = await ZencilloCredibancoPlatform.instance.credibancoQr(
      amount: total.toString(),
      tax: taxTotal.toString(),
    );
    if (result.isErr()) {
      return Err(result.unwrapErr());
    }
    final data = result.unwrap();
    return Ok(data.toFormaPagoDetalle(
      idTurno: 0,
      numeroTurno: 0,
      idDocument: 0,
      total: total,
      taxTotal: taxTotal,
      subTotal: subTotal,
      idFormaPago: 0,
    ));
  }

  Future<Result<CredibancoResponse, String>> credibancoAnulacion({
    required String receiptId,
  }) {
    return ZencilloCredibancoPlatform.instance.credibancoAnulacion(
      receiptId: receiptId,
    );
  }

  Future<Result<FormaPagoDetalleModel, String>> credibancoBreb({
    required double total,
    required double taxTotal,
    required double subTotal,
  }) async {
    final result = await ZencilloCredibancoPlatform.instance.credibancoBreb(
      amount: total.toString(),
      tax: taxTotal.toString(),
    );
    if (result.isErr()) {
      return Err(result.unwrapErr());
    }
    final data = result.unwrap();
    return Ok(data.toFormaPagoDetalle(
      idTurno: 0,
      numeroTurno: 0,
      idDocument: 0,
      total: total,
      taxTotal: taxTotal,
      subTotal: subTotal,
      idFormaPago: 0,
    ));
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

  Future<Result<String, String>> credibancoBluetooth() {
    return ZencilloCredibancoPlatform.instance.credibancoBluetooth();
  }
}
