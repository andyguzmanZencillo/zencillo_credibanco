import 'package:zencillo_helpers/zencillo_helpers.dart';

class CredibancoResponse {
  CredibancoResponse({
    required this.success,
    required this.resultCode,
    required this.response,
    required this.cardNumber,
    required this.lastFourDigitsCard,
    required this.approvedNumber,
    required this.transactionNumber,
    required this.terminalId,
    required this.franchise,
    required this.autorizationCode,
    required this.authorizationCode,
    required this.amount,
    required this.monto,
    required this.iva,
    required this.receipt,
    required this.rrn,
    required this.timeDate,
    required this.responseCode,
    required this.accountType,
    required this.quotas,
    required this.merchantPosId,
    required this.apiResult,
    required this.extraInformation,
    required this.rawExtras,
    required this.data,
  });

  CredibancoResponse.empty()
      : this(
          success: false,
          resultCode: 0,
          response: '',
          cardNumber: '',
          lastFourDigitsCard: '',
          approvedNumber: '',
          transactionNumber: '',
          terminalId: '',
          franchise: '',
          autorizationCode: '',
          authorizationCode: '',
          amount: '',
          monto: '',
          iva: 0,
          receipt: '',
          rrn: '',
          timeDate: '',
          responseCode: '',
          accountType: '',
          quotas: '',
          merchantPosId: '',
          apiResult: '',
          extraInformation: '',
          rawExtras: const {},
          data: '',
        );

  CredibancoResponse.fromJson(Map<String, dynamic> json)
      : success = _toBool(json.getPro('success', false)),
        resultCode = _toInt(json.getPro('resultCode', 0)),
        response = json.getPro('response', ''),
        cardNumber = json.getPro('cardNumber', ''),
        lastFourDigitsCard = json.getPro(
          'lastFourDigitsCard',
          json.getPro('cardNumber', ''),
        ),
        approvedNumber = json.getPro('approvedNumber', ''),
        transactionNumber = json.getPro('transactionNumber', ''),
        terminalId = json.getPro('terminalId', ''),
        franchise = json.getPro('franchise', ''),
        autorizationCode = json.getPro('autorizationCode', ''),
        authorizationCode = json.getPro(
          'authorizationCode',
          json.getPro('autorizationCode', ''),
        ),
        amount = json.getPro('amount', ''),
        monto = json.getPro(
          'monto',
          json.getPro('amount', ''),
        ),
        iva = _toInt(json.getPro('iva', 0)),
        receipt = json.getPro('receipt', ''),
        rrn = json.getPro('rrn', ''),
        timeDate = json.getPro('timeDate', ''),
        responseCode = json.getPro('responseCode', ''),
        accountType = json.getPro('accountType', ''),
        quotas = json.getPro('quotas', ''),
        merchantPosId = json.getPro('merchantPosId', ''),
        apiResult = json.getPro('apiResult', ''),
        extraInformation = json.getPro('extraInformation', ''),
        rawExtras = _toMap(json.getPro('rawExtras', <String, dynamic>{})),
        data = json.toString();

  final bool success;
  final int resultCode;

  final String response;
  final String cardNumber;
  final String lastFourDigitsCard;
  final String approvedNumber;
  final String transactionNumber;
  final String terminalId;
  final String franchise;

  /// Nombre original que ya usaba tu app.
  final String autorizationCode;

  /// Nombre adicional escrito correctamente.
  final String authorizationCode;

  final String amount;
  final String monto;
  final int iva;
  final String receipt;
  final String rrn;
  final String timeDate;
  final String responseCode;
  final String accountType;
  final String quotas;
  final String merchantPosId;
  final String apiResult;
  final String extraInformation;
  final Map<String, dynamic> rawExtras;

  final String data;

  static int _toInt(dynamic value) {
    if (value == null) return 0;
    if (value is int) return value;
    if (value is double) return value.toInt();
    if (value is num) return value.toInt();

    return int.tryParse(value.toString()) ?? 0;
  }

  static bool _toBool(dynamic value) {
    if (value == null) return false;
    if (value is bool) return value;
    if (value is int) return value == 1;

    final text = value.toString().toLowerCase().trim();

    return text == 'true' || text == '1' || text == 'yes' || text == 'si';
  }

  static Map<String, dynamic> _toMap(dynamic value) {
    if (value is Map<String, dynamic>) return value;

    if (value is Map) {
      return Map<String, dynamic>.from(value);
    }

    return <String, dynamic>{};
  }

  @override
  String toString() {
    return 'CredibancoResponse{'
        'success: $success, '
        'resultCode: $resultCode, '
        'response: $response, '
        'cardNumber: $cardNumber, '
        'lastFourDigitsCard: $lastFourDigitsCard, '
        'approvedNumber: $approvedNumber, '
        'transactionNumber: $transactionNumber, '
        'terminalId: $terminalId, '
        'franchise: $franchise, '
        'autorizationCode: $autorizationCode, '
        'authorizationCode: $authorizationCode, '
        'amount: $amount, '
        'monto: $monto, '
        'iva: $iva, '
        'receipt: $receipt, '
        'rrn: $rrn, '
        'timeDate: $timeDate, '
        'responseCode: $responseCode, '
        'accountType: $accountType, '
        'quotas: $quotas, '
        'merchantPosId: $merchantPosId, '
        'apiResult: $apiResult, '
        'extraInformation: $extraInformation, '
        'rawExtras: $rawExtras, '
        'data: $data'
        '}';
  }
}
