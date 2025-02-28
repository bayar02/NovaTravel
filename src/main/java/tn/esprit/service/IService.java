package tn.esprit.service;

import tn.esprit.entities.Event;

import java.sql.SQLException;
import java.util.List;

public interface IService<T>  {

    void ajouter(T t) throws SQLException;
    void modifier(T t)throws SQLException;
    void supprimer(int id)throws SQLException;
    public List<T> afficher() throws SQLException;
    public List<T> getAll() throws SQLException;

    public List<T> rechercher(String recherche) throws SQLException;


}
