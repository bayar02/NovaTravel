package tn.esprit.services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import tn.esprit.entities.Admin;
import tn.esprit.entities.Agent;
import tn.esprit.entities.RegularUser;
import tn.esprit.entities.User;
import tn.esprit.utils.MyDataBase;

public class ServiceUser implements IService<User> {

    private Connection connection = MyDataBase.getInstance().getConnection();

    public ServiceUser() {
    }

    @Override
    public void ajouter(User t) {
        String query = "INSERT INTO user (cin, nom, prenom, tel, role, mail, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stm = connection.prepareStatement(query)) {
            stm.setString(1, t.getCin());
            stm.setString(2, t.getNom());
            stm.setString(3, t.getPrenom());
            stm.setString(4, t.getTel());
            stm.setString(5, t.getRole().name()); // Convert enum to String
            stm.setString(6, t.getMail());
            stm.setString(7, t.getPassword());

            stm.executeUpdate();
            System.out.println("Utilisateur ajouté avec succès !");
        } catch (SQLException ex) {
            System.out.println("Erreur lors de l'ajout : " + ex.getMessage());
        }
    }

    @Override
    public void modifier(User t) {
        String updateQuery = "UPDATE user SET cin=?, prenom=?, nom=?, tel=?, mail=?, password=?, role=? WHERE id_user=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, t.getCin());
            preparedStatement.setString(2, t.getPrenom());
            preparedStatement.setString(3, t.getNom());
            preparedStatement.setString(4, t.getTel());
            preparedStatement.setString(5, t.getMail());
            preparedStatement.setString(6, t.getPassword());
            preparedStatement.setString(7, t.getRole().name()); // Convert enum to String
            preparedStatement.setInt(8, t.getId_user());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Utilisateur modifié avec succès !");
            } else {
                System.out.println("Aucune donnée modifiée.");
            }
        } catch (SQLException ex) {
            System.out.println("Erreur lors de la modification : " + ex.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String deleteQuery = "DELETE FROM user WHERE id_user=?";
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
        String query = "SELECT * FROM user WHERE id_user=?";
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

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        String roleString = rs.getString("role");
        User.Role role;

        try {
            role = User.Role.valueOf(roleString);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de conversion du rôle : " + roleString);
            role = User.Role.REGULAR_USER; // Default role
        }

        switch (role) {
            case ADMIN:
                return new Admin(
                        rs.getString("cin"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("tel"),
                        rs.getString("mail"),
                        rs.getString("password")
                );
            case AGENT:
                return new Agent(
                        rs.getString("cin"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("tel"),
                        rs.getString("mail"),
                        rs.getString("password")
                );
            case REGULAR_USER:
            default:
                return new RegularUser(
                        rs.getString("cin"),
                        rs.getString("prenom"),
                        rs.getString("nom"),
                        rs.getString("tel"),
                        rs.getString("mail"),
                        rs.getString("password")
                );
        }
    }
}
