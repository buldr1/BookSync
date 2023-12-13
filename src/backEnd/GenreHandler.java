package backEnd;

import java.sql.*;
import java.util.ArrayList;

public class GenreHandler {

    // Esta função lista os gêneros disponíveis no banco de dados
    // Retorna um ArrayList contendo os nomes dos gêneros
    public static ArrayList<String> listGenres() {
        ArrayList<String> genres = new ArrayList<>();
        String query = "SELECT name FROM genres";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String genre = resultSet.getString("name");
                genres.add(genre);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genres;
    }

    // Esta função lista os livros associados a um gênero específico
    // Recebe o nome do gênero como parâmetro
    // Retorna um ArrayList contendo os títulos dos livros relacionados ao gênero
    public static ArrayList<String> listBooksByGenre(String genreName) {
        ArrayList<String> books = new ArrayList<>();
        String query = "SELECT title FROM books WHERE genre_id = (SELECT id FROM genres WHERE name = ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, genreName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String bookTitle = resultSet.getString("title");
                    books.add(bookTitle);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Esta função lista os usuários associados a um gênero específico
    // Recebe o nome do gênero como parâmetro
    // Retorna um ArrayList contendo os nomes dos usuários relacionados ao gênero
    public static ArrayList<String> listUsersByGenre(String genreName) {
        ArrayList<String> users = new ArrayList<>();
        String query = "SELECT name FROM users WHERE genre_id = (SELECT id FROM genres WHERE name = ?)";

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, genreName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String userName = resultSet.getString("name");
                    users.add(userName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Esta função obtém o ID de um gênero específico
    // Recebe o nome do gênero como parâmetro
    // Retorna o ID do gênero
    public static int getGenre(String genreName) {
        String query = "SELECT id FROM genres WHERE name = ?";
        int genreId = 0;

        try (Connection connection = DatabaseConnector.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, genreName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    genreId = resultSet.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return genreId;
    }
}
