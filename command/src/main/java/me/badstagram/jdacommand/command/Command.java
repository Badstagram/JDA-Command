package me.badstagram.jdacommand.command;

import me.badstagram.jdacommand.command.utiil.FormatUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;

import java.text.Format;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Command {

    protected String name = "null";
    protected String help = "null";
    protected String arguments = "null";
    protected boolean owner = false;
    protected int cooldown = 0;
    protected String[] aliases = new String[0];
    protected Permission[] botPermissions = Permission.EMPTY_PERMISSIONS;
    protected Permission[] userPermissions = Permission.EMPTY_PERMISSIONS;


    protected abstract void execute(CommandContext ctx);

    protected final void run(CommandContext ctx) {
        Message msg = ctx.getMessage();
        User usr = msg.getAuthor();
        Member member = msg.getMember();

        if (member == null || msg.isWebhookMessage()) {
            return;
        }

        if (!msg.isFromType(ChannelType.TEXT)) {
            return;
        }

        Member self = msg.getGuild().getSelfMember();

        if (!self.hasPermission(this.botPermissions)) {

            String perms = Arrays.stream(this.botPermissions)
                    .map(Permission::getName)
                    .collect(Collectors.joining());

            ctx.getChannel().sendMessageFormat(":x: I don't have the right permissions for this command. I need %s", perms).queue();
            return;
        }

        if (!member.hasPermission(this.userPermissions)) {
            String perms = Arrays.stream(this.userPermissions)
                    .map(Permission::getName)
                    .collect(Collectors.joining());

            ctx.getChannel().sendMessageFormat(":x: You don't have the right permissions for this command. You need %s", perms).queue();
            return;
        }

        if (!usr.getId().equals(ctx.getClient().getOwnerId())) {
            ctx.getChannel().sendMessage(":x: You don't have the right permissions for this command. You need Bot Owner").queue();
            return;
        }

        long expireTime = OffsetDateTime.now()
                .plusSeconds(this.cooldown)
                .toEpochSecond();

        final String key = String.format("U:%d|S:%d", usr.getIdLong(), ctx.getGuild().getIdLong());


        if (this.cooldown > 0) {
            int remaining = ctx.getClient().getRemainingCooldown(key);

            if (remaining > 0) {
                ctx.reply(String.format("This command is on cooldown for another %s", FormatUtil.secondsToTime(remaining)));
                return;
            } else ctx.getClient().applyCooldown(key, cooldown);
        }

        this.execute(ctx);




    }


}
