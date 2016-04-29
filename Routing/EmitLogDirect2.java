import com.rabbitmq.client.*;
import java.io.IOException;

public class EmitLogDirect2 extends Thread implements Runnable{

  private static final String EXCHANGE_NAME = "direct_logs";
  private static ConnectionFactory factory;
  private static Connection connection;
  private static Channel channel;
  private static String queueName;

  @Override
  public void run(){
    while (true){
      try{
        Thread.sleep(2000);
        enviaM("B","HolaD");
      }
      catch(Exception e){}
    }
  }
  public static void conexionM()throws Exception{
    factory = new ConnectionFactory();
    factory.setHost("localhost");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "BB");
  }

  public static void enviaM (String id, String message) throws Exception {
    conexionM();
    channel.basicPublish(EXCHANGE_NAME, id, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + id + "':'" + message + "'");

    channel.close();
    connection.close();
  }
    
  public static void main(String args[]) throws Exception{
    conexionM();
    (new Thread(new EmitLogDirect2())).start();

    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
      }
    };
    channel.basicConsume(queueName, true, consumer);
  }
}