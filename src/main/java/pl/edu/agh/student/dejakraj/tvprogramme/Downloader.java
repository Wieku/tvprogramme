package pl.edu.agh.student.dejakraj.tvprogramme;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

public class Downloader {
    private static final Logger logger = Logging.getLogger("Downloader");

    public static Schedule Download()
    {
        Schedule sh = new Schedule();
        try {
            Document doc = Jsoup.connect("https://tv.mail.ru/moskva/").get();
            Elements channels = doc.select("div.p-channels__item"); //get channels
            channels.forEach((Element e) -> {
                String channelName = e.select("a.p-channels__item__info__title__link").first().ownText(); //get channel names
                ArrayList<Program> channelPrograms = new ArrayList<Program>();
                e.select("div.p-programms__item__inner").forEach((Element program) -> { //get programs
                    channelPrograms.add(new Program(program.select("span.p-programms__item__name-link").first().ownText(), //get program names
                            program.select("span.p-programms__item__time-value").first().ownText())); //get program hour
                });
                if(!channelName.isEmpty()) { //save if not empty
                    sh.channelList.add(channelName);
                    sh.programs.add(channelPrograms);
                }
            });
        }
        catch(IOException e) {
            logger.error(e);
            return null;
        }
        return sh;
    }

}
