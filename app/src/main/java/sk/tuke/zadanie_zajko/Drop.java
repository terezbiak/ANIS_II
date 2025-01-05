package sk.tuke.zadanie_zajko;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Drop {
    Bitmap drop[] = new Bitmap[4];
    int dropFrame = 0;
    int dropX, dropY;

    public Drop(Context context){
        drop[0] = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop1);
        drop[1] = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop2);
        drop[2] = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop3);
        drop[3] = BitmapFactory.decodeResource(context.getResources(), R.drawable.drop4);
    }

    public Bitmap getDrop(int dropFrame){
        return drop[dropFrame];
    }
}
