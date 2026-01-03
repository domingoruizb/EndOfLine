package es.us.dp1.lx_xy_24_25.endofline.configuration;

import es.us.dp1.lx_xy_24_25.endofline.user.User;
import es.us.dp1.lx_xy_24_25.endofline.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DecodeJWT {

    private static UserRepository userRepository;

    @Autowired
    public DecodeJWT (
        UserRepository userRepository
    ) {
        DecodeJWT.userRepository = userRepository;
    }

    public static User getUserFromJWT () {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        return user;
    }

}
