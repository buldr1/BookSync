package frontEnd.telas;

import javax.swing.*;
import backEnd.BookHandler;
import backEnd.GenreHandler;
import backEnd.User;
import backEnd.Services.RoundedBorder;
import backEnd.Services.BackgroundPanel;
import java.awt.*;
import java.util.ArrayList;

public class AddBookScreen extends JFrame {

    private Font font = new Font("Arial", Font.BOLD, 16);
    private Font font2 = new Font("Arial", Font.PLAIN, 18);

    private JTextField tfTitle;
    private JComboBox<String> genreComboBox;
    private JSpinner scoreSpinner;
    private JTextArea reviewTextArea;

    // Método para exibir a tela de adicionar livro
    public static void main(String[] args, User user) {
        SwingUtilities.invokeLater(() -> {
            new AddBookScreen(user);
        });
    }

    // Construtor da tela de adicionar livro
    public AddBookScreen(User user) {
        configureWindow();

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lbTitle = new JLabel("Título:");
        lbTitle.setFont(font);
        lbTitle.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(lbTitle, gbc);

        tfTitle = createRoundedTextField("Digite o Título", 10);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        contentPanel.add(tfTitle, gbc);

        JLabel lbGenre = new JLabel("Gênero:");
        lbGenre.setFont(font);
        lbGenre.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        contentPanel.add(lbGenre, gbc);

        ArrayList<String> genres = GenreHandler.listGenres();
        genreComboBox = new JComboBox<>(genres.toArray(new String[0]));
        genreComboBox.setFont(font2);
        genreComboBox.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        contentPanel.add(genreComboBox, gbc);

        JLabel lbScore = new JLabel("Nota:");
        lbScore.setFont(font);
        lbScore.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        contentPanel.add(lbScore, gbc);

        SpinnerModel scoreModel = new SpinnerNumberModel(0, 0, 10, 1);
        scoreSpinner = new JSpinner(scoreModel);
        scoreSpinner.setFont(font2);
        scoreSpinner.setPreferredSize(new Dimension(80, 30));
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        contentPanel.add(scoreSpinner, gbc);

        JLabel lbReview = new JLabel("Avaliação:");
        lbReview.setFont(font);
        lbReview.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        contentPanel.add(lbReview, gbc);

        reviewTextArea = new JTextArea(5, 20);
        reviewTextArea.setFont(font2);
        reviewTextArea.setLineWrap(true);
        reviewTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reviewTextArea);
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        contentPanel.add(scrollPane, gbc);

        JButton addBookButton = new JButton("Adicionar Livro");
        addBookButton.setFont(font);
        addBookButton.setForeground(Color.WHITE);
        addBookButton.setBackground(new Color(249, 161, 61));
        addBookButton.addActionListener(e -> {
            String title = tfTitle.getText();
            String genre = genreComboBox.getSelectedItem().toString();
            double score = (int) scoreSpinner.getValue();
            String review = reviewTextArea.getText();

            BookHandler.updateOrInsertBook(title, genre, score, review, user);
            dispose();
            if (user.isAdmin()){
                AdminMenuScreen.main(null, user);
                dispose();
            }else{
                MainMenuScreen.main(null, user);
                dispose();
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(20, 5, 15, 5);
        contentPanel.add(addBookButton, gbc);

        JButton returnButton = new JButton("Voltar para o Menu");
        returnButton.setFont(font);
        returnButton.setForeground(Color.WHITE);
        returnButton.setBackground(new Color(249, 161, 61));
        returnButton.addActionListener(e -> {
            dispose();
            if (user.isAdmin()){
                AdminMenuScreen.main(null, user);
                dispose();
            }else{
                MainMenuScreen.main(null, user);
                dispose();
            }
        });
        gbc.gridx = 2;
        contentPanel.add(returnButton, gbc);

        BackgroundPanel mainPanel = new BackgroundPanel("src\\frontEnd\\imgs\\FundoTelaTotal.png");
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Configurações da janela
    private void configureWindow() {
        setTitle("Adicionar Livro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    // Cria um campo de texto arredondado
    JTextField createRoundedTextField(String placeholder, int radius) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(font2);
        tf.setOpaque(true);
        tf.setBorder(new RoundedBorder(radius));
        return tf;
    }
}
