package yutori.tf.hangul.exam;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import yutori.tf.hangul.R;
import yutori.tf.hangul.hangul.HangulClassifier;
import yutori.tf.hangul.hangul.PaintView;

import static android.speech.tts.TextToSpeech.ERROR;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String LABEL_FILE = "256-common-hangul.txt";
    private static final String MODEL_FILE = "optimized_hangul_tensorflow.pb";

    private HangulClassifier classifier;
    private PaintView paintView, paintView2, paintView3, paintView4, paintView5, paintView6, paintView7, paintView8,
            paintView9, paintView10, paintView11, paintView12, paintView13, paintView14, paintView15, paintView16;
    //private EditText resultText;
    public TextView resultText;
    private String[] currentTopLabels;
    private ImageButton btnSpeak;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        initDraw();
        playToSpeech();

        Button clearButton = (Button) findViewById(R.id.btn_write_clear);
        clearButton.setOnClickListener(this);

        resultText = (TextView) findViewById(R.id.tv_write_result);

        loadModel();
    }

    private void initDraw() {
        paintView = (PaintView) findViewById(R.id.paint_write_1);
        paintView2 = (PaintView) findViewById(R.id.paint_write_2);
        paintView3 = (PaintView) findViewById(R.id.paint_write_3);
        paintView4 = (PaintView) findViewById(R.id.paint_write_4);
        paintView5 = (PaintView) findViewById(R.id.paint_write_5);
        paintView6 = (PaintView) findViewById(R.id.paint_write_6);
        paintView7 = (PaintView) findViewById(R.id.paint_write_7);
        paintView8 = (PaintView) findViewById(R.id.paint_write_8);
        paintView9 = (PaintView) findViewById(R.id.paint_write_9);
        paintView10 = (PaintView) findViewById(R.id.paint_write_10);
        paintView11 = (PaintView) findViewById(R.id.paint_write_11);
        paintView12 = (PaintView) findViewById(R.id.paint_write_12);
        paintView13 = (PaintView) findViewById(R.id.paint_write_13);
        paintView14 = (PaintView) findViewById(R.id.paint_write_14);
        paintView15 = (PaintView) findViewById(R.id.paint_write_15);
        paintView16 = (PaintView) findViewById(R.id.paint_write_16);

        TextView drawHereText = (TextView) findViewById(R.id.tv_write_drawHere1);
        TextView drawHereText2 = (TextView) findViewById(R.id.tv_write_drawHere2);
        TextView drawHereText3 = (TextView) findViewById(R.id.tv_write_drawHere3);
        TextView drawHereText4 = (TextView) findViewById(R.id.tv_write_drawHere4);
        TextView drawHereText5 = (TextView) findViewById(R.id.tv_write_drawHere5);
        TextView drawHereText6 = (TextView) findViewById(R.id.tv_write_drawHere6);
        TextView drawHereText7 = (TextView) findViewById(R.id.tv_write_drawHere7);
        TextView drawHereText8 = (TextView) findViewById(R.id.tv_write_drawHere8);
        TextView drawHereText9 = (TextView) findViewById(R.id.tv_write_drawHere9);
        TextView drawHereText10 = (TextView) findViewById(R.id.tv_write_drawHere10);
        TextView drawHereText11 = (TextView) findViewById(R.id.tv_write_drawHere11);
        TextView drawHereText12 = (TextView) findViewById(R.id.tv_write_drawHere12);
        TextView drawHereText13 = (TextView) findViewById(R.id.tv_write_drawHere13);
        TextView drawHereText14 = (TextView) findViewById(R.id.tv_write_drawHere14);
        TextView drawHereText15 = (TextView) findViewById(R.id.tv_write_drawHere15);
        TextView drawHereText16 = (TextView) findViewById(R.id.tv_write_drawHere16);

        paintView.setDrawText(drawHereText);
        paintView2.setDrawText(drawHereText2);
        paintView3.setDrawText(drawHereText3);
        paintView4.setDrawText(drawHereText4);
        paintView5.setDrawText(drawHereText5);
        paintView6.setDrawText(drawHereText6);
        paintView7.setDrawText(drawHereText7);
        paintView8.setDrawText(drawHereText8);
        paintView9.setDrawText(drawHereText9);
        paintView10.setDrawText(drawHereText10);
        paintView11.setDrawText(drawHereText11);
        paintView12.setDrawText(drawHereText12);
        paintView13.setDrawText(drawHereText13);
        paintView14.setDrawText(drawHereText14);
        paintView15.setDrawText(drawHereText15);
        paintView16.setDrawText(drawHereText16);
    }

    @Override
    public boolean onTouchEvent( MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            classify();
//                    Timer classify_timer = new Timer();
//                    TimerTask timer_task = new TimerTask() {
//                        @Override
//                        public void run() {
//
//                            resultText.setText("으왕");
//                        }
//                    };
//                    classify_timer.schedule(timer_task, 2000);

        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_write_clear:
                clear();
                resultText.setText("");

                paintView.touch = true;
                paintView2.touch = true;
                paintView3.touch = true;
                paintView4.touch = true;
                paintView5.touch = true;
                paintView6.touch = true;
                paintView7.touch = true;
                paintView8.touch = true;
                paintView9.touch = true;
                paintView10.touch = true;
                paintView11.touch = true;
                paintView12.touch = true;
                paintView13.touch = true;
                paintView14.touch = true;
                paintView15.touch = true;
                paintView16.touch = true;

                break;
        }
    }

    private void clear() {
        paintView.reset();
        paintView2.reset();
        paintView3.reset();
        paintView4.reset();
        paintView5.reset();
        paintView6.reset();
        paintView7.reset();
        paintView8.reset();
        paintView9.reset();
        paintView10.reset();
        paintView11.reset();
        paintView12.reset();
        paintView13.reset();
        paintView14.reset();
        paintView15.reset();
        paintView16.reset();
        paintView.invalidate();
        paintView2.invalidate();
        paintView3.invalidate();
        paintView4.invalidate();
        paintView5.invalidate();
        paintView6.invalidate();
        paintView7.invalidate();
        paintView8.invalidate();
        paintView9.invalidate();
        paintView10.invalidate();
        paintView11.invalidate();
        paintView12.invalidate();
        paintView13.invalidate();
        paintView14.invalidate();
        paintView15.invalidate();
        paintView16.invalidate();
    }

    private void playToSpeech() {
        btnSpeak = (ImageButton) findViewById(R.id.btn_write_speak);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "이사를 간 물고기";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                } else {
                    tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        paintView.onResume();
        paintView2.onResume();
        paintView3.onResume();
        paintView4.onResume();
        paintView5.onResume();
        paintView6.onResume();
        paintView7.onResume();
        paintView8.onResume();
        paintView9.onResume();
        paintView10.onResume();
        paintView11.onResume();
        paintView12.onResume();
        paintView13.onResume();
        paintView14.onResume();
        paintView15.onResume();
        paintView16.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        paintView.onPause();
        paintView2.onPause();
        paintView3.onPause();
        paintView4.onPause();
        paintView5.onPause();
        paintView6.onPause();
        paintView7.onPause();
        paintView8.onPause();
        paintView9.onPause();
        paintView10.onPause();
        paintView11.onPause();
        paintView12.onPause();
        paintView13.onPause();
        paintView14.onPause();
        paintView15.onPause();
        paintView16.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    private void classify() {
        resultText.setText("");

        float pixels[] = paintView.getPixelData();
        currentTopLabels = classifier.classify(pixels);
        //resultText.setText(currentTopLabels[0]);
        if(paintView.touch){
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels2[] = paintView2.getPixelData();
        currentTopLabels = classifier.classify(pixels2);
        //resultText.setText(currentTopLabels[0]);
        if(paintView2.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels3[] = paintView3.getPixelData();
        currentTopLabels = classifier.classify(pixels3);
        //resultText.setText(currentTopLabels[0]);
        if(paintView3.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels4[] = paintView4.getPixelData();
        currentTopLabels = classifier.classify(pixels4);
        //resultText.setText(currentTopLabels[0]);
        if(paintView4.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels5[] = paintView5.getPixelData();
        currentTopLabels = classifier.classify(pixels5);
        //resultText.setText(currentTopLabels[0]);
        if(paintView5.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels6[] = paintView6.getPixelData();
        currentTopLabels = classifier.classify(pixels6);
        //resultText.setText(currentTopLabels[0]);
        if(paintView6.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels7[] = paintView7.getPixelData();
        currentTopLabels = classifier.classify(pixels7);
        //resultText.setText(currentTopLabels[0]);
        if(paintView7.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels8[] = paintView8.getPixelData();
        currentTopLabels = classifier.classify(pixels8);
        //resultText.setText(currentTopLabels[0]);
        if(paintView8.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels9[] = paintView9.getPixelData();
        currentTopLabels = classifier.classify(pixels9);
        //resultText.setText(currentTopLabels[0]);
        if(paintView9.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels10[] = paintView10.getPixelData();
        currentTopLabels = classifier.classify(pixels10);
        //resultText.setText(currentTopLabels[0]);
        if(paintView10.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels11[] = paintView11.getPixelData();
        currentTopLabels = classifier.classify(pixels11);
        //resultText.setText(currentTopLabels[0]);
        if(paintView11.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels12[] = paintView12.getPixelData();
        currentTopLabels = classifier.classify(pixels12);
        //resultText.setText(currentTopLabels[0]);
        if(paintView12.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels13[] = paintView13.getPixelData();
        currentTopLabels = classifier.classify(pixels13);
        //resultText.setText(currentTopLabels[0]);
        if(paintView13.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels14[] = paintView14.getPixelData();
        currentTopLabels = classifier.classify(pixels14);
        //resultText.setText(currentTopLabels[0]);
        if(paintView14.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels15[] = paintView15.getPixelData();
        currentTopLabels = classifier.classify(pixels15);
        //resultText.setText(currentTopLabels[0]);
        if(paintView15.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }

        float pixels16[] = paintView16.getPixelData();
        currentTopLabels = classifier.classify(pixels16);
        //resultText.setText(currentTopLabels[0]);
        if(paintView16.touch){
            //resultText.append(currentTopLabels[0]);
            resultText.append(" ");
        }else{
            resultText.append(currentTopLabels[0]);
        }
    }

    private void loadModel() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = HangulClassifier.create(getAssets(),
                            MODEL_FILE, LABEL_FILE, PaintView.FEED_DIMENSION,
                            "input", "keep_prob", "output");
                } catch (final Exception e) {
                    throw new RuntimeException("Error loading pre-trained model.", e);
                }
            }
        }).start();
    }

}
