package net.guneyilmaz0.bmo

import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.requests.GatewayIntent
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder
import net.dv8tion.jda.api.sharding.ShardManager
import net.dv8tion.jda.api.utils.cache.CacheFlag
import net.guneyilmaz0.bmo.listeners.*
import net.guneyilmaz0.mongos.MongoS

class Main {

    companion object {
        lateinit var instance: Main
        lateinit var database: MongoS
        lateinit var shardManager: ShardManager

        @JvmStatic
        fun main(args: Array<String>) {
            Main()
        }
    }

    init {
        instance = this
        database = MongoS("mongodb://localhost:27017", "discord")

        //Initialize the shard manager
        shardManager = DefaultShardManagerBuilder.createDefault(database.getString("settings", "token")).apply {
            setStatus(OnlineStatus.ONLINE)
            enableCache(CacheFlag.entries)
            enableIntents(GatewayIntent.entries)
            addEventListeners(ICommandListener())
            addEventListeners(GuildListener())
        }.build()
    }
}