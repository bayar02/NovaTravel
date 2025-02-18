package services;

import java.sql.SQLException;
import java.util.List;

public interface Ihebergement<T> {
    void create(T t) throws SQLException;
    void update(T t)throws SQLException;
    boolean delete(int id)throws SQLException;
    List<T> getAll() throws SQLException;
}
