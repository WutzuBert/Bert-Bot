package top.berthua;

import net.mamoe.mirai.contact.Member;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WerewolvesKilled {
    public static void start(Member[] players,String[] identities){
        Map<Member,String> playersMap = IntStream.range(0, players.length).boxed()
                .collect(Collectors.toMap(i -> players[i], i -> identities[i]));

    }
}
