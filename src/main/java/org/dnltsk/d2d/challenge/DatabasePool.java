package org.dnltsk.d2d.challenge;

import com.github.davidmoten.rx.jdbc.ConnectionProvider;
import com.github.davidmoten.rx.jdbc.ConnectionProviderFromUrl;
import com.github.davidmoten.rx.jdbc.Database;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

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

    private ConnectionProvider connectionProvider;

    @PostConstruct
    private void initConnection(){
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        connectionProvider = new ConnectionProviderFromUrl(url, user, pass);
    }

    public Database getDatabase() {
        Database db = Database.from(connectionProvider);
        return db;
    }

}
