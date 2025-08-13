//package org.messplacement.messsecond.Service;
//
//import org.messplacement.messsecond.Dao.MessDao;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//
//import java.util.List;
//import java.util.Map;
//
//@Service
//public class MyUserDetailsService implements UserDetailsService {
//
//    @Autowired
//    private MessDao messDao;
//
//    @Override
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        // MySQL query to retrieve user details based on email
//        String sql = "SELECT reg, email, password FROM users WHERE email = ?";
//
//        List<Login> rows = MessDao.queryForList(sql, email);
//        if (rows.isEmpty()) {
//            throw new UsernameNotFoundException("User not found");
//        }
//
//        // Retrieve the first row (since email should be unique)
//        Map<String, Object> row = rows.get(0);
//        String reg = (String) row.get("reg");
//        String password = (String) row.get("password");
//
//        // Return the UserDetails, including reg as an authority or as custom info if needed
//        return User.withUsername(email)
//                .password(password) // Password should ideally be encoded
//                .authorities("ROLE_USER") // Add roles if applicable
//                .build();
//    }
//}
//
