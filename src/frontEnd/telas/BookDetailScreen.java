package frontEnd.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import backEnd.BookHandler;
import backEnd.User;

public class BookDetailScreen extends JFrame {

    // Inicializa a tela de detalhes do livro com base nos dados fornecidos
    public BookDetailScreen(Map<String, Object> data, User user) {
        setTitle("Detalhes do Livro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        // Extração de dados do mapa
        @SuppressWarnings("unchecked")
        List<Map<String, String>> reviews = (List<Map<String, String>>) data.get("reviews");
        String author = (String) data.get("author");
        double averageRating = (double) data.get("review_score");
        int reviewCount = (int) data.get("qtd_reviews");
        String publisher = (String) data.get("publisher");
        String coverPath = (String) data.get("cover");
        String synopsis = (String) data.get("synopsis");
        String title = (String) data.get("title");
        String genre = (String) data.get("genre_name");

        // Componentes
        JButton backButton = new JButton("Voltar para Lista de Livros");
        backButton.addActionListener(e -> {
            BookListScreen.main(null, user);
            dispose();
        });

        JLabel coverLabel = new JLabel(new ImageIcon(coverPath));

        JLabel detailLabel = new JLabel("<html><b>Título: " + title + "</b><br>" +
                "Autor: " + author + "</b><br>" +
                "Avaliação Média: " + averageRating + "</b><br>" +
                "Número de Avaliações: " + reviewCount + "</b><br>" +
                "Editora: " + publisher + "</b><br>" +
                "Gênero: " + genre + "</b><br>" +
                "Sinopse:\n" + synopsis + "</b><br>" + "</html>");

        JPanel reviewsPanel = new JPanel(new GridLayout(reviews.size(), 1));
        for (Map<String, String> review : reviews) {
            String userName = review.get("user_name");
            String bookReview = review.get("book_review");
            JLabel reviewLabel = new JLabel("<html><b>" + userName + "</b><br>" + bookReview + "</html>");
            reviewLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            reviewsPanel.add(reviewLabel);
        }

        JScrollPane reviewsScrollPane = new JScrollPane(reviewsPanel);

        // Layout
        setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(backButton);

        JPanel coverPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        coverPanel.add(coverLabel);

        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        detailsPanel.add(coverLabel);
        detailsPanel.add(detailLabel);

        // Adicionando componentes ao frame
        add(topPanel, BorderLayout.NORTH);
        add(coverPanel, BorderLayout.CENTER);
        add(detailsPanel, BorderLayout.CENTER);
        add(reviewsScrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Método principal para exibir a tela de detalhes do livro
    public static void main(String[] args, int book_id, User user) {
        SwingUtilities.invokeLater(() -> new BookDetailScreen(BookHandler.book_details(book_id), user));
    }
}
