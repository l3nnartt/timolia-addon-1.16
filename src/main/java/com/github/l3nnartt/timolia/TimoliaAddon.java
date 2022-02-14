package com.github.l3nnartt.timolia;

import java.util.List;
import net.labymod.api.LabyModAddon;
import net.labymod.settings.elements.SettingsElement;

public class TimoliaAddon extends LabyModAddon {

  private TimoliaAddon instance;

  @Override
  public void onEnable() {
    instance = this;
    api.registerServerSupport(this, new ServerSupport("Timolia", "timolia.de", "play.timolia.de", "*.timolia.de"));
  }

  @Override
  public void loadConfig() {

  }

  @Override
  protected void fillSettings(List<SettingsElement> list) {

  }
}