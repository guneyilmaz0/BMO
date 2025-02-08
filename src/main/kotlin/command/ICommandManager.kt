package net.guneyilmaz0.bmo.command

import kotlinx.coroutines.coroutineScope
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.interactions.commands.Command
import net.guneyilmaz0.bmo.command.commands.*
import java.util.function.Consumer

class ICommandManager {

    companion object {
        var guild: Guild? = null
        private val commands = mutableMapOf<String, ICommand>()

        suspend fun init(guild: Guild) {
            coroutineScope {
                Companion.guild = guild

                // Register commands
                registerCommand(AvatarCommand("avatar", "Shows the avatar of the user"))
                registerCommand(BanCommand("ban", "Bans a user from the server"))
                registerCommand(ClearCommand("clear", "Deletes messages in a channel"))
                registerCommand(CoinFlipCommand("coinflip", "Flips a coin and shows the result"))
                registerCommand(KickCommand("kick", "Kicks a member from the server"))

                guild.retrieveCommands().queue((Consumer<List<Command>> { guildCommands: List<Command> ->
                    for (command in guildCommands) if (!commands.containsKey(command.name)) guild.deleteCommandById(
                        command.idLong
                    ).queue()
                }))
            }
        }

        private fun registerCommand(cmd: ICommand) {
            val cmdNames = listOf(cmd.name) + cmd.aliases
            cmdNames.forEach { cmdName ->
                commands[cmdName] = cmd
                cmd.createSlashCommand(cmdName)
            }
        }

        fun getCommand(command: String): ICommand? = commands[command]
    }
}
