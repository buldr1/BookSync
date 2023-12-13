package backEnd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import frontEnd.telas.BookDetailScreen;

public class BookHandler extends JFrame {
    // Atualiza ou insere um livro no banco de dados
    // Parâmetros:
    // - bookTitle: título do livro
    // - genre: gênero do livro
    // - score: pontuação do livro
    // - review: revisão do livro
    // - user: usuário que está inserindo a revisão
    public static void updateOrInsertBook(String bookTitle, String genre, double score, String review, User user) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Verifica se o livro existe
            String checkQuery = "SELECT * FROM books WHERE title = ?";
            try (PreparedStatement checkStatement = connection.prepareStatement(checkQuery)) {
                checkStatement.setString(1, bookTitle);
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {
                    // Livro existe, realiza a atualização
                    int bookId = resultSet.getInt("id");
                    int qtdReviews = resultSet.getInt("qtd_reviews");
                    double currentScore = resultSet.getDouble("review_score");

                    double newScore = ((currentScore * qtdReviews) + score) / (qtdReviews + 1);

                    String updateQuery = "UPDATE books SET review_score = ?, qtd_reviews = ? WHERE title = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        updateStatement.setDouble(1, newScore);
                        updateStatement.setInt(2, qtdReviews + 1);
                        updateStatement.setString(3, bookTitle);
                        updateStatement.executeUpdate();

                        addBookReview(connection, bookId, user.getId(), review);
                    }
                } else {
                    // Livro não existe, insere um novo livro
                    String insertQuery = "INSERT INTO books (title, qtd_reviews, review_score, public, genre_id) VALUES (?, ?, ?, ?, ?)";
                    try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                        insertStatement.setString(1, bookTitle);
                        insertStatement.setInt(2, 1);
                        insertStatement.setDouble(3, score);
                        insertStatement.setBoolean(4, false);
                        insertStatement.setInt(5, GenreHandler.getGenre(genre));
                        insertStatement.executeUpdate();

                        ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            int bookId = generatedKeys.getInt(1);

                            // Adiciona a revisão à tabela book_reviews
                            addBookReview(connection, bookId, user.getId(), review);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Publica um livro no banco de dados
    // Parâmetros:
    // - bookName: nome do livro
    // - author: autor do livro
    // - publisher: editora do livro
    // - synopsis: sinopse do livro
    public static void publishBook(String bookName, String author, String publisher, String synopsis) {
        try (Connection connection = DatabaseConnector.connect()) {
            String updateQuery = "UPDATE books SET public = true, author = ?, publisher = ?, synopsis = ? WHERE title = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setString(1, author);
                updateStatement.setString(2, publisher);
                updateStatement.setString(3, synopsis);
                updateStatement.setString(4, bookName);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lista os livros públicos disponíveis para exibição
    // Parâmetros:
    // - parentFrame: frame pai onde os botões de detalhes serão exibidos
    // - user: usuário atual
    // Retorna uma lista de arrays representando os detalhes dos livros
    public static Object listBooks(JFrame parentFrame, User user) {
        ArrayList<Object[]> bookList = new ArrayList<>();

        try (Connection connection = DatabaseConnector.connect()) {
            String query = "SELECT id, title, cover, author, publisher, synopsis FROM books WHERE public = true";
            try (PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {
                    int bookId = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    InputStream cover = resultSet.getBinaryStream("cover");
                    String author = resultSet.getString("author");
                    String publisher = resultSet.getString("publisher");
                    String synopsis = resultSet.getString("synopsis");

                    JButton detailsButton = createDetailsButton(bookId, parentFrame, user);
                    JButton authorButton = new JButton(author);
                    JButton publisherButton = new JButton(publisher);
                    JButton synopsisButton = new JButton(synopsis);

                    Object[] bookEntry = { new ImageIcon(CoverHandler.saveImageAsTempFile(cover)), title, authorButton,
                            publisherButton, synopsisButton, detailsButton };

                    bookList.add(bookEntry);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList.toArray(new Object[0][0]);
    }

    // Cria um botão de detalhes para um livro
    // Parâmetros:
    // - bookId: ID do livro
    // - parentFrame: frame pai onde o botão de detalhes será exibido
    // - user: usuário atual
    // Retorna um botão de detalhes
    private static JButton createDetailsButton(int bookId, JFrame parentFrame, User user) {
        JButton detailsButton = new JButton("Detalhes");

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BookDetailScreen.main(null, bookId, user);
                parentFrame.dispose();
            }
        });

        return detailsButton;
    }

    // Adiciona uma revisão de livro ao banco de dados
    // Parâmetros:
    // - connection: conexão com o banco de dados
    // - bookId: ID do livro
    // - userId: ID do usuário
    // - review: revisão do livro
    private static void addBookReview(Connection connection, int bookId, int userId, String review)
            throws SQLException {
        String reviewInsertQuery = "INSERT INTO book_reviews (book_id, user_id, book_review) VALUES (?, ?, ?)";
        try (PreparedStatement reviewInsertStatement = connection.prepareStatement(reviewInsertQuery)) {
            reviewInsertStatement.setInt(1, bookId);
            reviewInsertStatement.setInt(2, userId);
            reviewInsertStatement.setString(3, review);
            reviewInsertStatement.executeUpdate();
        }
    }

    // Obtém os detalhes de um livro
    // Parâmetro:
    // - bookId: ID do livro
    // Retorna um mapa com os detalhes do livro
    public static Map<String, Object> book_details(int bookId) {
        Map<String, Object> bookDetails = new HashMap<>();
        try (Connection connection = DatabaseConnector.connect()) {
            String bookQuery = "SELECT b.id AS book_id, b.title, b.author, b.publisher, b.qtd_reviews, b.review_score, b.cover, b.synopsis, g.name AS genre_name " +
                    "FROM books b " +
                    "INNER JOIN genres g ON b.genre_id = g.id " +
                    "WHERE b.id = ?";
            try (PreparedStatement bookStatement = connection.prepareStatement(bookQuery)) {
                bookStatement.setInt(1, bookId);
                ResultSet bookResult = bookStatement.executeQuery();

                if (bookResult.next()) {
                    bookDetails.put("title", bookResult.getString("title"));
                    bookDetails.put("author", bookResult.getString("author"));
                    bookDetails.put("publisher", bookResult.getString("publisher"));
                    bookDetails.put("qtd_reviews", bookResult.getInt("qtd_reviews"));
                    bookDetails.put("review_score", bookResult.getDouble("review_score"));
                    bookDetails.put("cover", CoverHandler.saveImageAsTempFile(bookResult.getBinaryStream("cover")));
                    bookDetails.put("synopsis", bookResult.getString("synopsis"));
                    bookDetails.put("genre_name", bookResult.getString("genre_name"));
                }
            }

            String reviewsQuery = "SELECT r.book_review, r.user_id, u.name AS user_name " +
                                  "FROM book_reviews r " +
                                  "INNER JOIN users u ON r.user_id = u.id " +
                                  "WHERE r.book_id = ?";
            try (PreparedStatement reviewsStatement = connection.prepareStatement(reviewsQuery)) {
                reviewsStatement.setInt(1, bookId);
                ResultSet reviewsResult = reviewsStatement.executeQuery();

                List<Map<String, Object>> reviewsList = new ArrayList<>();
                while (reviewsResult.next()) {
                    Map<String, Object> reviewInfo = new HashMap<>();
                    reviewInfo.put("book_review", reviewsResult.getString("book_review"));
                    reviewInfo.put("user_name", reviewsResult.getString("user_name"));
                    reviewsList.add(reviewInfo);
                }
                bookDetails.put("reviews", reviewsList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookDetails;
    }

    public static void main(String[] args) {

    }
}
