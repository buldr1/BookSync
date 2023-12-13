package backEnd;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private int id;
    private String name;
    private String sex;
    private boolean admin;
    private String login;

    // Retorna o ID do usuário
    public int getId() {
        return id;
    }

    // Retorna o nome do usuário
    public String getName() {
        return name;
    }

    // Retorna o sexo do usuário
    public String getSex() {
        return sex;
    }

    // Verifica se o usuário é um administrador
    public boolean isAdmin() {
        return admin;
    }

    // Retorna o login do usuário
    public String getLogin() {
        return login;
    }
    
    // Define os valores a partir de uma linha do banco de dados
    public void setUserFromDBRow(ResultSet dbRow) throws SQLException {
        this.id = dbRow.getInt("id");
        this.name = dbRow.getString("name");
        this.sex = dbRow.getString("sex");
        this.admin = dbRow.getBoolean("admin");
        this.login = dbRow.getString("login");
    }

     // Sobrescreve o método toString()
     @Override
     public String toString() {
         return "User{" +
                 "id=" + id +
                 ", name='" + name + '\'' +
                 ", sex='" + sex + '\'' +
                 ", admin=" + admin +
                 ", login='" + login + '\'' +
                 '}';
     }
}
