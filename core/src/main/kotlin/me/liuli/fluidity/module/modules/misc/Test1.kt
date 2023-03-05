/*
 * This file is part of Fluidity Utility Mod.
 * Use of this source code is governed by the GPLv3 license that can be found in the LICENSE file.
 */

package me.liuli.fluidity.module.modules.misc

import com.google.gson.JsonParser
import io.netty.buffer.Unpooled
import me.liuli.fluidity.event.Listen
import me.liuli.fluidity.event.PacketEvent
import me.liuli.fluidity.event.Render3DEvent
import me.liuli.fluidity.module.Module
import me.liuli.fluidity.module.ModuleCategory
import me.liuli.fluidity.util.client.displayAlert
import me.liuli.fluidity.util.mc
import net.minecraft.network.PacketBuffer
import net.minecraft.network.play.client.C17PacketCustomPayload
import net.minecraft.network.play.server.S38PacketPlayerListItem
import net.minecraft.network.play.server.S3FPacketCustomPayload
import net.minecraft.util.Matrix4f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.net.Proxy
import java.net.URL
import java.util.*
import javax.imageio.ImageIO
import kotlin.concurrent.thread


class Test1 : Module("Test", "testing purpose", ModuleCategory.MISC) {

    @Listen
    fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S38PacketPlayerListItem) {
            if (packet.action == S38PacketPlayerListItem.Action.ADD_PLAYER && packet.entries.size == 1) {
                val entry = packet.entries.first()
                val name = entry.profile.name
                name.forEach {
                    if ((it !in 'a'..'z') && (it !in 'A'..'Z') && (it !in '0'..'9') && it != '_') return
                }
                displayAlert(name)
//                mc.thePlayer.sendChatMessage("你好, $name!")
            }
        } else if (packet is S3FPacketCustomPayload) {
            val data = packet.bufferData.array()
            if (packet.channelName == "MC|Brand" && mc.netHandler != null) {
                displayAlert("EMIT")
                val salutation = arrayOf("FML|HS", "FML", "FML|MP", "CustomSkinLoader", "UView").joinToString("\u0000")
                mc.netHandler.addToSendQueue(C17PacketCustomPayload("REGISTER", PacketBuffer(Unpooled.wrappedBuffer(salutation.toByteArray(Charsets.UTF_8)))))
                mc.netHandler.addToSendQueue(C17PacketCustomPayload("CustomSkinLoader", PacketBuffer(Unpooled.wrappedBuffer(UUID.randomUUID().toString().toByteArray()))))
                mc.netHandler.addToSendQueue(C17PacketCustomPayload("MC|Brand", PacketBuffer(Unpooled.wrappedBuffer("fml,forge".toByteArray()))))
            } else if (packet.channelName == "CustomSkinLoader") {
                displayAlert(packet.channelName)
                displayAlert(data.toString(Charsets.UTF_8))
                thread {
                    val data1 = ByteArrayOutputStream().also {
//                        val rand = Random(System.currentTimeMillis())
//                        val img = BufferedImage(1280, 720, 1)
//                        for (x in 0 until img.width) {
//                            for (y in 0 until img.height) {
//                                img.setRGB(x, y, rand.nextInt())
//                            }
//                        }
//                        ImageIO.write(img, "png", File("upload.png"))
                        val img = ImageIO.read(File("103209767_p0_master1200.jpg"))
                        ImageIO.write(img, "jpg", it)
                    }.toByteArray()
                    val resp = postFile("https://upload.server.domcer.com:25566/uploadJpg?key=0949a0d0-bc98-4535-9f5e-086835123f75&type=${data.toString(Charsets.UTF_8)}",
                        mapOf("file" to data1, "check" to "[\"419C05FE9BE71F792B2D76CFC9B67F1ED0FEC7F6\u0000soundsystem-20120107.jar\",\"070FA623BD47BE6ED9AF84BDD03C8172B188CD8B\u00004640208077513769652@3@0.jar\",\"99BF196CF2CA7CD630805D1C458ADBE0A99A0006\u0000config-1.2.1.jar\",\"320A2DFD18513A5F41B4E75729DF684488CBD925\u0000twitch-6.5.jar\",\"5EB80E4A779F012F4291B46FE0A36ABC357A03BD\u0000scala-continuations-plugin_2.11.1-1.0.2.jar\",\"9CE04E34240F674BC72680F8B843B1457383161A\u0000commons-codec-1.9.jar\",\"04B1C6F12CB04DBA033998B61E85118D80F0FA07\u00004649722094017330077@2@8.jar\",\"5E13BC04B72675A1FBA18785AAC01225445AE871\u00004649722094010538690@2@8.jar\",\"000FF86C9E10B8EA4DBF8164623D3B6580BA029E\u00004618827437296985101@3@0.jar\",\"803FF252FEDBD395BAFFD43B37341DC4A150A554\u0000jna-3.4.0.jar\",\"0BFC353F1258A6FE4DB6A4C546E7F4E49D06BA41\u0000scala-xml_2.11-1.0.2.jar\",\"A698750C16740FD5B3871425F4CB3BBAA87F529D\u0000commons-compress-1.8.1.jar\",\"3413ED8D96751B3E9E1A9703FC3D8FD3CD720652\u00004618419806295460477@3@0.jar\",\"90A3822C38EC8C996E84C16A3477EF632CBC87A3\u0000commons-lang3-3.3.2.jar\",\"31FBBFF1DDBF98F3AA7377C94D33B0447C646B6E\u0000httpcore-4.3.2.jar\",\"E14210EF2CD6DC4AC518A6F5B7E02A03DEB453FF\u00004649722094055818144@2@8.jar\",\"79846BA34CBD89E2422D74D53752F993DCC2CCAF\u0000vecmath-1.5.2.jar\",\"AE9094480BC6B2928A2B7C3CF1E637ACD514FB31\u00004620608833856825631@3@0.jar\",\"0294104AAF1781D6A56A07D561E792C5D0C95F45\u0000netty-all-4.0.23.Final.jar\",\"B84D2F815339D086F90E7437F584A3FEEEB8E69A\u00004649506805155167546@3@0.jar\",\"7AFE0626116F76479E8FE0A26E8F6D5C11E36695\u0000scala-swing_2.11-1.0.1.jar\",\"A94B1541B04EB2C231E893F0369A2BD7A88890EB\u0000scala-continuations-library_2.11-1.0.2.jar\",\"8F363B35FD4DF1389F3CD9408C5BE2B23B61E9A0\u00004649722094062504392@2@8.jar\",\"88F957BF19BD35FA25F55D6A0F03A4E18708890B\u00004649722093995280242@2@8.jar\",\"521616DC7487B42BEF0E803BD2FA3FAF668101D7\u0000lzma-0.0.1.jar\",\"BD4444D738A1F29732DEB22B8F8C0527B765EA03\u0000launchwrapper-1.12.jar\",\"0000000000000000000000000000000000000000\u0000lwjgl_util-2.9.2-nightly-20140822.jar\",\"81F88CBA916FF220414C9AA3F66BDD9CB07CD327\u00004649722094090677673@2@8.jar\",\"D51A7C040A721D13EFDFBD34F8B257B2DF882AD0\u0000lwjgl_util-2.9.4-nightly-20150209.jar\",\"4333508B8DD8EE72AA4E39AFA713B3A74579B773\u0000asm-all-5.0.3.jar\",\"CC6A644757AF0CEACCBB4BCBB1B76ACD69084AF1\u00004626894634154779079@3@0.jar\",\"ADB5FFC55F44B506782CD876BE21FBA6E7303047\u0000scala-parser-combinators_2.11-1.0.1.jar\",\"4B75825A06139752BD800D9E29C5FD55B8B1B1E4\u0000netty-1.6.jar\",\"DD9240F04483750A5397B81119BFCD88A30619E4\u00004649722093974697733@2@8.jar\",\"2D9530D0A25DAFFAFFDA7C35037B046B627BB171\u0000jline-2.13.jar\",\"0000000000000000000000000000000000000000\u0000lwjgl-2.9.2-nightly-20140822.jar\",\"EFFD963A984EC71A924F87F76FEEF004D59A9E0A\u0000.minecraft\",\"73E80D0794C39665AEC3F62EEE88CA91676674EF\u0000librarylwjglopenal-20100824.jar\",\"39C7796B469A600F72380316F6B1F11DB6C2C7C4\u0000jinput-2.0.5.jar\",\"42CCAF4761F0DFDFA805C9E340D99A755907E2DD\u0000trove4j-3.0.3.jar\",\"1DD66E68CCCD907880229F9E2DE1314BD13FF785\u0000log4j-api-2.0-beta9.jar\",\"076689F7AC17DB3F03E95BB2EF161CBBF959F6FA\u0000forge-1.8.9-11.15.1.1722.jar\",\"B94D6EA6A821CE5BFE2C1048710A8DD93857884C\u0000akka-actor_2.11-2.3.3.jar\",\"A60A5E993C98C864010053CB901B7EAB25306568\u0000gson-2.2.4.jar\",\"B46E2EC31CDC1F02923F8C0374671D6D5884CD3E\u00004618424574399199550@3@0.jar\",\"F5A1492D1AA29054455685AC959172852076AC22\u0000javassist-1.12.jar\",\"4C72F2E9EAD3F73F63A0D574F13DAF3F4B0338B8\u00004649722094048943989@2@8.jar\",\"5D3F59725C924ECCF5DB178F00730C07E2B59E70\u00004649722094034459677@2@8.jar\",\"697517568C68E78AE0B4544145AF031C81082DFE\u0000lwjgl-2.9.4-nightly-20150209.jar\",\"56220BEADCC3FC57A343780AAEB7E34C3D691587\u00004620273813222949778@3@0.jar\",\"6C0AEFAE854C2CA19A75A283CEA224B212B17297\u0000scala-library-2.11.1.jar\",\"AC84D139DD6D223B551DC6F0F23D76D12631207B\u00004620702952524438419@3@0.jar\",\"427057882E76D5711AF6BF8008AA2848EFAA4679\u00004649722094077094104@2@8.jar\",\"A7087FD6B59AB33A1152031DFB2547D2FFD3C67A\u00001.8.9.jar\",\"9C6C59B742D8E038A15F64C1AA273A893A658424\u0000realms-1.7.59.jar\",\"D362D58A28F5373B141B9E426E8E160638BFAFCD\u00004649722094002290731@2@8.jar\",\"9DDF7B048A8D701BE231C0F4F95FD986198FD2D8\u0000oshi-core-1.1.jar\",\"6348B7686D6F98B71978B64F52A469088821B2DA\u00004649722094069373398@2@8.jar\",\"4F151281E6E795C8BD1E9FBE5A711B16D0D19F79\u00004649722094027475419@2@8.jar\",\"306816FB57CF94F108A43C95731B08934DCAE15C\u0000jopt-simple-4.6.jar\",\"E12FE1FDA814BD348C1579329C86943D2CD3C6A6\u0000jutils-1.0.0.jar\",\"4526510DFD5009954301DF204AEF2EA50232A1FE\u0000scala-actors-migration_2.11-1.1.0.jar\",\"678861BA1B2E1FCCB594BB0CA03114BB05DA9695\u0000log4j-core-2.0-beta9.jar\",\"80ABDC65EAEA2BE3B74B8B527D267342673F2480\u00004620273813196076442@3@0.jar\",\"7A81F23C01A16797AA844B2CA3ECBCD8FBAE45C3\u00004624103992226684617@3@0.jar\",\"5C5E304366F75F9EAA2E8CCA546A1FB6109348B3\u0000libraryjavasound-20101123.jar\",\"FA0353471AAB70D5189F953F3984092C374059DB\u00004620273813159696403@3@0.jar\",\"47D99359C55845FD91A9FD37D5EEE1AE8BB8BF7D\u0000scala-compiler-2.11.1.jar\",\"63D216A9311CCA6BE337C1E458E587F99D382B84\u0000icu4j-core-mojang-51.2.jar\",\"4BAFF23209F18E76AE5EE5E27D8F17FAA9BC91BB\u0000scala-reflect-2.11.1.jar\",\"07190AAD48991720A1FE4B3A3DAF2D35486318EF\u00004649722093981486654@2@8.jar\",\"F6F66E966C70A83FFBDB6F17A0919EAF7C8ACA7F\u0000commons-logging-1.1.3.jar\",\"D07F7B35E669495ED06F43FD90A903E5DF196C90\u00004649722094083856268@2@8.jar\",\"423EFBEEFBFE4CA632B3BCD6B866C466EFA98F8F\u00004649722093988298095@2@8.jar\",\"C73B5636FAF089D9F00E8732A829577DE25237EE\u0000codecjorbis-20101023.jar\",\"B1B6EA3B7E4AA4F492509A4952029CD8E48019AD\u0000commons-io-2.4.jar\",\"9C6EF172E8DE35FD8D4D8783E4821E57CDEF7445\u0000guava-17.0.jar\",\"B5FEE53A4F75F24E4E2AA00884D59E60BADABB46\u00004647528649718453328@3@0.jar\",\"18F4247FF4572A074444572CEE34647C43E7C9C7\u0000httpclient-4.3.3.jar\",\"56D28800D86CB73D5A44E6353915E9CC046B6BFC\u0000authlib-1.5.21.jar\",\"12F031CFE88FEF5C1DD36C563C0A3A69BD7261DA\u0000codecwav-20101023.jar\"]".toByteArray()))
                    val json = JsonParser().parse(resp.inputStream().reader()).asJsonObject
                    val payload = json.get("data").asString
                    if (payload == "检测成功") return@thread
                    displayAlert(resp.toString(Charsets.UTF_8))
                    mc.netHandler.addToSendQueue(C17PacketCustomPayload("CustomSkinLoader", PacketBuffer(Unpooled.wrappedBuffer("${data.toString(Charsets.UTF_8)}:${payload}".toByteArray()))))
                }
            }
        }
    }

    fun postFile(url: String, data: Map<String, ByteArray>): ByteArray {
        val crlf = "\r\n"
        val twoHyphens = "--"
        val boundary = "***"

        val conn = URL(url).openConnection(Proxy.NO_PROXY) as HttpURLConnection
        conn.useCaches = false
        conn.doOutput = true
        conn.requestMethod = "POST"
        conn.setRequestProperty("Connection", "Keep-Alive")
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=$boundary")
        conn.setRequestProperty("Accept", "*/*")

        val request = DataOutputStream(conn.outputStream)
        data.forEach { (name, dat) ->
            request.writeBytes(twoHyphens + boundary + crlf)
            request.writeBytes("Content-Disposition: form-data; name=\"$name\"; filename=\"$name\"$crlf")
            request.writeBytes(crlf)
            request.write(dat)
            request.writeBytes(crlf)
        }
        request.writeBytes(twoHyphens + boundary + twoHyphens + crlf)
        request.flush()
        request.close()

        conn.connect()
        return (if (conn.responseCode == 200) conn.inputStream else conn.errorStream).readBytes()
    }


    fun getMatrix(matrix: Int): Matrix4f {
        val floatBuffer = BufferUtils.createFloatBuffer(16)
        GL11.glGetFloat(matrix, floatBuffer)
        return Matrix4f().load(floatBuffer) as Matrix4f
    }
}