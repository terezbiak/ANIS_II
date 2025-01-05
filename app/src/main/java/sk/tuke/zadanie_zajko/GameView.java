package sk.tuke.zadanie_zajko;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameView extends View { //DrawView
    Bitmap heart;

    Bitmap background, ground, bunny, bunnyOpen;
    Rect rectBackground, rectGround;
    Context context;
    Handler handler;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint textPaint = new Paint();
    Paint healtPaint = new Paint();
    float TEXT_SIZE = 120;
    int points = 0;
    int life = 3;
    static int dWidth, dHeight;
    Random random;
    float bunnyX, bunnyY;
    float oldX;
    float oldBunnyX;
    ArrayList<Carrot> carrots;
    ArrayList<Drop> drops;
    ArrayList<Stone> stones;
    private int stormFrame = 0;
    private int snowFrame = 0;
    private Bitmap[] stormBackgrounds;
    private Bitmap[] snowBackgrounds;


    public GameView(Context context) {
        super(context);
        this.context = context;
        SharedPreferences preferences = context.getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        String weather = preferences.getString("weather", "Clear");

        stormBackgrounds = new Bitmap[4];
        stormBackgrounds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.storm1);
        stormBackgrounds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.storm2);
        stormBackgrounds[2] = BitmapFactory.decodeResource(getResources(), R.drawable.storm3);
        stormBackgrounds[3] = BitmapFactory.decodeResource(getResources(), R.drawable.storm4);

        snowBackgrounds = new Bitmap[4];
        snowBackgrounds[0] = BitmapFactory.decodeResource(getResources(), R.drawable.snow1);
        snowBackgrounds[1] = BitmapFactory.decodeResource(getResources(), R.drawable.snow2);
        snowBackgrounds[2] = BitmapFactory.decodeResource(getResources(), R.drawable.snow3);
        snowBackgrounds[3] = BitmapFactory.decodeResource(getResources(), R.drawable.snow4);


        if (weather.equals("Clear")) {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.background);
        }
        if (weather.equals("Fog")) {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.fog);
        }
        if (weather.equals("Rain")) {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.rain);
        }
        if (weather.equals("Clouds")) {
            background = BitmapFactory.decodeResource(getResources(), R.drawable.cloudy);
        }
        if (weather.equals("Storm")) { //|| weather.equals("Default")
            background = stormBackgrounds[stormFrame];
            stormFrame = (stormFrame + 1) % stormBackgrounds.length;
        }
        if (weather.equals("Snow") || weather.equals("Default")) { //|| weather.equals("Default")
            background = snowBackgrounds[snowFrame];
            snowFrame = (snowFrame + 1) % snowBackgrounds.length;
        }


        ground = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        bunny = BitmapFactory.decodeResource(getResources(), R.drawable.bunny);
        bunnyOpen = BitmapFactory.decodeResource(getResources(), R.drawable.bunnyopenmouth);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        rectBackground = new Rect(0,0, dWidth, dHeight);
        rectGround = new Rect(0,dHeight - ground.getHeight(), dWidth, dHeight);
        heart = BitmapFactory.decodeResource(getResources(), R.drawable.heart);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        textPaint.setColor(Color.rgb(255, 165, 0));
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTypeface(ResourcesCompat.getFont(context, R.font.amatic));
        healtPaint.setColor(Color.GREEN);
        random = new Random();
        bunnyX = dWidth / 2 - bunny.getWidth() / 2;
        bunnyY = dHeight - ground.getHeight() - bunny.getHeight();
        carrots = new ArrayList<>();
        stones = new ArrayList<>();
        drops = new ArrayList<>();
        for (int i = 0; i < 1; i++){
            Carrot carrot = new Carrot(context);
            carrots.add(carrot);
        }
        for (int i = 0; i < 2; i++){
            Stone stone = new Stone(context);
            stones.add(stone);
        }

    }



    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);


        SharedPreferences preferences = context.getSharedPreferences("weather_pref", Context.MODE_PRIVATE);
        String weather = preferences.getString("weather", "Clear");
        if (weather.equals("Storm") ) {   //|| weather.equals("Default")
            background = stormBackgrounds[stormFrame];
            stormFrame = (stormFrame + 1) % stormBackgrounds.length;
        }
        if (weather.equals("Snow") || weather.equals("Default")) {
            background = snowBackgrounds[snowFrame];
            snowFrame = (snowFrame + 1) % snowBackgrounds.length;
        }


        canvas.drawBitmap(background, null, rectBackground, null);
        canvas.drawBitmap(ground, null, rectGround, null);
        canvas.drawBitmap(bunny, bunnyX, bunnyY, null);
        for (int i = 0; i < carrots.size(); i++){
            canvas.drawBitmap(carrots.get(i).getCarrot(carrots.get(i).carrotFrame), carrots.get(i).carrotX, carrots.get(i).carrotY, null);
            carrots.get(i).carrotFrame++;
            if (carrots.get(i).carrotFrame > 2){ //kmitanie
                carrots.get(i).carrotFrame = 0;
            }
            carrots.get(i).carrotY += carrots.get(i).carrotVelocity;
            if (bunnyY + bunny.getHeight() > carrots.get(i).carrotY
                    && bunnyY < carrots.get(i).carrotY + carrots.get(i).getCarrotHeight() + 600 &&
                    bunnyX + bunny.getWidth() > carrots.get(i).carrotX &&
                    bunnyX < carrots.get(i).carrotX + carrots.get(i).getCarrotWidth()) {
                canvas.drawBitmap(bunnyOpen, bunnyX, bunnyY, null);
            } else {
                canvas.drawBitmap(bunny, bunnyX, bunnyY, null);
            }
            if (carrots.get(i).carrotY + carrots.get(i).getCarrotHeight() >= dHeight - ground.getHeight()){
                //points -= 500;
                //life--;
                Drop drop = new Drop(context);
                drop.dropX = carrots.get(i).carrotX;
                drop.dropY = carrots.get(i).carrotY;
                drops.add(drop);
                carrots.get(i).resetPosition();
                if (life == 0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
        }
        for (int i = 0; i < stones.size(); i++){
            canvas.drawBitmap(stones.get(i).getStone(stones.get(i).stoneFrame), stones.get(i).stoneX, stones.get(i).stoneY, null);//
            stones.get(i).stoneFrame++; //
            if (stones.get(i).stoneFrame > 2) {
                stones.get(i).stoneFrame = 0;
            }
            stones.get(i).stoneY += stones.get(i).stoneVelocity;

            if (stones.get(i).stoneY + stones.get(i).getStoneHeight() >= dHeight - ground.getHeight()){
                Drop drop = new Drop(context);
                drop.dropX = stones.get(i).stoneX;
                drop.dropY = stones.get(i).stoneY;
                drops.add(drop);
                stones.get(i).resetPosition();
            }
        }
        for (int i = 0; i < stones.size(); i++){
            if (stones.get(i).stoneX + stones.get(i).getStoneWidth() -20 >= bunnyX
                    && stones.get(i).stoneX +20 <= bunnyX + bunny.getWidth()
                    && stones.get(i).stoneY + stones.get(i).getStoneWidth() -40 >= bunnyY
                    && stones.get(i).stoneY + stones.get(i).getStoneWidth() <= bunnyY + bunny.getHeight()){
                life--;
                stones.get(i).resetPosition();
                if (life == 0){
                    Intent intent = new Intent(context, GameOver.class);
                    intent.putExtra("points", points);
                    context.startActivity(intent);
                    ((Activity) context).finish();
                }
            }
            int j = 0;
            if (carrots.get(j).carrotX + carrots.get(j).getCarrotWidth() >= bunnyX && carrots.get(j).carrotX <= bunnyX + bunny.getWidth() && carrots.get(j).carrotY + carrots.get(j).getCarrotWidth() >= bunnyY && carrots.get(j).carrotY + carrots.get(j).getCarrotWidth() <= bunnyY + bunny.getHeight()) {
                points += 100;
                carrots.get(j).resetPosition();
            }
        }

        for (int i = 0; i < drops.size(); i++){ // animacia drop
            canvas.drawBitmap(drops.get(i).getDrop(drops.get(i).dropFrame), drops.get(i).dropX,
                    drops.get(i).dropY, null);
            drops.get(i).dropFrame++;
            if (drops.get(i).dropFrame > 3) {
                drops.remove(i);
            }
        }
        for (int i = 0; i < life; i++) {
            if (life == 3){ //3
                canvas.drawBitmap(heart, dWidth - 500 + i * (heart.getWidth() + 10), 30, null);
            }
            if (life == 2){ //2
                canvas.drawBitmap(heart, dWidth - 350 + i * (heart.getWidth() + 10), 30, null);
            }
            if (life == 1){ //1
                canvas.drawBitmap(heart, dWidth - 200 + i * (heart.getWidth() + 10), 30, null);

            }
        }
        canvas.drawText("" + points, 20, TEXT_SIZE, textPaint);
        handler.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //isIntersecting(x: Float, y: Float), len to mam poriesene cez event
        float touchX = event.getX();
        float touchY = event.getY();
        float touchThreshold = 50.0f;
        if (touchY >= bunnyY - touchThreshold && touchY <= bunnyY + bunny.getHeight() + touchThreshold) {

            //if (touchY >= bunnyY) {
            int action = event.getAction();
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.getX();
                oldBunnyX = bunnyX;
            }
            if (action == MotionEvent.ACTION_MOVE) {
                float shift = oldX - touchX; // aby nesiel mimo obrazovku
                float newBunnyX = oldBunnyX - shift;
                if (newBunnyX <= 0) {
                    bunnyX = 0;
                } else if (newBunnyX >= dWidth - bunny.getWidth()) {
                    bunnyX = dWidth - bunny.getWidth();
                } else {
                    bunnyX = newBunnyX;
                }
            }
        }
        return true;
    }
}
