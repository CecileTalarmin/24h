import org.eclipse.paho.client.mqttv3.*;

import static java.lang.Thread.sleep;

public class SimpleMqttClient implements MqttCallback {

    MqttClient myClient;
    MqttConnectOptions connOpt;

    static final String BROKER_URL = "tcp://mpd.lan:1883";
    static final String M2MIO_DOMAIN = "laumio";
    static final String M2MIO_STUFF = "Laumio_D454DB";
    static final String M2MIO_THING = "json";
    static final String M2MIO_USERNAME = "";
    static final String M2MIO_PASSWORD_MD5 = "";

    // the following two flags control whether this example is a publisher, a subscriber or both
    static final Boolean subscriber = true;
    static final Boolean publisher = true;

    /**
     *
     * connectionLost
     * This callback is invoked upon losing the MQTT connection.
     *
     */
    @Override
    public void connectionLost(Throwable t) {
        System.out.println("Connection lost!");
        // code to reconnect to the broker would go here if desired
    }

    /**
     *
     * deliveryComplete
     * This callback is invoked when a message published by this client
     * is successfully received by the broker.
     *
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    public void deliveryComplete(MqttDeliveryToken token) {
        //System.out.println("Pub complete" + new String(token.getMessage().getPayload()));
    }

    /**
     *
     * messageArrived
     * This callback is invoked when a message is received on a subscribed topic.
     *
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic);
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
    }


    public void messageArrived(MqttTopic topic, MqttMessage message) throws Exception {
        System.out.println("-------------------------------------------------");
        System.out.println("| Topic:" + topic.getName());
        System.out.println("| Message: " + new String(message.getPayload()));
        System.out.println("-------------------------------------------------");
    }

    /**
     *
     * MAIN
     *
     */
    public static void main(String[] args) {
        SimpleMqttClient smc = new SimpleMqttClient();
        smc.runClient();
    }


    /**
     * lightOff
     * The main functionnality is to light off a lamp
     */

    public void lightOff(String lampe){
        String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        String myTopic2 = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[0,0,0]}";

        int pubQoS = 0;
        MqttMessage message = new MqttMessage(myMessage.getBytes());
        message.setQos(pubQoS);
        message.setRetained(false);

        MqttTopic topic = myClient.getTopic(myTopic2);

        // Publish the message
        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(message);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void lightUp(String lampe){
        String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        String myTopic2 = M2MIO_DOMAIN + "/" + lampe + "/" + "json";
        String myMessage = "{\'command\' : \'fill\', \'rgb\' :[123,123,123]}";

        int pubQoS = 0;
        MqttMessage message = new MqttMessage(myMessage.getBytes());
        message.setQos(pubQoS);
        message.setRetained(false);

        MqttTopic topic = myClient.getTopic(myTopic2);

        // Publish the message
        System.out.println("Publishing to topic \"" + topic + "\" qos " + pubQoS);
        MqttDeliveryToken token = null;
        try {
            // publish message to broker
            token = topic.publish(message);
            // Wait until the message has been delivered to the broker
            token.waitForCompletion();
            sleep(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * runClient
     * The main functionality of this simple example.
     * Create a MQTT client, connect to broker, pub/sub, disconnect.
     *
     */
    public void runClient() {
        // setup MQTT Client
        String clientID = M2MIO_THING;
        connOpt = new MqttConnectOptions();

        connOpt.setCleanSession(true);
        connOpt.setKeepAliveInterval(30);
        //connOpt.setUserName(M2MIO_USERNAME);
        //connOpt.setPassword(M2MIO_PASSWORD_MD5.toCharArray());

        // Connect to Broker
        try {
            myClient = new MqttClient(BROKER_URL, clientID);
            myClient.setCallback(this);
            myClient.connect(connOpt);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Connected to " + BROKER_URL);

        // setup topic
        // topics on m2m.io are in the form <domain>/<stuff>/<thing>

        /*String myTopic = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + M2MIO_THING;
        String myTopic2 = M2MIO_DOMAIN + "/" + M2MIO_STUFF + "/" + "fill 0 0 255";
        MqttTopic topic = myClient.getTopic(myTopic);

        // subscribe to topic if subscriber
        if (subscriber) {
            try {
                int subQoS = 0;
                myClient.subscribe(myTopic, subQoS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/

        // publish messages if publisher
        if (publisher) {
            this.lightOff("Laumio_D454DB");
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.lightUp("Laumio_D454DB");

        }

        // disconnect
        try {
            // wait to ensure subscribed messages are delivered
            if (subscriber) {
                sleep(5000);
            }
            myClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
