package com.github.l3nnartt.timolia;

import net.labymod.api.event.events.client.gui.screen.overlay.PlayerTabListOverlayEvent;
import net.labymod.ingamegui.moduletypes.ColoredTextModule;
import net.labymod.servermanager.ChatDisplayAction;
import net.labymod.servermanager.Server;
import net.labymod.settings.elements.BooleanElement;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.HeaderElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Material;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.PacketBuffer;
import java.util.Collections;
import java.util.List;

public class ServerSupport extends Server {

    // init setting booleans
    private boolean displayServer;
    private boolean displayEnemy;
    private boolean displayKit;
    private boolean displayMap;

    // init value strings
    public String enemy = null;
    public String latestServer = null;
    public String kit = null;
    public String latestMap = null;

    // constructor
    public ServerSupport(String name, String... addressNames) {
        super(name, addressNames);
    }

    public void addModuleLines(List<DisplayLine> lines) {
        super.addModuleLines(lines);
        try {
            lines.add(new DisplayLine("Server", Collections.singletonList(ColoredTextModule.Text.getText(latestServer))));
            if (enemy != null)
                lines.add(new DisplayLine("Enemy", Collections.singletonList(ColoredTextModule.Text.getText(enemy))));
            if (kit != null)
                lines.add(new DisplayLine("Kit", Collections.singletonList(ColoredTextModule.Text.getText(kit))));
            if (latestMap != null)
                lines.add(new DisplayLine("Map", Collections.singletonList(ColoredTextModule.Text.getText(latestMap))));
        } catch (Exception error) {
            error.printStackTrace();
        }
    }

    // on server join
    @Override
    public void onJoin(ServerData serverData) {

    }

    // on chat message
    @Override
    public ChatDisplayAction handleChatMessage(String clean, String formatted) throws Exception {
        System.out.println("CLEAN: " + clean);
        System.out.println("FORMATTED: " + formatted);

        if (formatted.contains("1vs1") && formatted.contains("»")) {
            if (formatted.contains("Kit") && formatted.contains("Einstellungen")) {
                String kitname = formatted.split("§6")[1].split("§8")[0];
                kitname = kitname.substring(0, kitname.length()-1);
                kit = kitname;
                System.out.println(kitname);
            }
            if (formatted.contains("Kampf") && formatted.contains("beginnt")) {
                String enemyname = formatted.split("§6")[1].split("§7")[0];
                enemyname = enemyname.substring(0, enemyname.length()-2);
                enemy = enemyname;
                System.out.println(enemyname);
            }
            else if (formatted.contains("den Kampf gegen")) {
                enemy = null;
                kit = null;
            }
        }

        if (clean.contains("Mapvoting") && clean.contains("»") && clean.contains("beendet")) {
            String[] mapname = formatted.split("§6");
            String mapoutput = mapname[mapname.length-1];
            mapoutput = mapoutput.substring(0,mapoutput.length()-5);
            latestMap = mapoutput;
            System.out.println(mapoutput);
        }

        return ChatDisplayAction.NORMAL;
    }


    // handle payload settings
    @Override
    public void handlePayloadMessage(String s, PacketBuffer packetBuffer) throws Exception {

    }

    // handle tab info message
    @Override
    public void handleTabInfoMessage(PlayerTabListOverlayEvent.Type type, String s, String s1) throws Exception {
        System.out.println("handleTabInfoMessage");
        System.out.println("s: " + s);
        System.out.println("s1: " + s1);

        if (s1.contains("Du spielst auf")) {
            String[] servername = s1.split("§6");
            String serveroutput = servername[servername.length-1];
            serveroutput = serveroutput.substring(0,serveroutput.length()-4);
            latestServer = serveroutput;
        }
    }

    public void loadConfig() {
        this.displayServer = getBooleanAttribute("displayServer", true);
        this.displayEnemy = getBooleanAttribute("displayEnemy", true);
        this.displayKit = getBooleanAttribute("displayKit", true);
        this.displayMap = getBooleanAttribute("displayMap", false);
    }

    // fill addon settings
    @Override
    public void fillSubSettings(List<SettingsElement> settingsElements) {
        settingsElements.add(new HeaderElement("Server Support Modules"));
        settingsElements.add(new BooleanElement("Display Server", this, new ControlElement.IconData(Material.ANVIL), "displayServer"));
        settingsElements.add(new BooleanElement("Display Enemy", this, new ControlElement.IconData(Material.NAME_TAG), "displayEnemy"));
        settingsElements.add(new BooleanElement("Display Kit", this, new ControlElement.IconData(Material.DIAMOND_SWORD), "displayKit"));
        settingsElements.add(new BooleanElement("Display Current Map", this, new ControlElement.IconData(Material.SIGN), "displayMap"));
    }
}