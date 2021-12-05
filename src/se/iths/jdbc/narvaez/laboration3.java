package se.iths.jdbc.narvaez;

import java.sql.*;
import java.util.Scanner;

public class laboration3 {
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws SQLException {
        laboration3 laboration3 = new laboration3();
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/laboration3", "narvaez", "Password123");
        laboration3.createTableArtistIfNotExists(connection);
        laboration3.insertTestData(connection);

        laboration3.runMenu(connection);
    }

    private void createTableArtistIfNotExists(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        String createTable = "CREATE TABLE IF NOT EXISTS Artist " +
                "(id INTEGER AUTO_INCREMENT, " +
                " firstName VARCHAR(50), " +
                " lastName VARCHAR(50), " +
                " age INTEGER, " +
                " PRIMARY KEY ( id ));";
        statement.executeUpdate(createTable);
        System.out.println("Created Artist table");
    }

    private void createTestData(Connection connection, String artistInput) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(artistInput);
        System.out.println(artistInput);
    }

    private void insertTestData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Artist");
        if (!resultSet.next()) {
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Hans', 'Larsson', 35);");
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Johanna', 'Andersson', 35);");
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Robert', 'Svensson', 35);");
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Bert', 'Carlsson', 25);");
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Sandra', 'Eriksson', 25);");
            createTestData(connection, "INSERT INTO Artist(firstName, lastName, age) VALUES('Andreas', 'Berg', 25);");

        }
    }

    private void runMenu(Connection connection) throws SQLException {
        String choice;
        do {
            printMenu();
            choice = scanner.nextLine();
        } while (menuChoice(connection, choice));
    }

    private boolean menuChoice(Connection connection, String choice) throws SQLException {
        boolean continueApplication = true;
        switch (choice) {
            case "1" -> showAllValues(connection);
            case "2" -> findArtistById(connection);
            case "3" -> findArtistByAge(connection);
            case "4" -> findArtistByName(connection);
            case "5" -> addNewArtist(connection);
            case "6" -> deleteArtist(connection);
            case "7" -> updateArtist(connection);
            case "e", "E" -> continueApplication = false;
            default -> System.out.println("INVALID INPUT");
        }
        return continueApplication;
    }

    private void printMenu() {
        System.out.println("1.SHOW ALL");
        System.out.println("2.FIND BY ID");
        System.out.println("3.FIND BY AGE");
        System.out.println("4.FIND BY NAME");
        System.out.println("5.ADD NEW ARTIST");
        System.out.println("6.DELETE ARTIST");
        System.out.println("7.UPDATE ARTIST");
        System.out.println("e.EXIT");
        System.out.print("ENTER YOUR CHOICE: ");
    }

    private void showAllValues(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM Artist;");
        while (resultSet.next()) {
            System.out.println("id: " + resultSet.getInt("id") +
                    ", firstName: " + resultSet.getString("firstName") +
                    ", lastName: " + resultSet.getString("lastName") +
                    ", age: " + resultSet.getInt("age"));
        }
    }

    private void findArtistById(Connection connection) throws SQLException {
        try {
            System.out.print("ENTER ID: ");
            int idValue = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE id = ?");
            statement.setInt(1, idValue);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("id: " + resultSet.getInt("id") +
                        ", firstName: " + resultSet.getString("firstName") +
                        ", lastName: " + resultSet.getString("lastName") +
                        ", age: " + resultSet.getInt("age"));
            }
        } catch (Exception e) {
            System.out.println("INVALID INPUT");
            scanner.nextLine();
        }
    }

    private void findArtistByAge(Connection connection) throws SQLException {
        try {
            System.out.print("ENTER AGE: ");
            int ageValue = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE age = ?");
            statement.setInt(1, ageValue);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                System.out.println("id: " + resultSet.getInt("id") +
                        ", firstName: " + resultSet.getString("firstName") +
                        ", lastName: " + resultSet.getString("lastName") +
                        ", age: " + resultSet.getInt("age"));
            }
        } catch (Exception e) {
            System.out.println("INVALID INPUT");
            scanner.nextLine();
        }
    }

    private void findArtistByName(Connection connection) throws SQLException {
        System.out.print("ENTER FIRST NAME: ");
        String firstName = scanner.nextLine();
        System.out.print("ENTER LAST NAME: ");
        String lastName = scanner.nextLine();
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Artist WHERE firstName = ? AND lastName = ?");
        statement.setString(1, firstName);
        statement.setString(2, lastName);

        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            System.out.println("id: " + resultSet.getInt("id") +
                    ", firstName: " + resultSet.getString("firstName") +
                    ", lastName: " + resultSet.getString("lastName") +
                    ", age: " + resultSet.getInt("age"));
        }
    }

    private void addNewArtist(Connection connection) throws SQLException {
        try {
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Artist(firstName, lastName, age) VALUES(?, ?, ?);");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.executeUpdate();
            System.out.println("NEW ARTIS INSERTED");
        } catch (Exception e) {
            System.out.println("INVALID INPUT");
            scanner.nextLine();
        }
    }

    private void updateArtist(Connection connection) throws SQLException {
        try {
            System.out.print("ENTER ID FOR ARTIST YOU WOULD LIKE TO UPPDATE: ");
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("First Name: ");
            String firstName = scanner.nextLine();
            System.out.print("Last Name: ");
            String lastName = scanner.nextLine();
            System.out.print("Age: ");
            int age = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE Artist SET firstName = ?, lastName = ?, age = ? WHERE id = ?;");
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setInt(3, age);
            statement.setInt(4, id);
            System.out.println("ARTIST UPDATED");
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println("INVALID INPUT");
            scanner.nextLine();
        }
    }

    private void deleteArtist(Connection connection) throws SQLException {
        try {
            System.out.print("ENTER ID TO DELETE: ");
            int idValue = scanner.nextInt();
            scanner.nextLine();
            PreparedStatement statement = connection.prepareStatement("DELETE FROM Artist WHERE id = ?");
            statement.setInt(1, idValue);
            statement.executeUpdate();
            System.out.println("ARTIST DELETED");
        } catch (Exception e) {
            System.out.println("INVALID INPUT");
            scanner.nextLine();
        }
    }
}
