package com.tom.gui.perft;

import com.tom.chess.model.Fen;
import com.tom.gui.chess.ChessAreaGui;
import com.tom.gui.shared.HeaderPanel;
import com.tom.middleware.Mediator;
import com.tom.middleware.StandardMediator;
import java.awt.BorderLayout;
import javax.swing.JPanel;

public class PerftGui extends JPanel {
  public PerftGui() {
    setLayout(new BorderLayout());

    Mediator mediator = new StandardMediator(Fen.defaultFen());
    PerftGuiController controller = PerftGuiController.instantiate(mediator);

    HeaderPanel headerPanel = new HeaderPanel();
    PerftPanel perftPanel = new PerftPanel();
    ChessAreaGui chessAreaGui = new ChessAreaGui();

    add(headerPanel, BorderLayout.NORTH);
    add(chessAreaGui, BorderLayout.WEST);
    add(perftPanel, BorderLayout.EAST);

    controller.registerViews(perftPanel, chessAreaGui);

    setVisible(true);
  }
}
