package main;

import piece.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements Runnable {
       public static final int width = 1100;
       public static final int height =800;
       Thread gamethread;
       final int fps=60;

       boolean canMove;
       boolean validSquare;
       boolean promotion;
       boolean stalemate;
       boolean popupShown;
       Board board = new Board();
       Mouse mouse = new Mouse();

       public static ArrayList<Piece> pieces = new ArrayList<>() ;
       public static ArrayList<Piece> simPieces = new ArrayList<>() ;
       ArrayList<Piece> promoPieces = new ArrayList<>();

       Piece checkingP;
       boolean gameOver;
       Piece activeP;
       public  static Piece castP;

       public static final int WHITE = 0 ;
       public static final int BLACK = 1;
       int currentColor = WHITE;
       public GamePanel(){
           setPreferredSize(new Dimension(width,height));
           setBackground(new Color(0xE7728C35, true));
           addMouseMotionListener(mouse);
           addMouseListener(mouse);
           setPieces();
           copyPieces(pieces,simPieces);
       }
       public void launchGame(){
           gamethread = new Thread(this);
           gamethread.start();
       }

       private void checkCastling(){
            if(castP != null){
                if(castP.col ==0){
                    castP.col +=3;
                }
                else if (castP.col == 7){
                    castP.col -=2;
                }
                castP.x = castP.getX(castP.col);
            }
       }

   private boolean isStalemate(){
           int count =0;
           for(Piece p : simPieces){
               if(p.color !=currentColor){
                   count++;
               }
           }
            if(count==1){
                if(kingCanMove(getKing(true)) == false){
                    return true;
                }
            }
           return false;
   }
       private boolean canPromote(){

           if(activeP.type == Type.PAWN){
               if((currentColor == WHITE && activeP.row == 0) || (currentColor== BLACK && activeP.row==7)){
                     promoPieces.clear();
                     promoPieces.add(new Queen(currentColor,9,2));
                     promoPieces.add(new Rook(currentColor ,9,3));
                     promoPieces.add(new Knight(currentColor,9,4));
                     promoPieces.add(new Bishop(currentColor,9,5));
                     return true;
               }
           }

           return false;
       }

       public void setPieces(){

         //black
           pieces.add(new Pawn(BLACK,0,1));
           pieces.add(new Pawn(BLACK,1,1));
           pieces.add(new Pawn(BLACK,2,1));
           pieces.add(new Pawn(BLACK,3,1));
           pieces.add(new Pawn(BLACK,4,1));
           pieces.add(new Pawn(BLACK,5,1));
           pieces.add(new Pawn(BLACK,6,1));
           pieces.add(new Pawn(BLACK,7,1));
           pieces.add(new Rook(BLACK,0,0));
           pieces.add(new Rook(BLACK,7,0));
           pieces.add(new Knight(BLACK,1,0));
           pieces.add(new Knight(BLACK,6,0));
           pieces.add(new Bishop(BLACK,2,0));
           pieces.add(new Bishop(BLACK,5,0));
           pieces.add(new King(BLACK,4,0));
           pieces.add(new Queen(BLACK,3,0));
          //white
           pieces.add(new Pawn(WHITE,0,6));
           pieces.add(new Pawn(WHITE,1,6));
           pieces.add(new Pawn(WHITE,2,6));
           pieces.add(new Pawn(WHITE,3,6));
           pieces.add(new Pawn(WHITE,4,6));
           pieces.add(new Pawn(WHITE,5,6));
           pieces.add(new Pawn(WHITE,6,6));
           pieces.add(new Pawn(WHITE,7,6));
           pieces.add(new Rook(WHITE,0,7));
           pieces.add(new Rook(WHITE,7,7));
           pieces.add(new Knight(WHITE,1,7));
           pieces.add(new Knight(WHITE,6,7));
           pieces.add(new Bishop(WHITE,2,7));
           pieces.add(new Bishop(WHITE,5,7));
           pieces.add(new King(WHITE,4,7));
           pieces.add(new Queen(WHITE,3,7));





       }
       private void copyPieces(ArrayList<Piece> source , ArrayList<Piece> target){
           target.clear();
           for(int i =0;i<source.size();i++){
               target.add(source.get(i));
           }
       }

       private void changePlayer(){
            if(currentColor == WHITE){
                currentColor = BLACK;

                // reset black's two stepped status
                for(Piece p : pieces){
                    if(p.color == BLACK){
                        p.twoStepped = false;
                    }
                }
            }
            else {
                currentColor = WHITE;
                // reset white's two stepped status
                for(Piece p : pieces){
                    if(p.color == WHITE){
                        p.twoStepped = false;
                    }
                }
            }
            activeP = null;
       }
    @Override
    public void run() {
       double drawinterval = 1000000000/fps;
       double delta= 0 ;
       long lastTime = System.nanoTime();
       long currentTime;
        while(gamethread!=null){
            currentTime = System.nanoTime();
            delta +=(currentTime - lastTime)/drawinterval;
            lastTime = currentTime;
            if(delta>=1){
                update();
                repaint();
                delta--;
            }
        }
    }
       private void update()  {
           if (promotion) {
               promoting();
           } 
           else if(gameOver == false && stalemate==false ){
               if (mouse.pressed) {
                   if (activeP == null) {
                       for (Piece p : simPieces) {
                           if ((p.color == currentColor) &&
                                   (p.col == mouse.x / Board.Square_Size) &&
                                   (p.row == mouse.y / Board.Square_Size)) {
                               activeP = p;
                           }
                       }
                   } else {
                       simulate();
                   }
               }

               if (mouse.pressed == false) {
                   if (activeP != null) {
                       if (validSquare) {
                           copyPieces(simPieces, pieces);
                           activeP.updatePosition();

                           if (castP != null) {
                               castP.updatePosition();
                           }
                           if(isKingInCheck() && isCheckMate()){
                               gameOver = true;
                           }
                           else if(isStalemate() && isKingInCheck() ==false){
                               stalemate = true;

                           }
                           else {
                               if (canPromote()) {
                                   promotion = true;
                               } else {
                                   changePlayer();
                               }
                           }
                       } else {
                           copyPieces(pieces, simPieces);
                           activeP.resetPosition();
                           activeP = null;
                       }

                   }
               }

           }
           if (onlyKingsLeft()) {
               //gameOver = true;
              // handleGameOver();
             //  Thread.sleep(120000);
               stalemate = true; // Optionally, you can set stalemate if that's your intention
           }
       }

    private boolean onlyKingsLeft() {
        int kingCount = 0;
        for (Piece p : pieces) {
            if (p.type == Type.KING) {
                kingCount++;
            }
        }
        return kingCount == 2 && pieces.size()==2;
    }

    private boolean isKingInCheck() {
           Piece king =getKing(true);
           if(activeP.canMove(king.col,king.row)){
               checkingP = activeP;
               return true;
           }
           else{
               checkingP=null;
           }
           return false;
    }

    private boolean isCheckMate(){

           Piece king = getKing(true);
           if(kingCanMove(king)){
               return false;
           }
           else{
               //check you can block the attack with your piece
               int coldiff = Math.abs(checkingP.col -king.col);
               int rowdiff = Math.abs(checkingP.row-king.row);

               if(coldiff ==0){
                   // the checking is attacking vertically
                   if(checkingP.row < king.row){
                        //checking above king
                       for(int row = checkingP.row ; row <king.row ; row++){
                           for(Piece p : simPieces){
                               if(p != king && p.color != currentColor && p.canMove(checkingP.col,row)){
                                   return false;
                               }
                           }
                       }
                   }
                   if(checkingP.row> king.row){
                       //checking is below king
                       for(int row = checkingP.row ; row>king.row ; row--){
                           for(Piece p : simPieces){
                               if(p != king && p.color != currentColor && p.canMove(checkingP.col,row)){
                                   return false;
                               }
                           }
                       }


                   }
               }
               else if (rowdiff==0){
                   // the checking is attacking horizontally
                   if(checkingP.col<king.col){
                     // the checking piece is to the left
                       for(int row = checkingP.col ; row <king.col ; row++){
                           for(Piece p : simPieces){
                               if(p != king && p.color != currentColor && p.canMove(row,checkingP.col)){
                                   return false;
                               }
                           }
                       }
                   }
                   if(checkingP.col>king.col){
                       // the checking Piece is to the right
                       for(int row = checkingP.col ; row>king.col ; row--){
                           for(Piece p : simPieces){
                               if(p != king && p.color != currentColor && p.canMove(row,checkingP.col)){
                                   return false;
                               }
                           }
                       }
                   }
               }
               else if( coldiff == rowdiff){
                   //  the checking attacking diagonally
                if(checkingP.row < king.row){
                   // the checking piece is above the king
                    if(checkingP.col < king.col){
                        // the checking Piece is in the upper left
                        for(int col = checkingP.col , row = checkingP.row ; col < king.col ; col++,row++){
                             for(Piece p : simPieces){
                                 if(p!=king && p.color !=currentColor && p.canMove(col,row)){
                                     return true;
                                 }
                             }
                        }
                    }
                    if(checkingP.col < king.col) {
                        // the checking Piece is upper right
                        for (int col = checkingP.col, row = checkingP.row; col > king.col; col--, row++) {
                            for (Piece p : simPieces) {
                                if (p != king && p.color != currentColor && p.canMove(col, row)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
                if(checkingP.row > king.row){
                    // the checking  piece is below the king
                    if(checkingP.col < king.col){
                        // the checking Piece is in the lower left
                        for(int col = checkingP.col , row = checkingP.row ; col < king.col ; col++,row--){
                            for(Piece p : simPieces){
                                if(p!=king && p.color !=currentColor && p.canMove(col,row)){
                                    return true;
                                }
                            }
                        }
                    }
                    if(checkingP.col < king.col){
                        // the checking Piece is lower right
                        for(int col = checkingP.col , row = checkingP.row ; col > king.col ; col--,row--){
                            for(Piece p : simPieces){
                                if(p!=king && p.color !=currentColor && p.canMove(col,row)){
                                    return true;
                                }
                            }
                        }

                    }

                }

               }

           }
           return true;
    }
    private boolean kingCanMove(Piece king){
           // see king can move to any square
        if(isValidMove(king,-1,-1)){return true;}
        if(isValidMove(king,0,-1)){return true;}
        if(isValidMove(king , 1 , -1)){return true;}
        if(isValidMove(king,-1,0)){return true;}
        if(isValidMove(king,1,0)){return true;}
        if(isValidMove(king,-1,1)){return true;}
        if(isValidMove(king , 0 , 1)){return true;}
        if(isValidMove(king,1,1)){return true;}

        return false;
    }
    private boolean  isValidMove(Piece king,int colplus , int rowplus){
          boolean isValid = false;
          king.col +=colplus;
          king.row +=rowplus;
          if(king.canMove(king.col,king.row)){
              if(king.hittingP !=null){
                  simPieces.remove(king.hittingP.getIndex());
              }
              if(isIllegal(king)==false){
                  isValid = true;
              }
          }
          king.resetPosition();
          copyPieces(pieces,simPieces);
           return isValid;
    }


    private Piece getKing(boolean opp){
           Piece king = null;
           for(Piece p : simPieces){
               if(opp){
                   if(p.type == Type.KING && p.color !=currentColor){
                       king = p;
                   }
               }
               else{
                   if(p.type  == Type.KING && p.color == currentColor){
                       king = p;
                   }
               }
           }
           return king ;
    }

    private void promoting() {

           if(mouse.pressed){
               for(Piece p : promoPieces){
                   if(p.col == mouse.x/Board.Square_Size && p.row== mouse.y/Board.Square_Size){
                       switch(p.type){
                           case ROOK : simPieces .add(new Rook(currentColor,activeP.col , activeP.row));break;
                           case BISHOP: simPieces .add(new Bishop(currentColor,activeP.col , activeP.row));break;
                           case QUEEN:  simPieces .add(new Queen(currentColor,activeP.col , activeP.row));break;
                           case KNIGHT:  simPieces .add(new Knight(currentColor,activeP.col , activeP.row));break;
                           default : break;
                       }
                       simPieces.remove(activeP.getIndex());
                       copyPieces(simPieces, pieces);
                       activeP = null;
                       promotion = false;
                       changePlayer();
                   }
               }
           }

    }
    private boolean isIllegal(Piece king){
        if(king.type == Type.KING){
            for(Piece p : simPieces){
                if(p != king && p.color !=king.color && p.canMove(king.col , king.row)){
                    return true;
                }
            }
        }
        return false;
    }

    private void simulate() {

           canMove = false;
           validSquare = false;

           copyPieces(pieces,simPieces);

           if(castP!=null){
               castP.col = castP.precol;
               castP.x = castP.getX(castP.col);
               castP = null;
           }

           activeP.x = mouse.x - Board.Half_Square_Size;
           activeP.y = mouse.y - Board.Half_Square_Size;

           activeP.col = activeP.getCol(activeP.x);
           activeP.row = activeP.getRow(activeP.y);


           if(activeP.canMove(activeP.col , activeP.row)){
               canMove = true;

                if(activeP.hittingP !=null){
                    simPieces.remove(activeP.hittingP.getIndex());
                }

               checkCastling();

                if(isIllegal(activeP) == false && oppCanCaptureKing()==false){
                    validSquare = true;
                }
           }

    }

    private boolean oppCanCaptureKing(){
           Piece king = getKing(false);
           for(Piece p : simPieces){
               if(p.color !=king.color && p.canMove(king.col,king.row)){
                   return true;
               }
           }
           return false;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw the board first
        board.draw(g2);

        // Draw each piece on the board
        for (Piece p : simPieces) {
            p.draw(g2);
        }

        // Highlight the active piece with transparency
        if (activeP != null) {
            if(canMove) {
                // Set color to white and apply transparency
                if (isIllegal(activeP) || oppCanCaptureKing()) {
                    g2.setColor(Color.gray);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

                    // Draw the transparent rectangle around the active piece
                    g2.fillRect(activeP.col * Board.Square_Size, activeP.row * Board.Square_Size,
                            Board.Square_Size, Board.Square_Size);

                    // Reset transparency to opaque (1.0f)
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
                else {
                    g2.setColor(Color.white);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));

                    // Draw the transparent rectangle around the active piece
                    g2.fillRect(activeP.col * Board.Square_Size, activeP.row * Board.Square_Size,
                            Board.Square_Size, Board.Square_Size);

                    // Reset transparency to opaque (1.0f)
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            // Draw the active piece on top of the highlighted square
            activeP.draw(g2);
        }
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING , RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(new Font("Book Antiqua" , Font.PLAIN , 40));
        g2.setColor(Color.white);

        if(promotion){
            g2.drawString("Promote to:" , 840 , 150);
            for(Piece p : promoPieces){
                g2.drawImage(p.image , p.getX(p.col), p.getY(p.row) ,Board.Square_Size,
                        Board.Square_Size, null);
            }
        }
        else{
            if(currentColor == WHITE){
                g2.drawString("White's turn" , 840 , 400);
                if(checkingP !=null && checkingP.color ==BLACK){
                    Piece king = getKing(false);
                    Point previousKingPosition = new Point(king.col, king.row);
                    g2.setColor(Color.RED);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Set transparency
                    g2.fillRect(previousKingPosition.x * Board.Square_Size, previousKingPosition.y * Board.Square_Size,
                            Board.Square_Size, Board.Square_Size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
            else {
                g2.setColor(Color.black);
                g2.drawString("Black's turn" , 840 , 400);
                if(checkingP !=null && checkingP.color == WHITE){
                    Piece king = getKing(false);
                    Point previousKingPosition = new Point(king.col, king.row);
                    g2.setColor(Color.RED);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // Set transparency
                    g2.fillRect(previousKingPosition.x * Board.Square_Size, previousKingPosition.y * Board.Square_Size,
                            Board.Square_Size, Board.Square_Size);
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
                }
            }
        }
        if ((gameOver || stalemate) && !popupShown) {
            popupShown = true;  // Prevent further popups

            // Create a custom dialog
            JDialog dialog = new JDialog();
            dialog.setTitle(gameOver ? "Game Over" : "Stalemate");

            // Set dialog size and layout
            dialog.setSize(400, 200);
            dialog.setLayout(new BorderLayout());

            // Create a panel with a colorful background
            JPanel panel = new JPanel();
            panel.setBackground(new Color(50, 50, 150));  // Custom background color
            panel.setLayout(new BorderLayout());

            // Create a label for the result message with custom font and color
            JLabel messageLabel = new JLabel();
            String message = gameOver ? (currentColor == WHITE ? "White Wins!" : "Black Wins!") : "Match is Draw!";
            messageLabel.setText(message);
            messageLabel.setFont(new Font("Arial", Font.BOLD, 24));
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);

            panel.add(messageLabel, BorderLayout.CENTER);

            // Add the panel to the dialog
            dialog.add(panel, BorderLayout.CENTER);

            // Create custom buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBackground(Color.DARK_GRAY);

            JButton restartButton = new JButton("New Game");
            JButton closeButton = new JButton("Close");

            // Set button colors and font
            restartButton.setBackground(Color.GREEN);
            restartButton.setForeground(Color.WHITE);
            restartButton.setFont(new Font("Arial", Font.BOLD, 16));

            closeButton.setBackground(Color.RED);
            closeButton.setForeground(Color.WHITE);
            closeButton.setFont(new Font("Arial", Font.BOLD, 16));

            // Add buttons to the button panel
            buttonPanel.add(restartButton);
            buttonPanel.add(closeButton);

            // Add the button panel to the dialog
            dialog.add(buttonPanel, BorderLayout.SOUTH);

            // Action Listener for "Restart" Button
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Logic to restart the game
                    dialog.dispose();
                    restartGame();
                      // Close the dialog after restarting
                }
            });

            // Action Listener for "Close" Button
            closeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();// Simply close the dialog
                }
            });

            // Center the dialog on the screen and make it visible
            dialog.setLocationRelativeTo(null);  // Center on screen
            dialog.setVisible(true);
        }

// Method to restart the game

    }

        private void restartGame() {
            // Reset the game flags
            gameOver = false;
            stalemate = false;
            popupShown = false;
            currentColor = WHITE; // Reset to White's turn
            activeP = null; // No active piece

            // Clear the pieces and reset to initial state
            pieces.clear();
            simPieces.clear();
            setPieces(); // Reset the pieces to initial configuration
            copyPieces(pieces, simPieces); // Copy to the simulation array

             // Repaint the panel to reflect the new game state
        }

    }


