package de.snowii.mastermind.command

abstract class Command {
    val name: String
    val description: String
    var aliases: Array<out String>? = null

    constructor(name: String, description: String) {
        this.name = name
        this.description = description
    }

    constructor(name: String, description: String, vararg aliases: String) {
        this.name = name
        this.description = description
        this.aliases = aliases
    }

    abstract fun onCommand(args: Array<String>)
}
