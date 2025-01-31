package net.guneyilmaz0.bmo.command.commands

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.guneyilmaz0.bmo.command.ICommandManager
import net.guneyilmaz0.bmo.command.ICommand

class AvatarCommand(name: String, description: String) : ICommand(name, description) {

    override fun execute(member: Member, message: Message, args: Array<String>) {
        var target = member
        if (message.mentions.members.isNotEmpty()) target = message.mentions.members[0]

        message.channel.sendMessageEmbeds(EmbedBuilder().apply {
            setAuthor(target.user.name, null, target.user.avatarUrl)
            setTitle("Link", target.user.avatarUrl + "?size=1024")
            setColor(target.color)
            setImage(target.user.avatarUrl + "?size=1024")
            setFooter("Requested by " + member.user.name, member.user.avatarUrl)
        }.build()).queue()
    }

    override fun createSlashCommand(name: String) {
        ICommandManager.guild?.upsertCommand(
            Commands.slash(name, description)
                .addOption(OptionType.USER, "user", "Kullanıcı", false)
        )?.queue()
    }
}