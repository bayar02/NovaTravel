package Services;

import Entities.Reclamation;
import Entities.User;
import Utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService implements IReclamation<Reclamation> {
    private Connection connection;

    public ReclamationService() {
        connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO reclamation (id_user, date_reclamation, type, message) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, reclamation.getIdUser());  // ✅ Corrected index (id_user)
            pst.setDate(2, new java.sql.Date(reclamation.getDateReclamation().getTime()));  // ✅ Corrected index (date_reclamation)
            pst.setString(3, reclamation.getType());  // ✅ Corrected index (type)
            pst.setString(4, reclamation.getMessage());  // ✅ Corrected index (message)

            pst.executeUpdate();
            System.out.println("Reclamation added successfully!");
        }
    }



    @Override
    public void modifier(Reclamation reclamation) throws SQLException {
        String query = "UPDATE reclamation SET id_user=1, date_reclamation=?, type=?, message=? WHERE id=?";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setDate(1, new java.sql.Date(reclamation.getDateReclamation().getTime())); // 1st placeholder
            pst.setString(2, reclamation.getType());  // 2nd placeholder
            pst.setString(3, reclamation.getMessage()); // 3rd placeholder
            pst.setInt(4, reclamation.getId());  // 4th placeholder

            int rowsUpdated = pst.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Reclamation updated successfully!");
            } else {
                System.out.println("⚠ No reclamation found with ID: " + reclamation.getId());
            }
        }
    }


    @Override
    public List<Reclamation> afficher() throws SQLException {
        List<Reclamation> reclamationList = new ArrayList<>();
        String sql = "SELECT * FROM reclamation";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);

        while (resultSet.next()) {
            reclamationList.add(new Reclamation(
                    resultSet.getInt("id"),
                    resultSet.getInt("id_user"),
                    resultSet.getDate("date_Reclamation"),
                    resultSet.getString("type"),
                    resultSet.getString("message")
            ));
        }

        return reclamationList;
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM reclamation WHERE id=?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Reclamation deleted successfully!");
            } else {
                System.out.println("No reclamation found with ID: " + id);
            }
        }
    }
    public List<Reclamation> searchByType(String type) throws SQLException {
        List<Reclamation> filteredList = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE type LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, "%" + type + "%"); // 🔥 Supports partial matching

            System.out.println("Executing query: " + stmt.toString()); // 🔥 Debugging line

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getDate("date_reclamation"),
                        rs.getString("type"),
                        rs.getString("message")
                );
                filteredList.add(reclamation);
            }
        }
        return filteredList;
    }

    public List<Reclamation> getReclamationsSortedByDate() throws SQLException {
        List<Reclamation> reclamationList = new ArrayList<>();
        String sql = "SELECT * FROM reclamation ORDER BY date_reclamation DESC"; // Sorting by date (newest first)

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                reclamationList.add(new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_user"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("message")
                ));
            }
        }

        return reclamationList;
    }


    public List<Reclamation> afficherParUtilisateur(int userId) throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id_user = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Reclamation reclamation = new Reclamation(
                        resultSet.getInt("id"),
                        resultSet.getInt("id_user"),
                        resultSet.getDate("date_reclamation"),
                        resultSet.getString("type"),
                        resultSet.getString("message")
                );
                reclamations.add(reclamation);
            }
        }
        return reclamations;
    }

    public String getReclamationMessageById(int idReclamation) throws SQLException {
        String query = "SELECT message FROM reclamation WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, idReclamation);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("message"); // Return the reclamation message
            }
        }
        return "Réclamation introuvable"; // Return this if no message found
    }

    public List<Reclamation> searchByFields(String searchText) throws SQLException {
        List<Reclamation> filteredList = new ArrayList<>();
        String query = "SELECT * FROM reclamation WHERE id_user LIKE ? OR type LIKE ? OR message LIKE ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchText + "%"; // Supports partial matching

            // Set parameters for the query to filter by id_user, type, or message
            stmt.setString(1, searchPattern); // Search in id_user
            stmt.setString(2, searchPattern); // Search in type
            stmt.setString(3, searchPattern); // Search in message

            System.out.println("Executing query: " + stmt.toString()); // Debugging line

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Reclamation reclamation = new Reclamation(
                        rs.getInt("id"),
                        rs.getInt("id_user"),
                        rs.getDate("date_reclamation"),
                        rs.getString("type"),
                        rs.getString("message")
                );
                filteredList.add(reclamation);
            }
        }
        return filteredList;
    }

    public User findUserById(int userId) {
        User user = null;
        String query = "SELECT * FROM user WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt(1));
                user.setNom(resultSet.getString(2)); // Assuming column name is "username"
                user.setPrenom(resultSet.getString(3)); // Example: other fields
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

}
