package skywolf46.bsl.global.abstraction.enums;

import skywolf46.bsl.global.api.BSLCoreAPI;

public enum Side {
    CLIENT, SERVER, GLOBAL;

    public boolean isCurentSide(Side side) {
        return side == GLOBAL || side == BSLCoreAPI.getSide();
    }
}
