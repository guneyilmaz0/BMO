package net.guneyilmaz0.bmo.listeners

import kotlinx.coroutines.runBlocking
import net.dv8tion.jda.api.events.guild.GuildReadyEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.guneyilmaz0.bmo.command.ICommandManager

class GuildListener : ListenerAdapter() {

    override fun onGuildReady(event: GuildReadyEvent) {
        runBlocking { ICommandManager.init(event.guild) }
    }
}