package com.sfms;

import com.sfms.ui.FeeStructureMenu;
import com.sfms.ui.PaymentMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        FeeStructureMenu feeStructureMenu = new FeeStructureMenu();
        PaymentMenu paymentMenu = new PaymentMenu();

        while (true) {

            System.out.println("\n======================================");
            System.out.println(" STUDENT FINANCIAL MANAGEMENT SYSTEM");
            System.out.println("======================================");
            System.out.println("1. Fee Structure");
            System.out.println("2. Fee Payment");
            System.out.println("3. Exit");
            System.out.println("======================================");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {

                case 1:
                    feeStructureMenu.start();
                    break;

                case 2:
                    paymentMenu.start();
                    break;

                case 3:
                    System.out.println("Exiting Student Financial Management System...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}