package skywolf46.bsl.client.util

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.io.*
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

object BukkitUtil {
    private lateinit var EXTRACT: Method
    private lateinit var IMPORT: Method
    private lateinit var CRAFTITEM_COPY: Method
    private lateinit var BUKKIT_COPY: Method
    private lateinit var NET_ITEMSTACK_NBT_EXTRACT: Method
    private lateinit var NET_ITEMSTACK_NBT_IMPORT: Method


    private val ITEM_CLASS = BukkitVersionUtil.getOBCClass("inventory.CraftItemStack")
    private val ITEM_CLASS_CONSTRUCTOR = ITEM_CLASS.getDeclaredConstructor(ItemStack::class.java)

    private val HANDLE_FIELD = ITEM_CLASS.getDeclaredField("handle")
    private val SET_TAG_METHOD =
        BukkitVersionUtil.getNMSClass("ItemStack").getMethod("setTag", BukkitVersionUtil.getNMSClass("NBTTagCompound"))

    init {
        try {
            var c: Class<*> = BukkitVersionUtil.getOBCClass("inventory.CraftItemStack")
            CRAFTITEM_COPY = c.getMethod("asNMSCopy", Class.forName("org.bukkit.inventory.ItemStack"))
            BUKKIT_COPY = c.getMethod("asBukkitCopy", BukkitVersionUtil.getNMSClass("ItemStack"))
            c = BukkitVersionUtil.getNMSClass("ItemStack")
            NET_ITEMSTACK_NBT_EXTRACT = c.getMethod("getTag")
            NET_ITEMSTACK_NBT_IMPORT = c.getMethod("setTag", BukkitVersionUtil.getNMSClass("NBTTagCompound"))
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }

    fun toItemMetaByte(item: ItemStack): ByteArray? {
        try {
//            long start = System.currentTimeMillis();
            val baos = ByteArrayOutputStream()
            val dos = DataOutputStream(baos)
            val ex = extractNBT(item)
            if (ex == null) dos.writeBoolean(false) else {
                dos.writeBoolean(true)
                EXTRACT.invoke(null, ex, dos)
            }
            dos.flush()
            dos.close()
            val target: ByteArray
            target = baos.toByteArray()
            baos.close()
            return target
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun toByte(item: ItemStack): ByteArray? {
        try {
//            long start = System.currentTimeMillis();
            val baos = ByteArrayOutputStream()
            val dos = DataOutputStream(baos)
            dos.writeUTF(item.type.name)
            dos.writeInt(item.amount)
            dos.writeShort(item.durability.toInt())
            val ex = extractNBT(item)
            if (ex == null) dos.writeBoolean(false) else {
                dos.writeBoolean(true)
                EXTRACT.invoke(null, ex, dos)
            }
            dos.flush()
            dos.close()
            val target: ByteArray
            target = baos.toByteArray()
            baos.close()
            return target
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    //
    //    public static String toString(ItemStack item, boolean compress) {
    //        return Base64.getEncoder().encodeToString(toByte(item, compress));
    //    }
    fun fromByte(arr: ByteArray?): ItemStack? {
        try {
            val bais = ByteArrayInputStream(arr)
            val dis = DataInputStream(bais)
            val matter: String = dis.readUTF()
            val mat: Material = Material.getMaterial(matter) ?: return null
            //            System.out.println("Material: " + matter);
            val item = ItemStack(mat, dis.readInt(), dis.readShort())
            if (dis.readBoolean()) {
                val nbt: Any = IMPORT.invoke(null, dis)
                return importNBT(item, nbt)
            }
            return item
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }

    fun fromItemMetaByte(item: ItemStack, arr: ByteArray?) {
        try {
            val bais = ByteArrayInputStream(arr)
            val dis = DataInputStream(bais)
            if (dis.readBoolean()) {
                val nbt: Any = IMPORT.invoke(null, dis)
                importMetaNBT(item, nbt)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    fun importMetaNBT(item: ItemStack, compound: Any?) {
        try {
            val handle =
                HANDLE_FIELD.get(if (!ITEM_CLASS.isAssignableFrom(item.javaClass)) ITEM_CLASS_CONSTRUCTOR.newInstance(
                    item) else item)
            SET_TAG_METHOD.invoke(handle, compound)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    fun importNBT(item: ItemStack?, compound: Any?): ItemStack? {
        try {
            val next: Any = CRAFTITEM_COPY.invoke(null, item)
            NET_ITEMSTACK_NBT_IMPORT.invoke(next, compound)
            return BUKKIT_COPY.invoke(null, next) as ItemStack
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }

    fun extractNBT(item: ItemStack?): Any? {
        try {
            val next: Any = CRAFTITEM_COPY.invoke(null, item)
            return NET_ITEMSTACK_NBT_EXTRACT.invoke(next) ?: return null
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return null
    }


    init {
        var cl: Class<*>? = null
        try {
            cl = BukkitVersionUtil.getNMSClass("NBTCompressedStreamTools")
            EXTRACT = cl.getMethod("a", BukkitVersionUtil.getNMSClass("NBTTagCompound"), DataOutput::class.java)
            IMPORT = cl.getMethod("a", DataInputStream::class.java)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
//            e.printStackTrace();
            println("Method extracting failed. Retry with 1.16 method extraction.")
            try {
                cl = BukkitVersionUtil.getNMSClass("NBTCompressedStreamTools")
                EXTRACT = cl.getMethod("a", BukkitVersionUtil.getNMSClass("NBTTagCompound"), DataOutput::class.java)
                IMPORT = cl.getMethod("a", DataInput::class.java)
            } catch (exx: Exception) {
                exx.printStackTrace()
            }
        }
    }
}