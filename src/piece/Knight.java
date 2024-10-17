package piece;

import main.GamePanel;
import main.Type;

public class Knight extends Piece{
    public Knight(int color, int col, int row) {
        super(color, col, row);
        type = Type.KNIGHT;
        if(color == GamePanel.WHITE){
            image = getImage("/piece/WN");
        }
        else{
            image = getImage("/piece/BN");
        }
    }

    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol,targetRow)){
             if(Math.abs(targetCol - precol) * Math.abs(targetRow - prerow) ==2){
                 if(isValidSquare(targetCol,targetRow)){
                     return true;
                 }

             }
        }

        return false;
    }
}
