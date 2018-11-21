package org.dnltsk.d2d.challenge;

import org.dnltsk.d2d.challenge.write.DbInitiator;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@TestConfiguration
public class NoDbConfiguration {

    @Bean
    @Primary
    public DbInitiator mockDbInitiator(){
        DbInitiator mock = mock(DbInitiator.class);
        doNothing().when(mock).resetDb();
        return mock;
    }

    @Bean
    @Primary
    public DatabasePool mockDatabasePool(){
        return mock(DatabasePool.class);
    }


}
