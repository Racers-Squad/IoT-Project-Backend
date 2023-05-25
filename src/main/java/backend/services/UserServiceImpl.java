package backend.services;

import backend.DTO.AuthRequest;
import backend.entites.UserEntity;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.WrongPasswordException;
import backend.repositories.UserRepository;
import backend.security.Hasher;
import backend.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public void register(AuthRequest authRequest) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }
        UserEntity user = new UserEntity(authRequest.getEmail(), Hasher.encryptMD5(authRequest.getPassword()), false);
        userRepository.save(user);
    }

    public void login(AuthRequest authRequest) throws UserNotFoundException, WrongPasswordException {
        if (!userRepository.existsByEmail(authRequest.getEmail())) {
            throw new UserNotFoundException(authRequest.getEmail());
        }
        UserEntity entity = userRepository.findByEmail(authRequest.getEmail());
        if (!Hasher.encryptMD5(authRequest.getPassword()).equals(entity.getPassword())) {
            throw new WrongPasswordException(authRequest.getEmail());
        }
    }

    public boolean checkAdminRoots(String token){
        String email = jwtUtils.getEmailFromToken(token);
        return userRepository.findByEmail(email).isAdminRights();
    }
}
