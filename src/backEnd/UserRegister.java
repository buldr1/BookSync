package backEnd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class UserRegister {

    // Registra um novo usuário no sistema
    public static void registerUser(String name, String sex, String login, String password, int[] genreIds) {
        // Verifica se o usuário com o mesmo login já existe
        if (!userExists(login)) {
            // Se o usuário não existir, proceda com o registro
            String encryptedPassword = Services.encryptPassword(password);

            try (Connection connection = DatabaseConnector.connect()) {
                // Insere o usuário na tabela users
                String insertUserQuery = "INSERT INTO users (name, sex, admin, login, password) VALUES (?, ?, 0, ?, ?)";
                try (PreparedStatement insertUserStatement = connection.prepareStatement(insertUserQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertUserStatement.setString(1, name);
                    insertUserStatement.setString(2, sex);
                    insertUserStatement.setString(3, login);
                    insertUserStatement.setString(4, encryptedPassword);

                    int affectedRows = insertUserStatement.executeUpdate();

                    if (affectedRows > 0) {
                        // Usuário inserido com sucesso, obtém o ID de usuário gerado
                        ResultSet generatedKeys = insertUserStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int userId = generatedKeys.getInt(1);

                            // Insere os genre_ids na tabela users_genres
                            String insertGenresQuery = "INSERT INTO users_genres (user_id, genre_id) VALUES (?, ?)";
                            try (PreparedStatement insertGenresStatement = connection.prepareStatement(insertGenresQuery)) {
                                for (int genreId : genreIds) {
                                    insertGenresStatement.setInt(1, userId);
                                    insertGenresStatement.setInt(2, genreId);
                                    insertGenresStatement.executeUpdate();
                                }
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "Usuário com o mesmo login já existe. Falha no registro.");
        }
        JOptionPane.showMessageDialog(null, "Usuário registrado com sucesso!");
    }

    // Verifica se o usuário já existe com base no login
    private static boolean userExists(String login) {
        try (Connection connection = DatabaseConnector.connect()) {
            String checkUserQuery = "SELECT COUNT(*) FROM users WHERE login = ?";
            try (PreparedStatement checkUserStatement = connection.prepareStatement(checkUserQuery)) {
                checkUserStatement.setString(1, login);
                ResultSet resultSet = checkUserStatement.executeQuery();

                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Método principal para exemplo de uso
    public static void main(String[] args) {
        // Exemplo de uso:
        String name = "John Doe";
        String sex = "Male";
        String login = "john_doe";
        String password = "securepassword";
        int[] genreIds = {1, 2};

        registerUser(name, sex, login, password, genreIds);
    }
}
