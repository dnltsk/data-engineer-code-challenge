package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.sql.SQLException;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class NoDbConfiguration {

    @Bean
    @Primary
    public DbInitiator mockDbInitiator() throws SQLException {
        return mock(DbInitiator.class);
    }

    @Bean
    @Primary
    public DatabasePool mockDatabasePool(){
        return mock(DatabasePool.class);
    }


}
