package top.aprdec.onepractice;

import com.easy.query.api.proxy.client.EasyEntityQuery;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.aprdec.onepractice.entity.UserDO;
import top.aprdec.onepractice.entity.proxy.UserDOProxy;

import java.util.List;

@SpringBootTest
public class EasyQueryTests {
    @Autowired
    EasyEntityQuery easyEntityQuery;
    @Test
    void testselect(){
         String username=easyEntityQuery.queryable(UserDO.class)
                .where(u -> u.email().eq("aaa"))
                .select(u -> new UserDOProxy()
                        .username().set(u.username())
                ).toList().get(0).getUsername();
        System.out.println(username);


    }
}
