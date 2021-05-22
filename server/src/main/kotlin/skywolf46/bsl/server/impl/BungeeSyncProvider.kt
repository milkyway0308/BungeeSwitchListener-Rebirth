package skywolf46.bsl.server.impl

import net.md_5.bungee.BungeeCord
import skywolf46.bsl.core.abstraction.ISyncProvider
import skywolf46.bsl.server.BungeeSwitchListener
import java.util.concurrent.TimeUnit

class BungeeSyncProvider : ISyncProvider {
    override fun sync(unit: () -> Unit) {
        BungeeCord.getInstance().scheduler.schedule(BungeeSwitchListener.inst, unit, 0L, TimeUnit.MILLISECONDS)
    }

    override fun delayedSync(delayMillisecond: Long, unit: () -> Unit) {
        BungeeCord.getInstance().scheduler.schedule(BungeeSwitchListener.inst,
            unit,
            delayMillisecond,
            TimeUnit.MILLISECONDS)
    }

}