package backEnd;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;

public class Services {

    // Esta classe contém funções genéricas utilizadas em várias partes do programa

    public static String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());

            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hashedBytes) {
                stringBuilder.append(String.format("%02x", b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao criptografar a senha.");
        }
    }

    public static void removeUser(String login) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Remove entradas da tabela users_genres
            String removeUserGenresQuery = "DELETE FROM users_genres WHERE user_id = (SELECT id FROM users WHERE login = ?)";
            try (PreparedStatement removeUserGenresStatement = connection.prepareStatement(removeUserGenresQuery)) {
                removeUserGenresStatement.setString(1, login);
                removeUserGenresStatement.executeUpdate();
            }

            // Remove entradas da tabela users
            String removeUserQuery = "DELETE FROM users WHERE login = ?";
            try (PreparedStatement removeUserStatement = connection.prepareStatement(removeUserQuery)) {
                removeUserStatement.setString(1, login);
                removeUserStatement.executeUpdate();
                System.out.println("Usuário removido com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeBook(String bookName) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Remove entradas da tabela books_genres
            String removeBookGenresQuery = "DELETE FROM books_genres WHERE book_id = (SELECT id FROM books WHERE title = ?)";
            try (PreparedStatement removeBookGenresStatement = connection.prepareStatement(removeBookGenresQuery)) {
                removeBookGenresStatement.setString(1, bookName);
                removeBookGenresStatement.executeUpdate();
            }

            // Remove entradas da tabela books
            String removeBookQuery = "DELETE FROM books WHERE title = ?";
            try (PreparedStatement removeBookStatement = connection.prepareStatement(removeBookQuery)) {
                removeBookStatement.setString(1, bookName);
                removeBookStatement.executeUpdate();
                System.out.println("Livro removido com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void removeGenre(int genreId) {
        try (Connection connection = DatabaseConnector.connect()) {
            // Remove entradas da tabela users_genres
            String removeGenreUsersQuery = "DELETE FROM users_genres WHERE genre_id = ?";
            try (PreparedStatement removeGenreUsersStatement = connection.prepareStatement(removeGenreUsersQuery)) {
                removeGenreUsersStatement.setInt(1, genreId);
                removeGenreUsersStatement.executeUpdate();
            }

            // Remove entradas da tabela books_genres
            String removeGenreBooksQuery = "DELETE FROM books_genres WHERE genre_id = ?";
            try (PreparedStatement removeGenreBooksStatement = connection.prepareStatement(removeGenreBooksQuery)) {
                removeGenreBooksStatement.setInt(1, genreId);
                removeGenreBooksStatement.executeUpdate();
            }

            // Remove entrada da tabela genres
            String removeGenreQuery = "DELETE FROM genres WHERE id = ?";
            try (PreparedStatement removeGenreStatement = connection.prepareStatement(removeGenreQuery)) {
                removeGenreStatement.setInt(1, genreId);
                removeGenreStatement.executeUpdate();
                System.out.println("Gênero removido com sucesso.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidJpeg(String filePath) {
        // Verifica se o arquivo existe
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println("O arquivo não existe ou não é um arquivo válido.");
            return false;
        }

        // Verifica se o arquivo é um JPEG
        String fileName = file.getName();
        if (!fileName.toLowerCase().endsWith(".jpg") && !fileName.toLowerCase().endsWith(".jpeg")) {
            System.out.println("O arquivo não é um JPEG.");
            return false;
        }

        // Verifica se o tamanho do arquivo está dentro dos limites de um MEDIUMBLOB
        long fileSize = file.length(); // Tamanho em bytes
        long maxSize = 16777215; // Tamanho máximo para um MEDIUMBLOB em bytes

        if (fileSize > maxSize) {
            System.out.println("O tamanho do arquivo excede o tamanho máximo para um MEDIUMBLOB.");
            return false;
        }

        return true;
    }

    // Classe RoundBorder - Caixas de Texto
    public static class RoundedBorder extends AbstractBorder {
        private int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.WHITE);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.draw(new RoundRectangle2D.Double(x, y, width - 1, height - 1, radius, radius));
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius;
            return insets;
        }
    }

    // Fundo da tela - Background
    public static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            this.backgroundImage = new ImageIcon(imagePath).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
