package mk.ukim.finki.foafprofile.service;

import mk.ukim.finki.foafprofile.model.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;

@Service
public class EmailService {

    private final TemplateEngine templateEngine;

    private final JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(User user) throws MessagingException {
        Context context = new Context();
        context.setVariable("user", user);

        String process = templateEngine.process("/emails/welcome", context);
        javax.mail.internet.MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("noreplay@foafgenerator.com");
        helper.setSubject("Welcome to FOAF generator platform " + user.getUsername());
        helper.setText(process, true);
        helper.setTo(user.getEmail());
        javaMailSender.send(mimeMessage);
        return "Sent";
    }
}