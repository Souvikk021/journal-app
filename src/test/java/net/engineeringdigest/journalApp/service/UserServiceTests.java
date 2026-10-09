package net.engineeringdigest.journalApp.service;


import net.engineeringdigest.journalApp.repository.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
@SpringBootTest
public class UserServiceTests {

    @Autowired
    private UserRepository userRepository;


    @Test
    public void testFindByUsername(){
        assertNotNull(userRepository.findByUsername("admin"));
    }


    @Disabled
    @ParameterizedTest
    @CsvSource({
            "1, 1, 2",
            "2, 6, 8"
    })
    public void test(int a, int b, int expected){
        assertEquals(expected, a + b);
    }
}
