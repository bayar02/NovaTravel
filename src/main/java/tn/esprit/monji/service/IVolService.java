package tn.esprit.monji.service;

import tn.esprit.monji.model.Vol;
import java.util.List;
import java.util.Date;

public interface IVolService {
    void add(Vol vol);
    void update(Vol vol);
    void delete(int id);
    List<Vol> findAll();
    Vol findById(int id);
    List<Vol> findByCompagnie(String compagnie);
    List<Vol> findByDate(Date date);
    List<Vol> findByDestination(String destination);
} 