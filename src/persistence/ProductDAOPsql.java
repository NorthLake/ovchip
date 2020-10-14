package persistence;

import model.OVChipkaart;
import model.Product;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class ProductDAOPsql implements ProductDAO {
    private final Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.odao = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) throws SQLException {
        int rowsUpdated = 0;
        PreparedStatement statement1 = conn.prepareStatement("INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES (?, ?, ?, ?)");
        statement1.setInt(1, product.getProductNummer());
        statement1.setString(2, product.getNaam());
        statement1.setString(3, product.getBeschrijving());
        statement1.setFloat(4, product.getPrijs());
        rowsUpdated += statement1.executeUpdate();

        Set<OVChipkaart> ovChipkaarten = product.getKaarten();
        for (OVChipkaart ovChipkaart : ovChipkaarten) {
            PreparedStatement statement2 = conn.prepareStatement("INSERT INTO ov_chipkaart_product (product_nummer, kaart_nummer) VALUES (?, ?)");
            statement2.setInt(1, product.getProductNummer());
            statement2.setInt(2, ovChipkaart.getKaartNummer());
            rowsUpdated += statement2.executeUpdate();
        }

        return rowsUpdated == ovChipkaarten.size() + 1;
    }

    @Override
    public boolean update(Product product) throws SQLException {
        PreparedStatement statement1 = conn.prepareStatement("UPDATE product SET naam = ?, beschrijving = ?, prijs = ? WHERE product_nummer = ?");
        statement1.setString(1, product.getNaam());
        statement1.setString(2, product.getBeschrijving());
        statement1.setFloat(3, product.getPrijs());
        statement1.setInt(4, product.getProductNummer());
        if (statement1.executeUpdate() != 1)
            return false;

        PreparedStatement statement2 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
        statement2.setInt(1, product.getProductNummer());
        statement2.executeUpdate();
        for (OVChipkaart ovChipkaart : product.getKaarten()) {
            PreparedStatement statement3 = conn.prepareStatement("INSERT INTO ov_chipkaart_product (product_nummer, kaart_nummer) VALUES (?, ?) ON CONFLICT DO NOTHING");
            statement3.setInt(1, product.getProductNummer());
            statement3.setInt(2, ovChipkaart.getKaartNummer());
            statement3.executeUpdate();
        }

        return true;
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        int rowsUpdated = 0;
        PreparedStatement statement1 = conn.prepareStatement("DELETE FROM product WHERE product_nummer = ?");
        statement1.setInt(1, product.getProductNummer());
        rowsUpdated += statement1.executeUpdate();

        int aantalKaarten = product.getKaarten().size();
        PreparedStatement statement2 = conn.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
        statement2.setInt(1, product.getProductNummer());
        rowsUpdated += statement2.executeUpdate();

        return rowsUpdated == aantalKaarten + 1;
    }

    @Override
    public Product findByNummer(int nummer) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT naam, beschrijving, prijs FROM product WHERE product_nummer = ?");
        statement.setInt(1, nummer);
        ResultSet resultSet = statement.executeQuery();
        if (!resultSet.next())
            return null;
        return new Product(
                nummer,
                resultSet.getString("naam"),
                resultSet.getString("beschrijving"),
                resultSet.getFloat("prijs")
        );
    }

    @Override
    public Set<Product> findByOVChipKaart(OVChipkaart ovChipkaart) throws SQLException {
        Set<Product> producten = new HashSet<>();
        PreparedStatement statement = conn.prepareStatement("SELECT product.product_nummer as product_nummer, product.naam as naam, product.beschrijving as beschrijving, product.prijs as prijs FROM product LEFT OUTER JOIN ov_chipkaart_product koppel on product.product_nummer = koppel.product_nummer WHERE koppel.kaart_nummer = ?");
        statement.setInt(1, ovChipkaart.getKaartNummer());
        ResultSet resultSet = statement.executeQuery();
        while (resultSet.next()) {
            Product product = new Product(
                    resultSet.getInt("product_nummer"),
                    resultSet.getString("naam"),
                    resultSet.getString("beschrijving"),
                    resultSet.getFloat("prijs")
            );
            product.addKaart(ovChipkaart);
            producten.add(product);
        }
        return producten;
    }

    @Override
    public Set<Product> findAll() throws SQLException {
        Set<Product> producten = findAllWithoutProduct();
        Set<OVChipkaart> ovChipkaarten = odao.findAllWithoutProduct();
        for (Product product : producten) {
            PreparedStatement statement2 = conn.prepareStatement("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer = ?;");
            statement2.setInt(1, product.getProductNummer());
            ResultSet resultSet2 = statement2.executeQuery();
            while (resultSet2.next()) {
                int kaartNummer = resultSet2.getInt("kaart_nummer");
                for (OVChipkaart ovChipkaart : ovChipkaarten) {
                    if (ovChipkaart.getKaartNummer() == kaartNummer) {
                        product.addKaart(ovChipkaart);
                        break;
                    }
                }
            }
        }
        return producten;
    }

    @Override
    public Set<Product> findAllWithoutProduct() throws SQLException {
        Set<Product> producten = new HashSet<>();
        Statement statement1 = conn.createStatement();
        ResultSet resultSet1 = statement1.executeQuery("SELECT product_nummer, naam, beschrijving, prijs FROM product;");
        while (resultSet1.next()) {
            Product product = new Product(
                    resultSet1.getInt("product_nummer"),
                    resultSet1.getString("naam"),
                    resultSet1.getString("beschrijving"),
                    resultSet1.getFloat("prijs")
            );
            producten.add(product);
        }
        return producten;
    }
}
