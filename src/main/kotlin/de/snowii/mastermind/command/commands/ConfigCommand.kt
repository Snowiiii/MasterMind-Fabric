package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command

class ConfigCommand : Command("Config", "Load,Save Configs!", "c") {
    override fun onCommand(args: Array<String>) {
        /* if (args.size >= 3) {
             if (args[1].equals("save", ignoreCase = true)) {
                 Vent.instance.configManager!!.saveCurrent(args[2] + ".json")
                 PlayerUtil.sendMessage("Config saved: " + args[2])
                 return
             } else if (args[1].equals("load", ignoreCase = true)) {
                 if (!Vent.instance.configManager!!.loadConfig(args[2] + ".json")) {
                     PlayerUtil.sendMessage("No Config found: " + args[2])
                     return
                 }
                 PlayerUtil.sendMessage("Config loaded: " + args[2])
                 return
             }
             PlayerUtil.sendMessage(".c <load/save> <name>")
         } else PlayerUtil.sendMessage(".c <load/save> <name>")*/
    }
}
