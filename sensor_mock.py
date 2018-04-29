import random
import time
from kafka import KafkaProducer

producer = KafkaProducer(bootstrap_servers=['141.40.254.132:9092'])

while True:
    # Send a random float encoded as the bytes of its string representation
    # Note that Kafka is promisified now, so the promise needs to be resolved
    temperature = str(random.uniform(20, 25))
    print(temperature)
    producer.send('temperature', bytes(temperature, "utf-8")).get(timeout=10)
    time.sleep(1)