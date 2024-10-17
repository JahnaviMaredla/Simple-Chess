package piece;

import main.GamePanel;
import main.Type;

public class King  extends Piece{
    public King(int color, int col, int row) {
        super(color, col, row);
        type = Type.KING;

        if(color == GamePanel.WHITE){
            image = getImage("/piece/WK");
        }
        else{
            image = getImage("/piece/BK");
        }
    }
    public boolean canMove(int targetCol , int targetRow){
        //Movement
        if(isWithinBoard(targetCol , targetRow)){
            if(Math.abs(targetCol-precol) + Math.abs(targetRow- prerow) == 1 ||
                    Math.abs(targetCol-precol)*Math.abs(targetRow- prerow) == 1){
                if(isValidSquare(targetCol , targetRow)){
                    return true;
                }
            }
        }
        //castling
        if(moved == false){
            //right castling
            if(targetCol == precol+2 && targetRow == prerow && pieceIsonSl(targetCol,targetRow)==false){
                   for(Piece p : GamePanel.simPieces){
                       if(p.col == precol+3 && p.row == prerow && p.moved == false ){
                           GamePanel.castP = p;
                           return true;
                       }
                   }
            }
            // left castling
            if(targetCol == precol-2 && targetRow == prerow && pieceIsonSl(targetCol,targetRow)==false){

                Piece pi[] = new Piece[2];
                for(Piece p : GamePanel.simPieces){
                    if(p.col == precol-3 && p.row == targetRow){
                        pi[0] = p;
                    }
                    if(p.col == precol-4 && p.row== targetRow){
                        pi[1] = p;
                    }
                    if(pi[0]== null && pi[1] !=null && pi[1].moved == false ){
                        GamePanel.castP = pi[1];
                        return true;
                    }
                }


            }

        }

        return false;
    }

}
