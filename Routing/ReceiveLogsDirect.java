import com.rabbitmq.client.*;
import java.io.IOException;

public class ReceiveLogsDirect extends Thread implements Runnable{

  private static final String EXCHANGE_NAME = "direct_logs";
  private static ConnectionFactory factory;
  private static Connection connection;
  private static Channel channel;
  private static String queueName,queue2;

  @Override
  public void run(){
    while (true){}
  }

  public static void enviaM (String id, String message) throws Exception {
    conexionM();
    channel.basicPublish(EXCHANGE_NAME, id, null, message.getBytes("UTF-8"));
    System.out.println(" [x] Sent '" + id + "':'" + message + "'");

    channel.close();
    connection.close();
  }
    
  public static void conexionM()throws Exception{
    factory = new ConnectionFactory();
    factory.setHost("localhost");
    connection = factory.newConnection();
    channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "A");
    queue2 = channel.queueDeclare().getQueue();
    channel.queueBind(queue2, EXCHANGE_NAME, "B");
  }


  public static void recibirM() throws Exception{
    conexionM();
    Consumer consumer = new DefaultConsumer(channel) {
      @Override
      public void handleDelivery(String consumerTag, Envelope envelope,
                                 AMQP.BasicProperties properties, byte[] body) throws IOException {
        String message = new String(body, "UTF-8");
        System.out.println(" [x] Received '" + envelope.getRoutingKey() + "':'" + message + "'");
        try{
          if (envelope.getRoutingKey().equals("A"))
            enviaM("AA","HolaA");
          if (envelope.getRoutingKey().equals("B"))
            enviaM("BB","HolaB");
        }
        catch(Exception e){}
      }
    };
    channel.basicConsume(queueName, true, consumer);
    channel.basicConsume(queue2, true, consumer); 
  }

  public static void main(String args[]) throws Exception{
      recibirM();
      (new Thread(new ReceiveLogsDirect())).start();
  }
}