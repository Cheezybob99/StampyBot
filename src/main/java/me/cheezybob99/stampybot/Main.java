package me.cheezybob99.stampybot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Main {

    private static Main main;
    private final Config config;
    private final JDA jda;

    public static void main(String[] args)  {
        try {
            main = new Main();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bot failed to start!");
            System.exit(1);
        }
    }

    public Main() throws Exception {

        this.config = Config.loadConfig("config.json", Config.class);
        this.jda = JDABuilder.createDefault(config.getToken())
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(CacheFlag.ACTIVITY)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES)
                .build()
                .awaitReady();
        jda.addEventListener(new CommandManager(this));
        new ListenerStatusChange(this);

    }

    public Config getConfig() {
        return config;
    }

    public JDA getJda() {
        return jda;
    }

}
