package top.berthua;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.*;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class NetEaseMusic{
    public static void main(String searchName, Group subject, Member sender)throws Exception{
        URL url = new URL("https://ncm.lgc2333.top/search?keywords="+URLEncoder.encode(searchName,StandardCharsets.UTF_8));
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        httpConn.setRequestMethod("POST");
        httpConn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36 Edg/99.0.1150.55");

        InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                ? httpConn.getInputStream()
                : httpConn.getErrorStream();
        CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream,decoder));
        String response = bufferedReader.readLine();
        System.out.println(response);
        Plugin.INSTANCE.getLogger().info(URLEncoder.encode(searchName,StandardCharsets.UTF_8));
        String jsons = response.replace("{\"result\":{\"songs\":","");
        int songCount = JSONObject.parseObject(response).getJSONObject("result").getInteger("songCount");
        if(JSONObject.parseObject(response).getJSONObject("result").getBoolean("hasMore")){
            jsons = jsons.replace(",\"hasMore\":true,\"songCount\":"+songCount+"},\"code\":200}","");
        }else{
            jsons = jsons.replace(",\"hasMore\":false,\"songCount\":"+songCount+"},\"code\":200}","");
        }
        JSONArray jsonArray = JSONObject.parseArray(jsons);
        ArrayList<Music> musics = new ArrayList<>();
        for(int t = 0;t < jsonArray.size();t++){
            long id = jsonArray.getJSONObject(t).getLong("id");
            String name = jsonArray.getJSONObject(t).getString("name");
            JSONArray artistJson = jsonArray.getJSONObject(t).getJSONArray("artists");
            String ImgUrl = jsonArray.getJSONObject(t).getJSONObject("album").getJSONObject("artist").getString("img1v1Url");
            List<String> artists = new ArrayList<>();
            for(int a = 0;a < artistJson.size();a++){
                artists.add(artistJson.getJSONObject(a).getString("name"));
            }
            musics.add(new Music(id,name,artists.toString().replace("[","").replace(",","").replace("]",""),ImgUrl));
        }
        String musicList = null;
        for(int b = 0;b < musics.size();b++){
            musicList = musicList+b+musics.get(b).toString();
        }
        subject.sendMessage(new At(sender.getId()).plus(new PlainText("请使用\"#选择 序列号\"进行选择\n"+musicList+"\n" +
                "——————————————————————\n" +
                "爱发电赞助链接：https://afdian.net/@Wutzu")));
        GlobalEventChannel.INSTANCE.filter(ev -> ev instanceof GroupMessageEvent && ((GroupMessageEvent) ev).getSubject().getId() == subject.getId()).subscribe(GroupMessageEvent.class, event->{
            MessageChain chain = event.getMessage();
            String message = chain.contentToString();
            if(message.startsWith("#选择 ")){
                Music selectingMusic = musics.get(Integer.parseInt(message.replace("#选择 ","")));
                subject.sendMessage(new MusicShare(MusicKind.NeteaseCloudMusic,
                        selectingMusic.getName(),
                        selectingMusic.getArtists(),
                        "https://music.163.com/#/song?id="+selectingMusic.getId(),
                        selectingMusic.getImgUrl(),
                        "https://music.163.com/song/media/outer/url?id="+selectingMusic.getId()+".mp3"
                        ));
                return ListeningStatus.STOPPED;
            }else{
                return ListeningStatus.LISTENING;
            }
        });
    }
}
