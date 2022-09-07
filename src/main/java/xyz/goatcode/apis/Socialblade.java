package xyz.goatcode.apis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Socialblade {
    public static int subscribersCount = 0;


    // Function will be called every 3 minutes
    @Scheduled(fixedDelay = 1000*60*3)
    public static void setSubscribersCount() {
        System.out.println(subscribersCount);
        try {
            Document doc = Jsoup.connect("https://socialblade.com/youtube/channel/UCEl6qkGmXm1OpmqjYPvuzeQ").userAgent("Mozilla").get();

            Elements elements = doc.getElementsByClass("YouTubeUserTopInfo");

            for(Element element : elements) {
                if (element.text().length() >= 11) {
                    if (element.text().substring(0, 11).equals("Subscribers")) {
                        String[] split = element.text().substring(12).split("\\s+");
                        subscribersCount = Integer.parseInt(split[0]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
