package me.badstagram.jdacommand.command;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {
    private final Map<String, ICommand> commands = new HashMap<>();
    private final GuildMessageReceivedEvent event;
    private final String prefix;

    public CommandManager(GuildMessageReceivedEvent event, String prefix) {
        this.event = event;
        this.prefix = prefix;
    }

    public void addCommand(ICommand cmd) {
        if (this.commands.containsKey(cmd.getName())) {
            throw new IllegalArgumentException(String.format("Command with name %s already exists", cmd.getName()));
        }

        this.commands.put(cmd.getName(), cmd);
    }

    public void dispatchCommand() {
        Message msg = this.event.getMessage();
        String content = msg.getContentRaw();
        String invoke = content.substring(1);

        if (!this.commands.containsKey(invoke)) {
            return; // command doesnt exist
        }
        if (!this.event.getMessage().getContentRaw().startsWith(this.prefix)) {
            return;
        }

        Member member = event.getMember();
        Member self = event.getGuild().getSelfMember();

        if (member == null || member.getUser().isBot() || event.isWebhookMessage()) {
            return;
        }
        CommandContext ctx = new CommandContext(event, this.prefix);
        ICommand cmd = this.commands.get(invoke);
        ApplicationInfo info = event.getJDA().retrieveApplicationInfo().complete();
        List<Long> ownerIds = new ArrayList<>();

        if (info.getTeam() == null) {
            ownerIds.add(info.getOwner().getIdLong());
        } else {
            ownerIds = info.getTeam()
                    .getMembers()
                    .stream()
                    .map(TeamMember::getUser)
                    .map(User::getIdLong)
                    .collect(Collectors.toList());
        }


        List<Permission> botPermissions = cmd.getBotPermissions() == null ? Collections.emptyList() : cmd.getBotPermissions();
        List<Permission> userPermissions = cmd.getUserPermissions()  == null ? Collections.emptyList() : cmd.getUserPermissions();

        String readableBotPermissions = botPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.joining(", "));

        String readableUserPermissions = userPermissions.stream()
                .map(Permission::getName)
                .collect(Collectors.joining(", "));


        if (!member.hasPermission(userPermissions)) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Insufficient Permissions")
                    .setDescription(String.format("You need %s permissions to run this command.", readableUserPermissions))
                    .build();

            ctx.replyOrDefault(embed, String.format("You need %s permissions to run this command.", readableUserPermissions));
            return;
        }

        if (!self.hasPermission(userPermissions)) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Insufficient Permissions")
                    .setDescription(String.format("I need %s permissions to run this command.", readableBotPermissions))
                    .build();

            ctx.replyOrDefault(embed, String.format("I need %s permissions to run this command.", readableBotPermissions));
            return;
        }

        if (cmd.isCommandRestricted() && !ownerIds.contains(member.getIdLong())) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("Insufficient Permissions")
                    .setDescription("This command is restricted to the bot owner(s).")
                    .build();

            ctx.replyOrDefault(embed, "This command is restricted to the bot owner(s).");
            return;
        }

        if (cmd.isNSFW() && !event.getChannel().isNSFW()) {
            MessageEmbed embed = new EmbedBuilder()
                    .setTitle("NSFW Command")
                    .setDescription("This command can only be used in NSFW channels.")
                    .build();

            ctx.replyOrDefault(embed, "This command can only be used in NSFW channels.");
            return;
        }

        cmd.dispatchCommand(ctx);
    }
}
