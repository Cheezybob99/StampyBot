package me.cheezybob99.stampybot;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Activity.ActivityType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ListenerStatusChange {

    private final Main main;
    private final HashMap<String, String> statusCache;

    public ListenerStatusChange(Main main) {

        this.main = main;
        this.statusCache = new HashMap<>();
        work();

    }

    private void work() {

        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(() -> {

            for (Member member : main.getJda().getGuildById(main.getConfig().getGuild()).getMembers()) {

                for (Activity activity : member.getActivities()) {

                    if (activity.getType() == ActivityType.CUSTOM_STATUS) {

                        String name = activity.getName();
                        List<String> bannedWords = main.getConfig().getAlertStatus();

                        if (statusCache.containsKey(member.getId()) && statusCache.get(member.getId()).equals(name)) {
                            return;
                        }

                        for (String s : name.split(" ")) {

                            if (bannedWords.contains(s.toLowerCase())) {

                                TextChannel channel = main.getJda().getTextChannelById(main.getConfig().getStatusChannel());

                                if (channel == null) {
                                    return;
                                }

                                channel.sendMessage(member.getAsMention() + " may have an inappropriate status! (" + name + ")").queue();
                                statusCache.put(member.getId(), name);

                            }

                        }

                    }

                }

            }

        }, 10, 30, TimeUnit.SECONDS);

    }

}
