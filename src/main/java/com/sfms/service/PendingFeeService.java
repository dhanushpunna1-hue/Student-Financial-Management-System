package com.sfms.service;

import com.sfms.dao.PendingFeeDAO;
import com.sfms.model.PendingFee;

import java.sql.SQLException;
import java.util.List;

public class PendingFeeService {

    private final PendingFeeDAO pendingFeeDAO;

    public PendingFeeService() {
        this.pendingFeeDAO = new PendingFeeDAO();
    }

    public List<PendingFee> getAllPendingFees() throws SQLException {
        return pendingFeeDAO.getAllPendingFees();
    }

    public List<PendingFee> getPendingFeesByStudentId(int studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        return pendingFeeDAO.getPendingFeesByStudentId(studentId);
    }
}