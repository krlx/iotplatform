from kafka import KafkaProducer
import random
import time
producer = KafkaProducer(bootstrap_servers=['141.40.254.37:9092'])
while True:
    temperature = str(random.uniform(20,25))
    producer.send('testing', repr(temperature))
    producer.flush()