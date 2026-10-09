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
                    s.roll_number,
                    s.full_name,
                    fs.fee_structure_id,
                    fs.fee_name,
                    sf.total_amount,
                    COALESCE(SUM(p.amount), 0) AS paid_amount,
                    sf.total_amount - COALESCE(SUM(p.amount), 0)
                        AS pending_amount,
                    sf.due_date
                FROM student_fee sf
                JOIN student s
                    ON sf.student_id = s.student_id
                JOIN fee_structure fs
                    ON sf.fee_structure_id = fs.fee_structure_id
                LEFT JOIN payment p
                    ON p.student_fee_id = sf.student_fee_id
                GROUP BY
                    sf.student_fee_id,
                    s.student_id,
                    s.roll_number,
                    s.full_name,
                    fs.fee_structure_id,
                    fs.fee_name,
                    sf.total_amount,
                    sf.due_date
                ORDER BY s.roll_number
                """;

        try (Connection connection = DBConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                PendingFee fee = new PendingFee();

                fee.setStudentId(resultSet.getInt("student_id"));
                fee.setRollNumber(resultSet.getString("roll_number"));
                fee.setStudentName(resultSet.getString("full_name"));

                fee.setFeeStructureId(
                        resultSet.getInt("fee_structure_id"));

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