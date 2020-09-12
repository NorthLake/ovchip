package persistence;

import model.OVChipkaart;
import model.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection conn;
    private ReizigerDAO rdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)");
        statement.setInt(1, ovChipkaart.getKaartNummer());
        statement.setDate(2, Date.valueOf(ovChipkaart.geldigTot()));
        statement.setInt(3, ovChipkaart.getKlasse());
        statement.setFloat(4, ovChipkaart.getSaldo());
        statement.setInt(5, ovChipkaart.getReiziger().getId());
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?, reiziger_id = ? WHERE kaart_nummer = ?");
        statement.setDate(1, Date.valueOf(ovChipkaart.geldigTot()));
        statement.setInt(2, ovChipkaart.getKlasse());
        statement.setFloat(3, ovChipkaart.getSaldo());
        statement.setInt(4, ovChipkaart.getReiziger().getId());
        statement.setInt(5, ovChipkaart.getKaartNummer());
        return statement.executeUpdate() == 1;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer = ?");
        statement.setInt(1, ovChipkaart.getKaartNummer());
        return statement.executeUpdate() == 1;
    }

    @Override
    public OVChipkaart findByKaartNummer(int kaartNummer) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE kaart_nummer = ?");
        statement.setInt(1, kaartNummer);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return null;
        return new OVChipkaart(
                resultSet.getInt("kaart_nummer"),
                resultSet.getDate("geldig_tot").toLocalDate(),
                resultSet.getInt("klasse"),
                resultSet.getFloat("saldo"),
                rdao.findById(resultSet.getInt("reiziger_id"))
        );
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id = ? ORDER BY kaart_nummer");
        statement.setInt(1, reiziger.getId());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            ovChipkaarten.add(new OVChipkaart(
                    resultSet.getInt("kaart_nummer"),
                    resultSet.getDate("geldig_tot").toLocalDate(),
                    resultSet.getInt("klasse"),
                    resultSet.getFloat("saldo"),
                    reiziger
            ));
        }
        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll() throws SQLException {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM ov_chipkaart ORDER BY kaart_nummer");
        while (resultSet.next()) {
            ovChipkaarten.add(new OVChipkaart(
                    resultSet.getInt("kaart_nummer"),
                    resultSet.getDate("geldig_tot").toLocalDate(),
                    resultSet.getInt("klasse"),
                    resultSet.getFloat("saldo"),
                    rdao.findById(resultSet.getInt("reiziger_id"))
            ));
        }
        return ovChipkaarten;
    }
}
