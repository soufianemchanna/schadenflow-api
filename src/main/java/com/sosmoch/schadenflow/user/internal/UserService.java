package com.sosmoch.schadenflow.user.internal;


import com.sosmoch.schadenflow.user.CreateUserCommand;
import com.sosmoch.schadenflow.user.UserFacade;
import com.sosmoch.schadenflow.user.UserView;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserFacade {
    private final UserRepository userRepository;

    @Override
    public Optional<UserView> findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(u -> new UserView(u.getId(), u.getEmail(), u.getUserRole(), u.getPasswordHash()));
    }

    @Override
    public UserView createUser(CreateUserCommand command) {
        User user = new User();
        user.setEmail(command.email());
        user.setPasswordHash(command.password());
        user.setUserRole(command.role());
        userRepository.save(user);
        return UserView.from(user);
    }

    @Override
    public Optional<UserView> findUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserView::from);
    }
}
