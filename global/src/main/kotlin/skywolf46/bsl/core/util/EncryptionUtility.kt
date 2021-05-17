package skywolf46.bsl.core.util

import java.security.*
import javax.crypto.Cipher

object EncryptionUtility {
    fun generateKeypair(): KeyPair {
        return KeyPairGenerator.getInstance("RSA").run {
            initialize(2048, SecureRandom())
            return@run genKeyPair()
        }
    }

    fun encrypt(data: ByteArray, key: PublicKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher.doFinal(data)
    }

    fun decrypt(data: ByteArray, key: PrivateKey): ByteArray {
        val cipher = Cipher.getInstance("RSA")
        cipher.init(Cipher.DECRYPT_MODE, key)
        return cipher.doFinal(data)
    }
}