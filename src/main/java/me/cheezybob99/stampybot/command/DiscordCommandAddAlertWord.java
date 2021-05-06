package me.cheezybob99.stampybot.command;

import me.cheezybob99.stampybot.Main;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

public class DiscordCommandAddAlertWord extends DiscordCommand {

    public DiscordCommandAddAlertWord(Main main) {

        super(main, "addalertword", main.getConfig().getAdminRole());

    }

    @Override
    public void onCommand(SlashCommandEvent event) {

        String word = event.getOption("word").getAsString();
        if (main.getConfig().getStatsRoles().contains(word)) {
            event.reply("This word is already in the status checker!").setEphemeral(true).queue();
            return;
        }

        main.getConfig().addFilterWord(word);
        event.reply("Added " + word + " to the status checker!").queue();

    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("addalertword", "Adds a word to the status alert")
                .addOption(new OptionData(OptionType.STRING, "word", "Word to be added.").setRequired(true));
    }

}
