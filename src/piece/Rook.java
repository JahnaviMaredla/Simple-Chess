package piece;

import main.GamePanel;
import main.Type;

public class Rook extends Piece{

    public Rook(int color, int col, int row) {
        super(color, col, row);
        type = Type.ROOK;
        if(color == GamePanel.WHITE){
            image = getImage("/piece/WE");
        }
        else{
            image = getImage("/piece/BE");
        }
    }
    @Override
    public boolean canMove(int targetCol, int targetRow) {

        if(isWithinBoard(targetCol,targetRow) && isSameSquare(targetCol,targetRow)== false){
            if(targetCol == precol || targetRow == prerow ){
                if(isValidSquare(targetCol,targetRow)){
                    if(isValidSquare(targetCol , targetRow) && pieceIsonSl(targetCol,targetRow) == false) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
