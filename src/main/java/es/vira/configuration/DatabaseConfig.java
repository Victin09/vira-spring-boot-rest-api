package es.vira.configuration;

import es.vira.common.enums.UserRole;
import es.vira.model.User;
import es.vira.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class DatabaseConfig implements CommandLineRunner {

    private final UserService userService;

    @Autowired
    public DatabaseConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... params) {
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setEmail("admin@vira.es");
        admin.setRoles(new ArrayList<>(Collections.singletonList(UserRole.ROLE_ADMIN)));

        userService.signup(admin);

        User client = new User();
        client.setUsername("client");
        client.setPassword("client");
        client.setEmail("client@vira.es");
        client.setRoles(new ArrayList<>(Collections.singletonList(UserRole.ROLE_CLIENT)));

        userService.signup(client);
    }
}
