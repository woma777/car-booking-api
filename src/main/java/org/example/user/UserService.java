package org.example.user;
import org.example.exception.ResourceNotFundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
private final AppUserRepository appUserRepository;
public UserService(AppUserRepository appUserRepository) {
		this.appUserRepository = appUserRepository;
}
    public List<UserResponse> selectAllUsers(){
        return appUserRepository.findAll()
                .stream()
                .map(appUser -> new UserResponse(
                        appUser.getUserId(),
                        appUser.getName()
                ))
                .collect(Collectors.toList());
    }
    public UserResponse selectUserById(UUID userId){
    AppUser appUser = appUserRepository.findById(userId)
            .orElseThrow(()-> new ResourceNotFundException("User is not found"));
    return new UserResponse(
            appUser.getUserId(),
            appUser.getName()
    );
    }

}
