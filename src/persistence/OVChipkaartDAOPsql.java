package persistence;

import model.OVChipkaart;
import model.Product;
import model.Reiziger;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private final Connection conn;
    private ReizigerDAO rdao;
    private ProductDAO pdao;

    public OVChipkaartDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO rdao) {
        this.rdao = rdao;
    }

    @Override
    public void setProductDAO(ProductDAO pdao) {
        this.pdao = pdao;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) throws SQLException {
        int rowsUpdated = 0;
        PreparedStatement statement1 = conn.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES (?, ?, ?, ?, ?)");
        statement1.setInt(1, ovChipkaart.getKaartNummer());
        statement1.setDate(2, Date.valueOf(ovChipkaart.geldigTot()));
        statement1.setInt(3, ovChipkaart.getKlasse());
        statement1.setFloat(4, ovChipkaart.getSaldo());
        statement1.setInt(5, ovChipkaart.getReiziger().getId());
        rowsUpdated += statement1.executeUpdate();

        Set<Product> producten = ovChipkaart.getProducten();
        for (Product product : producten) {
            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)");
            statement2.setInt(1, ovChipkaart.getKaartNummer());
            statement2.setInt(2, product.getProductNummer());
            rowsUpdated += statement2.executeUpdate();
        }

        return rowsUpdated == producten.size() + 1;
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
        PreparedStatement statement = conn.prepareStatement("SELECT geldig_tot, klasse, saldo, reiziger_id FROM ov_chipkaart WHERE kaart_nummer = ?");
        statement.setInt(1, kaartNummer);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return null;
        return new OVChipkaart(
                kaartNummer,
                resultSet.getDate("geldig_tot").toLocalDate(),
                resultSet.getInt("klasse"),
                resultSet.getFloat("saldo"),
                rdao.findById(resultSet.getInt("reiziger_id"))
        );
    }

    @Override
    public Set<OVChipkaart> findByProduct(Product product) throws SQLException {
        Set<OVChipkaart> ovChipkaarten = new HashSet<>();
        PreparedStatement statement = conn.prepareStatement("SELECT ov_chipkaart.kaart_nummer as kaart_nummer, ov_chipkaart.geldig_tot as geldig_tot, ov_chipkaart.klasse as klasse, ov_chipkaart.saldo as saldo, ov_chipkaart.reiziger_id as reiziger_id FROM ov_chipkaart LEFT OUTER JOIN ov_chipkaart_product koppel on ov_chipkaart.kaart_nummer = koppel.kaart_nummer WHERE koppel.product_nummer = ?");
        statement.setInt(1, product.getProductNummer());
        ResultSet resultSet = statement.executeQuery();
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

    @Override
    public Set<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        Set<OVChipkaart> ovChipkaarten = new HashSet<>();
        PreparedStatement statement = conn.prepareStatement("SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart WHERE reiziger_id = ? ORDER BY kaart_nummer");
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
    public Set<OVChipkaart> findAll() throws SQLException {
        Set<OVChipkaart> ovChipkaarten = findAllWithoutProduct();
        Set<Product> producten = pdao.findAllWithoutProduct();
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            PreparedStatement statement2 = conn.prepareStatement("SELECT product_nummer FROM ov_chipkaart_product WHERE kaart_nummer = ?;");
            statement2.setInt(1, ovChipkaart.getKaartNummer());
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                int productNummer = resultSet2.getInt("product_nummer");
                for (Product product : producten) {
                    if (product.getProductNummer() == productNummer) {
                        product.addKaart(ovChipkaart);
                        break;
                    }
                }
            }
        }
        return ovChipkaarten;
    }

    @Override
    public Set<OVChipkaart> findAllWithoutProduct() throws SQLException {
        Set<OVChipkaart> ovChipkaarten = new HashSet<>();
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
