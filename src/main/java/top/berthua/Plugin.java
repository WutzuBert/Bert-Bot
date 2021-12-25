package top.berthua;

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

import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

public final class Plugin extends JavaPlugin {
    public static Plugin INSTANCE = new Plugin();
    public Plugin() {
        super(new JvmPluginDescriptionBuilder("top.berthua.plugin", "1.2.7-SNAPSHOT").build());
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
        }});
        bot.login();
        ArrayList<Group> groups = new ArrayList<>();
        List<Long> groupids = myData.getGroups();
        for(int t = 0;t<groupids.size();t++){
            groups.add(bot.getGroup(groupids.get(t)));
        }
        for(int c = 0;c<groups.size();c++){
            groups.get(c).sendMessage(new PlainText("Bert Bot已加载！By Bert"));
        }
        List<String> keywords = myData.getKeywords();//初始化关键词
        GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, event -> {
            MessageChain chain = event.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent
            String content = chain.contentToString();
            if (content.equals("test")) {
                event.getSubject().sendMessage("test!"); // 回复消息
            }
            if (content.equals("骰子")) {
                Random rand = new Random();
                int temp = 1 + rand.nextInt(5);
                event.getSubject().sendMessage(new Dice(temp));
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
                event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("建议已提交至开发者！")));
            }
            /*-------------------------------群管功能-----------------------------------*/
            for(int t = 0;t<keywords.size();t++){
                if(content.contains(keywords.get(t))){
                    MessageSource.recall(chain);
                    event.getSender().mute(60);
                    event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("你触发了违禁词“"+keywords.get(t)+"”")));
                }
            }
            MemberPermission permission = event.getSender().getPermission();
            if(content.contains("#添加关键词 ")){

                if(permission == MemberPermission.MEMBER){
                    event.getSubject().sendMessage((new At(event.getSender().getId()).plus(new PlainText("仅管理员可进行该操作！"))));
                }else{
                    keywords.add(content.replace("#添加关键词 ",""));
                    event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("成功添加关键词“"+content.replace("#添加关键词 ","")+"”")));
                }
            }
            if(content.contains("#删除关键词 ")){
                if(permission == MemberPermission.MEMBER){
                    event.getSubject().sendMessage((new At(event.getSender().getId()).plus(new PlainText("仅管理员可进行该操作！"))));
                }else{
                    if(keywords.contains(content.replace("#删除关键词 ",""))){
                        for(int t=0;t<keywords.size();t++){
                            if(keywords.get(t).equals(content.replace("#删除关键词 ",""))){
                                keywords.remove(t);
                            }
                        }
                        event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("成功删除关键词“"+content.replace("#删除关键词 ","”"))));
                    }else{
                        event.getSubject().sendMessage((new At(event.getSender().getId())).plus(new PlainText("未找到关键词“"+content.replace("#删除关键词 ","“，请尝试”#列出关键词”检查关键词是否存在"))));
                    }

                }
            }
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
                for(int t = 0;t < i.length;t++){
                    for(int a = 0;a < groups.size();a++){
                        groups.get(a).sendMessage(i[t]);
                    }
                    try{
                        Thread.sleep(1000);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}