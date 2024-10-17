package piece;

import main.GamePanel;
import main.Type;

public class Bishop extends Piece{
    public Bishop(int color, int col, int row) {
        super(color, col, row);
        type = Type.BISHOP;
        if(color == GamePanel.WHITE){
            image = getImage("/piece/WB");
        }
        else{
            image = getImage("/piece/BB");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol , targetRow) == false ){
            if(Math.abs(targetCol - precol) == Math.abs(targetRow - prerow)){
                if(isValidSquare(targetCol,targetRow) && pieceIsOnDl(targetCol,targetRow)==false){
                    return true;
                }

            }
        }

        return false;
    }
}
