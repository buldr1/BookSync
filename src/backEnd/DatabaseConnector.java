package backEnd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // Esta classe gerencia as credenciais do banco de dados e fornece uma função para se conectar ao banco

    private static final String URL = "jdbc:mysql://localhost:3306/booksync";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "booksync123";

    // Estabelece uma conexão com o banco de dados
    // Retorna uma conexão ativa com o banco de dados
    public static Connection connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Falha ao conectar ao banco de dados.");
        }
    }
}
