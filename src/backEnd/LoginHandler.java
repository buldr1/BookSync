package backEnd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

public class LoginHandler {

    // Esta função realiza o processo de login do usuário
    // Recebe um nome de usuário, uma senha e um objeto do tipo User como parâmetros
    // Não retorna valor
    public static Void login(String username, String password, User user) {
        String encryptedPassword = Services.encryptPassword(password);

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT * FROM users WHERE login = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, encryptedPassword);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user.setUserFromDBRow(resultSet);
                        if (user.isAdmin()) {
                            frontEnd.telas.AdminMenuScreen.main(null, user);
                        } else {
                            frontEnd.telas.MainMenuScreen.main(null, user);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Login ou Senha Incorretos, por favor tente novamente!");
                        frontEnd.telas.LoginScreen.main(null, user);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //DEBUG
    public static void main(String[] args) {

    }
}
