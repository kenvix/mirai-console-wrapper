/*
 * Copyright 2020 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/mamoe/mirai/blob/master/LICENSE
 */
package net.mamoe.mirai.console.wrapper

import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

class FilePath(delegate: List<Pair<String, String>>) : List<Pair<String, String>> by delegate

internal object MiraiDownloader {
    suspend fun download(from: FilePath, to: File) {
        from.forEach {
            println("${it.first}: ${it.second}")
        }

        for (s in from) {
            kotlin.runCatching {
                if (!UIMode) {
                    download(DownloadTask(s.first, s.second, to), MiraiDownloaderProgressBarInTerminal())
                } else {
                    download(DownloadTask(s.first, s.second, to), MiraiDownloaderProgressBarInUI())
                }
            }.fold(
                    onSuccess = {
                        return
                    },
                    onFailure = {
                        println()
                        println("无法从 ${s.first} 下载. 正在更换下一个源")
                    }
            )
        }
    }
}

class DownloadTask(
        val provider: String,
        val fromUrl: String,
        val toFile: File
)

@OptIn(KtorExperimentalAPI::class)
suspend fun download(
        task: DownloadTask,
        bar: MiraiDownloadProgressBar
) = coroutineScope {
    val totalDownload = AtomicInteger(0)
    val totalSize = AtomicInteger(0)

    bar.ad(task.provider)

    val progressJob = launch {
        while (true) {
            bar.update(totalDownload.get().toFloat() / totalSize.get(), (totalSize.get() / (1024 * 1024)).toString() + "MB")
            delay(1000 / 60) // 60 fps
        }
    }

    withContext(Dispatchers.IO) {
        val con = if (Http.engineConfig.proxy != null) URL(task.fromUrl).openConnection(Http.engineConfig.proxy) else URL(task.fromUrl).openConnection()
        con as HttpURLConnection
        val input = con.inputStream
        totalSize.addAndGet(con.contentLength)
        val outputStream = FileOutputStream(task.toFile)
        var len: Int
        val buff = ByteArray(1024)
        while (input.read(buff).also { len = it } != -1) {
            totalDownload.addAndGet(buff.size)
            outputStream.write(buff, 0, len)
        }
    }

    progressJob.cancel()

    bar.update(1F, "Complete")
    bar.complete()
}


interface MiraiDownloadProgressBar {
    fun reset()
    fun update(rate: Float, message: String)
    fun complete()
    fun ad(message: String)
}

class MiraiDownloaderProgressBarInTerminal : MiraiDownloadProgressBar {

    override fun reset() {
        print('\r')
    }

    override fun ad(message: String) {
        println()
        println(message)
    }

    private val barLen = 40

    override fun update(rate: Float, message: String) {
        reset()
        print("Progress: ")
        val len = (rate * barLen).toInt()
        for (i in 0 until len) {
            print("#")
        }
        for (i in 0 until barLen - len) {
            print(" ")
        }
        print("  | $message")
    }

    override fun complete() {
        println()
    }
}

class MiraiDownloaderProgressBarInUI : MiraiDownloadProgressBar {

    override fun reset() {
        WrapperMain.uiBarOutput.clear()
    }

    override fun ad(message: String) {
        WrapperMain.uiLog(message)
    }

    private val barLen = 20

    override fun update(rate: Float, message: String) {
        reset()
        WrapperMain.uiBarOutput.append("Progress: ")
        val len = (rate * barLen).toInt()
        for (i in 0 until len) {
            WrapperMain.uiBarOutput.append("#")
        }
        for (i in 0 until barLen - len) {
            WrapperMain.uiBarOutput.append(" ")
        }
        WrapperMain.uiBarOutput.append("  | $message")
    }

    override fun complete() {
    }

}


