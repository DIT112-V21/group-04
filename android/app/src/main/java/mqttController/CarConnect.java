package mqttController;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import vibrator.VibratorWrapper;

import androidx.appcompat.app.AppCompatActivity;

import com.example.medcarapp.R;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CarConnect extends AppCompatActivity {
    private static final String TAG = "SmartcarMqttController";
    private static final String EXTERNAL_MQTT_BROKER = "3.138.188.190";
    private static final String LOCALHOST = "10.0.2.2";
    private static final String TA_SERVER = "aerostun.dev";
    private static final String MQTT_SERVER = "tcp://" + LOCALHOST + ":1883";
    private static final String TURNING_TOPIC = "/smartcar/control/turning";
    private static final String SPEED_TOPIC = "/smartcar/control/speed";
    private static final String OBSTACLE_TOPIC = "/smartcar/obstacle";
    private static final String CAMERA_TOPIC = "Camera_Stream";
    private static final int QOS = 0;
    private static final int IMAGE_WIDTH = 160;
    private static final int IMAGE_HEIGHT = 120;
    private static final String SUCCESSFUL_CONNECTION = "Connected to MQTT broker";
    private static final String FAILED_CONNECTION = "Failed to connect to MQTT broker";
    private static final String LOST_CONNECTION = "Connection to MQTT broker lost";
    private static final String OBSTACLE_DETECTED = "Obstacle detected";
    private static final String STARTING_AUTONOMOUS = "AUTO";

    Context context;

    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private ImageView mCameraView;
    private Button autoButton;

    public CarConnect(Context context, ImageView mCameraView, Button autoButton) {
        this.context = context;
        mMqttClient = new MqttClient(context, MQTT_SERVER, TAG);
        this.mCameraView = mCameraView;
        this.autoButton = autoButton;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mMqttClient.disconnect(new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                Log.i(TAG, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    public void connectToMqttBroker(TextView connectionText) {
        if (!isConnected) {
            mMqttClient.connect(TAG, "", new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;
                    Log.i(TAG, SUCCESSFUL_CONNECTION);
                    feedbackMessage(SUCCESSFUL_CONNECTION);
                    connectionText.setText("Connected");
                    connectionText.setTextColor(Color.parseColor("#32CD32"));


                    mMqttClient.subscribe(OBSTACLE_TOPIC,QOS,null);
                    //mMqttClient.subscribe(TURNING_TOPIC, QOS, null);
                    //mMqttClient.subscribe(SPEED_TOPIC, QOS, null);
                    mMqttClient.subscribe(CAMERA_TOPIC, QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    autoButton.setText(STARTING_AUTONOMOUS);

                    mCameraView.setImageResource(R.drawable.intermission);

                    Log.e(TAG, FAILED_CONNECTION);
                    feedbackMessage(FAILED_CONNECTION);

                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                    autoButton.setText(STARTING_AUTONOMOUS);


                    mCameraView.setImageResource(R.drawable.intermission);

                    Log.w(TAG, LOST_CONNECTION);
                    feedbackMessage(LOST_CONNECTION);
                  
                    connectionText.setText("Disconnected");
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if (topic.equals(OBSTACLE_TOPIC)) {
                        Log.i(TAG, OBSTACLE_DETECTED);
                        feedbackMessage(OBSTACLE_DETECTED);
                        phoneVibration(1000);
                    }

                    if (topic.equals(CAMERA_TOPIC)) {
                        final Bitmap bm = Bitmap.createBitmap(IMAGE_WIDTH, IMAGE_HEIGHT, Bitmap.Config.ARGB_8888);

                        final byte[] payload = message.getPayload();
                        final int[] colors = new int[IMAGE_WIDTH * IMAGE_HEIGHT];
                        for (int ci = 0; ci < colors.length; ++ci) {
                            final byte r = payload[3 * ci];
                            final byte g = payload[3 * ci + 1];
                            final byte b = payload[3 * ci + 2];
                            colors[ci] = Color.rgb(r, g, b);
                        }
                        bm.setPixels(colors, 0, IMAGE_WIDTH, 0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);

                        mCameraView.setImageBitmap(bm);
                    } else {
                        Log.i(TAG, "[MQTT] Topic: " + topic + " | Message: " + message.toString());
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Message delivered");
                }
            });
        }
    }

    public void disconnect(IMqttActionListener disconnectionCallback) {
        mMqttClient.disconnect(disconnectionCallback);
    }

    public void subscribe(String topic, int qos, IMqttActionListener subscriptionCallback) {
        mMqttClient.subscribe(topic, qos, subscriptionCallback);
    }

    public void unsubscribe(String topic, IMqttActionListener unsubscriptionCallback) {
        mMqttClient.unsubscribe(topic, unsubscriptionCallback);
    }

   public void publish(String topic, String message, int qos, IMqttActionListener publishCallback){
        mMqttClient.publish(topic, message, qos, publishCallback);
    }

    public void feedbackMessage(String message){
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP,0,0);
        toast.show();
    }

    private void phoneVibration(int milliSeconds) {
        VibratorWrapper obstacleAvoidanceVibrator = new VibratorWrapper(context);
        obstacleAvoidanceVibrator.vibrate(milliSeconds);
    }

}