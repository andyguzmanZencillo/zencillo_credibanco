import 'package:zencillo_helpers/zencillo_helpers.dart';

class CredibancoResponse {
  CredibancoResponse({
    required this.response,
    required this.cardNumber,
    required this.approvedNumber,
    required this.transactionNumber,
    required this.terminalId,
    required this.franchise,
    required this.autorizationCode,
    required this.amount,
    required this.iva,
    required this.receipt,
    required this.rrn,
    required this.timeDate,
    required this.responseCode,
    required this.accountType,
    required this.quotas,
    required this.merchantPosId,
    required this.data,
  });

  CredibancoResponse.empty()
      : this(
          response: '',
          cardNumber: '',
          approvedNumber: '',
          transactionNumber: '',
          terminalId: '',
          franchise: '',
          autorizationCode: '',
          amount: '',
          iva: 0,
          receipt: '',
          rrn: '',
          timeDate: '',
          responseCode: '',
          accountType: '',
          quotas: '',
          merchantPosId: '',
          data: '',
        );

  CredibancoResponse.fromJson(Map<String, dynamic> json)
      : response = json.getPro('response', ''),
        cardNumber = json.getPro('cardNumber', ''),
        approvedNumber = json.getPro('approvedNumber', ''),
        transactionNumber = json.getPro('transactionNumber', ''),
        terminalId = json.getPro('terminalId', ''),
        franchise = json.getPro('franchise', ''),
        autorizationCode = json.getPro('autorizationCode', ''),
        amount = json.getPro('amount', ''),
        iva = json.getPro('iva', 0),
        receipt = json.getPro('receipt', ''),
        rrn = json.getPro('rrn', ''),
        timeDate = json.getPro('timeDate', ''),
        responseCode = json.getPro('responseCode', ''),
        accountType = json.getPro('accountType', ''),
        quotas = json.getPro('quotas', ''),
        merchantPosId = json.getPro('merchantPosId', ''),
        data = json.toString();

  final String response;
  final String cardNumber;
  final String approvedNumber;
  final String transactionNumber;
  final String terminalId;
  final String franchise;
  final String autorizationCode;
  final String amount;
  final int iva;
  final String receipt;
  final String rrn;
  final String timeDate;
  final String responseCode;
  final String accountType;
  final String quotas;
  final String merchantPosId;

  final String data;

  @override
  String toString() {
    return 'CredibancoResponse{response: $response, cardNumber: $cardNumber, approvedNumber: $approvedNumber, transactionNumber: $transactionNumber, terminalId: $terminalId, franchise: $franchise, autorizationCode: $autorizationCode, amount: $amount, iva: $iva, receipt: $receipt, rrn: $rrn, timeDate: $timeDate, responseCode: $responseCode, accountType: $accountType, quotas: $quotas, merchantPosId: $merchantPosId, data: $data}';
  }
}
