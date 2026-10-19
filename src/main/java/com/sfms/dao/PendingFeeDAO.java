package com.sfms.dao;

import com.sfms.database.DBConnection;
import com.sfms.model.PendingFee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PendingFeeDAO {

    public List<PendingFee> getAllPendingFees() throws SQLException {

        List<PendingFee> pendingFees = new ArrayList<>();

        String sql = """
                SELECT
                    s.student_id,
                    s.full_name,
                    f.fee_id,
                    f.fee_name,
                    f.amount AS total_amount,
                    COALESCE(SUM(p.amount_paid), 0) AS paid_amount,
                    GREATEST(f.amount - COALESCE(SUM(p.amount_paid), 0), 0)
                        AS pending_amount,
                    f.due_date
                FROM fees f
                JOIN students s
                    ON f.student_id = s.student_id
                LEFT JOIN payments p
                    ON p.fee_id = f.fee_id
                GROUP BY
                    s.student_id,
                    s.full_name,
                    f.fee_id,
                    f.fee_name,
                    f.amount,
                    f.due_date
                ORDER BY s.student_id, f.due_date, f.fee_id
                """;

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                PendingFee fee = new PendingFee();

                fee.setStudentId(resultSet.getInt("student_id"));
                fee.setStudentName(resultSet.getString("full_name"));

                fee.setFeeStructureId(
                        resultSet.getInt("fee_id"));

                fee.setFeeName(resultSet.getString("fee_name"));

                BigDecimal totalAmount = resultSet.getBigDecimal("total_amount");

                BigDecimal paidAmount = resultSet.getBigDecimal("paid_amount");

                BigDecimal pendingAmount = resultSet.getBigDecimal("pending_amount");

                fee.setTotalAmount(totalAmount);
                fee.setPaidAmount(paidAmount);
                fee.setPendingAmount(pendingAmount);

                java.sql.Date dueDate = resultSet.getDate("due_date");

                if (dueDate != null) {
                    fee.setDueDate(dueDate.toLocalDate());
                }

                if (pendingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                    fee.setStatus("Paid");
                } else if (paidAmount.compareTo(BigDecimal.ZERO) > 0) {
                    fee.setStatus("Partially Paid");
                } else {
                    fee.setStatus("Pending");
                }

                pendingFees.add(fee);
            }
        }

        return pendingFees;
    }

    public List<PendingFee> getPendingFeesByStudentId(int studentId)
            throws SQLException {

        List<PendingFee> allFees = getAllPendingFees();
        List<PendingFee> studentFees = new ArrayList<>();

        for (PendingFee fee : allFees) {
            if (fee.getStudentId() == studentId) {
                studentFees.add(fee);
            }
        }

        return studentFees;
    }
}
