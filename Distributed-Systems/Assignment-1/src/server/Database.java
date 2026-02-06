package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class Database {

    /*
     * Utilitário simples para obter uma Conexão JDBC.
     * Lê parâmetros em config/db.properties: db.url, db.user, db.password
     * Retorna null em caso de falha (o caller deve tratar dessa situação).
     */
    public static Connection getConnection() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config/db.properties"));

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            return DriverManager.getConnection(url, user, password);

        } catch (Exception e) {
            System.out.println("🚨[ERRO] na ligação à BD: " + e.getMessage());
            return null;
        }
    }
}
