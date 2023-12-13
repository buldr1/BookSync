package frontEnd.telas;

import javax.swing.*;
import backEnd.User;

import backEnd.Services.RoundedBorder;

import backEnd.Services.BackgroundPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class LoginScreen extends JFrame {
    // Fontes do sistema
    private Font systemFont = new Font("Arial", Font.BOLD, 16);
    private Font plainSystemFont = new Font("Arial", Font.PLAIN, 18);
    private Font baskervilleFont = new Font("Baskerville", Font.PLAIN, 20);
    private Font italicSystemFont= new Font("Arial", Font.ITALIC, 14);

    JTextField loginField, passwordField;

    public static void main(String[] args,User user) {
        try {
            SwingUtilities.invokeLater(() -> {
                LoginScreen ga = new LoginScreen(user);
                ga.desenhar(user);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LoginScreen(User user) {
        configurarJanela(user);
    }

    // Especificações da Janela e Icone
    private void configurarJanela(User user) {
        this.setTitle("BookSync");
        this.setSize(1040, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        try {
            ImageIcon icon = new ImageIcon("src\\frontEnd\\imgs\\BooksyncImagem.PNG");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Background
    public void desenhar(User user) {
        // Configuração do panel de fundo
        BackgroundPanel backgroundPanel = new BackgroundPanel("src\\frontEnd\\imgs\\FundoBookSync.jpg");

        // Configuração da imagem do logo, esquerda
        JLabel imageLogoLabel = new JLabel();
        ImageIcon logoImage = new ImageIcon("src\\frontEnd\\imgs\\BooksyncImagem.PNG");
        imageLogoLabel.setIcon(logoImage);

        // Configuração da mensagem de boas-vindas
        String welcomeText = "<html><center>\nSEJA BEM-VINDO<br>Caso tenha cadastro, Acesse sua conta:</center></html>";
        JLabel descriptionLabel = new JLabel(welcomeText);
        descriptionLabel.setFont(baskervilleFont);
        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Configuração dos campos de login e senha
        JLabel loginLabel = new JLabel("  E-mail");
        loginLabel.setFont(systemFont);
        loginLabel.setForeground(Color.WHITE);

        loginField = new JTextField();
        loginField.setFont(plainSystemFont);
        loginField.setBackground(Color.WHITE);
        loginField.setForeground(Color.WHITE);
        Dimension novaDimensao = new Dimension(300, 40);
        loginField.setPreferredSize(novaDimensao);
        loginField.setOpaque(false);
        loginField.setBorder(new RoundedBorder(10));

        JLabel passwordLabel = new JLabel("  Senha");
        passwordLabel.setFont(systemFont);
        passwordLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();
        passwordField.setFont(plainSystemFont);
        passwordField.setBackground(Color.WHITE);
        passwordField.setForeground(Color.WHITE);
        passwordField.setPreferredSize(novaDimensao);
        passwordField.setOpaque(false);
        passwordField.setBorder(new RoundedBorder(10));

        // Configuração do texto de recuperação de senha
        JLabel forgotPasswordLabel = new JLabel(
                "<html><center>Esqueceu sua senha? Fale com o administrador.</center></html>");
        forgotPasswordLabel.setFont(italicSystemFont);
        forgotPasswordLabel.setForeground(Color.WHITE);

        // Configuração do botão de acesso ao sistema
        JButton accessButton = btnAcessar();
        accessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the values from loginField and passwordField
                String login = loginField.getText();
                String senha = passwordField.getText();

                // Call the login function from another file with login and senha values
                backEnd.LoginHandler.login(login, senha, user);

                // Close the current screen
                dispose();
            }
        });

        // Configuração do layout
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        panel.setOpaque(false);

        // Configuração da posição dos elementos
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 15, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;

        panel.add(descriptionLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(loginLabel, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(loginField, gbc);

        gbc.gridy = 3;
        gbc.gridwidth = 1;
        panel.add(passwordLabel, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(passwordField, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 15, 5);
        panel.add(accessButton, gbc);

        // Configuração panel com Botão Acessar
        JPanel accessPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        accessPanel.setOpaque(false);
        accessPanel.add(accessButton);

        // Configuração do panel de Botão Acessar e Painel EsqueceuSenha
        JPanel forgotPasswordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        forgotPasswordPanel.setOpaque(false);
        forgotPasswordPanel.add(forgotPasswordLabel);
        panel.add(accessPanel, gbc);
        accessPanel.add(forgotPasswordPanel);

        // Painel de Posição do Elementos
        JPanel positionPanel = new JPanel(new BorderLayout());
        positionPanel.setOpaque(false);
        positionPanel.add(panel, BorderLayout.CENTER);

        // Configuração de Posições do panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(imageLogoLabel, BorderLayout.WEST);
        contentPanel.add(positionPanel, BorderLayout.CENTER);

        // Configuração do panel final - Visualização
        setContentPane(backgroundPanel);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Design - Caixa de Login
    JTextField createRoundedTextField(String placeholder, int roundedRadius) {
        JTextField tf = new JTextField(placeholder);
        tf.setFont(plainSystemFont);
        tf.setOpaque(true);
        tf.setBorder(new RoundedBorder(roundedRadius));
        return tf;
    }

    // Design - Caixa de Senha
    JPasswordField createRoundedPasswordField(String placeholder, int roundedRadius) {
        JPasswordField tf = new JPasswordField(placeholder);
        tf.setFont(plainSystemFont);
        tf.setOpaque(true);
        tf.setBorder(new RoundedBorder(roundedRadius));
        return tf;
    }

    // Desigin - Botão Acessar
    JButton btnAcessar() {
        RoundedButton accessButton = new RoundedButton("Acessar", 50);
        accessButton.setFont(systemFont);
        accessButton.setForeground(Color.WHITE);
        accessButton.setBackground(new Color(249, 161, 61));

        // Definação de dimensões do botão
        accessButton.setPreferredSize(new Dimension(100, 40));
        accessButton.setMinimumSize(new Dimension(100, 40));
        accessButton.setMaximumSize(new Dimension(100, 40));

        return accessButton;
    }

    // Classe RoundBorder - Caixas de Botão Acessar
    class RoundedButton extends JButton {
        private int roundedRadius;

        public RoundedButton(String label, int roundedRadius) {
            super(label);
            this.roundedRadius = roundedRadius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, roundedRadius, roundedRadius));
            super.paintComponent(g2d);
            g2d.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }

        @Override
        public boolean contains(int x, int y) {
            return new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, roundedRadius, roundedRadius).contains(x, y);
        }
    }

    
}
