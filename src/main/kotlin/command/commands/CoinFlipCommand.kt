package net.guneyilmaz0.bmo.command.commands

import kotlinx.coroutines.*
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.guneyilmaz0.bmo.command.ICommand
import net.guneyilmaz0.bmo.command.ICommandManager
import kotlin.random.Random

class CoinFlipCommand(name: String, description: String) : ICommand(name, description) {

    companion object {
        private val activeChannels = mutableSetOf<Long>()
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun execute(member: Member, message: Message, args: Array<String>) {
        val channelId = message.channel.idLong

        if (activeChannels.contains(channelId)) {
            message.reply("A coin flip is already in progress in this channel. Please wait.").queue()
            return
        }

        activeChannels.add(channelId)

        message.channel.sendMessage("<a:coinflip:1233732940949885039> Flipping...").queue { sentMessage ->
            GlobalScope.launch {
                delay(2000)
                val result = if (Random.nextBoolean()) "<:head:1233732880027615273> Heads!" else "<:tail:1233733238992801813> Tails!"
                sentMessage.editMessage(":coin: Coinflip result: $result").queue {
                    activeChannels.remove(channelId)
                }
            }
        }
    }

    override fun createSlashCommand(name: String) {
        ICommandManager.guild?.upsertCommand(
            Commands.slash(name, description)
        )?.queue()
    }
}
