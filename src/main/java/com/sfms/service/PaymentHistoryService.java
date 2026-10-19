package com.sfms.service;

import com.sfms.dao.PaymentHistoryDAO;
import java.math.BigDecimal;

public class PaymentHistoryService {

    private final PaymentHistoryDAO paymentHistoryDAO;

    public PaymentHistoryService() {
        paymentHistoryDAO = new PaymentHistoryDAO();
    }

    public void viewPaymentHistory() {
        paymentHistoryDAO.viewPaymentHistory();
    }

    public void viewTotalPayments() {
        paymentHistoryDAO.viewTotalPayments();
    }

    public BigDecimal getTotalPayments() {
        return paymentHistoryDAO.getTotalPayments();
    }
}
