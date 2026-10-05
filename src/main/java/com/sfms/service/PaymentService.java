package com.sfms.service;

import com.sfms.dao.PaymentDAO;
import com.sfms.model.Payment;

import java.time.LocalDate;
import java.util.List;

public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService() {
        paymentDAO = new PaymentDAO();
    }

    // Add payment
    public void addPayment(int feeStructureId,
            double amount,
            LocalDate paymentDate,
            String paymentMethod,
            String transactionReference) {

        if (feeStructureId <= 0) {
            System.out.println("Invalid fee structure ID.");
            return;
        }

        if (amount <= 0) {
            System.out.println("Payment amount must be greater than 0.");
            return;
        }

        if (paymentDate == null) {
            System.out.println("Payment date cannot be empty.");
            return;
        }

        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            System.out.println("Payment method cannot be empty.");
            return;
        }

        Payment payment = new Payment(
                feeStructureId,
                amount,
                paymentDate,
                paymentMethod,
                transactionReference);

        paymentDAO.addPayment(payment);
    }

    // View all payments
    public void viewAllPayments() {

        List<Payment> payments = paymentDAO.getAllPayments();

        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.println("\n========== PAYMENT HISTORY ==========");

        for (Payment payment : payments) {

            System.out.println("Payment ID          : "
                    + payment.getPaymentId());

            System.out.println("Fee Structure ID    : "
                    + payment.getFeeStructureId());

            System.out.println("Amount              : ₹"
                    + payment.getAmount());

            System.out.println("Payment Date        : "
                    + payment.getPaymentDate());

            System.out.println("Payment Method      : "
                    + payment.getPaymentMethod());

            System.out.println("Transaction Ref.    : "
                    + payment.getTransactionReference());

            System.out.println("-------------------------------------");
        }
    }

    // Delete payment
    public void deletePayment(int paymentId) {

        if (paymentId <= 0) {
            System.out.println("Invalid payment ID.");
            return;
        }

        paymentDAO.deletePayment(paymentId);
    }
}