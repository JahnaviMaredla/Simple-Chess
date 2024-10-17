package piece;

import main.GamePanel;
import main.Type;

public class Queen extends Piece {
    public Queen(int color, int col, int row) {
        super(color, col, row);
        type = Type.QUEEN;
        if(color == GamePanel.WHITE){
            image = getImage("/piece/WM");
        }
        else{
            image = getImage("/piece/BM");
        }
    }
    public boolean canMove(int targetCol , int targetRow){
       //vertical && horizontal
        if(isWithinBoard(targetCol , targetRow) && isSameSquare(targetCol,targetRow)==false){
            if(targetCol == precol || targetRow == prerow){
                if(isValidSquare(targetCol , targetRow) && pieceIsonSl(targetCol, targetRow) ==false){
                    return true;
                }
            }
        }
        //diagonal
        if(Math.abs(targetCol-precol) == Math.abs(targetRow-prerow)){
            if(isValidSquare(targetCol,targetRow) && pieceIsOnDl(targetCol, targetRow) ==false){
                return true;
            }
        }
        return false;
    }
}
