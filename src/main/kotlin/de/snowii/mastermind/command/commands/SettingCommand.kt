package de.snowii.mastermind.command.commands

import de.snowii.mastermind.command.Command

class SettingCommand : Command("Setting", "Sets Module setting", "s", "config", "val") {
    override fun onCommand(args: Array<String>) {
        /*if (args.size >= 4) {
            for (module in ModuleManager.modules) {
                if (module.name.equals(args[1], ignoreCase = true)) {
                    for (setting in module.settings) {
                        if (setting.name.equals(args[2], ignoreCase = true)) {
                            when (setting) {
                                is SettingBoolean -> {
                                    if (args[3].equals("true", ignoreCase = true)) {
                                        setting.value = true
                                    } else if (args[3].equals("false", ignoreCase = true)) {
                                        setting.value = false
                                    } else PlayerUtil.sendMessage(ChatFormatting.RED.toString() + setting.name + " must be True|False")
                                    PlayerUtil.sendMessage(ChatFormatting.GREEN.toString() + "Set " + module.name + " -> " + setting.name + " to " + setting.value)
                                    return
                                }

                                is SettingInt -> {
                                    try {
                                        val i = args[3].toInt()
                                        if (i > setting.max || i < setting.min) {
                                            PlayerUtil.sendMessage(ChatFormatting.RED.toString() + setting.name + " must be a Number between " + setting.min + "-" + setting.max)
                                            return
                                        }
                                        setting.value = i
                                    } catch (e: NumberFormatException) {
                                        PlayerUtil.sendMessage(ChatFormatting.RED.toString() + setting.name + " must be a Number between " + setting.min + "-" + setting.max)
                                    }
                                    PlayerUtil.sendMessage(ChatFormatting.GREEN.toString() + "Set " + module.name + " -> " + setting.name + " to " + setting.value)
                                    return
                                }

                                is SettingFloat -> {
                                    try {
                                        val i = args[3].replace(",", ".").toFloat()
                                        if (i > setting.max || i < setting.min) {
                                            PlayerUtil.sendMessage(ChatFormatting.RED.toString() + setting.name + " must be a Number between " + setting.min + "-" + setting.max)
                                            return
                                        }
                                        setting.value = i
                                    } catch (e: NumberFormatException) {
                                        PlayerUtil.sendMessage(ChatFormatting.RED.toString() + setting.name + " must be a Float between " + setting.min + "-" + setting.max)
                                    }
                                    PlayerUtil.sendMessage(ChatFormatting.GREEN.toString() + "Set " + module.name + " -> " + setting.name + " to " + setting.value)
                                    return
                                }

                                is SettingString -> {
                                    setting.value = args[3]
                                    PlayerUtil.sendMessage(ChatFormatting.GREEN.toString() + "Set " + module.name + " -> " + setting.name + " to " + setting.value)
                                    return
                                }

                                is SettingMode -> {
                                    for (mode in setting.modes) {
                                        if (mode.equals(setting.getMode(), ignoreCase = true)) {
                                            setting.setMode(args[3])
                                            PlayerUtil.sendMessage(ChatFormatting.GREEN.toString() + "Set " + module.name + " -> " + setting.name + " to " + setting.getMode())
                                            return
                                        }
                                    }
                                    PlayerUtil.sendMessage(ChatFormatting.RED.toString() + "Mode " + args[3] + " is not Valid")
                                    return
                                }
                            }
                        }
                    }
                    PlayerUtil.sendMessage(ChatFormatting.RED.toString() + "Unknown Setting " + args[2])
                    return
                }
            }
            PlayerUtil.sendMessage("Unknown Module: " + args[1])
        } else PlayerUtil.sendMessage(".s <module> <setting> <value>")
    }*/
    }
}
