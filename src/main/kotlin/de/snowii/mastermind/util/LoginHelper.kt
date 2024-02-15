package de.snowii.mastermind.util

import com.mojang.authlib.yggdrasil.YggdrasilEnvironment
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService
import de.snowii.mastermind.MasterMind.Companion.LOGGER
import net.ccbluex.liquidbounce.authlib.account.MicrosoftAccount
import net.ccbluex.liquidbounce.authlib.account.MinecraftAccount
import net.ccbluex.liquidbounce.authlib.yggdrasil.clientIdentifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.session.ProfileKeys
import net.minecraft.client.session.Session
import net.minecraft.util.Util
import java.net.Proxy
import java.util.*

class LoginHelper {
    enum class LoginMode {
        MICROSOFT,
        CRACKED
    }

    companion object {
        fun loginCracked() {
            // TODO
        }

        fun loginMicrosoft() {
            MicrosoftAccount.buildFromOpenBrowser(object : MicrosoftAccount.OAuthHandler {
                override fun authError(error: String) {
                    error(error)
                }

                override fun authResult(account: MicrosoftAccount) {
                    loginDirectAccount(account)
                }

                override fun openUrl(url: String) {
                    Util.getOperatingSystem().open(url)
                }
            })
        }

        // Thx liquidbounce
        fun loginDirectAccount(account: MinecraftAccount) = runCatching {
            val mc = MinecraftClient.getInstance()
            val (compatSession, service) = account.login()
            val session = Session(
                compatSession.username, compatSession.uuid, compatSession.token,
                Optional.empty(),
                Optional.of(clientIdentifier),
                Session.AccountType.byName(compatSession.type)
            )

            var profileKeys = ProfileKeys.MISSING
            runCatching {
                val environment = YggdrasilEnvironment.PROD.environment
                val userAuthenticationService =
                    YggdrasilUserApiService(session.accessToken, Proxy.NO_PROXY, environment)
                profileKeys = ProfileKeys.create(userAuthenticationService, session, mc.runDirectory.toPath())
            }.onFailure {
                LOGGER.error("Failed to create profile keys for ${session.username} due to ${it.message}")
            }

            mc.session = session
            mc.sessionService = service.createMinecraftSessionService()
            mc.profileKeys = profileKeys

        }.onFailure {
            LOGGER.error("Failed to login into account", it)
        }.getOrThrow()

    }

}