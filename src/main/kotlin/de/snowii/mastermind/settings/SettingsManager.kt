package de.snowii.mastermind.settings

import com.google.common.collect.Lists
import com.google.gson.GsonBuilder
import com.google.gson.JsonParseException
import de.snowii.mastermind.MasterMind
import java.io.FileNotFoundException
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Path

class SettingsManager {
    val gson = GsonBuilder().serializeNulls().setPrettyPrinting().create()
    val settingsFile: Path = MasterMind.directory.resolve("settings.json").toPath()

    init {
        try {
            if (!Files.exists(settingsFile)) {
                Files.createFile(settingsFile)
            }
        } catch (e: IOException) {
            throw RuntimeException("Failed to create Settings File", e)
        }
    }

    fun <T> load(file: Path?, clazz: Class<*>): List<T>? {
        try {
            Files.newBufferedReader(file).use { bufferedreader ->
                val type: ParameterizedType = object : ParameterizedType {
                    override fun getActualTypeArguments(): Array<Type> {
                        return arrayOf(clazz)
                    }

                    override fun getRawType(): Type {
                        return MutableList::class.java
                    }

                    override fun getOwnerType(): Type? {
                        return null
                    }
                }
                val list = gson.fromJson<List<T>>(bufferedreader, type) ?: return null
                return Lists.reverse(list)
            }
        } catch (ignored: FileNotFoundException) {
        } catch (ignored: JsonParseException) {
        } catch (e: IOException) {
            throw RuntimeException("Failed to load JSON File", e)
        }
        return null
    }

    fun save(file: Path?, list: List<*>?) {
        val json = gson.toJson(list)
        try {
            Files.newBufferedWriter(file).use { writer -> writer.write(json) }
        } catch (e: IOException) {
            throw RuntimeException("Failed to save JSON File", e)
        }
    }
}
