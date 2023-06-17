package backend.services;

import backend.DTO.AuthRequest;
import backend.DTO.RegistrationRequest;
import backend.entity.UserEntity;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.WrongPasswordException;
import backend.repository.UserRepository;
import backend.security.Hasher;
import backend.security.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public void register(RegistrationRequest request) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email is already in use.");
        }
        UserEntity user = new UserEntity();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setPhone(request.getPhone());
        user.setPassword(request.getPassword());
        user.setAdminRights(request.getIsAdmin());
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

    public void deleteById(Long userId) {
        userRepository.deleteById(userId);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
}
