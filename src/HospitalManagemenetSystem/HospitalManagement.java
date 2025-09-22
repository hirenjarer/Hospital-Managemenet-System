package HospitalManagemenetSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagement {

    private static final String url = "jdbc:mysql://localhost:3306/hospital";
    private static final String username = "root";
    private static final String password = "hREN$7636#";

    public static void main(String[] args) {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Scanner sc = new Scanner(System.in);

        try {
            Connection cn = DriverManager.getConnection(url, username, password);

            Patient patient = new Patient(cn, sc);
            Doctor doctor = new Doctor(cn);

            while (true) {
                System.out.println("\n--:: HOSPITAL MANAGEMENT SYSTEM ::--\n");

                System.out.println("1. Add Patient");
                System.out.println("2. View Patient");
                System.out.println("3. View Doctors");
                System.out.println("4. Book Appointment");
                System.out.println("5. Exit");

                System.out.println();
                System.out.print("Enter your choice : ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        patient.viewPatient();
                        System.out.println();
                        break;
                    case 3:
                        doctor.viewDoctors();
                        System.out.println();
                        break;
                    case 4:
                        bookAppointment(patient, doctor, cn, sc);
                        System.out.println();
                        break;
                    case 5:
                        return; // Exit

                    default:
                        System.out.println("Enter valid choice!!");
                        break;
                }

            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient, Doctor doctor, Connection cn, Scanner sc) {

        System.out.print("Enter Patient ID : ");
        int patientId = sc.nextInt();

        System.out.print("Enter Doctor ID : ");
        int doctorId = sc.nextInt();

        sc.nextLine();

        System.out.print("Enter Appointment date (YYYY-MM-DD) : ");
        String appointmentDate = sc.nextLine();


        if (patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)) {
            if (checkDoctorAvailability(doctorId, appointmentDate, cn)) {
                String appointmentQuery = "INSERT INTO appointments(patient_id, doctor_id, appointment_date) VALUES(?,?,?)";

                try {
                    PreparedStatement preSt = cn.prepareStatement(appointmentQuery);

                    preSt.setInt(1, patientId);
                    preSt.setInt(2, doctorId);
                    preSt.setString(3, appointmentDate);

                    int rowsAffected = preSt.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Appointment Booked.");
                    } else {
                        System.out.println("Failed to book Appointment!!");
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Doctor not available on this date.");
            }
        } else {
            System.out.println("Either Doctor or Patient doesn't exists.");
        }


    }

    public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection cn) {

        String query = "SELECT COUNT(*) FROM appointments WHERE doctor_id = ? AND appointment_date = ?";

        try {
            PreparedStatement preSt = cn.prepareStatement(query);

            preSt.setInt(1, doctorId);
            preSt.setString(2, appointmentDate);

            ResultSet rslt = preSt.executeQuery();

            if (rslt.next()) {
                int count = rslt.getInt(1);

                if (count == 0) {
                    return true;
                } else {
                    return false;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
