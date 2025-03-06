package tn.esprit.monji.service;

import tn.esprit.monji.model.ReservationVol;
import java.util.List;

public interface IReservationVolService {
    void add(ReservationVol reservation);
    void update(ReservationVol reservation);
    void delete(int id);
    List<ReservationVol> findAll();
    ReservationVol findById(int id);
    List<ReservationVol> findByUserId(int userId);
    List<ReservationVol> findByVolId(int volId);
} 