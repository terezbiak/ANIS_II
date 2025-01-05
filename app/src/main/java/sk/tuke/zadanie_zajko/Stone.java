package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Stone {
    Bitmap stone[] = new Bitmap[3];
    int stoneFrame = 0;
    int stoneX, stoneY, stoneVelocity;
    Random random;
    public Stone(Context context){
        stone[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone1);
        stone[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone2);
        stone[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.stone3);
        random = new Random();
        resetPosition();
    }

    public Bitmap getStone(int stoneFrame){
        return stone[stoneFrame];
    }

    public int getStoneWidth(){
        return stone[0].getWidth();
    }
    public int getStoneHeight(){
        return stone[0].getHeight();
    }


    public void resetPosition() {
        stoneX = random.nextInt(GameView.dWidth - getStoneWidth());
        stoneY = -200 + random.nextInt(600) * -1;
        stoneVelocity = 35 + random.nextInt(16);

    }

}
