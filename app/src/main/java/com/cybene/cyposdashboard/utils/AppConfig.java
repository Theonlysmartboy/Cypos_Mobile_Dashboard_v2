package com.cybene.cyposdashboard.utils;

public class AppConfig {

    public static final String BASE_URL = AppController.getUrlPath() ;

    public static final String URL_REGISTER = BASE_URL+"/cyposbackend/controller/auth/RegisterController.php";
    public static final String URL_LOGIN = BASE_URL+"/cyposbackend/controller/auth/LoginController.php";
    public static final String URL_PASSWORD_RESET= BASE_URL+"/cyposbackend/controller/auth/PasswordResetController.php";
    public static final String URL_RECOVER_PASSWORD= BASE_URL+"/cyposbackend/controller/auth/LoginController.php";

    public static final String URL_INVOICE_LIST = BASE_URL+"/cyposbackend/controller/sales/InvoiceListController.php";
    public static final String URL_TILL_CASH_PICKUP = BASE_URL+"/cyposbackend/controller/sales/TillCashPickUpController.php";
    public static final String URL_CASH_SALE_MODE = BASE_URL+"/cyposbackend/controller/sales/CashSalePaymentModeController.php";
    public static final String URL_COMPUTER_WISE_SALE = BASE_URL+"/cyposbackend/controller/sales/ComputerWiseSalesController.php";
    public static final String URL_VAT_CODE_SALE = BASE_URL+"/cyposbackend/controller/sales/VatCodeWiseSalesController.php";
    public static final String URL_CREDIT_NOTE_LIST = BASE_URL+"/cyposbackend/controller/sales/CreditNoteListController.php";
    public static final String URL_TRANSACTION_WISE_SALE = BASE_URL+"/cyposbackend/controller/sales/TransactionWiseSalesDataGridController.php";
    public static final String URL_SALESMAN_WISE_SALE = BASE_URL+"/cyposbackend/controller/sales/SalesManWiseSalesDataGridController.php";
    public static final String URL_CUSTOMER_WISE_SALE = BASE_URL+"/cyposbackend/controller/sales/CustomerWiseSalesDataGridController.php";

    public static final String URL_DAILY_PURCHASE = BASE_URL+"/cyposbackend/controller/purchases/DailyPurchaseDataGridController.php";
    public static final String URL_MONTHLY_PURCHASE = BASE_URL+"/cyposbackend/controller/purchases/MonthlyPurchaseDataGridController.php";
    public static final String URL_GRN_LIST = BASE_URL+"/cyposbackend/controller/purchases/GrnListController.php";
    public static final String URL_GRT_LIST = BASE_URL+"/cyposbackend/controller/purchases/ReturnedGoodsListController.php";
    public static final String URL_LPO_LIST = BASE_URL+"/cyposbackend/controller/purchases/LpoListController.php";
    public static final String URL_SWP_LIST = BASE_URL+"/cyposbackend/controller/purchases/SupplierWisePurchaseListController.php";
    public static final String URL_DWP_LIST = BASE_URL+"/cyposbackend/controller/purchases/DepartmentWisePurchaseListController.php";

    public static final String URL_DNOTE_LIST = BASE_URL+"/cyposbackend/controller/inventories/DeliveryNoteListController.php";
    public static final String URL_DEPT_WISE_STOCK = BASE_URL+"/cyposbackend/controller/inventories/DepartmentWiseStockValuationController.php";
    public static final String URL_TOTAL_STOCK = BASE_URL+"/cyposbackend/controller/inventories/TotalStockValuationController.php";

    public static final String URL_CUSTOMER_OUTSTANDING = BASE_URL+"/cyposbackend/controller/accounts/CustomerOutstandingController.php";
    public static final String URL_SUPPLIER_OUTSTANDING = BASE_URL+"/cyposbackend/controller/accounts/SupplierOutstandingController.php";
    public static final String URL_RECEIPT_LIST = BASE_URL+"/cyposbackend/controller/accounts/ReceiptListController.php";
    public static final String URL_RECEIPT_REVERSAL_LIST = BASE_URL+"/cyposbackend/controller/accounts/ReceiptReversalListController.php";
    public static final String URL_PAYMENT_LIST = BASE_URL+"/cyposbackend/controller/accounts/PaymentListController.php";
    public static final String URL_PAYMENT_REVERSAL_LIST = BASE_URL+"/cyposbackend/controller/accounts/PaymentReversalListController.php";
    public static final String URL_POST_DATED_CHEQUE_CUSTOMER_LIST = BASE_URL+"/cyposbackend/controller/accounts/PostDatedChequeCustomerListController.php";
    public static final String URL_POST_DATED_CHEQUE_SUPPLIER_LIST = BASE_URL+"/cyposbackend/controller/accounts/PostDatedChequeSupplierListController.php";
    public static final String URL_POST_DATED_CREDIT_NOTE_PURCHASE_LIST = BASE_URL+"/cyposbackend/controller/accounts/PostCreditNotePurchaseListController.php";
    public static final String URL_POST_DATED_CREDIT_NOTE_SALES_LIST = BASE_URL+"/cyposbackend/controller/accounts/PostCreditNoteSalesListController.php";

    public static final String URL_PAYMENT_LIST_2 = BASE_URL+"/cyposbackend/controller/branches/BranchPaymentListController.php";
    public static final String URL_PAYMENT_REVERSAL_LIST_2 = BASE_URL+"/cyposbackend/controller/branches/BranchPaymentReversalListController.php";
    public static final String URL_RECEIPT_LIST_2 = BASE_URL+"/cyposbackend/controller/branches/BranchReceiptListController.php";
    public static final String URL_RECEIPT_REVERSAL_LIST_2 = BASE_URL+"/cyposbackend/controller/branches/BranchReceiptReversalListController.php";
    public static final String URL_RECEIVED_LIST = BASE_URL+"/cyposbackend/controller/branches/BranchReceivedListController.php";
    public static final String URL_TRANSFER_LIST = BASE_URL+"/cyposbackend/controller/branches/BranchTransferListController.php";

    public static final String URL_CLIENTS = BASE_URL+"/cyposbackend/controller/customers/ClientController.php";
    public static final String URL_CUSTOMER_INVOICE_LIST = BASE_URL+"/cyposbackend/controller/customers/InvoiceListController.php";
    public static final String URL_CUSTOMER_CREDIT_NOTE_LIST = BASE_URL+"/cyposbackend/controller/customers/CreditNoteListController.php";
    public static final String URL_CUSTOMER_POST_DATED_CHEQUE_LIST = BASE_URL+"/cyposbackend/controller/customers/PostDatedChequeListController.php";
    public static final String URL_CUSTOMER_RECEIPT_LIST = BASE_URL+"/cyposbackend/controller/customers/ReceiptListController.php";
    public static final String URL_CUSTOMER_MONTHLY_SALES = BASE_URL+"/cyposbackend/controller/customers/MonthlyCustomerSalesController.php";
    public static final String URL_CUSTOMER_DAILY_SALES = BASE_URL+"/cyposbackend/controller/customers/DailyCustomerSalesController.php";
    public static final String URL_CUSTOMER_MONTHLY_PURCHASE = BASE_URL+"/cyposbackend/controller/customers/MonthlyCustomerPurchaseChartController.php";
    public static final String URL_CUSTOMER_LAST_INVOICE_DETAILS =  BASE_URL+"/cyposbackend/controller/customers/LastInvoiceDetailsController.php";
    public static final String URL_CUSTOMER_TOTAL_SALES = BASE_URL+"/cyposbackend/controller/customers/TotalSalesCustomerWiseController.php";
    public static final String URL_CUSTOMER_TOTAL_INVOICE = BASE_URL+"/cyposbackend/controller/customers/TotalMonthlyInvoiceController.php";
    public static final String URL_CUSTOMER_WISE_MONTHLY_SALES = BASE_URL+"/cyposbackend/controller/customers/MonthlySalesCustomerWiseController.php";
    public static final String URL_CUSTOMER_LAST_RECEIPT_DETAILS = BASE_URL+"/cyposbackend/controller/customers/LastReceiptDetailsController.php";
    public static final String URL_CUSTOMER_CREDIT_DETAILS = BASE_URL+"/cyposbackend/controller/customers/CustomerCreditDetailsController.php";

    public static final String URL_SUPPLIERS = BASE_URL+"/cyposbackend/controller/suppliers/SupplierController.php";
    public static final String URL_SUPPLIER_GRN_LIST = BASE_URL+"/cyposbackend/controller/suppliers/GRNListController.php";
    public static final String URL_SUPPLIER_GOODS_RETURN_NOTE_LIST = BASE_URL+"/cyposbackend/controller/suppliers/GoodsReturnNoteListController.php";
    public static final String URL_SUPPLIER_GOODS_POST_DATED_CHEQUE_LIST = BASE_URL+"/cyposbackend/controller/suppliers/PostDatedChequeListController.php";
    public static final String URL_SUPPLIER_PAYMENT_LIST = BASE_URL+"/cyposbackend/controller/suppliers/PaymentListController.php";
    public static final String URL_SUPPLIER_MONTHLY_PURCHASE = BASE_URL+"/cyposbackend/controller/suppliers/MonthlySupplierPurchaseChartController.php";
    public static final String URL_SUPPLIER_MONTHLY_PURCHASE_GRID = BASE_URL+"/cyposbackend/controller/suppliers/MonthlySupplierPurchaseController.php";
    public static final String URL_SUPPLIER_DAILY_PURCHASE = BASE_URL+"/cyposbackend/controller/suppliers/DailySupplierPurchaseController.php";
    public static final String URL_SUPPLIER_TOTAL_PURCHASE = BASE_URL+"/cyposbackend/controller/suppliers/TotalPurchaseSupplierWiseController.php";
    public static final String URL_SUPPLIER_TOTAL_GRN = BASE_URL+"/cyposbackend/controller/suppliers/TotalMonthlyGRNController.php";
    public static final String URL_SUPPLIER_LAST_GRN_DETAILS = BASE_URL+"/cyposbackend/controller/suppliers/LastGRNDetailsController.php";
    public static final String URL_SUPPLIER_WISE_MONTHLY_PURCHASE = BASE_URL+"/cyposbackend/controller/suppliers/MonthlyPurchaseSupplierWiseController.php";
    public static final String URL_SUPPLIER_LAST_PAYMENT_DETAILS = BASE_URL+"/cyposbackend/controller/suppliers/LastPaymentDetailsController.php";
    public static final String URL_SUPPLIER_CREDIT_DETAILS = BASE_URL+"/cyposbackend/controller/suppliers/SupplierCreditDetailsController.php";
}
