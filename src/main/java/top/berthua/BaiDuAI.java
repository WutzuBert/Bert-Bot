package top.berthua;

import com.alibaba.fastjson.JSONObject;
import com.baidubce.http.ApiExplorerClient;
import com.baidubce.http.HttpMethodName;
import com.baidubce.model.ApiExplorerRequest;
import com.baidubce.model.ApiExplorerResponse;
import net.mamoe.mirai.IMirai;
import net.mamoe.mirai._MiraiInstance;
import net.mamoe.mirai.contact.PermissionDeniedException;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;

@Deprecated
public class BaiDuAI {
    public static void main(String accessToken, GroupMessageEvent event) {
        MessageChain messageChain = event.getMessage();
        MessageContent message = messageChain.get(PlainText.Key);
        String text;
        if (message != null) {
            text = message.toString();
            textCheck(text,event,accessToken,messageChain);
        }
        Image img = messageChain.get(Image.Key);
        if (img != null) {
            IMirai iMirai = _MiraiInstance.get();
            String imgUrl = iMirai.queryImageUrl(event.getBot(), img);
            imgCheck(imgUrl,event,accessToken,messageChain);
        }


    }

    static void textCheck(String text, GroupMessageEvent event, String accessToken, MessageChain messageChain) {
        ApiExplorerClient client = new ApiExplorerClient();
        String path = "https://aip.baidubce.com/rest/2.0/solution/v1/text_censor/v2/user_defined";
        ApiExplorerRequest request = new ApiExplorerRequest(HttpMethodName.POST, path);
        request.addHeaderParameter("Content-Type", "application/json;charset=UTF-8");

        // 设置query参数
        request.addQueryParameter("access_token", accessToken);
        String jsonBody = "text=" + text;
        request.setJsonBody(jsonBody);
        try {
            ApiExplorerResponse response = client.sendRequest(request);
            // 返回结果格式为Json字符串
            String re = response.getResult();
            JSONObject jsonResponse = JSONObject.parseObject(re);
            switch (jsonResponse.getInteger("conclusionType")) {
                case (1):
                    break;
                case (2):
                    int subType = jsonResponse.getJSONArray("data").getJSONObject(0).getInteger("subType");
                    switch (subType) {
                        case (0):
                            try {
                                MessageSource.recall(messageChain);
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在百度官方默认违禁词库不合规,已被处罚\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)\n")));
                                event.getSender().mute(60);
                            } catch (PermissionDeniedException e) {
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你的消息存在百度官方默认违禁词库不合规,警告一次\n"+
                                        "相关判定结果由百度智能云提供，如有疑问请联系Bot主人："+
                                        "WutzuBert(2138681574)")));
                            }
                            break;
                        case (1):
                            try {
                                MessageSource.recall(messageChain);
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                         "你的消息存在违法言论,已被处罚\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)\n")));
                                event.getSender().mute(3600);
                            } catch (PermissionDeniedException e) {
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在违法言论,警告一次\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                            }
                            break;
                        case (2):
                            try {
                                MessageSource.recall(messageChain);
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                         "你的消息含有色情信息,已被处罚\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                                event.getSender().mute(600);
                            } catch (PermissionDeniedException e) {
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息含有色情信息,警告一次\n "
                                                +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                                +"WutzuBert(2138681574)")));
                            }
                            break;
                        case (4):
                            try {
                                MessageSource.recall(messageChain);
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在恶意推广,已被处罚\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                                event.getSender().mute(2591940);
                            } catch (PermissionDeniedException e) {
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在恶意推广,警告一次\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                            }
                            break;
                        case (5):
                            try {
                                MessageSource.recall(messageChain);
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在低俗辱骂,已被处罚\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                                event.getSender().mute(60);
                            } catch (PermissionDeniedException e) {
                                event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                                        "你的消息存在百度官方默认违禁词库不合规,警告一次\n"
                                        +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                        +"WutzuBert(2138681574)")));
                            }
                            break;
                    }
                case (3):
                    try {
                        MessageSource.recall(messageChain);
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你的消息疑似违规,暂作处罚\n"
                                +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                +"WutzuBert(2138681574)\n")));
                        event.getSender().mute(60);
                    } catch (PermissionDeniedException e) {
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你的消息疑似违规,警告一次\n"
                                +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                                +"WutzuBert(2138681574)")));
                    }
                    break;
                case (4):
                    MessageSource.recall(messageChain);
                    event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText(
                            "云AI审核失败，暂时对你的消息进行撤回\n"
                            +"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n"
                            +"WutzuBert(2138681574)")));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    static void imgCheck(String imgUrl, GroupMessageEvent event, String accessToken, MessageChain messageChain){
        try {
        String path = "https://aip.baidubce.com/rest/2.0/solution/v1/img_censor/v2/user_defined";
        String param = "imgUrl="  + imgUrl;
        String result = HttpUtil.post(path, accessToken, param);
        JSONObject data = JSONObject.parseObject(result);
            switch (data.getInteger("conclusionType")) {
                case (1):{
                }
                case (2):{
                    if (data.getJSONArray("data").getJSONObject(0).getInteger("type") != 4 &&
                            data.getJSONArray("data").getJSONObject(0).getInteger("type") != 5 &&
                            data.getJSONArray("data").getJSONObject(0).getInteger("type") != 6){
                        MessageSource.recall(messageChain);
                        event.getSubject().sendMessage(new At(event.getSender().getId()).plus(new PlainText("你的消息" + data.getJSONArray("data").getJSONObject(0).getString("msg")+"相关判定结果由百度智能云提供，如有疑问请联系Bot主人：\n" +
                                "WutzuBert(2138681574)")));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}