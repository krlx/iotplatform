from kafka import KafkaProducer
producer = KafkaProducer(bootstrap_servers=['141.40.254.132:9092'])
#only works with python 2.7
producer.send('testing', repr("test1"))
producer.flush()