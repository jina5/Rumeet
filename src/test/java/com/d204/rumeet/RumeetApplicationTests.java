package com.d204.rumeet;

import com.d204.rumeet.user.model.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RumeetApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Test
    void contextLoads() {
    }

//    @Test
//    void doLoginTest(){
//        Assertions.assertThat(userMapper).isNotNull();
//        LoginDto loginDto = new LoginDto("test","test");
//        int userDto = userMapper.doLogin(loginDto);
//        Assertions.assertThat(userDto).isEqualTo(1);
//        RespData<UserDto> testx = new RespData<>();
////        testx.setData(userDto);
//        testx.setFlag("success");
//        testx.setMsg("ok");
//        Assertions.assertThat(testx.builder()).isNotNull();
//        System.out.println("testx.builder() = " + testx.builder());
//    }
}
