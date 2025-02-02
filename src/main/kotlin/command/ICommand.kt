package net.guneyilmaz0.bmo.command

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message

abstract class ICommand(val name: String) {
    var description: String = ""
    var usageMessage: String = ""
    var aliases: Array<String> = emptyArray()
    var permission: Permission? = null
    var permissionMessage: String = ""

    constructor(name: String, description: String) : this(name) {
        this.description = description
    }

    constructor(name: String, description: String, usageMessage: String) : this(name, description) {
        this.usageMessage = usageMessage
    }

    constructor(name: String, description: String, usageMessage: String, aliases: Array<String>) : this(
        name,
        description,
        usageMessage
    ) {
        this.aliases = aliases
    }

    abstract fun execute(member: Member, message: Message, args: Array<String>)

    abstract fun createSlashCommand(name: String)

    fun testPermission(message: Message): Boolean {
        if (testPermissionSilent(message)) return true

        val response = if (permissionMessage.isEmpty()) "Command not found."
        else permissionMessage.replace("<permission>", permission?.name ?: "")
        message.channel.sendMessage(response).queue()
        return false
    }

    private fun testPermissionSilent(message: Message) = permission?.let { message.member?.hasPermission(it) } ?: true
}