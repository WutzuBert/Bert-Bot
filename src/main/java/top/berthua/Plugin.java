package top.berthua;

import com.alibaba.fastjson.JSONObject;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.MemberPermission;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import net.mamoe.mirai.utils.BotConfiguration;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public final class Plugin extends JavaPlugin {
    public static Plugin INSTANCE = new Plugin();
    public Plugin() {
        super(new JvmPluginDescriptionBuilder("top.berthua.plugin", "1.2.9-SNAPSHOT").build());
    }
    @Override
    public void onEnable() {
        getLogger().info("Plugin loaded!");
        MyData myData = MyData.INSTANCE;
        this.reloadPluginConfig(myData);
        Long qq = myData.getQq();
        String password = myData.getPassword();
        Bot bot = BotFactory.INSTANCE.newBot(qq, password, new BotConfiguration() {{
            // 配置，例如：
            fileBasedDeviceInfo("botdeviceinfo.json");
            setProtocol(MiraiProtocol.ANDROID_PAD);
        }});
        bot.login();
        ArrayList<Group> groups = new ArrayList<>();
        List<Long> groupids = myData.getGroups();
        for (Long groupid : groupids) {
            groups.add(bot.getGroup(groupid));
        }
        for (Group value : groups) {
            value.sendMessage(new PlainText("Bert Bot已加载！By Bert\n" +
                    "——————————————————————————\n" +
                    "爱发电赞助链接：https://afdian.net/@Wutzu"));
        }
        List<String> keywords = myData.getKeywords();//初始化关键词
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            MessageChain chain = event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent
            String content = chain.contentToString();
            if (content.equals("test")) {
                event.getSubject().sendMessage("test!\n" +
                        "——————————————————————\n" +
                        "爱发电赞助链接：https://afdian.net/@Wutzu"); // 回复消息
            }
            if (content.equals("骰子")) {
                if(event.getSender().getId() == 2138681574){
                    event.getSubject().sendMessage(new Dice(6));
                }else{
                    Random rand = new Random();
                    int temp = 1 + rand.nextInt(5);
                    event.getSubject().sendMessage(new Dice(temp));
                }
            }
            if (content.contains("#提交 ")) {
                Date date = new Date();
                String dat = "yyyy-MM-dd HH:mm:ss";
                SimpleDateFormat sdf = new SimpleDateFormat(dat);
                MessageChain messageChain = new MessageChainBuilder()
                        .append(new PlainText(sdf.format(date)))
                        .append(new PlainText(event.getSenderName()))
                        .append(chain)
                        .build();
                bot.getFriend(2138681574).sendMessage(messageChain);
                event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("建议已提交至开发者！\n" +
                        "——————————————————————\n" +
                        "爱发电赞助链接：https://afdian.net/@Wutzu")));
            }
            if (content.startsWith("#点歌 ")){
                if(content.replace("#点歌 ","").equals("")){
                    event.getSubject().sendMessage("用法：#点歌 [曲名]");
                }else{
                    try {
                        NetEaseMusic.main(content.replace("#点歌 ",""), event.getSubject(), event.getSender());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (content.equals("#一言")){
                try {
                    URL url = new URL("https://saying.api.azwcl.com/saying/get");
                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                            ? httpConn.getInputStream()
                            : httpConn.getErrorStream();
                    CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream,decoder));
                    String response = bufferedReader.readLine();
                    response = JSONObject.parseObject(response).getJSONObject("data").getString("content")+"\n——"+JSONObject.parseObject(response).getJSONObject("data").getString("author");
                    event.getSubject().sendMessage(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (content.startsWith("#群发 ") && event.getSender().getId() == 2138681574){
                for (Group value : groups) {
                    value.sendMessage(new PlainText("Bot群发通知：\n"+content.replace("#群发 ","")));
                }
            }
            /*-------------------------------群管功能-----------------------------------*/
            /*try {
                URL url = new URL("https://aip.baidubce.com/oauth/2.0/token?grant_type=client_credentials&client_id=dNYphLGB3VQlwGUlETjM8g1u&client_secret=Rb64eXyrbQCj5SxPw0k1ffGvTqAGhbKc&");
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.74 Safari/537.36 Edg/99.0.1150.55");
                InputStream responseStream = httpConn.getResponseCode() / 100 == 2
                        ? httpConn.getInputStream()
                        : httpConn.getErrorStream();
                CharsetDecoder decoder = Charset.forName("utf-8").newDecoder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(responseStream,decoder));
                String response = bufferedReader.readLine();
                String accessToken = JSONObject.parseObject(response).getString("access_token");
                BaiDuAI.main(accessToken,event);
            } catch (Exception e) {
                e.printStackTrace();
            }*/

        });
        //noinspection InfiniteLoopStatement
        while(true) {
            Calendar c = Calendar.getInstance();
            int m = c.get(Calendar.MONTH);
            int d = c.get(Calendar.DAY_OF_MONTH);
            int h = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);
            int s = c.get(Calendar.SECOND);
            if (m == myData.getM() && d == myData.getD() && h == myData.getH() && min == myData.getMin() && s == myData.getS()) {
                Image[] i = new Image[6];
                i[0] = groups.get(0).uploadImage(ExternalResource.create(new File("./5.png")));
                i[1] = groups.get(0).uploadImage(ExternalResource.create(new File("./4.png")));
                i[2] = groups.get(0).uploadImage(ExternalResource.create(new File("./3.png")));
                i[3] = groups.get(0).uploadImage(ExternalResource.create(new File("./2.png")));
                i[4] = groups.get(0).uploadImage(ExternalResource.create(new File("./1.png")));
                i[5] = groups.get(0).uploadImage(ExternalResource.create(new File("./end.png")));
                for (Image image : i) {
                    for (Group group : groups) {
                        group.sendMessage(image);
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}