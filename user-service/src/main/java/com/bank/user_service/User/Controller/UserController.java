package com.bank.user_service.User.Controller;


import com.bank.user_service.User.Entity.User;
import com.bank.user_service.User.Repository.UserRepository;
import com.bank.user_service.User.Service.UserService;
import com.bank.user_service.User.dtos.LoginDto;
import com.bank.user_service.User.dtos.LoginResponseDto;
import com.bank.user_service.User.dtos.SignUpDto;
import com.bank.user_service.User.dtos.SignUpResponseDto;
import com.bank.user_service.Util.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserController(
            UserService userService,
            JwtService jwtService,
            UserRepository userRepository
    ){
        this.userService=userService;
        this.jwtService=jwtService;
        this.userRepository=userRepository;
    }

    //teacher sign up route
    @PostMapping("/register/admin")
    @PreAuthorize("hasAuthority('CREATE_ADMIN')")
    public ResponseEntity<SignUpResponseDto> adminSignupRoute(SignUpDto signUpDto){
        //register user
        User registeredUser= userService.adminSignUp(signUpDto);

        //response
        SignUpResponseDto signUpResponse=new SignUpResponseDto();
        signUpResponse.setEmail(registeredUser.getEmail());
        signUpResponse.setName(registeredUser.getName());
        signUpResponse.setMessage("Successfully signed up");
        //return response
        return ResponseEntity.ok(signUpResponse);
    }



    //loan officer sign up route
    @PostMapping("/register/loan-officer")
    @PreAuthorize("hasAuthority('CREATE_USERS')")
    public ResponseEntity<SignUpResponseDto> teacherSignUpRoute(SignUpDto signUpDto){
        //register user
        User registeredUser= userService.loanOfficerSignUp(signUpDto);

        //response
        SignUpResponseDto signUpResponse=new SignUpResponseDto();
        signUpResponse.setEmail(registeredUser.getEmail());
        signUpResponse.setName(registeredUser.getName());
        signUpResponse.setMessage("Successfully signed up");
        //return response
        return ResponseEntity.ok(signUpResponse);
    }



    //customer sign up route
    @PostMapping("/register/customer")
    //@PreAuthorize("hasAuthority('CREATE_USERS')")
    public ResponseEntity<SignUpResponseDto> customerSignUpRoute(SignUpDto signUpDto){
        //register user
        User registeredUser= userService.customerSignUp(signUpDto);

        //response
        SignUpResponseDto signUpResponse=new SignUpResponseDto();
        signUpResponse.setEmail(registeredUser.getEmail());
        signUpResponse.setName(registeredUser.getName());
        signUpResponse.setMessage("Successfully signed up");
        //return response
        return ResponseEntity.ok(signUpResponse);
    }


    //LOGIN ROUTE
    //PUBLIC
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginRoute(@RequestBody LoginDto loginDto){
        //login user
        User authenticatedUser= userService.authenticate(loginDto);
        String jwtToken = jwtService.generateToken(authenticatedUser);

        //response
        LoginResponseDto loginResponse=new LoginResponseDto();
        loginResponse.setToken(jwtToken);
        loginResponse.setExpiresIn(jwtService.getExpirationTime());

        //return response
        return ResponseEntity.ok(loginResponse);
    }


    @GetMapping("/users/email/{email}")
    public User getUserByEmail(@PathVariable String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


}

