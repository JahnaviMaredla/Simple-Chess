package main;
import java.awt.*;

public class Board {
    final int Max_Col = 8;
    final int Max_Row = 8;
    public static final int Square_Size = 100;
    public static final int Half_Square_Size = 50;


    public void draw (Graphics2D g2){
         int c=0;
          for(int row=0 ; row<Max_Row;row++){
                for(int col=0 ; col<Max_Col;col++){
                    if(c==0){
                        g2.setColor(new Color(210,165,125));
                        c=1;
                    }
                    else {
                        g2.setColor(new Color(175,115,70));
                        c=0;
                    }
                    g2.fillRect(col*Square_Size , row*Square_Size , Square_Size , Square_Size);
                }

                if(c==0){
                    c=1;
                }
                else{
                    c=0;
                }
        }
    }
}
