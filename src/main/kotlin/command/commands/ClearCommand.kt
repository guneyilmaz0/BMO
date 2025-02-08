package net.guneyilmaz0.bmo.command.commands

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.guneyilmaz0.bmo.command.ICommand
import net.guneyilmaz0.bmo.command.ICommandManager

class ClearCommand(name: String, description: String) : ICommand(name, description) {

    init {
        usageMessage = "-clear <amount>"
        permission = Permission.MESSAGE_MANAGE
        permissionMessage = "You need the `<permission>` permission to use this command."
    }

    override fun execute(member: Member, message: Message, args: Array<String>) {
        if (!testPermission(message)) return

        if (args.isEmpty()) {
            message.channel.sendMessage("Usage: $usageMessage").queue()
            return
        }

        val amount = args[0].toIntOrNull()
        if (amount == null || amount !in 1..100) {
            message.channel.sendMessage("Please provide a number between 1 and 100.").queue()
            return
        }

        val textChannel = message.channel as? TextChannel ?: run {
            message.channel.sendMessage("This command can only be used in text channels.").queue()
            return
        }

        textChannel.history.retrievePast(amount).queue { messages ->
            try {
                textChannel.purgeMessages(messages)
                message.channel.sendMessage(":white_check_mark: Deleted $amount messages.").queue()
            } catch (e: InsufficientPermissionException) {
                message.channel.sendMessage("I don't have permission to delete messages.").queue()
            }
        }
    }

    override fun createSlashCommand(name: String) {
        ICommandManager.guild?.upsertCommand(
            Commands.slash(name, description)
                .addOption(OptionType.INTEGER, "amount", "Amount of messages to delete", true)
        )?.queue()
    }
}