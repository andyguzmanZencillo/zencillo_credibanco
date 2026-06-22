import 'package:oxidized/oxidized.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'models/credibanco_response.dart';
import 'zencillo_credibanco_method_channel.dart';

abstract class ZencilloCredibancoPlatform extends PlatformInterface {
  ZencilloCredibancoPlatform() : super(token: _token);

  static final Object _token = Object();

  static ZencilloCredibancoPlatform _instance =
      MethodChannelZencilloCredibanco();

  static ZencilloCredibancoPlatform get instance => _instance;

  static set instance(ZencilloCredibancoPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('getPlatformVersion() has not been implemented.');
  }

  Future<Result<CredibancoResponse, String>> credibanco({
    required String amount,
    required String tax,
  }) {
    throw UnimplementedError('credibanco() has not been implemented.');
  }

  Future<Result<Unit, String>> credibancoPrint(String text) {
    throw UnimplementedError('credibancoPrint() has not been implemented.');
  }

  Future<Result<String, String>> credibancoScan() {
    throw UnimplementedError('credibancoScan() has not been implemented.');
  }

  Future<Result<String, String>> credibancoNFC() {
    throw UnimplementedError('credibancoNFC() has not been implemented.');
  }

  Future<Result<CredibancoResponse, String>> credibancoQr({
    required String amount,
    required String tax,
  }) {
    throw UnimplementedError('credibancoQr() has not been implemented.');
  }

  Future<Result<CredibancoResponse, String>> credibancoAnulacion({
    required String receiptId,
  }) {
    throw UnimplementedError('credibancoAnulacion() has not been implemented.');
  }

  Future<Result<CredibancoResponse, String>> credibancoBreb({
    required String amount,
    required String tax,
  }) {
    throw UnimplementedError('credibancoBreb() has not been implemented.');
  }

  Future<Result<Map<String, dynamic>, String>> credibancoOtherFunctions() {
    throw UnimplementedError(
      'credibancoOtherFunctions() has not been implemented.',
    );
  }

  Future<Result<String, String>> credibancoBluetooth() {
    throw UnimplementedError(
      'credibancoBluetooth() has not been implemented.',
    );
  }
}
