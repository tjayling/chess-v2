package com.tom.gui;

import static com.tom.gui.model.GuiPages.LANDING_PAGE;
import static com.tom.gui.model.GuiPages.PERFT_GUI;

import com.tom.gui.model.GuiPages;
import com.tom.gui.perft.PerftGui;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;

public class GuiController {
  private static GuiController INSTANCE;
  private final Gui gui;
  private GuiPages activeGui = LANDING_PAGE;
  private final Map<GuiPages, JPanel> pageMap;

  private GuiController(Gui gui) {
    this.gui = gui;
    pageMap = new HashMap<>();
  }

  public static GuiController instantiate(Gui gui) {
    if (INSTANCE != null) {
      throw new RuntimeException("GuiController already initialized");
    }
    INSTANCE = new GuiController(gui);
    INSTANCE.registerPages();
    return INSTANCE;
  }

  public static GuiController getInstance() {
    if (INSTANCE == null) {
      throw new RuntimeException("GuiController not initialized");
    }
    return INSTANCE;
  }

  public void showActiveGui() {
    gui.showActiveGui(pageMap.get(activeGui));
  }

  public void setActiveGui(GuiPages guiPage) {
    activeGui = guiPage;
    showActiveGui();
  }

  private void registerPages() {
    pageMap.put(LANDING_PAGE, new LandingPage());
    pageMap.put(PERFT_GUI, new PerftGui());
  }
}
