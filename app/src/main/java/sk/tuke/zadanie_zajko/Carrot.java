package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.Random;

public class Carrot {
    Bitmap carrot[] = new Bitmap[3];
    int carrotFrame = 0;
    int carrotX, carrotY, carrotVelocity;
    Random random;
    public Carrot(Context context){
        carrot[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.carrot1);
        carrot[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.carrot4);
        carrot[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.carrrot);
        random = new Random();
        resetPosition();
    }

    public Bitmap getCarrot(int carrotFrame){
        return carrot[carrotFrame];
    }

    public int getCarrotWidth(){
        return carrot[0].getWidth();
    }
    public int getCarrotHeight(){
        return carrot[0].getHeight();
    }


    public void resetPosition() {
        carrotX = random.nextInt(GameView.dWidth - getCarrotWidth());
        carrotY = -200 + random.nextInt(600) * -1;
        carrotVelocity = 10 + random.nextInt(16);

    }

}
