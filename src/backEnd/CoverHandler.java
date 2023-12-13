package backEnd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;

public class CoverHandler {

    // Obtém os livros não públicos e suas informações de capa para exibição
    // Retorna uma matriz de objetos representando os detalhes dos livros
    public static Object[][] getNonPublicBooks() throws IOException {
        Object[][] data = null;
        String[] columns = { "Título", "Capa Adicionada", "Imagem" };
        String queryCount = "SELECT Count(*) FROM books WHERE public = false";
        String query = "SELECT title, cover FROM books WHERE public = false";
        int rowCount = 0;

        try (Connection connection = DatabaseConnector.connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(queryCount)) {
            while (resultSet.next()) {
                rowCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (Connection connection = DatabaseConnector.connect();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query)) {

            data = new Object[rowCount][columns.length];
            int row = 0;

            while (resultSet.next()) {
                data[row][0] = resultSet.getString("title");

                InputStream inputStream = resultSet.getBinaryStream("cover");
                String tempFilePath = saveImageAsTempFile(inputStream);

                data[row][2] = new ImageIcon(tempFilePath);
                if (data[row][2].toString().length() > 0){
                    data[row][1] = true;
                }else{
                    data[row][1] = false;
                }
                row++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

    // Atualiza a capa do livro no banco de dados
    // Parâmetros:
    // - filePath: caminho do arquivo da nova capa
    // - bookName: nome do livro para o qual a capa será atualizada
    public static void updateBookCover(String filePath, String bookName) throws IOException {

        if (Services.isValidJpeg(filePath)) {

            try (Connection connection = DatabaseConnector.connect()) {
                // Encontra o ID do livro
                int bookId = -1;
                String query = "SELECT id FROM books WHERE title = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setString(1, bookName);
                    ResultSet resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        bookId = resultSet.getInt("id");
                    }
                }

                if (bookId != -1) {
                    // Atualiza a capa e define o livro como público
                    String updateQuery = "UPDATE books SET cover = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                        File file = new File(filePath);
                        try (InputStream inputStream = new FileInputStream(file)) {
                            updateStatement.setBlob(1, inputStream);
                            updateStatement.setInt(2, bookId);
                            updateStatement.executeUpdate();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    System.out.println("Livro não encontrado.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // Salva uma imagem como arquivo temporário
    // Parâmetro:
    // - inputStream: fluxo de entrada da imagem
    // Retorna o caminho do arquivo temporário
    public static String saveImageAsTempFile(InputStream inputStream) {
        if (inputStream == null){
            return "";
        }
        String tempFilePath = null;
        try {
            File tempFile = File.createTempFile("tempImage", ".jpeg");
            try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            tempFile.deleteOnExit();
            tempFilePath = tempFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempFilePath;
    }

    public static void main(String[] args) throws IOException {
        // Uso de exemplo
    }
}
