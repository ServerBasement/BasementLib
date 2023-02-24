package it.ohalee.basementlib.bukkit.nms.v1_8_R3.chat;

import it.ohalee.basementlib.api.bukkit.chat.Colorizer;

public class ColorizerNMS implements Colorizer.ColorAdapter {

    @Override
    public String translateHex(String msg) {
        return msg;
    }
}
