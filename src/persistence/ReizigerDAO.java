package persistence;

import model.Reiziger;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface ReizigerDAO {
    void setAdresDAO(AdresDAO adao);
    void setOVChipkaartDAO(OVChipkaartDAO odao);
    boolean save(Reiziger reiziger) throws SQLException;
    boolean update(Reiziger reiziger) throws SQLException;
    boolean delete(Reiziger reiziger) throws SQLException;
    Reiziger findById(int id) throws SQLException;
    List<Reiziger> findByGbdatum(LocalDate datum) throws SQLException;
    List<Reiziger> findAll() throws SQLException;
}
