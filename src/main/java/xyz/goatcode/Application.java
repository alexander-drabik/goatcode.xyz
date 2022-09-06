package xyz.goatcode;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        try {
            Document doc = Jsoup.connect("https://socialblade.com/youtube/channel/UCEl6qkGmXm1OpmqjYPvuzeQ").userAgent("Mozilla").get();

            Elements elements = doc.getElementsByClass("YouTubeUserTopInfo");

            for(Element element : elements) {
                if (element.text().length() >= 11) {
                    if (element.text().substring(0, 11).equals("Subscribers")) {
                        String[] splited = element.text().substring(12).split("\\s+");
                        System.out.println(splited[0]);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //SpringApplication.run(Application.class, args);
    }
}
