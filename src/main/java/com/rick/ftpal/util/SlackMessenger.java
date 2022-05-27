package com.rick.ftpal.util;

import com.github.seratch.jslack.Slack;
import com.github.seratch.jslack.api.methods.SlackApiException;
import com.github.seratch.jslack.api.methods.request.chat.ChatPostMessageRequest;
import com.github.seratch.jslack.api.methods.response.chat.ChatPostMessageResponse;
import com.rick.ftpal.entity.Execution;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SlackMessenger {
    private static List<String> ACTOR_BLACK_LIST = new ArrayList<>();

    static {
        ACTOR_BLACK_LIST.add("wukong");
    }

    public static void sendMaintenanceMessage(String channelName, String text) {
        Slack slack = Slack.getInstance();
        ChatPostMessageResponse response = null;
        try {
            response = slack.methods().chatPostMessage(
                    ChatPostMessageRequest.builder()
                            .token(Constants.RAYLIS_SLACK_TOKEN)
                            .username(Constants.SLACK_USER_NAME)
                            .iconUrl(Constants.SLACK_ICON_URL)
                            .channel(channelName)
                            .text(String.format("Hi <@toncai> <@runzhou>\n") + text)
                            .build());
            if (StringUtils.isNoneBlank(response.getError())) {
                log.error("Slack Message sent with response error message [{}]", response.getError());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SlackApiException e) {
            e.printStackTrace();
        }
    }

    public static void sendThreadMessage(String actor, String channelName, String threadTs, String text) {
        if (ACTOR_BLACK_LIST.contains(actor)) {
            log.info(actor + "in ACTOR_BLACK_LIST");
            return;
        }
        if (StringUtils.isBlank(threadTs)) {
            log.error("Skip sending message since threadTs is empty");
            return;
        }
        try {
            Slack slack = Slack.getInstance();
            ChatPostMessageResponse response = slack.methods().chatPostMessage(
                    ChatPostMessageRequest.builder()
                            .token(Constants.RAYLIS_SLACK_TOKEN)
                            .username(Constants.SLACK_USER_NAME)
                            .iconUrl(Constants.SLACK_ICON_URL)
                            .threadTs(threadTs)
                            .channel(channelName)
                            .text(String.format("Hi <@%s>\n", actor) + text)
                            .build());
            if (StringUtils.isNoneBlank(response.getError())) {
                log.error("Slack Message sent with response error message [{}]", response.getError());
            }
        } catch (IOException e) {
            log.error("sendMessage failed due to IOException", e);
        } catch (SlackApiException e) {
            log.error("sendMessage failed due to SlackApiException", e);
        }
    }

    public static void sendFailedOnlyMessage(String channelName, Execution execution) {
        if (ACTOR_BLACK_LIST.contains(execution.getActor())) {
            log.info(execution.getActor() + "in ACTOR_BLACK_LIST");
            return;
        }
        if (StringUtils.isBlank(execution.getThreadTs())) {
            log.error("Skip sending message since threadTs is empty");
            return;
        }

        try {
            Slack slack = Slack.getInstance();
            ChatPostMessageResponse response = slack.methods().chatPostMessage(
                    ChatPostMessageRequest.builder()
                            .token(Constants.RAYLIS_SLACK_TOKEN)
                            .username(Constants.SLACK_USER_NAME)
                            .iconUrl(Constants.SLACK_ICON_URL)
                            .threadTs(execution.getThreadTs())
                            .channel(channelName)
                            .text(buildFTPalFailedMessage(execution))
                            .build());
            if (StringUtils.isNoneBlank(response.getError())) {
                log.error("Slack Message sent with response error message [{}]", response.getError());
            }
        } catch (IOException e) {
            log.error("sendMessage failed due to IOException", e);
        } catch (SlackApiException e) {
            log.error("sendMessage failed due to SlackApiException", e);
        }
    }

    private static String buildFTPalFailedMessage(Execution execution) {
        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        sb.append("{\n");
        sb.append("    \"component\": \"" + execution.getComponent() + "\",\n");
        sb.append("    \"version\": \"" + execution.getVersion() + "\",\n");
        sb.append("    \"prid\": \"" + execution.getPrId() + "\",\n");
        sb.append("    \"suite\": \"jdcloud_failed.xml\",\n");
        if (!execution.getRepo().equalsIgnoreCase("Compliance-R")) {
            sb.append("    \"repo\": \"" + execution.getRepo() + "\",\n");
        }
        if (!execution.getBranch().equalsIgnoreCase("develop")) {
            sb.append("    \"branch\": \"" + execution.getBranch() + "\",\n");
        }
        sb.append("    \"ntid\": \"" + execution.getActor() + "\"\n");
        sb.append("}\n");
        sb.append("```");
        return sb.toString();
    }

}
