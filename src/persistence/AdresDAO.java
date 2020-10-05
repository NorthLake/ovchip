package persistence;

import model.Adres;
import model.Reiziger;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface AdresDAO {
    void setReizigerDAO(ReizigerDAO rdao);
    boolean save(Adres adres) throws SQLException;
    boolean update(Adres adres) throws SQLException;
    boolean delete(Adres adres) throws SQLException;
    Adres findByReiziger(Reiziger reiziger) throws SQLException;
    Adres findById(int id) throws SQLException;
    Set<Adres> findByStad(String stad) throws SQLException;
    Set<Adres> findAll() throws SQLException;
}
