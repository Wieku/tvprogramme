package pl.edu.agh.student.dejakraj.tvprogramme;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.logging.log4j.Logger;

public class Downloader {
    private static final Logger logger = Logging.getLogger("TVProgramme");
    public ArrayList<String> channelList = new ArrayList<String>();
    public ArrayList<ArrayList<Program>> programs = new ArrayList<ArrayList<Program>>();

    public Downloader(URL url)
    {
        try {
            Document doc = Jsoup.connect(url.toString()).get();
            Elements channels = doc.select("div.p-channels__item");
            channels.forEach((Element e) -> {
                String channelName = e.select("a.p-channels__item__info__title__link").first().ownText();
                ArrayList<Program> channelPrograms = new ArrayList<Program>();
                e.select("div.p-programms__item__inner").forEach((Element program) -> {
                    channelPrograms.add(new Program(program.select("span.p-programms__item__name-link").first().ownText(),
                            program.select("span.p-programms__item__time-value").first().ownText()));
                });
                if(!channelName.isEmpty()) {
                    channelList.add(channelName);
                    programs.add(channelPrograms);
                }
            });
        }
        catch(IOException e) {
            logger.error(e);
        }

    }

}
