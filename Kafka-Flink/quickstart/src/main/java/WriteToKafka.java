import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer010;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import java.util.Properties;

public class WriteToKafka {

  public static void main(String[] args) throws Exception {
	//create a new StreamExecutionEnvironment =  basis of any Flink application
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");

	//create a new DataStream in the application environment, the SimpleStringGenerator class implements the SourceFunction = base interface for all streams data sources in Flink.
    DataStream<String> stream = env.addSource(new SimpleStringGenerator());
	//add the FlinkKafkaProducer10 sink to the topic
    stream.addSink(new FlinkKafkaProducer010<>("testing", new SimpleStringSchema(), properties));

    env.execute();
  }

  /**
   * Simple Class to generate data
   */
  public static class SimpleStringGenerator implements SourceFunction<String> {
    private static final long serialVersionUID = 119007289730474249L;
    boolean running = true;
    long i = 0;
    @Override
    public void run(SourceContext<String> ctx) throws Exception {
      while(running) {
        ctx.collect("OINK-"+ (i++));
        Thread.sleep(10);
      }
    }
    @Override
    public void cancel() {
      running = false;
    }
  }
}