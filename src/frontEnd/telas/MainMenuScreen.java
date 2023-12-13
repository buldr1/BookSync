package frontEnd.telas;

import javax.swing.*;

import backEnd.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

public class MainMenuScreen {

    // Inicializa a tela principal do menu
    public static void main(String[] args, User user) {
        SwingUtilities.invokeLater(() -> createAndShowGUI(user));
    }

    // Cria e exibe a interface gráfica do menu principal
    private static void createAndShowGUI(User user) {
        JFrame frame = new JFrame("Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 650);
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

        RoundButton bookShowcaseButton = createStyledButton("Vitrine de Livros", 300, 400);
        RoundButton addBookButton = createStyledButton("Adicionar Livro", 300, 400);

        bookShowcaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frontEnd.telas.BookListScreen.main(null, user);
                frame.dispose();
            }
        });

        addBookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddBookScreen.main(null, user);
                frame.dispose();
            }
        });

        buttonPanel.add(bookShowcaseButton);
        buttonPanel.add(addBookButton);

        panel.add(buttonPanel, BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.setVisible(true);
    }

    // Cria botões redondos estilizados para a interface
    private static RoundButton createStyledButton(String text, int width, int height) {
        RoundButton button = new RoundButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 25));
        button.setPreferredSize(new Dimension(width, height));
        button.setBackground(new Color(195, 145, 128));
        button.setForeground(Color.WHITE);
        return button;
    }
}

// Classe RoundButton - botões redondos estilizados
class RoundButton extends JButton {

    public RoundButton(String label) {
        super(label);
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(new Color(150, 150, 150));
        } else {
            g.setColor(getBackground());
        }
        g.fillRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRoundRect(0, 0, getSize().width - 1, getSize().height - 1, 20, 20);
    }

    @Override
    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
        }
        return shape.contains(x, y);
    }

    private Shape shape;
}
