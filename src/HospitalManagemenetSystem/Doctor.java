package HospitalManagemenetSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Doctor {

    private Connection cn;

    public Doctor(Connection cn) {
        this.cn = cn;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors";

        try (PreparedStatement preSt = cn.prepareStatement(query);
             ResultSet rsltSet = preSt.executeQuery()) {

            System.out.println("Doctors : ");

            // Header
            System.out.println("+------------+--------------------+-----------------+");
            System.out.println("| Doctor ID  | Name               | Specialization  |");
            System.out.println("+------------+--------------------+-----------------+");

            // Rows
            while (rsltSet.next()) {
                int id = rsltSet.getInt("id");
                String name = rsltSet.getString("name");
                String specialization = rsltSet.getString("specialization");

                System.out.printf("| %-10d | %-18s | %-15s |\n", id, name, specialization);
            }

            // Footer (after all rows)
            System.out.println("+------------+--------------------+-----------------+");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorById(int doctorId) {
        String query = "SELECT * FROM doctors WHERE ID = ?";

        try {
            PreparedStatement preSt = cn.prepareStatement(query);

            preSt.setInt(1, doctorId);

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
