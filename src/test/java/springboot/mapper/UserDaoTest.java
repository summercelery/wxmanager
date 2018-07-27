package springboot.mapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import springboot.entity.Role;
import springboot.entity.User;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserDaoTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void userDaoTest(){
//        User user = new User();
//        user.setLoginName("11");
//        user.setPassword("111");
//        Role role = new Role();
//        role.setId("123");
//        role.setRoleName("管理");
//        List list = new ArrayList<>();
//        list.add(role);
//        user.setRoleList(list);
//        userMapper.insert(user);


        User user1 = userMapper.findUserById("2222");
        System.out.println(user1.getStringRoles());

    }


}