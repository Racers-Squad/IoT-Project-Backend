package backend.controllers;

import backend.DTO.AuthRequest;
import backend.DTO.RegistrationRequest;
import backend.exceptions.UserAlreadyExistsException;
import backend.exceptions.UserNotFoundException;
import backend.exceptions.WrongPasswordException;
import backend.security.JwtUtils;
import backend.services.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
@Slf4j
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        try {
            userService.register(request);
            log.debug("User " + request.getEmail() + " registered");
            return ResponseEntity.ok(jwtUtils.generateToken(request.getEmail()));
        } catch (UserAlreadyExistsException e) {
            log.debug("User " + request.getEmail() + " already exists");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("An error occured in server", e);
            return ResponseEntity.internalServerError().body("An error occurred on the server");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest user) {
        try {
            userService.login(user);
            log.debug("User " + user.getEmail() + " logged in");
            return ResponseEntity.ok(jwtUtils.generateToken(user.getEmail()));
        } catch (UserNotFoundException | WrongPasswordException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String token = jwtUtils.restructJwtHeader(auth);
        if(token != null){
            if(jwtUtils.validateToken(token)){
                log.trace("Successful token check");
                return ResponseEntity.ok(jwtUtils.generateToken(jwtUtils.getEmailFromToken(token)));
            }
        }
        log.debug("Unsuccessful token check");
        return ResponseEntity.ok("LOGOUT");
    }

    @GetMapping("/admin")
    public ResponseEntity<?> checkAdminRoots(@RequestHeader(HttpHeaders.AUTHORIZATION) String auth) {
        String token = jwtUtils.restructJwtHeader(auth);    boolean rights = userService.checkAdminRoots(token);
        return ResponseEntity.ok(rights);
    }

    @PostMapping("/users/delete")
    public ResponseEntity<?> deleteUser(@RequestBody Long userId) {
        userService.deleteById(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUserEmails() {
        List<Pair<Long, String>> emails = userService.findAll().stream()
                .map(it -> Pair.of(it.getId(), it.getEmail()))
                .toList();
        return ResponseEntity.ok(emails);
    }
}
