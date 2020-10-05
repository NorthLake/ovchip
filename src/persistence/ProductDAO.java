package persistence;

import model.OVChipkaart;
import model.Product;

import java.sql.SQLException;
import java.util.Set;

public interface ProductDAO {
    void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO);
    boolean save(Product product) throws SQLException;
    boolean update(Product product) throws SQLException;
    boolean delete(Product product) throws SQLException;
    Product findByNummer(int id) throws SQLException;
    Set<Product> findByOVChipKaart(OVChipkaart ovChipkaart) throws SQLException;
    Set<Product> findAll() throws SQLException;
    Set<Product> findAllWithoutProduct() throws SQLException;
}
