package frontEnd.telas;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.*;

import backEnd.Services.RoundedBorder;
import backEnd.User;

public class BookmanagementScreen extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private Font font2 = new Font("Arial", Font.PLAIN, 18);
    JTextField tfAuthor, tfPublisher, tfSynopsis;

    // Método principal para iniciar a tela de gerenciamento de livros
    public static void main(String[] args, User user) {
        SwingUtilities.invokeLater(() -> {
            try {
                new BookmanagementScreen(backEnd.CoverHandler.getNonPublicBooks(), user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // Construtor para a tela de gerenciamento de livros
    public BookmanagementScreen(Object[][] data, User user) {
        setTitle("Gerenciamento de Livros");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);

        // Criação da tabela
        String[] columns = {"Título", "Capa Adicionada", "Imagem"};
        model = new DefaultTableModel(data, columns) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 1 ? Boolean.class : columnIndex == 2 ? ImageIcon.class : Object.class;
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 2; // Permite edição apenas na coluna "Imagem"
            }
        };
        table = new JTable(model);

        // Adiciona o renderizador de imagem para a coluna "Imagem"
        table.getColumnModel().getColumn(2).setCellRenderer(new ImageRenderer());

        // Adiciona o editor de imagem para a coluna "Imagem"
        table.getColumnModel().getColumn(2).setCellEditor(new ImageEditor());

        // Cria campos de texto adicionais para autor, editora e sinopse
        tfAuthor = createRoundedTextField("Autor", 10);
        tfPublisher = createRoundedTextField("Editora", 10);
        tfSynopsis = new JTextField();

        // Cria um painel para os campos adicionais
        JPanel detailsPanel = new JPanel(new GridLayout(3, 2));
        detailsPanel.setOpaque(false);
        detailsPanel.add(new JLabel("Autor:"));
        detailsPanel.add(tfAuthor);
        detailsPanel.add(new JLabel("Editora:"));
        detailsPanel.add(tfPublisher);
        detailsPanel.add(new JLabel("Sinopse:"));
        detailsPanel.add(tfSynopsis);

        // Adiciona o painel ao frame
        add(detailsPanel, BorderLayout.SOUTH);

        // Adiciona botões
        JButton addButton = new JButton("Adicionar Capa");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowInView = table.getSelectedRow();
                int rowInModel = table.convertRowIndexToModel(rowInView);

                if (rowInModel != -1) {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setFileFilter(new FileNameExtensionFilter("Imagens", "jpg", "png", "jpeg"));

                    int result = fileChooser.showOpenDialog(BookmanagementScreen.this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                        model.setValueAt(true, rowInModel, 1);
                        model.setValueAt(new ImageIcon(imagePath), rowInModel, 2);
                        model.fireTableDataChanged();
                        try {
                            backEnd.CoverHandler.updateBookCover(imagePath, model.getValueAt(rowInModel,0).toString());
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        dispose();
                        frontEnd.telas.BookmanagementScreen.main(null, user);
                    }
                }
            }
        });

        JButton removeButton = new JButton("Remover Capa");
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowInView = table.getSelectedRow();
                int rowInModel = table.convertRowIndexToModel(rowInView);

                if (rowInModel != -1) {
                    model.setValueAt(false, rowInModel, 1);
                    model.setValueAt(null, rowInModel, 2);
                    model.fireTableDataChanged();
                }
            }
        });

        JButton removeBookButton = new JButton("Remover Livro");
        removeBookButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int rowInView = table.getSelectedRow();
                int rowInModel = table.convertRowIndexToModel(rowInView);

                if (rowInModel != -1) {
                    backEnd.Services.removeBook(model.getValueAt(rowInModel,0).toString());
                    dispose();
                    frontEnd.telas.BookmanagementScreen.main(null, user);
                }
            }
        });

        JButton publishBookButton = new JButton("Publicar Livro");
        publishBookButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int rowInView = table.getSelectedRow();
                int rowInModel = table.convertRowIndexToModel(rowInView);
                backEnd.BookHandler.publishBook(model.getValueAt(rowInModel,0).toString(), tfAuthor.getText(), tfPublisher.getText(), tfSynopsis.getText());
                dispose();
                frontEnd.telas.BookmanagementScreen.main(null, user);
            }
        });
        JButton returnButton = new JButton("Voltar para o Menu");
        returnButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
                frontEnd.telas.AdminMenuScreen.main(null, user);
            }
        });

        // Adiciona os componentes ao frame
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(removeBookButton);
        buttonPanel.add(publishBookButton);
        buttonPanel.add(returnButton);
        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        setVisible(true);
        
    }

    // Método para criar um JTextField com borda arredondada
    JTextField createRoundedTextField(String placeholder, int radius) {
            JTextField tf = new JTextField(placeholder);
            tf.setFont(font2);
            tf.setOpaque(true);
            tf.setBorder(new RoundedBorder(radius));
            return tf;
        }

    // Renderizador TableCellRenderer para imagens
    class ImageRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();
            if (value instanceof ImageIcon) {
                label.setIcon((ImageIcon) value);
            }
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }

    // Editor TableCellEditor personalizado para abrir imagens
    class ImageEditor extends AbstractCellEditor implements TableCellEditor {
        private JButton button;
        private ImageIcon image;

        public ImageEditor() {
            button = new JButton();
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (image != null) {
                        ImageIconViewer viewer = new ImageIconViewer(image);
                        viewer.setVisible(true);
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (value instanceof ImageIcon) {
                image = (ImageIcon) value;
                button.setIcon(image);
            } else {
                image = null;
                button.setIcon(null);
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return image;
        }
    }

    // JFrame simples para exibir ImageIcon em uma nova janela
    class ImageIconViewer extends JFrame {
        public ImageIconViewer(ImageIcon image) {
            setTitle("Visualizar Imagem");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 400);
            JLabel label = new JLabel(image);
            label.setHorizontalAlignment(SwingConstants.CENTER);
            add(label);
        }
    }

}







