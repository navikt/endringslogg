package no.nav.k9.endringslogg

import io.netty.buffer.Unpooled
import io.netty.channel.embedded.EmbeddedChannel
import io.netty.handler.codec.http.HttpRequest
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.LastHttpContent
import io.netty.util.CharsetUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class NettyHttpDecoderTest {
    @Test
    fun `rejects malformed transfer encoding without decoding a smuggled request`() {
        val channel = EmbeddedChannel(HttpRequestDecoder())
        try {
            val request =
                "POST / HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Transfer-Encoding: chunked, identity\r\n" +
                    "Content-Length: 48\r\n\r\n" +
                    "0\r\n\r\n" +
                    "GET /smuggled HTTP/1.1\r\n" +
                    "Host: localhost\r\n\r\n"

            channel.writeInbound(Unpooled.copiedBuffer(request, CharsetUtil.US_ASCII))

            val decoded = channel.readInbound<HttpRequest>()
            assertTrue(decoded.decoderResult().isFailure)
            assertNull(channel.readInbound<Any>())
        } finally {
            channel.finishAndReleaseAll()
        }
    }

    @Test
    fun `accepts a valid chunked request`() {
        val channel = EmbeddedChannel(HttpRequestDecoder())
        try {
            val request =
                "POST / HTTP/1.1\r\n" +
                    "Host: localhost\r\n" +
                    "Transfer-Encoding: chunked\r\n\r\n" +
                    "0\r\n\r\n"

            channel.writeInbound(Unpooled.copiedBuffer(request, CharsetUtil.US_ASCII))

            val decoded = channel.readInbound<HttpRequest>()
            assertTrue(decoded.decoderResult().isSuccess)
            assertEquals("/", decoded.uri())
            val content = channel.readInbound<LastHttpContent>()
            try {
                assertTrue(content.decoderResult().isSuccess)
            } finally {
                content.release()
            }
            assertNull(channel.readInbound<Any>())
        } finally {
            channel.finishAndReleaseAll()
        }
    }
}
