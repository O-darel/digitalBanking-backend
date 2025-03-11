package com.bank.user_service.User.Service;

import com.bank.user_service.Role.Entity.Role;
import com.bank.user_service.Role.Repository.RoleRepository;
import com.bank.user_service.User.Entity.User;
import com.bank.user_service.User.Repository.UserRepository;
import com.bank.user_service.User.dtos.LoginDto;
import com.bank.user_service.User.dtos.SignUpDto;
import com.bank.user_service.User.dtos.UserResponseDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public UserService(
            RoleRepository roleRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ){
        this.roleRepository=roleRepository;
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.authenticationManager=authenticationManager;
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


    public UserResponseDto getUserDetailsByUuid(String uuid) {
        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return new UserResponseDto(true, user.getName(), user.getEmail());
        } else {
            return new UserResponseDto(false, null, null);
        }
    }
}
