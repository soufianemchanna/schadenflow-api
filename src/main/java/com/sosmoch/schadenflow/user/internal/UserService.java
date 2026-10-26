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
                .map(u -> new UserView(u.getId(), u.getEmail(), u.getUserRole(), u.getPasswordHash(), u.isEnabled()));
    }

    @Override
    public boolean isUserEnabled(UUID id) {
        return userRepository.findById(id)
                .map(User::isEnabled)
                .orElse(false);
    }

    @Override
    public void enableUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void disableUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow();
        user.setEnabled(false);
        userRepository.save(user);
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
