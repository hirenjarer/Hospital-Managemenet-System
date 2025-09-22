package HospitalManagemenetSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection cn;
    private Scanner sc;

    public Patient(Connection cn, Scanner sc) {
        this.cn = cn;
        this.sc = sc;
    }

    public void addPatient() {

        sc.nextLine();

        // Taking inputs from Patients
        System.out.print("Enter Patient name : ");
        String patientName = sc.nextLine();

        System.out.print("Enter Patient Age : ");
        int patientAge = sc.nextInt();
        sc.nextLine(); // consume the leftover newline

        System.out.print("Enter Patient Gender : ");
        String patientGender = sc.nextLine();


        try {

            String query = "INSERT INTO patients (name, age, gender) VALUES (? , ?, ?)";

            PreparedStatement preSt = cn.prepareStatement(query);

            preSt.setString(1, patientName);
            preSt.setInt(2, patientAge);
            preSt.setString(3, patientGender);

            int affectedRows = preSt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Patient registered Successfully!");
            } else {
                System.out.println("Failed to add Patient");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void viewPatient() {
        String query = "SELECT * FROM patients";

        try {
            PreparedStatement preSt = cn.prepareStatement(query);
            ResultSet rsltSet = preSt.executeQuery();

            // Format of taking input
            System.out.println("Patients : ");

            System.out.println("+------------+--------------------+--------+----------+");
            System.out.println("| Patient ID | Patient Name       | Age    | Gender   | ");
            System.out.println("+------------+--------------------+--------+----------+");

            while (rsltSet.next()) {
                int id = rsltSet.getInt("id");
                String name = rsltSet.getString("name");
                int age = rsltSet.getInt("age");
                String gender = rsltSet.getString("gender");

                // printf helps to maintain proper format
                System.out.printf("| %-10s | %-18s | %-6s | %-8s |\n", id, name, age, gender);
                System.out.println("+------------+--------------------+--------+----------+");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getPatientById(int patientId) {
        String query = "SELECT * FROM patients WHERE ID = ?";

        try {
            PreparedStatement preSt = cn.prepareStatement(query);

            preSt.setInt(1, patientId);

            ResultSet rsltSt = preSt.executeQuery();

            if (rsltSt.next()) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
