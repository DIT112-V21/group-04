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
import com.example.medcarapp.login;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class CarConnect extends AppCompatActivity{
    private String User;
    private String Pass;
    private static final String TAG="SmartcarmqttController";
    private static final String OBSTACLE_TOPIC = "/smartcar/obstacle";
    private static final String SWITCH_SERVER_TOPIC = "/smartcar/switchServer";
    private static final String CAMERA_TOPIC = "Camera_Stream";
    private static final int QOS = 0;
    private static final int IMAGE_WIDTH = 160;
    private static final int IMAGE_HEIGHT = 120;
    private static final String EXTERNAL_MQTT_BROKER = "tcp://18.222.170.203:1883";
    private static final String LOCALHOST = "tcp://10.0.2.2:1883";
    private String MQTT_SERVER;
    Context context;

    private MqttClient mMqttClientLocal;
    private MqttClient mMqttClientExternal;
    private MqttClient mMqttClient;
    private boolean isConnected = false;
    private ImageView mCameraView;
    private Button autoButton;

    public CarConnect(Context context, ImageView mCameraView, boolean shouldSwitch, Button autoButton,String user, String password) {
        this.context = context;
        MQTT_SERVER = LOCALHOST;
        mMqttClientLocal = new MqttClient(context, LOCALHOST, user);
        mMqttClientExternal = new MqttClient(context, EXTERNAL_MQTT_BROKER, user);
        if (shouldSwitch){
            MQTT_SERVER = EXTERNAL_MQTT_BROKER;
            switchServer();
        }
        this.mCameraView = mCameraView;
        this.autoButton = autoButton;
        this.User=user;
        this.Pass=password;
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
                Log.i(User, "Disconnected from broker");
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            }
        });
    }

    public void connectToMqttBroker(TextView connectionText) {
        if (MQTT_SERVER.equals(EXTERNAL_MQTT_BROKER)){
            mMqttClient = mMqttClientExternal;
        } else {
            mMqttClient = mMqttClientLocal;
        }
        if (!isConnected) {
            mMqttClient.connect(User, Pass, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    isConnected = true;

                    String successfulConnectionMessage = context.getString(R.string.connectedToMQTTBrokerMessage);
                    Log.i(TAG, successfulConnectionMessage);
                    feedbackMessage(successfulConnectionMessage);

                    String connectedMessage = context.getString(R.string.connectedIndicator);
                    connectionText.setText(connectedMessage);
                    connectionText.setTextColor(Color.parseColor("#32CD32"));


                    mMqttClient.subscribe(OBSTACLE_TOPIC,QOS,null);
                    mMqttClient.subscribe(CAMERA_TOPIC, QOS, null);
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {

                    String startingAutonomousButtonText = context.getString(R.string.startingAutonomousButtonText);
                    autoButton.setText(startingAutonomousButtonText);

                    mCameraView.setImageResource(R.drawable.intermission);
                    String failedConnectionMessage = context.getString(R.string.failedToConnectToMQTTBrokerMessage);
                    Log.e(TAG, failedConnectionMessage);
                    feedbackMessage(failedConnectionMessage);

                    String disconnectedMessage = context.getString(R.string.disconnectedIndicator);
                    connectionText.setText(disconnectedMessage);
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }
            }, new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                    String startingAutonomousButtonText = context.getString(R.string.startingAutonomousButtonText);
                    autoButton.setText(startingAutonomousButtonText);


                    mCameraView.setImageResource(R.drawable.intermission);

                    String lostConnectionMessage = context.getString(R.string.connectionToMQTTBrokerLostMessage);
                    Log.w(TAG, lostConnectionMessage);
                    feedbackMessage(lostConnectionMessage);

                    String disconnectedMessage = context.getString(R.string.disconnectedIndicator);
                    connectionText.setText(disconnectedMessage);
                    connectionText.setTextColor(Color.parseColor("#EF1919"));
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {

                    if (topic.equals(OBSTACLE_TOPIC)) {
                        String obstacleDetectedMessage = context.getString(R.string.obstacleDetectedMessage);
                        Log.i(TAG, obstacleDetectedMessage);
                        feedbackMessage(obstacleDetectedMessage);
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

    private void switchServer(){
        mMqttClientLocal.connect(TAG, "", new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
                mMqttClientLocal.publish(SWITCH_SERVER_TOPIC, "Switch", QOS, null);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                Log.e(TAG, "Could not send switch message");
            }
        }, null);
    }
}