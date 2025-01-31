package net.guneyilmaz0.bmo.listeners

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.channel.ChannelType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.InteractionHook
import net.guneyilmaz0.bmo.command.ICommandManager

class ICommandListener : ListenerAdapter() {

    override fun onMessageReceived(event: MessageReceivedEvent) {
        if (event.author.isBot || event.channelType == ChannelType.PRIVATE) return

        val content = event.message.contentRaw
        val prefix = "-"

        if (content.startsWith(prefix)) {
            val args = content.substring(prefix.length).split(" ").toTypedArray()
            val commandString = args[0].lowercase()
            val command = ICommandManager.getCommand(commandString)

            if (command != null) {
                val commandArgs = args.copyOfRange(1, args.size)
                event.member?.let { command.execute(it, event.message, commandArgs) }
            } else event.channel.sendMessage("Böyle bir komut bulunamadı.").queue()
        }
    }

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        val commandString = event.name
        val command = ICommandManager.getCommand(commandString)

        if (command != null) {
            val args = event.options.map { it.asString }.toMutableList()
            event.deferReply().queue { hook: InteractionHook ->
                hook.retrieveOriginal().queue { message: Message ->
                    event.member?.let {
                        command.execute(it, message, args.toTypedArray())
                    }
                }
            }
        } else event.reply("Böyle bir komut bulunamadı.").setEphemeral(true).queue()
    }
}
