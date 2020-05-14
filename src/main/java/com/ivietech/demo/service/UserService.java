package com.ivietech.demo.service;


import com.ivietech.demo.dao.RoLeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.UserDto;
import com.ivietech.demo.entity.Balance;

import com.ivietech.demo.entity.Role;
import com.ivietech.demo.entity.User;
import java.util.HashSet;
import java.util.Set;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoLeRepository roLeRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
   
    @Autowired
    BalanceService balanceService;

    @Override
    public User registerNewUserAccount(UserDto accountDto) {
        User user = new User();
        user.setUserName(accountDto.getUserName());
        user.setEmail(accountDto.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(accountDto.getPassword()));
        user.setEmail(accountDto.getEmail());
        user.setPhone(accountDto.getPhone());
        user.setName(accountDto.getName());
        Set<Role> role = new HashSet<Role>();
        role.add(roLeRepository.findById(1));
        user.setRoles(role);
        user = userRepository.save(user);
        balanceService.addBalance(user.getId());
        return user;
    }

   

    @Override
    public void saveRegisteredUser(User user) {
        userRepository.save(user);
    }


    @Override
    public void changeUserPassword(User user, String password) {
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
    }

    
    @Override
    public void changeMobilePhone(User user, String phone) {
        user.setPhone(phone);
        userRepository.save(user);
    }

}
