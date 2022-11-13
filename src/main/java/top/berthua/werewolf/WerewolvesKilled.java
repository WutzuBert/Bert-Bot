package top.berthua.werewolf;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.PlainText;

import java.util.ArrayList;
import java.util.List;

public class WerewolvesKilled {
    public static void start(Group subject, Member[] members, String[] identities){
        List<Player> players = new ArrayList<>();
        for(int x=0;x< members.length;x++){
            players.add(new Player(identities[x],members[x]));
        }
        int humans = 0;
        ArrayList<Player> werewolves = new ArrayList<>();
        boolean isGaming = true;
        while(isGaming){
            humans = 0;
            werewolves.clear();
            for(int x=0;x< players.size();x++){
                if(players.get(x).identity.equals("Werewolf") && players.get(x).isAlive){
                    werewolves.add(players.get(x));
                    players.get(x).QMember.sendMessage("你的身份是：狼人");
                }else if(players.get(x).isAlive){
                    humans++;
                    players.get(x).QMember.sendMessage("你的身份是："+players.get(x).identity);
                }
            }
            subject.sendMessage(new PlainText("天黑请闭眼"));
            for(int x=0;x< players.size();x++){
                Player player = players.get(x);
                if(player.identity.equals("Werewolf") && player.isAlive){
                    player.QMember.sendMessage("狼人请睁眼");
                    player.QMember.sendMessage("请选择 你要刀死的人\n"+players);
                }else if(players.get(x).isAlive){

                }
            }
        }
    }
}
