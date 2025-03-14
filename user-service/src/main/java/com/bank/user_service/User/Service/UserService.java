package com.bank.user_service.User.Service;

import com.bank.user_service.Role.Entity.Role;
import com.bank.user_service.Role.Repository.RoleRepository;
import com.bank.user_service.User.Entity.User;
import com.bank.user_service.User.Repository.UserRepository;
import com.bank.user_service.User.dtos.LoginDto;
import com.bank.user_service.User.dtos.SignUpDto;
import com.bank.user_service.User.dtos.UserDetailsDto;
import com.bank.user_service.User.dtos.UserResponseDto;
import com.bank.user_service.Util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public UserService(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserDetailsService userDetailsService
    ){
        this.roleRepository=roleRepository;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
        this.jwtService=jwtService;
        this.userDetailsService=userDetailsService;
    }

    //admin sign up service
    public User adminSignUp(SignUpDto signUpDto){
        Optional<Role> optionalRole = roleRepository.findByName("ADMIN");

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user=new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setRoles(Set.of(optionalRole.get()));
        //encoding password before storing
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        //save
        return userRepository.save(user);
    }

    //Loan officer sign up
    public User loanOfficerSignUp(SignUpDto signUpDto){
        Optional<Role> optionalRole = roleRepository.findByName("LOAN_OFFICER");

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user=new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setRoles(Set.of(optionalRole.get()));
        //encoding password before storing
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        //save
        return userRepository.save(user);
    }

    //customer sign up
    public User customerSignUp(SignUpDto signUpDto){
        Optional<Role> optionalRole = roleRepository.findByName("CUSTOMER");

        if (optionalRole.isEmpty()) {
            return null;
        }

        User user=new User();
        user.setEmail(signUpDto.getEmail());
        user.setName(signUpDto.getName());
        user.setRoles(Set.of(optionalRole.get()));
        //encoding password before storing
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        //save
        return userRepository.save(user);
    }

//    //customer sign up
//    public User customerSignUp(SignUpDto signUpDto){
//        Optional<Role> optionalRole = roleRepository.findByName("CUSTOMER");
//
//        if (optionalRole.isEmpty()) {
//            return null;
//        }
//
//        User user=new User();
//        user.setEmail(signUpDto.getEmail());
//        user.setName(signUpDto.getName());
//        user.setRoles(Set.of(optionalRole.get()));
//        //encoding password before storing
//        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
//        //save
//        return userRepository.save(user);
//    }



    //login service
    public User authenticate(LoginDto loginDto){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        return userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow();
    }


//    public UserResponseDto getUserDetailsByUuid(String uuid) {
//        Optional<User> userOptional = userRepository.findByUuid(uuid);
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//            return new UserResponseDto(true, user.getName(), user.getEmail());
//        } else {
//            return new UserResponseDto(false, null, null);
//        }
//    }

    public UserResponseDto getUserDetails() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;

            // Assuming the username is the email (this depends on your auth setup)
            String email = userDetails.getUsername();

            // If you store the UUID in JWT claims, you can extract it like this
//            String uuid = userDetails instanceof CustomUserDetails
//                    ? ((CustomUserDetails) userDetails).getUuid()
//                    : "Unknown";
            Optional<User> user=userRepository.findByEmail(email);
            if(user.isPresent()){
                String uuid = user.get().getUuid();
                UserResponseDto userResponseDto=new UserResponseDto();
                userResponseDto.setEmail(user.get().getEmail());
                userResponseDto.setUuid(user.get().getUuid());

                return userResponseDto;
            }

            throw new UsernameNotFoundException("User email does not exist: " + email);

        }
        throw new AccessDeniedException("User is not authenticated");
    }



    public List<UserResponseDto> getAllUsers() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add);

        return users.stream().map(user -> {
            UserResponseDto dto = new UserResponseDto();
            dto.setUuid(user.getUuid());
            dto.setEmail(user.getEmail());
            return dto;
        }).collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow();
//        Optional<User> user=userRepository.findById(id);

        UserResponseDto dto = new UserResponseDto();
        dto.setUuid(user.getUuid());
        dto.setEmail(user.getEmail());
        return dto;
    }


    public UserDetailsDto validateToken(String token){
        String username = jwtService.extractUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!jwtService.isTokenValid(token, userDetails)) {
            throw new RuntimeException("Invalid or expired token");
        }

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(auth -> auth.getAuthority())
                .collect(Collectors.toList());

        return new UserDetailsDto(username, roles);
    }
}
