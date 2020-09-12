package persistence;

import model.OVChipkaart;
import model.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface OVChipkaartDAO {
    void setReizigerDAO(ReizigerDAO rdao);
    boolean save(OVChipkaart ovChipkaart) throws SQLException;
    boolean update(OVChipkaart ovChipkaart) throws SQLException;
    boolean delete(OVChipkaart ovChipkaart) throws SQLException;
    OVChipkaart findByKaartNummer(int id) throws SQLException;
    List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    List<OVChipkaart> findAll() throws SQLException;
}
