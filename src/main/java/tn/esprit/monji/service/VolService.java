package tn.esprit.monji.service;

import tn.esprit.monji.model.Vol;
import tn.esprit.monji.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class VolService implements IVolService {
    private final DatabaseConnection dbConnection;

    public VolService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }
    
    @Override
    public List<Vol> findAll() {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                Vol vol = new Vol(
                    rs.getInt("id"),
                    rs.getString("compagnie"),
                    rs.getString("aeroport_depart"),
                    rs.getString("aeroport_arrivee"),
                    rs.getString("destination"),
                    rs.getDate("date_depart"),
                    rs.getDate("date_arrivee"),
                    rs.getDouble("prix")
                );
                vols.add(vol);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vols: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return vols;
    }
    
    @Override
    public Vol findById(int id) {
        String query = "SELECT * FROM vol WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Vol(
                        rs.getInt("id"),
                        rs.getString("compagnie"),
                        rs.getString("aeroport_depart"),
                        rs.getString("aeroport_arrivee"),
                        rs.getString("destination"),
                        rs.getDate("date_depart"),
                        rs.getDate("date_arrivee"),
                        rs.getDouble("prix")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vol by id: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
    
    @Override
    public void add(Vol vol) {
        String query = "INSERT INTO vol (compagnie, aeroport_depart, aeroport_arrivee, destination, date_depart, date_arrivee, prix) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, vol.getCompagnie());
            pstmt.setString(2, vol.getAeroportDepart());
            pstmt.setString(3, vol.getAeroportArrivee());
            pstmt.setString(4, vol.getDestination());
            pstmt.setDate(5, new java.sql.Date(vol.getDateDepart().getTime()));
            pstmt.setDate(6, new java.sql.Date(vol.getDateArrivee().getTime()));
            pstmt.setDouble(7, vol.getPrix());
            
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vol.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error adding vol: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void update(Vol vol) {
        String query = "UPDATE vol SET compagnie = ?, aeroport_depart = ?, aeroport_arrivee = ?, " +
                      "destination = ?, date_depart = ?, date_arrivee = ?, prix = ? WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, vol.getCompagnie());
            pstmt.setString(2, vol.getAeroportDepart());
            pstmt.setString(3, vol.getAeroportArrivee());
            pstmt.setString(4, vol.getDestination());
            pstmt.setDate(5, new java.sql.Date(vol.getDateDepart().getTime()));
            pstmt.setDate(6, new java.sql.Date(vol.getDateArrivee().getTime()));
            pstmt.setDouble(7, vol.getPrix());
            pstmt.setInt(8, vol.getId());
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating vol: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void delete(int id) {
        String query = "DELETE FROM vol WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting vol: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Vol> findByCompagnie(String compagnie) {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol WHERE compagnie LIKE ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + compagnie + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {

                Vol vol = new Vol(
                    rs.getInt("id"),
                    rs.getString("compagnie"),
                    rs.getString("aeroport_depart"),
                    rs.getString("aeroport_arrivee"),
                    rs.getString("destination"),
                    rs.getDate("date_depart"),
                    rs.getDate("date_arrivee"),
                    rs.getDouble("prix")
                );
                vols.add(vol);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vols by compagnie: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return vols;
    }

    @Override
    public List<Vol> findByDate(Date date) {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol WHERE DATE(date_depart) = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setDate(1, new java.sql.Date(date.getTime()));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vol vol = new Vol(
                    rs.getInt("id"),
                    rs.getString("compagnie"),
                    rs.getString("aeroport_depart"),
                    rs.getString("aeroport_arrivee"),
                    rs.getString("destination"),
                    rs.getDate("date_depart"),
                    rs.getDate("date_arrivee"),
                    rs.getDouble("prix")
                );
                vols.add(vol);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vols by date: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return vols;
    }

    @Override
    public List<Vol> findByDestination(String destination) {
        List<Vol> vols = new ArrayList<>();
        String query = "SELECT * FROM vol WHERE destination LIKE ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, "%" + destination + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Vol vol = new Vol(
                    rs.getInt("id"),
                    rs.getString("compagnie"),
                    rs.getString("aeroport_depart"),
                    rs.getString("aeroport_arrivee"),
                    rs.getString("destination"),
                    rs.getDate("date_depart"),
                    rs.getDate("date_arrivee"),
                    rs.getDouble("prix")
                );
                vols.add(vol);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching vols by destination: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return vols;
    }
} 