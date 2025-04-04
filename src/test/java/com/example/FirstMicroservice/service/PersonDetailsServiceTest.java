package com.example.FirstMicroservice.service;


import com.example.FirstMicroservice.dto.PersonDTO;
import com.example.FirstMicroservice.security.PersonDetails;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PersonDetailsServiceTest {

    @Mock
    private PersonService personService;

    private PersonDetailsService personDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personDetailsService = new PersonDetailsService(personService);
    }

    @Test
    void loadUserByUsername_WhenUserExists_ReturnsUserDetails() {
        String username = "testUser";
        PersonDTO personDTO = new PersonDTO.PersonDTOBuilder()
                .setUsername(username)
                .setPassword("password")
                .build();
        
        Mockito.when(personService.getPersonDTO(username)).thenReturn(Optional.of(personDTO));

        UserDetails userDetails = personDetailsService.loadUserByUsername(username);

        Assertions.assertNotNull(userDetails);
        Assertions.assertEquals(username, userDetails.getUsername());
        Assertions.assertTrue(userDetails instanceof PersonDetails);
    }

    @Test
    void loadUserByUsername_WhenUserDoesNotExist_ThrowsException() {
        String username = "nonExistentUser";
        Mockito.when(personService.getPersonDTO(username)).thenReturn(Optional.empty());

        Assertions.assertThrows(UsernameNotFoundException.class, () -> personDetailsService.loadUserByUsername(username));
    }
} 