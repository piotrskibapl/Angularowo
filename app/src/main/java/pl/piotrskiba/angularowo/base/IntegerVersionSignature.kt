package pl.piotrskiba.angularowo.base

import com.bumptech.glide.load.Key
import java.nio.ByteBuffer
import java.security.MessageDigest

class IntegerVersionSignature(private val currentVersion: Int) : Key {
    override fun equals(other: Any?): Boolean {
        if (other is IntegerVersionSignature) {
            return currentVersion == other.currentVersion
        }
        return false
    }

    override fun hashCode(): Int {
        return currentVersion
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update(ByteBuffer.allocate(Integer.SIZE).putInt(currentVersion).array())
    }
}
