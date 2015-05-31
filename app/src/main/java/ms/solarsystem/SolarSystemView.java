package ms.solarsystem;
//Version 0.1.1 xxxx
//changed pref theme
///

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;

import java.util.Calendar;
import java.util.Formatter;
import android.graphics.Typeface;
import android.widget.Toast;
import android.util.Log;

public class SolarSystemView extends View {
        //from asus
    private static int NONE = 0;
    private static int DRAG = 1;
    private static int ZOOM = 2;
    private int mode;
    private static float MIN_ZOOM = 1f;
    private static float MAX_ZOOM = 10f;
    private float scaleFactor = 1f;
    private ScaleGestureDetector detector;
    private float startX = 0f;
    private float startY = 0f;
    private boolean dragged;
    private float translateX = 0f;
    private float translateY = 0f;
    private float previousTranslateX = 0f;
    private float previousTranslateY = 0f;
    private float displayWidth;
    private float displayHeight;
    private float  mLastGestureX;
    private float mLastGestureY;

    private float sunRadius = 8; // Ball's radius
    private float earthRadius = 6; // 2 Ball's  radius
    private float sunX = 650;  // Ball's center (x,y)
    private float earthX = 650;  // Ball's center (x,y)
    private float sunY = 300;
    private float earthY= 480;
    private float venusRadius = 5;
    private float venX = 650;  //
    private float venY = 408;
    private float mrcRadius = 3;
    private float mrcX = 650;  //
    private float mrcY = 295;
    private float mrsRadius = 3;
    private float mrsX = 650;  //
    private float mrsY = 528;
    private RectF ballBounds;      // Needed for Canvas.drawOval
    private Paint paint;           // The paint (e.g. style, color) used for drawing
    private double radearth=150;
    private double radmrs=1.524*radearth;
    private double radmrc=.3871*radearth;
    private double radven=.723*radearth;
    private float flrdmrc = (float) radmrc;
    private float flrdvev = (float) radven;
    private float flrdearth = (float) radearth;
    private float flrdmrs = (float) radmrs;
    private String Suncolor="#ffea7d";
    private String Suncolor2="#fffffe";
    private String Earthcolor="#017ed5";
    private String Merccolor="#999999";
    private String Marscolor="#e0301e";
    private String Vencolor= "#fffae2";
    private String Earthorbclr="#017ed5";
    private String Mercorbclr="#999999";
    private String Marsorbclr="#e0301e";
    private String Venorbclr= "#fffae2";
    private  double thcns=.001;
    Calendar c = Calendar.getInstance();
    int dyr = c.get(Calendar.DAY_OF_YEAR);
    int yr = c.get(Calendar.YEAR)-1;
    //int oyr=2014; int oday=132;
    double dayspass= (yr * 365.25)+dyr-735745.5;
    private double earthdegrees=(dayspass*0.98563);
    private double vencnv =1.625524007069; //venus covers 1.622.. degress while earth covers 1 degree
    private double mrccnv =4.0;//203082904;
    private double mrscnv=0.5316864552961;///
    private double theta=130-earthdegrees;// earth at 139 on may 12 2015
    private double Vtheta=200-(earthdegrees*vencnv);
    private double Mrctheta=180-(earthdegrees*mrccnv);
    private double Mrstheta=300-(earthdegrees*mrscnv);
    //String dayOfyear = Integer.toString(dyr);
    String dp = Double.toString(dayspass);
    // Status message to show Ball's (x,y) position and speed.
    private StringBuilder statusMsg = new StringBuilder();
    private Formatter formatter = new Formatter(statusMsg);  // Formatting the statusMsg
    // private StringBuilder statusMsg2 = new StringBuilder();
    //private Formatter formatter2 = new Formatter(statusMsg2);

    SharedPreferences sharedPrefs =
            PreferenceManager.getDefaultSharedPreferences(getContext());
//

    // Constructor
    public SolarSystemView(Context context) {
        super(context);
        detector = new ScaleGestureDetector(getContext(), new ScaleListener());
        ballBounds = new RectF();
        paint = new Paint();
        // Set the font face and size of drawing text
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(16);
        // To enable keypad on this View
        this.setFocusable(true);
        this.requestFocus();
        // To enable touch mode
        this.setFocusableInTouchMode(true);
        SharedPreferences sharedPrefs =
                PreferenceManager.getDefaultSharedPreferences(context);
        // Log.i("dayyear", dayOfyear);
        //Log.i("dayyear", dp);


    }

    // Called back to draw the view. Also called by invalidate().
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        if (detector.isInProgress()){

            canvas.scale(scaleFactor, scaleFactor,this.getPivotX(), this.getPivotY());
        }
        else{

            canvas.scale(scaleFactor, scaleFactor, this.getX(), this.getY());
        }
        //If translateX times -1 is lesser than zero, let's set it to zero. This takes care of the left bound

        displayWidth = this.getWidth();
        displayHeight = this.getHeight();

        if((translateX * -1) < 0) {
            translateX = 0;
        }
        //This is where we take care of the right bound. We compare translateX times -1 to (scaleFactor - 1) * displayWidth.
        //If translateX is greater than that value, then we know that we've gone over the bound. So we set the value of
        //translateX to (1 - scaleFactor) times the display width. Notice that the terms are interchanged; it's the same
        //as doing -1 * (scaleFactor - 1) * displayWidth
        else if((translateX * -1) > (scaleFactor - 1) * displayWidth) {
            translateX = (1 - scaleFactor) * displayWidth;
        }
        if(translateY * -1 < 0) {
            translateY = 0;
        }
        //We do the exact same thing for the bottom bound, except in this case we use the height of the display
        else if((translateY * -1) > (scaleFactor - 1) * displayHeight) {
            translateY = (1 - scaleFactor) * displayHeight;
        }
        //We need to divide by the scale factor here, otherwise we end up with excessive panning based on our zoom level
        //because the translation amount also gets scaled according to how much we've zoomed into the canvas.
        canvas.translate(translateX / scaleFactor, translateY / scaleFactor);
        //String unitType = getContext().getString(R.string.pref_units_key);

        String unitType = sharedPrefs.getString(
                getContext().getString(R.string.pref_units_key),
                getContext().getString(R.string.pref_units_metric));
        if (unitType.equals(getContext().getString(R.string.pref_units_imperial))) {
            Earthorbclr="#090404";
            Mercorbclr="#090404";
            Marsorbclr="#090404";
            Venorbclr= "#090404";
        } else  {

            Earthorbclr="#017ed6";
            Mercorbclr="#999998";
            Marsorbclr="#e0301e";
            Venorbclr= "#fffae2";
        }
        // Log.i("myapp", unitType);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor(Mercorbclr));
        canvas.drawCircle(656, 302, flrdmrc, paint);
        paint.setColor(Color.parseColor(Venorbclr));
        canvas.drawCircle(650, 300, flrdvev, paint);
        paint.setColor(Color.parseColor(Earthorbclr));
        canvas.drawCircle(650, 300, flrdearth, paint);
        paint.setColor(Color.parseColor(Marsorbclr));
        canvas.drawCircle(645, 313, flrdmrs, paint);
        // Draw Planets
        paint.setStyle(Paint.Style.FILL);
        ballBounds.set(sunX - sunRadius, sunY - sunRadius, sunX + sunRadius, sunY + sunRadius);
        paint.setColor(Color.parseColor(Suncolor));
        canvas.drawOval(ballBounds, paint);//must be done for each
        ballBounds.set(earthX - earthRadius, earthY - earthRadius, earthX + earthRadius, earthY + earthRadius);
        paint.setColor(Color.parseColor(Earthcolor));
        canvas.drawOval(ballBounds, paint);
        ballBounds.set(venX - venusRadius, venY - venusRadius, venX + venusRadius, venY + venusRadius);
        paint.setColor(Color.parseColor(Vencolor));
        canvas.drawOval(ballBounds, paint);
        ballBounds.set(mrcX - mrcRadius, mrcY - mrcRadius, mrcX + mrcRadius, mrcY + mrcRadius);
        paint.setColor(Color.parseColor(Merccolor));
        canvas.drawOval(ballBounds, paint);
        ballBounds.set(mrsX - mrsRadius, mrsY - mrsRadius, mrsX + mrsRadius, mrsY + mrsRadius);
        paint.setColor(Color.parseColor(Marscolor));
        canvas.drawOval(ballBounds, paint);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(12);
        formatter.format("MERCURY");
        // Draw the status message
        paint.setColor(Color.parseColor(Mercorbclr));
        canvas.drawText(statusMsg.toString(), 627, 353, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
        formatter.format("VENUS");
        paint.setColor(Color.parseColor(Venorbclr));
        canvas.drawText(statusMsg.toString(), 630, 420, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer

        formatter.format("EARTH");
        paint.setColor(Color.parseColor(Earthorbclr));
        canvas.drawText(statusMsg.toString(), 629, 460, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer
        formatter.format("MARS");
        paint.setColor(Color.parseColor(Marsorbclr));
        canvas.drawText(statusMsg.toString(), 635, 551, paint);
        statusMsg.delete(0, statusMsg.length()); // Empty buffer

        //  "Earth@(%3.0f,%3.0f),Venus=(%2.0f,%2.0f)",0 ,0, 0, 0);
        update();

        // Delay
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) { }

        canvas.restore();
        invalidate();  // Force a re-draw
        // update();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction() & MotionEvent.ACTION_MASK) {

            case MotionEvent.ACTION_DOWN:
                mode = DRAG;

                //We assign the current X and Y coordinate of the finger to startX and startY minus the previously translated
                //amount for each coordinates This works even when we are translating the first time because the initial
                //values for these two variables is zero.
                startX = event.getX() - previousTranslateX;
                startY = event.getY() - previousTranslateY;

                if (translateX == 0 && translateY == 0){
                    if(event.getX() >= 600 && event.getX() <= 700 && event.getY() >= 250 && event.getY() < 350) {
                        update2();

                    }}
                break;

            case MotionEvent.ACTION_MOVE:
                if (detector.isInProgress()){
                    mLastGestureX = detector.getFocusX();
                    mLastGestureY =detector.getFocusY();
                }

                translateX = event.getX() - startX;
                translateY = event.getY() - startY;

                //We cannot use startX and startY directly because we have adjusted their values using the previous translation values.
                //This is why we need to add those values to startX and startY so that we can get the actual coordinates of the finger.
                double distance = Math.sqrt(Math.pow(event.getX() - (startX + previousTranslateX), 2) +
                                Math.pow(event.getY() - (startY + previousTranslateY), 2)
                );

                if(distance > 0) {
                    dragged = true;
                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                break;

            case MotionEvent.ACTION_UP:
                mode = NONE;
                dragged = false;

                //All fingers went up, so let's save the value of translateX and translateY into previousTranslateX and
                //previousTranslate
                previousTranslateX = translateX;
                previousTranslateY = translateY;


                break;

            case MotionEvent.ACTION_POINTER_UP:
                mode = DRAG;

                //This is not strictly necessary; we save the value of translateX and translateY into previousTranslateX
                //and previousTranslateY when the second finger goes up
                previousTranslateX = translateX;
                previousTranslateY = translateY;
                break;
        }

        detector.onTouchEvent(event);

        //We redraw the canvas only in the following cases:
        //
        // o The mode is ZOOM
        //        OR
        // o The mode is DRAG and the scale factor is not equal to 1 (meaning we have zoomed) and dragged is
        //   set to true (meaning the finger has actually moved)
        if ((mode == DRAG && scaleFactor != 1f && dragged) || mode == ZOOM) {
            invalidate();
        }

        return true;
    }


    // Detect collision and update the position of the ball.
    private void update() {
        // Get new (x,y) position
        SharedPreferences mSettings = getContext().getSharedPreferences("Settings", 0);
        String spd= mSettings.getString("id", "missing");
        if (spd=="hrs"){thcns=.01;
        }else if(spd=="dys"){thcns=.1;
        }else if(spd=="mth") {
            thcns = .5;
        }else if(spd=="act"){thcns=.001;}
       // String rotspd= ;
        theta-=thcns*1; Vtheta-=vencnv*thcns;Mrctheta-=mrccnv*thcns;Mrstheta-=thcns*mrscnv;/// 365/ orb period of planets
        double Ex=radearth* Math.cos(Math.toRadians(theta));
        double Ey=radearth* Math.sin(Math.toRadians(theta));
        double Vx=radven* Math.cos(Math.toRadians(Vtheta));
        double Vy=radven* Math.sin(Math.toRadians(Vtheta));
        double Mrcx=radmrc* Math.cos(Math.toRadians(Mrctheta));
        double Mrcy=radmrc* Math.sin(Math.toRadians(Mrctheta));
        double Mrsx=radmrs* Math.cos(Math.toRadians(Mrstheta));
        double Mrsy=radmrs* Math.sin(Math.toRadians(Mrstheta));

        earthX =650+ (float) Ex;
        earthY =300+(float) Ey;
        venX =650+ (float) Vx;
        venY =300+(float) Vy;
        mrcX =656+ (float) Mrcx;
        mrcY =302+(float) Mrcy;
        mrsX =645+ (float) Mrsx;
        mrsY =313+(float) Mrsy;


    }

    private void update2() {

        if(Suncolor==Suncolor2){
           // thcns=1;
            Suncolor="#ffea7d";
        }else {
           /// thcns=.0001;
            Suncolor=Suncolor2;
        }
    }

    // Called back when the view is first created or its size changes.
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(MIN_ZOOM, Math.min(scaleFactor, MAX_ZOOM));
            invalidate();
            return true;
        }
    }

    int update3() {
        return 1;

    }



}