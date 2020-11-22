package me.badstagram.jdacommand.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandContext implements ICommandContext {
    private final GuildMessageReceivedEvent event;
    private final CommandClient client;
    private final String argsRaw;


    public CommandContext(GuildMessageReceivedEvent event, String argsRaw, CommandClient client) {
        this.event = event;
        this.client = client;
        this.argsRaw = argsRaw;
    }

    @Override
    public List<String> getArgs() {
        return Arrays.asList(this.argsRaw.split("\\s+"));

    }

    @Override
    public Guild getGuild() {
        return event.getGuild();
    }

    @Override
    public Member getMember() {
        return event.getMember();
    }

    @Override
    public User getAuthor() {
        return event.getAuthor();
    }

    @Override
    public JDA getJDA() {
        return event.getJDA();
    }

    @Override
    public Message getMessage() {
        return event.getMessage();
    }

    @Override
    public GuildMessageReceivedEvent getEvent() {
        return event;
    }

    @Override
    public TextChannel getChannel() {
        return event.getChannel();
    }

    @Override
    public Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }

    @Override
    public SelfUser getSelfUser() {
        return this.getJDA().getSelfUser();
    }


    @Override
    public void reply(String msg) {
        this.getChannel().sendMessage(msg).queue();
    }

    @Override
    public void replyOrDefault(MessageEmbed embed, String msg) {

        if (!this.getSelfMember().hasPermission(this.getChannel(), Permission.MESSAGE_EMBED_LINKS)) {
            this.reply(msg);
            return;
        }

        this.getChannel().sendMessage(embed).queue();
    }

    @Override
    public void replyPinging(String msg) {
        this.reply(String.format("%s, %s", this.getAuthor().getAsMention(), msg));
    }

    @Override
    public CommandClient getClient() {
        return null;
    }
}
