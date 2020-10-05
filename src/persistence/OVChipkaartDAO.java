package persistence;

import model.OVChipkaart;
import model.Product;
import model.Reiziger;

import java.sql.SQLException;
import java.util.Set;

public interface OVChipkaartDAO {
    void setReizigerDAO(ReizigerDAO rdao);
    void setProductDAO(ProductDAO pdao);
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    OVChipkaart findByKaartNummer(int id) throws SQLException;
    Set<OVChipkaart> findByProduct(Product product) throws SQLException;
    Set<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    Set<OVChipkaart> findAll() throws SQLException;
    Set<OVChipkaart> findAllWithoutProduct() throws SQLException;
}
