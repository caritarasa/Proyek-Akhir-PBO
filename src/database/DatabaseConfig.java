package src.database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String CONFIG_FILE = "database.properties";
    private static Properties props = new Properties();
    
    static {
        loadConfig();
    }
    
    private static void loadConfig() {
        try {
            // Coba load dari file properties
            props.load(new FileInputStream(CONFIG_FILE));
        } catch (IOException e) {
            // Jika file tidak ada, gunakan default values
            setDefaultConfig();
        }
    }
    
    private static void setDefaultConfig() {
        props.setProperty("db.host", "localhost");
        props.setProperty("db.port", "3306");
        props.setProperty("db.name", "philosofit_db");
        props.setProperty("db.username", "root");
        props.setProperty("db.password", "");
        props.setProperty("db.useSSL", "false");
        props.setProperty("db.allowPublicKeyRetrieval", "true");
        props.setProperty("db.serverTimezone", "Asia/Jakarta");
    }
    
    public static String getHost() {
        return props.getProperty("db.host", "localhost");
    }
    
    public static String getPort() {
        return props.getProperty("db.port", "3306");
    }
    
    public static String getDatabaseName() {
        return props.getProperty("db.name", "philosofit_db");
    }
    
    public static String getUsername() {
        return props.getProperty("db.username", "root");
    }
    
    public static String getPassword() {
        return props.getProperty("db.password", "");
    }
    
    public static String getConnectionUrl() {
        return String.format("jdbc:mysql://%s:%s/%s", getHost(), getPort(), getDatabaseName());
    }
    
    public static Properties getConnectionProperties() {
        Properties connProps = new Properties();
        connProps.setProperty("user", getUsername());
        connProps.setProperty("password", getPassword());
        connProps.setProperty("useSSL", props.getProperty("db.useSSL", "false"));
        connProps.setProperty("allowPublicKeyRetrieval", props.getProperty("db.allowPublicKeyRetrieval", "true"));
        connProps.setProperty("serverTimezone", props.getProperty("db.serverTimezone", "Asia/Jakarta"));
        connProps.setProperty("useUnicode", "true");
        connProps.setProperty("characterEncoding", "UTF-8");
        return connProps;
    }
}
