package tn.esprit.monji.service;

import tn.esprit.monji.model.User;
import tn.esprit.monji.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private final DatabaseConnection dbConnection;

    public UserService() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                users.add(new User(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("cin"),
                    rs.getString("mail"),
                    rs.getString("tel"),
                    rs.getString("role")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage());
        }
        
        return users;
    }

    public User findById(int id) {
        String query = "SELECT * FROM user WHERE id = ?";
        
        try (Connection conn = dbConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("cin"),
                        rs.getString("mail"),
                        rs.getString("tel"),
                        rs.getString("role")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user: " + e.getMessage());
        }
        
        return null;
    }
} 