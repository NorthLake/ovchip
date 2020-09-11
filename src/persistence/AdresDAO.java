package persistence;

import model.Adres;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    void setReizigerDAO(ReizigerDAO rdao);
    boolean save(Adres adres) throws SQLException;
    boolean update(Adres adres) throws SQLException;
    boolean delete(Adres adres) throws SQLException;
    Adres findById(int id) throws SQLException;
    Adres findByIdWithoutReiziger(int id) throws SQLException;
    List<Adres> findByStad(String stad) throws SQLException;
    List<Adres> findAll() throws SQLException;
}
