package skywolf46.bsl.client.serializer.minecraft

import io.netty.buffer.ByteBuf
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import skywolf46.bsl.client.util.BukkitUtil
import skywolf46.bsl.core.abstraction.IByteBufSerializer
import skywolf46.bsl.core.util.readByteArray
import skywolf46.bsl.core.util.readString
import skywolf46.bsl.core.util.writeByteArray
import skywolf46.bsl.core.util.writeString

class ItemStackSerializer : IByteBufSerializer<ItemStack> {
    override fun ByteBuf.writePacketHeader(data: ItemStack) {
        // Write Item Type / Amount / Durability as header.
        writeString(data.type.name)
        writeInt(data.amount)
        writeShort(data.durability.toInt())
    }

    override fun ByteBuf.writePacketField(data: ItemStack) {
        // Write ItemMeta as header.
        writeByteArray(BukkitUtil.toItemMetaByte(data)!!)
    }

    override fun ByteBuf.readPacketHeader(): ItemStack {
        // Read Item Type / Amount / Durability as header.
        val itemType = readString()
        return ItemStack(try {
            Material.valueOf(itemType)
        } catch (e: Exception) {
            System.err.println("BSL deserialization warning; Item type detected as $itemType, but current version not have type $itemType. Changing item to \"Material.GRASS\".")
            Material.GRASS
        }, readInt(), readShort())
    }

    override fun ByteBuf.readPacketField(orig: ItemStack) {
        // Read ItemMeta as header.
        BukkitUtil.fromItemMetaByte(orig, readByteArray())
    }

}