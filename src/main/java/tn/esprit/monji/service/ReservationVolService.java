package tn.esprit.monji.service;

import tn.esprit.monji.model.ReservationVol;
import tn.esprit.monji.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class  ReservationVolService implements IReservationVolService {
    private final DatabaseConnection dbConnection;

    public ReservationVolService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public List<ReservationVol> findAll() {
        List<ReservationVol> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation_vol";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                reservations.add(new ReservationVol(
                    rs.getInt("id"),
                    rs.getInt("id_user"),
                    rs.getInt("id_vol"),
                    rs.getString("classe"),
                    rs.getInt("nb_billets")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservations: " + e.getMessage());
        }
        
        return reservations;
    }

    @Override
    public ReservationVol findById(int id) {
        String query = "SELECT * FROM reservation_vol WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new ReservationVol(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getInt("id_vol"),
                        rs.getString("classe"),
                        rs.getInt("nb_billets")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservation: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<ReservationVol> findByUserId(int userId) {
        List<ReservationVol> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation_vol WHERE id_user = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(new ReservationVol(
                    rs.getInt("id"),
                    rs.getInt("id_user"),
                    rs.getInt("id_vol"),
                    rs.getString("classe"),
                    rs.getInt("nb_billets")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservations by user id: " + e.getMessage());
        }
        
        return reservations;
    }

    @Override
    public List<ReservationVol> findByVolId(int volId) {
        List<ReservationVol> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservation_vol WHERE id_vol = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, volId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                reservations.add(new ReservationVol(
                    rs.getInt("id"),
                    rs.getInt("id_user"),
                    rs.getInt("id_vol"),
                    rs.getString("classe"),
                    rs.getInt("nb_billets")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching reservations by vol id: " + e.getMessage());
        }
        
        return reservations;
    }

    @Override
    public void add(ReservationVol reservation) {
        String query = "INSERT INTO reservation_vol (id_user, id_vol, classe, nb_billets) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, reservation.getIdUser());
            pstmt.setInt(2, reservation.getIdVol());
            pstmt.setString(3, reservation.getClasse());
            pstmt.setInt(4, reservation.getNbBillets());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservation.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding reservation: " + e.getMessage());
        }
    }

    @Override
    public void update(ReservationVol reservation) {
        String query = "UPDATE reservation_vol SET id_user = ?, id_vol = ?, classe = ?, nb_billets = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, reservation.getIdUser());
            pstmt.setInt(2, reservation.getIdVol());
            pstmt.setString(3, reservation.getClasse());
            pstmt.setInt(4, reservation.getNbBillets());
            pstmt.setInt(5, reservation.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating reservation: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM reservation_vol WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting reservation: " + e.getMessage());
        }
    }
} 