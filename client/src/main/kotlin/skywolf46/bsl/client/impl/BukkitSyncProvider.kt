package skywolf46.bsl.client.impl

import skywolf46.bsl.core.abstraction.ISyncProvider
import skywolf46.extrautility.util.schedule

class BukkitSyncProvider : ISyncProvider {
    override fun sync(unit: () -> Unit) {
        schedule {
            unit()
        }
    }

    override fun delayedSync(delayMillisecond: Long, unit: () -> Unit) {
        schedule(delayMillisecond / 50) {
            unit()
        }
    }
}