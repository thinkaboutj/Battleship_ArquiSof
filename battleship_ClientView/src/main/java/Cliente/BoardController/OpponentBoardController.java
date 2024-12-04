package Cliente.BoardController;

import Cliente.Board.GameBoard;
import Cliente.Board.GameBoardBuilder;
import Cliente.Game.Game;

public class OpponentBoardController extends BoardController{

    public OpponentBoardController(GameBoard gameBoard, Game game) {
        super(gameBoard, game);
    }

    @Override
    public void buildBoard() {
        GameBoardBuilder gameBoardBuilder = new GameBoardBuilder(this);
        gameBoardBuilder.fillTheBoardWithButtonsAndLabels();
    }

    public void setBoardDisabled(boolean disable) {
        gameBoard.getGridPane().setDisable(disable);
    }
}
