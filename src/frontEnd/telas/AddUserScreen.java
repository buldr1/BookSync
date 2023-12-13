package frontEnd.telas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import backEnd.GenreHandler;
import backEnd.User;
import backEnd.UserRegister;

public class AddUserScreen extends JFrame{
    private Font font = new Font("Arial", Font.BOLD, 12);
    private Font font2 = new Font("Arial", Font.PLAIN, 14);
    private Font font3 = new Font("Baskerville", Font.PLAIN, 16);

    private JTextField userNameField;
    private JTextField emailField;
    private JTextField idadeField;
    private JComboBox<String> genderComboBox;
    private JComboBox<String> favoriteGenreComboBox;
    private JPasswordField passwordField;

    public static void main(String[] args, User user) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            AddUserScreen AddUserScreen = new AddUserScreen();
            AddUserScreen.desenhar(user);
        });
    }

    public AddUserScreen() {
        setTitle("Cadastro de Usuário");
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

    public void desenhar(User user) {
        BackgroundPanel mainPanel = new BackgroundPanel(
                "src\\frontEnd\\imgs\\FundoBookSync.jpg");

        JLabel lbImagemLogo = new JLabel();
        ImageIcon imagemLogo = new ImageIcon("src\\frontEnd\\imgs\\BooksyncImagem.PNG");
        lbImagemLogo.setIcon(imagemLogo);

        String lbBemVindo = "<html><center>Seja bem-vindo Admin!<br>Para realizar um cadastro, insira as informacoes do usuario:</center></html>";
        JLabel lbDescricao = new JLabel(lbBemVindo);
        lbDescricao.setFont(font3);
        lbDescricao.setForeground(Color.WHITE);
        lbDescricao.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel userNameLabel = new JLabel("Nome Completo:");
        userNameLabel.setFont(font);
        userNameLabel.setForeground(Color.WHITE);

        userNameField = new JTextField();
        userNameField.setFont(font2);
        userNameField.setForeground(Color.WHITE);
        Dimension novaDimensao = new Dimension(300, 35);
        userNameField.setPreferredSize(novaDimensao);
        userNameField.setOpaque(false);
        userNameField.setBorder(new RoundedBorder(10));

        JLabel emailLabel = new JLabel("Login:");
        emailLabel.setFont(font);
        emailLabel.setForeground(Color.WHITE);

        emailField = new JTextField();
        emailField.setFont(font2);
        emailField.setForeground(Color.WHITE);
        emailField.setPreferredSize(novaDimensao);
        emailField.setOpaque(false);
        emailField.setBorder(new RoundedBorder(10));

        JLabel ageLabel = new JLabel("Idade:");
        ageLabel.setFont(font);
        ageLabel.setForeground(Color.WHITE);

        idadeField = new JTextField();
        idadeField.setFont(font2);
        idadeField.setForeground(Color.WHITE);
        idadeField.setPreferredSize(novaDimensao);
        idadeField.setOpaque(false);
        idadeField.setBorder(new RoundedBorder(10));

        JLabel genderLabel = new JLabel("Sexo:");
        genderLabel.setFont(font);
        genderLabel.setForeground(Color.WHITE);

        String[] sexos = { "Masculino", "Feminino", "Outro" };
        genderComboBox = new JComboBox<>(sexos);
        genderComboBox.setFont(font2);
        genderComboBox.setForeground(Color.WHITE);
        genderComboBox.setPreferredSize(novaDimensao);
        genderComboBox.setOpaque(false);
        genderComboBox.setBorder(new RoundedBorder(10));
        genderComboBox.setUI(new CustomComboBoxUI());

        JLabel favoriteGenreLabel = new JLabel("Genero Favorito:");
        favoriteGenreLabel.setFont(font);
        favoriteGenreLabel.setForeground(Color.WHITE);

        ArrayList<String> generos = GenreHandler.listGenres();
        favoriteGenreComboBox = new JComboBox<>(generos.toArray(new String[0]));
        favoriteGenreComboBox.setFont(font2);
        favoriteGenreComboBox.setForeground(Color.WHITE);
        favoriteGenreComboBox.setPreferredSize(novaDimensao);
        favoriteGenreComboBox.setOpaque(false);
        favoriteGenreComboBox.setBorder(new RoundedBorder(10));
        favoriteGenreComboBox.setUI(new CustomComboBoxUI());

        JLabel passwordLabel = new JLabel("Insira uma senha:");
        passwordLabel.setFont(font);
        passwordLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();
        passwordField.setFont(font2);
        passwordField.setForeground(Color.WHITE);
        passwordField.setPreferredSize(novaDimensao);
        passwordField.setOpaque(false);
        passwordField.setBorder(new RoundedBorder(10));
        add(passwordField);

        JButton registerButton = btnCadastrar();
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = userNameField.getText();
                String login = emailField.getText();
                String sex = (String) genderComboBox.getSelectedItem();
                String password = new String(passwordField.getPassword());
                // Assuming GenreHandler.getGenre(name) returns genre ID based on name
                int[] genreIds = { GenreHandler.getGenre((String) favoriteGenreComboBox.getSelectedItem()) };

                // Call the UserRegister method to register the user
                UserRegister.registerUser(name, sex, login, password, genreIds);

                if (user.isAdmin()) {
                    AdminMenuScreen.main(null, user);
                } else {
                    MainMenuScreen.main(null, user);
                }
                dispose();



            }
        });

        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBorder(BorderFactory.createEmptyBorder(40, 50, 50, 50));
        painel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 2, 0, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        gbc.insets = new Insets(0, 2, 0, 15);
        gbc.gridy = 0;
        painel.add(lbDescricao, gbc);

        gbc.insets = new Insets(10, 2, 0, 15);
        gbc.gridy++;
        painel.add(userNameLabel, gbc);

        gbc.gridy++;
        painel.add(userNameField, gbc);

        gbc.gridy++;
        painel.add(emailLabel, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        painel.add(emailField, gbc);

        gbc.gridy++;
        painel.add(ageLabel, gbc);

        gbc.gridy++;
        painel.add(idadeField, gbc);

        gbc.gridy++;
        painel.add(genderLabel, gbc);

        gbc.gridy++;
        painel.add(genderComboBox, gbc);

        gbc.gridy++;
        painel.add(favoriteGenreLabel, gbc);

        gbc.gridy++;
        painel.add(favoriteGenreComboBox, gbc);

        gbc.gridy++;
        painel.add(passwordLabel, gbc);

        gbc.gridy++;
        painel.add(passwordField, gbc);

        gbc.gridy++;
        painel.add(registerButton, gbc);

        JPanel formBotoesPanel = new JPanel(new BorderLayout());
        formBotoesPanel.setOpaque(false);
        formBotoesPanel.add(painel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(lbImagemLogo, BorderLayout.WEST);
        contentPanel.add(formBotoesPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);

        // Caixas de Opções, mudança de cor
        passwordField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (new String(passwordField.getPassword()).equals("Senha")) {
                    passwordField.setText("");
                    passwordField.setForeground(Color.BLACK); // Restaura a cor preta
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (new String(passwordField.getPassword()).isEmpty()) {
                    passwordField.setText("Senha");
                    passwordField.setForeground(Color.GRAY);
                }
            }
        });

    }

    // Botão e Design
    JButton btnCadastrar() {
        JButton registerButton = new RoundedButton("Cadastrar", 50);
        registerButton.setFont(font);
        registerButton.setForeground(Color.WHITE);
        registerButton.setBackground(new Color(249, 161, 61));

        // Definação de dimensões do botão
        registerButton.setPreferredSize(new Dimension(100, 40));
        registerButton.setMinimumSize(new Dimension(100, 40));
        registerButton.setMaximumSize(new Dimension(100, 40));

        return registerButton;
    }

    // Design botão de alternativa
    class CustomComboBoxUI extends BasicComboBoxUI {
        private boolean comboBoxHasFocus = false;

        @Override
        protected JButton createArrowButton() {
            return new JButton() {
                @Override
                public int getWidth() {
                    return 0;
                }
            };
        }

        @Override
        protected ComboPopup createPopup() {
            return new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    return new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
                }
            };
        }

        @Override
        @SuppressWarnings("unchecked")
        public void installDefaults() {
            super.installDefaults();
            addComboBoxFocusListener(); // Adiciona o FocusListener à JComboBox
            comboBox.setRenderer(createRenderer()); // Configura o ListCellRenderer
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(new Color(0, 0, 0, 0));
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }

        @Override
        @SuppressWarnings({"unchecked", "rawtypes"})
        protected ListCellRenderer createRenderer() {
            return new BasicComboBoxRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {
                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                    if (comboBoxHasFocus) {
                        // Defina a cor do texto quando a JComboBox estiver aberta
                        setForeground(Color.BLACK);
                    } else {
                        // Defina a cor do texto quando a JComboBox estiver fechada
                        setForeground(Color.WHITE);
                    }

                    setOpaque(false);
                    return this;
                }

                @Override
                public void paintComponent(Graphics g) {
                    setBackground(new Color(0, 0, 0, 0));
                    super.paintComponent(g);
                }
            };
        }

        // Adiciona um FocusListener à JComboBox
        private void addComboBoxFocusListener() {
            comboBox.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    comboBoxHasFocus = true;
                    comboBox.repaint();
                }

                @Override
                public void focusLost(FocusEvent e) {
                    comboBoxHasFocus = false;
                    comboBox.repaint();
                }
            });
        }
    }

    // Fundo da tela, background
    class BackgroundPanel extends JPanel {
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

    // Classe RoundBorder caixas de botão
    class RoundedButton extends JButton {
        private int radius;

        public RoundedButton(String label, int radius) {
            super(label);
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
            super.paintComponent(g2d);
            g2d.dispose();
        }

        @Override
        protected void paintBorder(Graphics g) {
        }

        @Override
        public boolean contains(int x, int y) {
            return new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, radius, radius).contains(x, y);
        }
    }

    // Classe RoundBorder de texto
    class RoundedBorder extends AbstractBorder {
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
}


