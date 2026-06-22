import 'package:oxidized/oxidized.dart';

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

  Future<Result<Unit, String>> credibancoPrint(String text) {
    return ZencilloCredibancoPlatform.instance.credibancoPrint(text);
  }

  Future<Result<String, String>> credibancoScan() {
    return ZencilloCredibancoPlatform.instance.credibancoScan();
  }

  Future<Result<String, String>> credibancoNFC() {
    return ZencilloCredibancoPlatform.instance.credibancoNFC();
  }
}
