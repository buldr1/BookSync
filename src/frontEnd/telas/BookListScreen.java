package frontEnd.telas;

import javax.swing.*;

import backEnd.BookHandler;
import backEnd.User;

import java.awt.*;

public class BookListScreen extends JFrame {

    private static final int COVER_WIDTH = 100;
    private static final int COVER_HEIGHT = 150;

    // Inicializa a tela da lista de livros com base no usuário fornecido
    public BookListScreen(User user) {
        Object[][] books = (Object[][]) BookHandler.listBooks(this, user);

        setTitle("Lista de Livros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 1000);
        setLayout(new BorderLayout());

        // Cria um painel para a lista de livros
        JPanel bookPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Cria um botão acima da lista
        JButton backButton = new JButton("Voltar ao Menu");
        backButton.addActionListener(e -> {
            if (user.isAdmin()) {
                AdminMenuScreen.main(null, user);
            } else {
                MainMenuScreen.main(null, user);
            }
            dispose();
        });

        // Adiciona o botão acima da lista de livros
        add(backButton, BorderLayout.NORTH);

        // Cria entradas para cada livro
        for (Object[] book : books) {
            ImageIcon imageIcon = (ImageIcon) book[0];
            Image image = imageIcon.getImage().getScaledInstance(COVER_WIDTH, COVER_HEIGHT, Image.SCALE_SMOOTH);
            ImageIcon scaledImageIcon = new ImageIcon(image);

            String title = (String) book[1];
            JButton detailsButton = (JButton) book[5];

            JLabel imageLabel = new JLabel(scaledImageIcon);
            JLabel titleLabel = new JLabel(title);

            // Cria um painel para cada entrada de livro
            JPanel bookEntryPanel = new JPanel(new BorderLayout());
            bookEntryPanel.setPreferredSize(new Dimension(600, 180));
            bookEntryPanel.setMaximumSize(new Dimension(Short.MAX_VALUE, 180));
            bookEntryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            bookEntryPanel.add(imageLabel, BorderLayout.WEST);
            bookEntryPanel.add(titleLabel, BorderLayout.CENTER);
            bookEntryPanel.add(detailsButton, BorderLayout.EAST);

            // Adiciona uma linha separadora entre as linhas
            if (gbc.gridy > 0) {
                JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
                bookPanel.add(separator, gbc);
                gbc.gridy++;
            }

            bookPanel.add(bookEntryPanel, gbc);
            gbc.gridy++;
        }

        JScrollPane scrollPane = new JScrollPane(bookPanel);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // Método principal para exibir a tela da lista de livros
    public static void main(String[] args, User user) {
        SwingUtilities.invokeLater(() -> new BookListScreen(user));
    }
}