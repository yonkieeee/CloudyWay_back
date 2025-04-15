package org.example.userservice.services;


import org.example.userservice.dto.MailBody;
import org.example.userservice.models.User;
import org.example.userservice.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;
import java.util.Objects;

@Service
public class ForgetPasswordService {
    private final UserRepository userRepository;
    private final UnifiedJedis jedis;
    private final EmailService emailService;

    private static final int OTP_EXPIRED_TIME = 15 * 60;

    @Autowired
    public ForgetPasswordService(UserRepository userRepository,
                                 StringRedisTemplate stringRedisTemplate, UnifiedJedis jedis,
                                 EmailService emailService) {
        this.userRepository = userRepository;
        this.jedis = jedis;
        this.emailService = emailService;
    }

    private String generateOTP(){
        int otp = (int) (Math.random() * 900000);
        return String.valueOf(otp);
    }

    public void sendOTP(String email) {
        String otp = generateOTP();

        String key = email;

        jedis.set(key, otp);
        jedis.expire(key, OTP_EXPIRED_TIME);

        MailBody mailBody = new MailBody(email, "Зміна пароля",
                otp);
        emailService.sendMail(mailBody);
    }

    public Boolean validateOTP(String email, String otp) {
        try {
            String sendOTP = jedis.get(email);

            if (Objects.equals(sendOTP, otp)) {
                jedis.del(email);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    public void changePassword(User user, Map<String, Object> body) {
        try {
            UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(user.getUid())
                    .setPassword(body.get("password").toString());
            FirebaseAuth.getInstance().updateUser(request);

            body.put("password",
                    BCrypt.hashpw((String) body.get("password"), BCrypt.gensalt()));
            body.remove("identifier");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
