import com.prigunoff.App;
import com.prigunoff.todolist.model.User;
import com.prigunoff.todolist.repository.UserRepository;
import com.prigunoff.todolist.service.RoleService;
import com.prigunoff.todolist.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class UserRepoTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void test_for_users_not_empty() {
        List<User> users = userRepository.findAll();
        Assertions.assertFalse(users.isEmpty());
    }
    @Test
    public void test2_for_users_not_empty() {
        User user = userService.readById(5);
        Assertions.assertNotNull(user);
    }
}
