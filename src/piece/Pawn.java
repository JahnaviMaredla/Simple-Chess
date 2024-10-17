package piece;

import main.GamePanel;
import main.Type;

public class Pawn extends Piece{

    public Pawn(int color, int col, int row) {
        super(color, col, row);
        type = Type.PAWN;
        if(color == GamePanel.WHITE){
            image = getImage("/piece/WS");
        }
        else{
            image = getImage("/piece/BS");
        }
    }
    public boolean canMove(int targetCol , int targetRow){
        if(isWithinBoard(targetCol , targetRow) && isSameSquare(targetCol,targetRow)==false){

            int moveValue;
            if(color == GamePanel.WHITE){
                moveValue=-1;
            }
            else{
                moveValue=1;
            }

            hittingP = getHittingP(targetCol, targetRow);

            // 1 sq for mov
            if(targetCol == precol && targetRow == prerow +moveValue && hittingP == null){
                return true;
            }
            // 2 sq for mov
            if(targetCol == precol && targetRow == prerow + (moveValue*2) &&
                    hittingP == null && moved == false &&
                    pieceIsonSl(targetCol,targetRow)==false){
                return true;
            }
            // diagonal mov & capture
            if(Math.abs(targetCol-precol)==1 && targetRow == prerow + moveValue
             && hittingP !=null && hittingP.color != color){
                return true;
            }

            //en passant
            if(Math.abs(targetCol-precol)==1 && targetRow == prerow + moveValue){
                for(Piece p : GamePanel.simPieces){
                    if(p.col == targetCol && p.row == prerow && p.twoStepped == true){
                         hittingP = p ;
                         return true;
                    }
                }
            }
            // pawn promotion



        }
        return false;
    }
}
