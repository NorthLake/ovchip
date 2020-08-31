import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "ovchip", "ovchip");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT voorletters, tussenvoegsel, achternaam, geboortedatum FROM reiziger");
            System.out.println("Alle reizigers:");
            while (resultSet.next()) {
                String naam;
                if (resultSet.getString("tussenvoegsel") == null)
                    naam = resultSet.getString("voorletters") + " " + resultSet.getString("achternaam");
                else
                    naam = resultSet.getString("voorletters") + " " + resultSet.getString("tussenvoegsel") + " " + resultSet.getString("achternaam");
                System.out.println("\t#" + resultSet.getRow() + ": " + naam + " (" + resultSet.getDate("geboortedatum") + ")");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
