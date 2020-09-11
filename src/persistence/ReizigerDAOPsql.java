package persistence;

import model.Adres;
import model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private final Connection conn;
    private AdresDAO adao;

    public ReizigerDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setAdresDAO(AdresDAO adao) {
        this.adao = adao;
    }

    @Override
    public boolean save(Reiziger reiziger) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES (?, ?, ?, ?, ?)");
        statement.setInt(1, reiziger.getId());
        statement.setString(2, reiziger.getVoorletters());
        statement.setString(3, reiziger.getTussenvoegsel());
        statement.setString(4, reiziger.getAchternaam());
        statement.setDate(5, Date.valueOf(reiziger.getGeboortedatum()));
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean update(Reiziger reiziger) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE reiziger SET voorletters = ?, tussenvoegsel = ?, achternaam = ?, geboortedatum = ? WHERE reiziger_id = ?");
        statement.setString(1, reiziger.getVoorletters());
        statement.setString(2, reiziger.getTussenvoegsel());
        statement.setString(3, reiziger.getAchternaam());
        statement.setDate(4, Date.valueOf(reiziger.getGeboortedatum()));
        statement.setInt(5, reiziger.getId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean delete(Reiziger reiziger) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM reiziger WHERE reiziger_id = ?");
        statement.setInt(1, reiziger.getId());
        return statement.executeUpdate() == 1;  // Nooit meer dan één doordat reiziger_id unique is
    }

    @Override
    public Reiziger findById(int id) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger LEFT OUTER JOIN adres on reiziger.reiziger_id = adres.reiziger_id WHERE adres.reiziger_id = ?");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        Reiziger reiziger = new Reiziger(
                resultSet.getInt("reiziger_id"),
                resultSet.getString("voorletters"),
                resultSet.getString("tussenvoegsel"),
                resultSet.getString("achternaam"),
                resultSet.getDate("geboortedatum").toLocalDate()
        );
        int adresId = resultSet.getInt("adres_id");
        if (adresId != 0) {
            Adres adres = adao.findByIdWithoutReiziger(adresId);
            adres.setReiziger(reiziger);
            reiziger.setAdres(adres);

        }
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbdatum(String datum) throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM reiziger LEFT OUTER JOIN adres on reiziger.reiziger_id = adres.reiziger_id WHERE geboortedatum = ? ORDER BY adres.reiziger_id");
        statement.setDate(1, Date.valueOf(datum));
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Reiziger reiziger = new Reiziger(
                    resultSet.getInt("reiziger_id"),
                    resultSet.getString("voorletters"),
                    resultSet.getString("tussenvoegsel"),
                    resultSet.getString("achternaam"),
                    resultSet.getDate("geboortedatum").toLocalDate()
            );
            int adresId = resultSet.getInt("adres_id");
            if (adresId != 0) {
                Adres adres = adao.findByIdWithoutReiziger(adresId);
                adres.setReiziger(reiziger);
                reiziger.setAdres(adres);
            }
            reizigers.add(reiziger);
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() throws SQLException {
        List<Reiziger> reizigers = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM reiziger LEFT OUTER JOIN adres on reiziger.reiziger_id = adres.reiziger_id ORDER BY adres.reiziger_id");
        while (resultSet.next()) {
            Reiziger reiziger = new Reiziger(
                    resultSet.getInt("reiziger_id"),
                    resultSet.getString("voorletters"),
                    resultSet.getString("tussenvoegsel"),
                    resultSet.getString("achternaam"),
                    resultSet.getDate("geboortedatum").toLocalDate()
            );
            int adresId = resultSet.getInt("adres_id");
            if (adresId != 0) {
                Adres adres = adao.findByIdWithoutReiziger(adresId);
                adres.setReiziger(reiziger);
                reiziger.setAdres(adres);
            }
            reizigers.add(reiziger);
        }
        return reizigers;
    }
}
