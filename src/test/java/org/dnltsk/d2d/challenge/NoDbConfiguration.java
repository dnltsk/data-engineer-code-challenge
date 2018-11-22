package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.dnltsk.d2d.challenge.write.DbWriter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.sql.Connection;
import java.sql.SQLException;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class NoDbConfiguration {

    @Bean
    @Primary
    public DbInitiator mockDbInitiator() throws SQLException {
        return mock(DbInitiator.class);
    }

    @Bean
    @Primary
    public DatabasePool mockDatabasePool() throws SQLException {
        DatabasePool databasePool = mock(DatabasePool.class);
        when(databasePool.openJdbcConnection()).thenReturn(mock(Connection.class));
        return databasePool;
    }

    @Bean
    @Primary
    public DbWriter mockDbWriter(){
        return mock(DbWriter.class);
    }

}
