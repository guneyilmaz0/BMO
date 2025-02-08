package net.guneyilmaz0.bmo.command.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.guneyilmaz0.bmo.command.ICommand
import net.guneyilmaz0.bmo.command.ICommandManager
import java.util.concurrent.TimeUnit

class BanCommand(name: String, description: String) : ICommand(name, description) {

    init {
        usageMessage = "-ban <@user>"
        permission = Permission.BAN_MEMBERS
        permissionMessage = "You need the <permission> permission to use this command."
    }

    override fun execute(member: Member, message: Message, args: Array<String>) {
        if (!testPermission(message)) return

        val mentionedMember = message.mentions.members.firstOrNull()
        if (mentionedMember == null) {
            message.channel.sendMessage("Please mention a user to ban.").queue()
            return
        }

        if (!member.canInteract(mentionedMember)) {
            message.channel.sendMessage("You cannot ban this member.").queue()
            return
        }

        try {
            mentionedMember.ban(0, TimeUnit.SECONDS).queue {
                message.channel.sendMessage(":hammer: ${mentionedMember.effectiveName} has been banned.").queue()
            }
        } catch (e: InsufficientPermissionException) {
            message.channel.sendMessage("I don't have permission to ban members.").queue()
        }
    }

    override fun createSlashCommand(name: String) {
        ICommandManager.guild?.upsertCommand(
            Commands.slash(name, description)
                .addOption(OptionType.USER, "user", "Kullanıcı", true)
        )?.queue()
    }
}
