import java.sql.*;
import java.util.Scanner;
class Student {
    int studentID;
    String name;
    String department;
    double marks;
}
class StudentController {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";
    static void addStudent(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Marks: ");
        double marks = scanner.nextDouble();
        scanner.nextLine();
        String sql = "INSERT INTO Student (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            pstmt.setInt(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, department);
            pstmt.setDouble(4, marks);
            pstmt.executeUpdate();
            con.commit();
            System.out.println("Student added successfully.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Transaction failed. Rolled back.");
        }
    }
    static void viewStudents(Connection con) throws SQLException {
        String sql = "SELECT * FROM Student";
        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            System.out.println("StudentID | Name | Department | Marks");
            System.out.println("--------------------------------------");
            while (rs.next()) {
                System.out.println(rs.getInt("StudentID") + " | " + rs.getString("Name") + " | " + rs.getString("Department") + " | " + rs.getDouble("Marks"));
            }
        }
    }
    static void updateStudent(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter new Marks: ");
        double marks = scanner.nextDouble();
        scanner.nextLine();
        String sql = "UPDATE Student SET Marks = ? WHERE StudentID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            pstmt.setDouble(1, marks);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            con.commit();
            System.out.println("Student updated successfully.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Transaction failed. Rolled back.");
        }
    }
    static void deleteStudent(Connection con, Scanner scanner) throws SQLException {
        System.out.print("Enter Student ID to delete: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        String sql = "DELETE FROM Student WHERE StudentID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            con.setAutoCommit(false);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.commit();
            System.out.println("Student deleted successfully.");
        } catch (SQLException e) {
            con.rollback();
            System.out.println("Transaction failed. Rolled back.");
        }
    }
}
public class StudentManagementApp {
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(StudentController.URL, StudentController.USER, StudentController.PASSWORD);
             Scanner scanner = new Scanner(System.in)) {

            while (true) {
                System.out.println("\n1. Add Student\n2. View Students\n3. Update Student\n4. Delete Student\n5. Exit");
                System.out.print("Enter choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> StudentController.addStudent(con, scanner);
                    case 2 -> StudentController.viewStudents(con);
                    case 3 -> StudentController.updateStudent(con, scanner);
                    case 4 -> StudentController.deleteStudent(con, scanner);
                    case 5 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid choice, try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
