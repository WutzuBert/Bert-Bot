package top.berthua.werewolf;

import net.mamoe.mirai.contact.Member;

public class Player {
    public Player(String identity,Member QMember){
        this.identity = identity;
        this.QMember = QMember;
    }
    public String identity;
    public Member QMember;
    public boolean isAlive = true;
    @Override
    public String toString(){
        return QMember.getNick()+"\n";
    }
}
