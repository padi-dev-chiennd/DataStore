package com.example.datastore;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class TouchControlledSpriteView extends SurfaceView implements SurfaceHolder.Callback {
    private SpriteThread spriteThread;
    private int spriteX, spriteY;
    private Bitmap spriteBitmap;
    private int targetColor, currentColor;

    private int targetX = 500; // Change color when the sprite reaches this x-coordinate
    private boolean isTouching = false;

    public TouchControlledSpriteView(Context context) {
        super(context);
        init();
    }

    public TouchControlledSpriteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        getHolder().addCallback(this);
        spriteBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test3);
        spriteX = 100;
        spriteY = 100;
        currentColor = Color.RED;  // Initial color
        targetColor = Color.BLUE; // Color to transition to
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        spriteThread = new SpriteThread(holder);
        spriteThread.setRunning(true);
        spriteThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        spriteThread.setRunning(false);
        while (retry) {
            try {
                spriteThread.join();
                retry = false;
            } catch (InterruptedException e) {
                // Retry
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Update sprite position based on touch input
                spriteX = (int) event.getX();
                spriteY = (int) event.getY();

                // Start color transition animation
                startColorTransition();
                isTouching = true;
                return true;

            case MotionEvent.ACTION_UP:
                // Stop color transition animation when touch is released
                isTouching = false;
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void startColorTransition() {
        // Create a ValueAnimator for color transition
        ValueAnimator colorAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), currentColor, targetColor);
        colorAnimator.setDuration(1000); // Duration in milliseconds

        // Update color and start the animation
        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                currentColor = (int) animator.getAnimatedValue();
            }
        });

        colorAnimator.start();
    }

    private class SpriteThread extends Thread {
        private SurfaceHolder surfaceHolder;
        private boolean isRunning;

        public SpriteThread(SurfaceHolder holder) {
            surfaceHolder = holder;
            isRunning = false;
        }

        public void setRunning(boolean running) {
            isRunning = running;
        }

        @Override
        public void run() {
            while (isRunning) {
                Canvas canvas = null;
                try {
                    canvas = surfaceHolder.lockCanvas();
                    if (canvas != null) {
                        drawSprite(canvas);
                    }
                } finally {
                    if (canvas != null) {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }
                }
            }
        }

        private void drawSprite(Canvas canvas) {
            canvas.drawColor(Color.WHITE); // Clear the canvas

            // Check if the sprite has reached the target position
            if (spriteX >= targetX) {
                currentColor = Color.RED; // Change color to red
            }

            // Draw the sprite at its current position with the current color
            Paint paint = new Paint();
            paint.setColor(currentColor);
            canvas.drawBitmap(spriteBitmap, spriteX - spriteBitmap.getWidth() / 2, spriteY - spriteBitmap.getHeight() / 2, paint);

            // Update sprite position for animation
            if (isTouching) {
                spriteX += 5; // Example: Move sprite horizontally
            }

            // Add animation logic or handle sprite boundaries as needed
        }
    }
}
