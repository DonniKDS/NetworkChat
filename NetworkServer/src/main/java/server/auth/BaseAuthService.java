package server.auth;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;

public class BaseAuthService implements AuthService {

    private static final Logger LOGGER = LogManager.getLogger(BaseAuthService.class);

    private static Connection connection;
    private static Statement statement;

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) {
        String query = String.format("select username from users where login='%s' and password='%s'", login, password);
        try (ResultSet set = statement.executeQuery(query)) {
            if (set.next())
                return set.getString(1);
        } catch (SQLException e) {
            LOGGER.error("Ошибка подключенния к базе данных.");
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:NetworkServer/users.db");
            statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            LOGGER.error("Ошибка подключенния к базе данных.");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            connection.close();
        } catch (SQLException e) {
            LOGGER.error("Ошибка подключенния к базе данных.");
            throw new RuntimeException(e);
        }
    }
}
