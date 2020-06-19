package pl.edu.agh.student.dejakraj.tvprogramme;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.net.URL;
import java.util.LinkedHashMap;

public class JsonDownloader {
    private static final Logger logger = Logging.getLogger("JsonDownloader");

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static LinkedHashMap<String, ArrayList<Program>> JsonDownload(String date)
    {
        LinkedHashMap<String, ArrayList<Program>> map = new LinkedHashMap<String, ArrayList<Program>>();
        String url = "https://tv.mail.ru/ajax/index/?date=" + date; //rrrr-mm-dd format
        try{
            BufferedReader rd = new BufferedReader(new InputStreamReader(new URL(url).openStream(), Charset.forName("UTF-8"))); //Connection
            String data = readAll(rd);
            logger.info("Fetched data from: " + url);
            Gson gson = new Gson();
            JsonObj obj = gson.fromJson(data, JsonObj.class); //Moving data from JSON structure to Java object.
            for (int i = 0; i < obj.schedule.size(); i++) {
                ArrayList<Program> pr = new ArrayList<Program>();
                for (int j = 0; j < obj.schedule.get(i).event.size(); j++) {
                    String name = obj.schedule.get(i).event.get(j).name;
                    if (!obj.schedule.get(i).event.get(j).episode_title.isEmpty()) //Add episode name if exists
                        name = name + " (" + obj.schedule.get(i).event.get(j).episode_title + ")";
                    pr.add(new Program(name, obj.schedule.get(i).event.get(j).start));
                }
                map.put(obj.schedule.get(i).channel.name, pr); //Saving each channel
            }
        }
        catch(Exception e) {
            logger.error(e);
            return null;
        }
        logger.info("Data parsed successfully");
        return map;
    }

    private class JsonObj {
        public String status;
        public ArrayList<JSchedule> schedule;
        public JChannelLive[] channels_live;
        public int current_offset;
    }

    private class JSchedule {
        public JChannel channel;
        public ArrayList<JEvents> event;
    }

    private class JEvents {
        public String channel_id;
        public String name;
        public int genre_id;
        public String episode_title;
        public String url;
        public String id;
        public String start;
        public int episode_num;
    }

    private class JChannelLive {
        public JChannel channel;
        public JEvent event;
    }

    private class JEvent {
        public String name;
        public String id;
        public String image;
    }

    private class JChannel {
        public int has_live;
        public String name;
        public int is_promo;
        public String url_online;
        public String pic_url;
        public JLive live;
        public String pic_url_128;
        public String url;
        public String slug;
        public int id;
        public String pic_url_64;
    }

    private class JLive {
        public int show_region_warning;
        public String src;
        public String type;
        public JAttr attr;
    }

    private class JAttr {
        public int allowfullscreen;
        public int frameborder;
        public String allow;
    }
}
