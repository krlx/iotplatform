import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;


import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch5.ElasticsearchSink;
import org.apache.flink.util.Collector;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import java.util.Properties;

public class ReadFromKafka {


  public static void main(String[] args) throws Exception {
    // create execution environment
    StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    Properties properties = new Properties();
    properties.setProperty("bootstrap.servers", "localhost:9092");
    properties.setProperty("group.id", "flink_consumer");


    DataStream<String> stream = env
            .addSource(new FlinkKafkaConsumer010<>("testing", new SimpleStringSchema(), properties));

    stream.map(new MapFunction<String, String>() {
      private static final long serialVersionUID = -6867736771747690202L;

      @Override
      public String map(String value) throws Exception {
        return "Stream Value: " + value;
      }
    }).print();
	
	
	//Insert streaming data to elasticseach
	Map<String, String> config = new HashMap<>();
	config.put("cluster.name", "elasticsearch");
	// This instructs the sink to emit after every element, otherwise they would be buffered
	config.put("bulk.flush.max.actions", "1");

	List<InetSocketAddress> transportAddresses = new ArrayList<>();
	transportAddresses.add(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 9300));
	
	stream.addSink(new ElasticsearchSink<>(
				config,
				transportAddresses,
				new MyInserter()));

    env.execute();
  }
  
	public static class MyInserter
			implements ElasticsearchSinkFunction<String> {

		// construct index request
		@Override
		public void process(String element, RuntimeContext ctx, RequestIndexer indexer) {

			// construct JSON document to index
			Map<String, String> json = new HashMap<>();
			json.put("name", element);

			IndexRequest rqst = Requests.indexRequest()
					.index("testindex")        // index name
					.type("agents")  			// mapping name
					.source(json);

			indexer.add(rqst);
		}
	}
}


