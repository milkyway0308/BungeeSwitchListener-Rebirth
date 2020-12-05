package skywolf46.bsltest;

import skywolf46.bsl.global.BungeeSwitchListenerCore;
import skywolf46.bsl.global.abstraction.enums.Side;

public class Test {
    public static void main(String[] args) {
        BungeeSwitchListenerCore.init(Side.CLIENT);
    }
}
