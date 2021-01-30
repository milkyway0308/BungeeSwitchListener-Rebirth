package skywolf46.bsl.global.thread;


import skywolf46.bsl.global.impl.packets.PacketServerAlive;
import skywolf46.bsl.global.util.BSLChannel;

import java.util.concurrent.atomic.AtomicBoolean;

public class ChannelKeepAliveThread extends Thread {
    private AtomicBoolean isEnabled = new AtomicBoolean(true);
    private BSLChannel chan;

    public ChannelKeepAliveThread(BSLChannel chan) {
        this.chan = chan;
        start();
    }


    @Override
    public void run() {
        while (isEnabled.get()) {
            try {
                Thread.sleep(2000L);
                chan.send(new PacketServerAlive());
//                System.out.println("Sending...");
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
    }

    public void stopThread() {
        isEnabled.set(false);
    }
}
