package tn.esprit.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.entities.User;
import tn.esprit.utils.MyDataBase;
import tn.esprit.utils.SecurityUtil;

public class ServiceUser implements IService<User> {

    private Connection connection;

    public ServiceUser() {
        connection = MyDataBase.getInstance().getConnection();
    }

    @Override
    public void ajouter(User t) {
        // Validate user data before insertion
        if (!validateUserData(t)) {
            System.out.println("Erreur: Données utilisateur invalides");
            return;
        }

        String query = "INSERT INTO user (cin, nom, prenom, tel, role, mail, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, t.getCin());
            stm.setString(2, t.getNom());
            stm.setString(3, t.getPrenom());
            stm.setInt(4, Integer.parseInt(t.getTel()));  // Convert String to int for database
            stm.setString(5, t.getRole().name());
            stm.setString(6, t.getMail());
            stm.setString(7, SecurityUtil.hashPassword(t.getPassword()));

            int affectedRows = stm.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La création de l'utilisateur a échoué, aucune ligne affectée.");
            }

            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    t.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("La création de l'utilisateur a échoué, aucun ID obtenu.");
                }
            }
            System.out.println("Utilisateur ajouté avec succès ! ID: " + t.getId());
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getMessage().contains("mail")) {
                System.out.println("Erreur: Cette adresse email est déjà utilisée");
            } else if (e.getMessage().contains("cin")) {
                System.out.println("Erreur: Ce numéro CIN est déjà utilisé");
            } else {
                System.out.println("Erreur de contrainte d'intégrité: " + e.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    @Override
    public void modifier(User t) {
        if (!validateUserData(t)) {
            System.out.println("Erreur: Données utilisateur invalides");
            return;
        }

        String updateQuery = "UPDATE user SET cin=?, prenom=?, nom=?, tel=?, mail=?, role=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, t.getCin());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getNom());
            preparedStatement.setString(4, t.getTel());
            preparedStatement.setString(5, t.getMail());
            preparedStatement.setString(6, t.getRole().name());
            preparedStatement.setInt(7, t.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur modifié avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            if (e.getMessage().contains("mail")) {
                System.out.println("Erreur: Cette adresse email est déjà utilisée");
            } else if (e.getMessage().contains("cin")) {
                System.out.println("Erreur: Ce numéro CIN est déjà utilisé");
            } else {
                System.out.println("Erreur de contrainte d'intégrité: " + e.getMessage());
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification : " + ex.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String deleteQuery = "DELETE FROM user WHERE id=?";
        try (PreparedStatement stm = connection.prepareStatement(deleteQuery)) {
            stm.setInt(1, id);
            int rowsDeleted = stm.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Utilisateur supprimé avec succès !");
            } else {
                System.out.println("Aucun utilisateur trouvé avec cet ID.");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la suppression : " + ex.getMessage());
        }
    }

    @Override
    public User getOne(int id) {
        String query = "SELECT * FROM user WHERE id=?";
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setInt(1, id);
            ResultSet rs = stm.executeQuery();

            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération : " + ex.getMessage());
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        String query = "SELECT * FROM user";
        List<User> users = new ArrayList<>();
        try (Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(query)) {
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des utilisateurs : " + ex.getMessage());
        }
        return users;
    }

    public List<User> getUsersByRole(User.Role role) {
        String query = "SELECT * FROM user WHERE role = ?";
        List<User> users = new ArrayList<>();
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, role.name());
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération des utilisateurs par rôle : " + ex.getMessage());
        }
        return users;
    }

    public User getUserByEmail(String mail) {
        String query = "SELECT * FROM user WHERE mail=?";
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, mail);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération par email : " + ex.getMessage());
        }
        return null;
    }

    public User getUserByCin(String cin) {
        String query = "SELECT * FROM user WHERE cin=?";
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, cin);
            ResultSet rs = stm.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la récupération par CIN : " + ex.getMessage());
        }
        return null;
    }

    public User authenticate(String mail, String password) throws SQLException {
        String sql = "SELECT * FROM user WHERE mail = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, mail);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                System.out.println("Debug - Found user with email: " + mail);
                System.out.println("Debug - Stored password hash: " + storedPassword);
                String inputHash = SecurityUtil.hashPassword(password);
                System.out.println("Debug - Input password hash: " + inputHash);
                
                if (SecurityUtil.verifyPassword(password, storedPassword)) {
                    System.out.println("Debug - Password verified successfully");
                    return mapResultSetToUser(rs);
                } else {
                    System.out.println("Debug - Password verification failed");
                }
            } else {
                System.out.println("Debug - No user found with email: " + mail);
            }
        } catch (SQLException e) {
            System.out.println("Debug - SQL Error during authentication: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public boolean updatePassword(int userId, String newPassword) throws SQLException {
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe ne peut pas être vide");
        }

        String sql = "UPDATE user SET password=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, SecurityUtil.hashPassword(newPassword));
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateRole(int userId, User.Role newRole) throws SQLException {
        String sql = "UPDATE user SET role=? WHERE id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newRole.name());
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    private boolean validateUserData(User user) {
        if (user == null) return false;
        if (user.getCin() == null || user.getCin().trim().isEmpty()) return false;
        if (user.getNom() == null || user.getNom().trim().isEmpty()) return false;
        if (user.getPrenom() == null || user.getPrenom().trim().isEmpty()) return false;
        if (user.getTel() == null || !user.getTel().matches("\\d{8}")) return false;
        if (user.getMail() == null || !user.getMail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) return false;
        if (user.getRole() == null) return false;
        return true;
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setCin(rs.getString("cin"));
        user.setNom(rs.getString("nom"));
        user.setPrenom(rs.getString("prenom"));
        user.setTel(String.valueOf(rs.getInt("tel")));  // Convert int to String for the model
        user.setMail(rs.getString("mail"));
        
        String roleString = rs.getString("role");
        try {
            user.setRole(User.Role.valueOf(roleString));
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de conversion du rôle : " + roleString);
            user.setRole(User.Role.REGULAR_USER); // Default role
        }
        
        return user;
    }
}
