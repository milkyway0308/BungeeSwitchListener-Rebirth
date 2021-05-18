package skywolf46.bsl.core.impl.packet.security

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.annotations.BSLBeforeWrite
import skywolf46.bsl.core.annotations.BSLExclude
import skywolf46.bsl.core.util.EncryptionUtility
import java.security.PrivateKey
import java.security.PublicKey

class PacketIntroduceSelf(
    key: PublicKey?,
    var serverName: String,
    password: String,
    var port: Int = -1,
) : AbstractPacketBase<PacketIntroduceSelf>() {

    private constructor() : this(null, "", "", -1)

    @BSLExclude
    var key: PublicKey? = key

    private var passwordArr = password.toByteArray()

    fun getPassword(key: PrivateKey?) =
        if (key == null) String(passwordArr) else String(EncryptionUtility.decrypt(passwordArr, key))

    @BSLBeforeWrite
    private fun encrypt() {
        if (key != null){
            passwordArr = EncryptionUtility.encrypt(passwordArr, key!!)
            println("Key not null. Encrypt!")
        }
    }

}