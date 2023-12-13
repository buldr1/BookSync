package frontEnd.telas;

import javax.swing.*;
import backEnd.User;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

class RoundButton extends JButton {
    public RoundButton(String label) {
        super(label);
        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 30, 30);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 30, 30);
    }

    Shape shape;

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
        }
        return shape.contains(x, y);
    }
}

public class AdminMenuScreen {

    // Método principal que inicia o programa
    public static void main(String[] args, User user) {
        SwingUtilities.invokeLater(() -> createAndShowGUI(user)); // TODO: Refatorar para separar a criação e exibição da GUI
    }

    // Cria e exibe a interface gráfica
    private static void createAndShowGUI(User user) {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 650);
        frame.setLocationRelativeTo(null);
        
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/frontEnd/imgs/FundoBookSync.jpg");
                Image img = backgroundImage.getImage();
                g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new BorderLayout());
        panel.add(Box.createVerticalStrut(30), BorderLayout.NORTH);

        String greetingText = "<html><div style='font-family: Arial; font-size: 30px; font-weight: 400; line-height: 54px; letter-spacing: 0em; text-align: center; margin: 0;'>Olá, o que você deseja realizar hoje?</div></html>";
        JLabel greeting = new JLabel(greetingText);
        greeting.setForeground(Color.WHITE);
        greeting.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(greeting, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 60));
        buttonPanel.setOpaque(false);

        RoundButton newsButton = createStyledButton("Gerenciar livros", 300, 400, e -> {
            frontEnd.telas.BookmanagementScreen.main(null, user);
            frame.dispose();
        });

        RoundButton bookShowcaseButton = createStyledButton("Vitrine de Livros", 300, 400, e -> {
            BookListScreen.main(null, user);
            frame.dispose();
        });

        RoundButton addBookButton = createStyledButton("Adicionar Livro", 300, 400, e -> {
            AddBookScreen.main(null, user);
            frame.dispose();
        });

        RoundButton registerButton = createStyledButton("Cadastro Usuário", 300, 400, e -> {
            AddUserScreen.main(null, user);
            frame.dispose();
        });

        buttonPanel.add(newsButton);
        buttonPanel.add(bookShowcaseButton);
        buttonPanel.add(addBookButton);
        buttonPanel.add(registerButton);
        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    // Cria e configura um botão com um estilo específico
    private static RoundButton createStyledButton(String text, int width, int height, ActionListener actionListener) {
        RoundButton button = new RoundButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 25));
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(195, 145, 128));
        button.setForeground(Color.WHITE);
        button.addActionListener(actionListener);
        return button;
    }
}
