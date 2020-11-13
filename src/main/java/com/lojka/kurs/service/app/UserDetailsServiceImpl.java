package com.lojka.kurs.service.app;
import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.UserAuthoException;
import com.lojka.kurs.model.user.*;
import com.lojka.kurs.repository.app.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    PasswordEncoder encoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userRepository.getUser(email);
        } catch (DbAccessException e) {
            e.printStackTrace();
            throw new UsernameNotFoundException("access exception");
        }
        UserDetails userDetails =
                new CustomUserDetails(user);
        return userDetails;
    }

    public void signUpUser(User user) throws DbAccessException, UserAuthoException {
        user.setPassword(encoder.encode(user.getPassword()));

        if (userRepository.existUser(user.getLogin())){
            throw new UserAuthoException("user with this login already exist");
        }

        userRepository.addUser(user);
    }

}
