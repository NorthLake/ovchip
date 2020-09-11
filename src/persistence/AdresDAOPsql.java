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
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rdao) {
        this.rdao = rdao;
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
        if (!resultSet.next())
            return null;
        Adres adres = new Adres(
                resultSet.getInt("adres_id"),
                resultSet.getString("postcode"),
                resultSet.getString("huisnummer"),
                resultSet.getString("straat"),
                resultSet.getString("woonplaats")
        );
        int reizigerId = resultSet.getInt("reiziger_id");
        if (reizigerId != 0) {
            Reiziger reiziger = rdao.findById(reizigerId);
            adres.setReiziger(reiziger);
            reiziger.setAdres(adres);
        }
        return adres;
    }

    @Override
    public Adres findByIdWithoutReiziger(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM adres WHERE adres_id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return null;
        return new Adres(
                resultSet.getInt("adres_id"),
                resultSet.getString("postcode"),
                resultSet.getString("huisnummer"),
                resultSet.getString("straat"),
                resultSet.getString("woonplaats")
        );
    }

    @Override
    public List<Adres> findByStad(String stad) throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM adres WHERE woonplaats = ? ORDER BY adres_id");
        statement.setString(1, stad);
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next()) {
            Adres adres = new Adres(
                    resultSet.getInt("adres_id"),
                    resultSet.getString("postcode"),
                    resultSet.getString("huisnummer"),
                    resultSet.getString("straat"),
                    stad
            );
            int reizigerId = resultSet.getInt("reiziger_id");
            if (reizigerId != 0) {
                Reiziger reiziger = rdao.findById(reizigerId);
                adres.setReiziger(reiziger);
                reiziger.setAdres(adres);
            }
            adressen.add(adres);
        }
        return adressen;
    }

    @Override
    public List<Adres> findAll() throws SQLException {
        List<Adres> adressen = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM adres ORDER BY adres_id");
        while (resultSet.next()) {
            Adres adres = new Adres(
                    resultSet.getInt("adres_id"),
                    resultSet.getString("postcode"),
                    resultSet.getString("huisnummer"),
                    resultSet.getString("straat"),
                    resultSet.getString("woonplaats")
            );
            int reizigerId = resultSet.getInt("reiziger_id");
            if (reizigerId != 0) {
                Reiziger reiziger = rdao.findById(reizigerId);
                adres.setReiziger(reiziger);
                reiziger.setAdres(adres);
            }
            adressen.add(adres);
        }
        return adressen;
    }
}
