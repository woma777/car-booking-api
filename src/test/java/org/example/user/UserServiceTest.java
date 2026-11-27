package org.example.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private AppUserRepository appUserRepository;
    @InjectMocks
    private UserService underTest;
    @Test
    void shouldSelectAllUsers() {
        // Given
        AppUser james = new AppUser("James");
        AppUser jamila = new AppUser("Jamila");
        james.setUserId(UUID.randomUUID());
        jamila.setUserId(UUID.randomUUID());
        when(appUserRepository.findAll()).thenReturn(List.of(james, jamila));
        List<UserResponse> expected = List.of(
                new UserResponse(james.getUserId(), "James"),
                new UserResponse(jamila.getUserId(), "Jamila")
        );
        // When
        List<UserResponse> actual = underTest.selectAllUsers();
        // Then
        assertThat(actual).hasSize(2).containsExactlyElementsOf(expected);
        verify(appUserRepository).findAll();
    }

    @Test
    void shouldSelectUserById() {
        // Given
        AppUser james = new AppUser("James");
        UUID userId = UUID.randomUUID();
        james.setUserId(userId);
        when(appUserRepository.findById(userId)).thenReturn(Optional.of(james));
        UserResponse expected = new UserResponse(userId, "James");
        // When
        UserResponse actual = underTest.selectUserById(userId);
        // Then
        assertThat(actual).isEqualTo(expected);
        verify(appUserRepository).findById(userId);
    }
    @Test
    void shouldReturnEmptyListWhenNoUsersExist() {
        // Given
        when(appUserRepository.findAll()).thenReturn(List.of());
        // When
        List<UserResponse> actual = underTest.selectAllUsers();
        // Then
        assertThat(actual).isEmpty();
        verify(appUserRepository).findAll();
    }
}