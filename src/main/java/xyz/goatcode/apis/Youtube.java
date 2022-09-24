package xyz.goatcode.apis;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

@Component
public class Youtube {
    public static int subscribersCount = 0;

    // Function will be called every 3 minutes
    @Scheduled(fixedDelay = 1000*60*3)
    public static void setSubscribersCount() {
        var key = "AIzaSyBqZNU6Yiure6CB3zrEufUFWZOtl5oguBg";
        var channel = "UCEl6qkGmXm1OpmqjYPvuzeQ";

        try {
            URL url = new URL("https://www.googleapis.com/youtube/v3/channels?part=statistics&id="+channel+"&key="+key);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();

            String data = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

            JSONObject json = new JSONObject(data);
            subscribersCount = json
                    .getJSONArray("items")
                    .getJSONObject(0)
                    .getJSONObject("statistics")
                    .getInt("subscriberCount");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
