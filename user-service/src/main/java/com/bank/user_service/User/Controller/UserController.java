package com.bank.user_service.User.Controller;


import com.bank.user_service.User.Repository.UserRepository;
import com.bank.user_service.User.Service.UserService;
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



    //vet sign up route
    @PostMapping("/register/vet")
    @PreAuthorize("hasAuthority('CREATE_USERS')")
    public ResponseEntity<SignUpResponseDto> teacherSignUpRoute(SignUpDto signUpDto){
        //register user
        User registeredUser= userService.vetSignUp(signUpDto);

        //response
        SignUpResponseDto signUpResponse=new SignUpResponseDto();
        signUpResponse.setEmail(registeredUser.getEmail());
        signUpResponse.setName(registeredUser.getName());
        signUpResponse.setMessage("Successfully signed up");
        //return response
        return ResponseEntity.ok(signUpResponse);
    }


    //receptionist sign up route
    @PostMapping("/register/receptionist")
    @PreAuthorize("hasAuthority('CREATE_USERS')")
    public ResponseEntity<SignUpResponseDto> receptionistSignUpRoute(SignUpDto signUpDto){
        //register user
        User registeredUser= userService.receptionistSignUp(signUpDto);

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

