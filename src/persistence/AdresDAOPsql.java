package persistence;

import model.Adres;
import model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    Connection conn;
    ReizigerDAO rdao;

    public AdresDAOPsql(Connection conn) {
        this.conn = conn;
        this.rdao = new ReizigerDAOPsql(conn);
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setInt(1, adres.getId());
        statement.setString(2, adres.getPostcode());
        statement.setString(3, adres.getHuisnummer());
        statement.setString(4, adres.getStraat());
        statement.setString(5, adres.getWoonplaats());
        statement.setInt(6, adres.getReiziger().getId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE adres SET postcode = ?, huisnummer = ?, straat = ?, woonplaats = ?, reiziger_id = ? WHERE adres_id = ?");
        statement.setString(1, adres.getPostcode());
        statement.setString(2, adres.getHuisnummer());
        statement.setString(3, adres.getStraat());
        statement.setString(4, adres.getWoonplaats());
        statement.setInt(5, adres.getReiziger().getId());
        statement.setInt(6, adres.getId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM adres WHERE adres_id = ?");
        statement.setInt(1, adres.getId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public Adres findById(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM adres WHERE adres_id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        return new Adres(
                resultSet.getInt("adres_id"),
                resultSet.getString("postcode"),
                resultSet.getString("huisnummer"),
                resultSet.getString("straat"),
                resultSet.getString("woonplaats"),
                rdao.findById(resultSet.getInt("reiziger_id")));
    }

    @Override
    public List<Adres> findByReiziger(Reiziger reiziger) throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM adres WHERE reiziger_id = ?");
        statement.setInt(1, reiziger.getId());
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        adressen.add(new Adres(
                resultSet.getInt("adres_id"),
                resultSet.getString("postcode"),
                resultSet.getString("huisnummer"),
                resultSet.getString("straat"),
                resultSet.getString("woonplaats"),
                reiziger));
        return adressen;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reiziger");
        while (resultSet.next()) {
            adressen.add(new Adres(
                    resultSet.getInt("adres_id"),
                    resultSet.getString("postcode"),
                    resultSet.getString("huisnummer"),
                    resultSet.getString("straat"),
                    resultSet.getString("woonplaats"),
                    rdao.findById(resultSet.getInt("reiziger_id"))));
        }
        return adressen;
    }
}
