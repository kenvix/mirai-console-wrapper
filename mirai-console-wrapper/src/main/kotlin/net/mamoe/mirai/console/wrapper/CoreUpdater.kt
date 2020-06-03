/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */

@file:Suppress("EXPERIMENTAL_API_USAGE")

package net.mamoe.mirai.console.wrapper

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.http.isSuccess
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.io.File

private val cuiCloudAvailabilityJob = GlobalScope.async {
    kotlin.runCatching {
        Http.get<HttpStatusCode>("https://pan.jasonczc.cn").isSuccess()
    }.getOrElse { false }
}

suspend fun isCuiCloudAvailable() = cuiCloudAvailabilityJob.await()

internal object CoreUpdater {
    init {
        cuiCloudAvailabilityJob // init
    }

    fun getProtocolLib(): File? {
        contentPath.listFiles()?.forEach { file ->
            if (file != null && file.extension == "jar" && file.name.contains("qqandroid")) {
                return file
            }
        }
        return null
    }


    suspend fun versionCheck(strategy: VersionUpdateStrategy) {
        println("Fetching Newest Core Version .. ")
        val current = getCurrentVersion()
        if (current != "0.0.0" && strategy == VersionUpdateStrategy.KEEP) {
            println("Stay on current version.")
            return
        }

        val newest = getNewestVersion(strategy, "net/mamoe/mirai-core-qqandroid/")
        println()
        println("Local Core Version: $current | Newest $strategy Core Version: $newest")
        if (current != newest) {
            println("Updating shadowed-core from V$current -> V$newest")
            this.getProtocolLib()?.delete()
            MiraiDownloader
                    .download(
                            getFilePaths("mirai-core-qqandroid", newest),
                            getContent("mirai-core-qqandroid-$newest.jar")
                    )
            //.addTask("https://raw.githubusercontent.com/mamoe/mirai-repo/master/shadow/mirai-core-qqandroid/mirai-core-qqandroid-$newest.jar", getContent("mirai-core-qqandroid-jvm-$newest.jar"))
        }
    }

    /**
     * 判断当前版本
     * 默认返回 "0.0.0"
     */
    fun getCurrentVersion(): String {
        val file = getProtocolLib() ?: return "0.0.0"
        return file.name.substringBefore(".jar").substringAfter("mirai-core-qqandroid-").substringAfter("jvm-")
    }


    /*
    private suspend fun downloadCore(version: String) {
        /**
         * from github
         */


        /**
         * from jcenter
        coroutineScope {
            launch {
                tryNTimesOrQuit(3, "Failed to download newest Protocol lib, please seek for help") {
                    Http.downloadMavenArchive("net/mamoe", "mirai-core-qqandroid-jvm", version)
                        .saveToContent("mirai-core-qqandroid-jvm-$version.jar")
                }
            }

            launch {
                tryNTimesOrQuit(3, "Failed to download newest core, please seek for help") {
                    Http.downloadMavenArchive("net/mamoe", "mirai-core-jvm", version)
                        .saveToContent("mirai-core-jvm-$version.jar")
                }
            }
        }
        */
    }


     */


}