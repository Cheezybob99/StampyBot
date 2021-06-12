package me.cheezybob99.stampybot.command;

import me.cheezybob99.stampybot.Main;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.components.Button;

public class DiscordCommandAddAlertWord extends DiscordCommand implements IButton {

    public DiscordCommandAddAlertWord(Main main) {

        super(main, "addalertword", main.getConfig().getAdminRole());

    }

    @Override
    public void onCommand(SlashCommandEvent event) {

        String word = event.getOption("word").getAsString();
        if (main.getConfig().getAlertStatus().contains(word.toLowerCase())) {
            event.reply("This word is already in the status checker!").setEphemeral(true).queue();
            return;
        }

        String id = "cancel:" + word + ":" + event.getUser().getId();
        main.getCommandManager().registerButtons(id, this);
        main.getConfig().addFilterWord(word);
        event.reply("Added " + word + " to the status checker!")
                .addActionRow(Button.danger(id, "Cancel"))
                .queue();

    }

    @Override
    public CommandData buildCommand() {
        return new CommandData("addalertword", "Adds a word to the status alert")
                .addOption(OptionType.STRING, "word", "Word to be added.", true);
    }

    @Override
    public void onClick(String id, ButtonClickEvent event) {

        String[] words = id.split(":");
        String userId = words[2];
        if (!userId.equals(event.getUser().getId())) {
            event.reply("Only the sender can cancel!").setEphemeral(true).queue();
            return;
        }
        String word = words[1];
        main.getConfig().removeFilterWord(word);
        event.reply("Removed " + word + " from the status checker!").queue();
        main.getCommandManager().unregisterButton(id);

    }

}
