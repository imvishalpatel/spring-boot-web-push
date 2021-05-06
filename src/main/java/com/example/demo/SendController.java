package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.commons.lang3.exception.ExceptionUtils;
import nl.martijndwars.webpush.Subscription;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Notification;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

@RestController
public class SendController {
    private static final String PUBLIC_KEY = "BDiGKkaDD6iEmPVqG_eMr17HPfsW10owkgOErOjk2gkaYHlfVFQjdIFSizdaaz60L1DluhF09V-blmMarCF8R2w";
    private static final String PRIVATE_KEY = "sIWO36EUQfLEoCC4-WeLzP-qoo20yEdrrSVokmtEww8";
    private static final String SUBJECT = "Foobarbaz";
    private static final String PAYLOAD = "My fancy message";

    @RequestMapping("/send")
    public String send(@RequestParam("subscriptionJson") String subscriptionJson) {
        Security.addProvider(new BouncyCastleProvider());

        try {
            PushService pushService = new PushService(PUBLIC_KEY, PRIVATE_KEY, SUBJECT);
            Subscription subscription = new Gson().fromJson(subscriptionJson, Subscription.class);
            Notification notification = new Notification(subscription, PAYLOAD);
            HttpResponse httpResponse = pushService.send(notification);
            int statusCode = httpResponse.getStatusLine().getStatusCode();

            return String.valueOf(statusCode);
        } catch (Exception e) {
            return ExceptionUtils.getStackTrace(e);
        }
    }
}
