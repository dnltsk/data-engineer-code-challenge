package org.dnltsk.d2d.challenge.db;

import com.github.davidmoten.rx.jdbc.ConnectionProvider;
import com.github.davidmoten.rx.jdbc.ConnectionProviderFromUrl;
import com.github.davidmoten.rx.jdbc.Database;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Service
public class DatabasePool {

    @Value("${postgres.host}")
    private String host;

    @Value("${postgres.port}")
    private String port;

    @Value("${postgres.dbname}")
    private String dbname;

    @Value("${postgres.user}")
    private String user;

    @Value("${postgres.pass}")
    private String pass;

    private String connectionUrl;

    private ConnectionProvider connectionProvider;

    @PostConstruct
    private void initConnection(){
        connectionUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        connectionProvider = new ConnectionProviderFromUrl(connectionUrl, user, pass);
    }

    /**
     * opens a jdbc connection with deactivated autocommit
     *
     * @return Connection
     * @throws SQLException
     */
    public Connection openJdbcConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(connectionUrl, user, pass);
        conn.setAutoCommit(false);
        return conn;
    }

    public Database openRxDatabase() {
        return Database.from(connectionProvider);
    }


}
