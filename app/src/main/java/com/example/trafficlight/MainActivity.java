package com.example.trafficlight;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Gpio gpioRed, gpioYellow, gpioGreen;
    private int light = 0;
    private int iCounter = 0;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManager peripheralManager = PeripheralManager.getInstance();

        try {
            gpioRed = peripheralManager.openGpio("BCM17");
            gpioRed.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpioRed.setValue(false);

            gpioYellow = peripheralManager.openGpio("BCM27");
            gpioYellow.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpioYellow.setValue(false);

            gpioGreen = peripheralManager.openGpio("BCM22");
            gpioGreen.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            gpioGreen.setValue(true);

        }catch (Exception e){
            Log.v("brad", e.toString());
        }

        timer = new Timer();
        timer.schedule(new InitTask(), 0, 100);

    }

    private class InitTask extends TimerTask {
        @Override
        public void run() {
            try{
                if (iCounter % 3 == 0){
                    gpioGreen.setValue(true);
                    gpioYellow.setValue(false);
                    gpioRed.setValue(false);
                }else if (iCounter % 3 == 1){
                    gpioGreen.setValue(false);
                    gpioYellow.setValue(true);
                    gpioRed.setValue(false);
                }else if (iCounter % 3 == 2){
                    gpioGreen.setValue(false);
                    gpioYellow.setValue(false);
                    gpioRed.setValue(true);
                }
                iCounter++;
                if (iCounter == 50){
                    cancel();
                    iCounter = 0;
                    timer.schedule(new LightTask(), 0, 1000);
                }

            }catch (Exception e){
                Log.v("brad", e.toString());
            }
        }
    }

    private class LightTask extends TimerTask {
        @Override
        public void run() {
            try{
                if (light == 0){
                    // green
                    gpioGreen.setValue(true);
                    gpioYellow.setValue(false);
                    gpioRed.setValue(false);

                    iCounter++;
                    if (iCounter == 10){
                        light = 1;
                        iCounter = 0;
                    }
                }else if(light == 1){
                    // yellow
                    gpioGreen.setValue(false);
                    gpioYellow.setValue(true);
                    gpioRed.setValue(false);

                    iCounter++;
                    if (iCounter == 3){
                        light = 2;
                        iCounter = 0;
                    }
                }else if(light == 2){
                    // red
                    gpioGreen.setValue(false);
                    gpioYellow.setValue(false);
                    gpioRed.setValue(true);

                    iCounter++;
                    if (iCounter == 10){
                        light = 3;
                        iCounter = 0;
                    }
                }else if(light == 3){
                    // yellow
                    gpioGreen.setValue(false);
                    gpioYellow.setValue(true);
                    gpioRed.setValue(false);

                    iCounter++;
                    if (iCounter == 3){
                        light = 0;
                        iCounter = 0;
                    }
                }


            }catch (Exception e){
                Log.v("brad", e.toString());
            }
        }
    }


}
