package piece;

import main.Board;
import main.GamePanel;
import main.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Piece {

     public Type type;
     public BufferedImage image ;
     public int x , y;
     public int col;
     public int row;
     public int precol;
     public int prerow;
     public int color;
     public Piece hittingP;
     public boolean moved = false;
     public boolean twoStepped;



     public Piece(int color , int col , int row ){
         this.color = color;
         this.col = col;
         this.row = row;
         x = getX(col);
         y = getY(row);
         precol=col;
         prerow = row;
     }

     public BufferedImage getImage(String imagePath){
         BufferedImage image = null;
         try{
             image = ImageIO.read(getClass().getResourceAsStream(imagePath+".png"));
         } catch (IOException e) {
             throw new RuntimeException(e);
         }
         return image;
     }

     public int getX(int col){
         return col*Board.Square_Size;
     }
     public int getY(int row){
         return row*Board.Square_Size;
     }
    public int getCol(int col){
        return (x+Board.Half_Square_Size)/Board.Square_Size;
    }
    public int getRow(int row){
        return (y+Board.Half_Square_Size)/Board.Square_Size;
    }
    public void updatePosition(){

         // To check en passant
        if(type == Type.PAWN){
            if(Math.abs(row-prerow)==2){
                twoStepped = true;
            }
        }

         x = getX(col);
         y = getY(row);
         precol = getCol(x);
         prerow = getRow(y);
         moved = true;
    }

     public int getIndex(){
         for(int ind = 0 ; ind < GamePanel.simPieces.size();ind++){
              if(GamePanel.simPieces.get(ind) == this){
                  return ind;
              }
         }
         return 0;
     }

    public boolean isSameSquare(int targetCol , int targetRow){
          if(targetCol == precol && targetRow == prerow){
              return true;
          }
          return false;
    }
    public boolean pieceIsonSl(int targetCol , int targetRow){
        //when moving left
         for(int c=precol-1; c>targetCol ; c--){
             for(Piece p : GamePanel.simPieces){
                 if(p.col == c && p.row== targetRow){
                     hittingP = p;
                     return true;
                 }
             }
         }
         // when moving right
        for(int c=precol+1; c<targetCol ; c++){
            for(Piece p : GamePanel.simPieces){
                if(p.col == c && p.row== targetRow){
                    hittingP = p;
                    return true;
                }
            }
        }
         // when moving up
        for(int c=prerow-1; c>targetRow ; c--){
            for(Piece p : GamePanel.simPieces){
                if(p.col == targetCol && p.row== c){
                    hittingP = p;
                    return true;
                }
            }
        }
         // when moving down
        for(int c= prerow+1 ; c < targetRow ; c++){
            for(Piece p : GamePanel.simPieces){
                if(p.col == targetCol && p.row== c){
                    hittingP = p;
                    return true;
                }
            }
        }

         return false;
    }
    public boolean pieceIsOnDl(int targetCol , int targetRow){
         if(targetRow < prerow){
             //upleft
             for(int c = precol-1 ; c> targetCol ; c--){
                 int diff = Math.abs(c-precol);
                 for(Piece p : GamePanel.simPieces){
                     if(p.col == c && p.row == prerow-diff){
                         hittingP = p;
                         return true;
                     }
                 }
             }
             // upright
             for(int c = precol+1 ; c<targetCol ; c++){
                 int diff = Math.abs(c-precol);
                 for(Piece p : GamePanel.simPieces){
                     if(p.col == c && p.row == prerow-diff){
                         hittingP = p;
                         return true;
                     }
                 }
             }
         }
         if(targetRow > prerow){
             //down left
             for(int c = precol-1 ; c> targetCol ; c--){
                 int diff = Math.abs(c-precol);
                 for(Piece p : GamePanel.simPieces){
                     if(p.col == c && p.row == prerow+diff){
                         hittingP = p;
                         return true;
                     }
                 }
             }
             //down right
             for(int c = precol+1 ; c<targetCol ; c++){
                 int diff = Math.abs(c-precol);
                 for(Piece p : GamePanel.simPieces){
                     if(p.col == c && p.row == prerow+diff){
                         hittingP = p;
                         return true;
                     }
                 }
             }
         }

         return false;
    }
    public boolean canMove(int targetCol , int targetRow){
          return false;
    }
    public boolean isWithinBoard(int targetCol , int targetRow){
        if(targetCol >=0 && targetCol <=7 && targetRow>=0 && targetRow<=7){
            return true;
        }
        return false;
    }

    public Piece getHittingP( int targetCol , int targetRow){
         for(Piece p : GamePanel.simPieces){
             if(p.col == targetCol && p.row == targetRow && p!=this){
                  return p;
             }
         }
         return null;
    }



    public void draw(Graphics2D g2) {

         g2.drawImage(image , x , y , Board.Square_Size , Board.Square_Size , null);

    }
    public boolean isValidSquare(int targetCol , int targetRow){
         hittingP = getHittingP(targetCol , targetRow);

          if(hittingP == null){
              return true;
          }
          else {
              if(hittingP.color != this.color){
                  return true;
              }
              else {
                  hittingP = null;
              }
          }
         return false;
    }
    public void resetPosition() {
         col = precol;
         row= prerow;
         x = getX(col);
         y = getY(row);
    }
}
