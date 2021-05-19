package skywolf46.bsl.core.impl.packet.security

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLAfterRead
import skywolf46.bsl.core.annotations.BSLBeforeWrite
import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.annotations.BSLHeader
import skywolf46.bsl.core.util.EncryptionUtility
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec

class PacketRequestAuthenticate : AbstractPacketBase<PacketRequestAuthenticate>() {
    companion object {
        fun generate(): PacketRequestAuthenticate {
            val packet = PacketRequestAuthenticate()
            val key = EncryptionUtility.generateKeypair()
            packet.public = key.public
            packet.private = key.private
            return packet
        }
    }

    @BSLExclude
    lateinit var public: PublicKey

    @BSLExclude
    var private: PrivateKey? = null


    @BSLHeader
    private lateinit var keyArray: ByteArray

    @BSLAfterRead
    fun checkRead() {
        public = KeyFactory.getInstance("RSA")
            .generatePublic(X509EncodedKeySpec(keyArray))
    }

    @BSLBeforeWrite
    fun checkWrite() {
        keyArray = public.encoded
    }
}