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
            LocalDate paymentDate) {

        if (feeStructureId <= 0) {
            System.out.println("Invalid fee structure ID.");
            return;
        }

        if (amount <= 0 || !Double.isFinite(amount)) {
            System.out.println("Payment amount must be greater than 0.");
            return;
        }

        if (paymentDate == null) {
            System.out.println("Payment date cannot be empty.");
            return;
        }

        Payment payment = new Payment(
                feeStructureId,
                amount,
                paymentDate,
                null,
                null);

        paymentDAO.addPayment(payment);
    }

    // View all payments
    public void viewAllPayments() {
        List<Payment> payments = getAllPayments();

        if (payments.isEmpty()) {
            System.out.println("No payments found.");
            return;
        }

        System.out.println("\n========== PAYMENT HISTORY ==========");

        for (Payment payment : payments) {

            System.out.println("Payment ID          : "
                    + payment.getPaymentId());

            System.out.println("Fee ID              : "
                    + payment.getFeeStructureId());

            System.out.println("Amount              : ₹"
                    + payment.getAmount());

            System.out.println("Payment Date        : "
                    + payment.getPaymentDate());

            System.out.println("-------------------------------------");
        }
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.getAllPayments();
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
