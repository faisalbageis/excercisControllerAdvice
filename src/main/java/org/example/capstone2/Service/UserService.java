package org.example.capstone2.Service;

import lombok.RequiredArgsConstructor;
import org.example.capstone2.APi.ApiException;
import org.example.capstone2.Model.User;
import org.example.capstone2.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<User> getUsers(){
        List<User> users = userRepository.findAll();

        if(users.isEmpty()){
            throw new ApiException("there is no users in the system");
        }

        return users;
    }

    public void addUser(User user){
        user.setStatus("active");
        userRepository.save(user);
    }

    public void updateUser(Integer id,User user){
        User olduser = userRepository.findUserById(id);

        if(olduser == null){
            throw new ApiException("id not found");
        }

        olduser.setEmail(user.getEmail());
        olduser.setFullName(user.getFullName());
        olduser.setPhoneNumber(user.getPhoneNumber());
        olduser.setPassword(user.getPassword());
        userRepository.save(olduser);
    }

    public void deleteUser(Integer id){
        User olduser = userRepository.findUserById(id);

        if(olduser == null){
            throw new ApiException("id not found");
        }

        userRepository.delete(olduser);
    }

    public void login(String email,String Password){
        User user = userRepository.findUserByEmail(email);

        if(user==null){
            throw new ApiException("wrong email");
        }

        if(!user.getPassword().equals(Password)){
            throw new ApiException("wrong password");
        }
    }

}