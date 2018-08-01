package springboot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.mapper.UserMapper;
import springboot.entity.User;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int createUser(User user) {
        return userMapper.insertSelective(user);
    }

    public User findUserByEmail(String email) {
        return userMapper.findUserByEmail(email);
    }

    public User findUserByPhone(String phone) {
        return userMapper.findUserByPhone(phone);
    }

    public int countUserNumByPhone(String phone) {
        return userMapper.countUserNumByPhone(phone);
    }


}
