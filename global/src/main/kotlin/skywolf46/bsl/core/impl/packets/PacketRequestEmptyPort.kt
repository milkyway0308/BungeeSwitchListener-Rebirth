package skywolf46.bsl.core.impl.packets

import skywolf46.bsl.core.abstraction.AbstractPacketBase
import skywolf46.bsl.core.abstraction.ResponsiblePacket
import java.nio.channels.ServerSocketChannel

class PacketRequestEmptyPort : ResponsiblePacket {

    var port: Int = -1
        private set

    constructor() {

    }

    constructor(port: Int) {
        this.port = port
    }

}