import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;

public class KafkaProducerBasic {

    // 자바를 이용한 프로듀서 producer.java
    public static void main(String[] args) {

        Properties props = new Properties();
        // 브로커 리스트 저장
        props.put("bootstrap.servers", "peter-kafka001:9092, peter-kafka002:9092, peter-kafka003:9092");

        // 메시지의 키와 값에 문자열을 사용할 예정이므로 내장된 StringSerailizer를 지정
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        // Properties 오브젝트를 전달해 새 프로듀서 생성
        Producer<String, String> producer = new KafkaProducer<String, String>(props);

        // ProducerRecord 오브젝트를 생성하고, send() 메서드를 사용해 peter-topic 으로 아래 문자열 send

        /**
         *  send() 메서드를 사용해 ProducerRecord를 보냅니다. 메시즈는 버퍼에 저장되고 별도의 스레드를 통해 브로커로 전송합니다.
         *  send()는 자바 퓨처 객체로 RecordMetadata를 리턴 받지만 리턴값을 무시하기 때문에 미시지가 성공적으로 전송 되었는지 알 수 없습니다.
         *  이 방식은 메시지 손실 가능성이 있기 때문에 일반적인 서비스 환경에서는 사용하지 않습니다.
         */
        producer.send(new ProducerRecord<String, String>("peter-topic", "Apache Kafka is a distributed streaming platform"));

        /**
         *  ** 동기 전송
         *  프로듀서는 메시지를 보내고 send() 메소드의 Future객체를 리턴합니다. get() 메소드를 사용해 Future를 기다린 후 send()가 성공했는지
         *  실패했는지 확인합니다. 이러한 방법을 통해 메시지마다 브로커에게 전송한 메시지가 성공했는지 실패했는지 확인하여 더욱 신뢰성 있는 메시지 전송을
         *  할 수 있습니다.
         *
         */
        try {
            // get 메소드를 이용해 카프카의 응답을 기다립니다.
            RecordMetadata metadata = producer.send(new ProducerRecord<String, String>("peter-topic", "Apache Kafka is a distributed streaming platform"))
            .get();
            System.out.printf( "Partition: %d, Offset: %d", metadata.partition(), metadata.offset());
        } catch ( Exception e){

        }

        /**
         *  ** 비동기 전송
         *  프로듀서는 send() 메소드를 콜백과 같이 호출하고 카프카 브로커에서 응답을 받으면 콜백합니다. 만약 프로듀서가 보낸 모든 메시지에 대해
         *  응답을 기다린다면 응답을 기다리는 시간이 더 많이 소요됩니다. 하지만 비동기적으로 전송한다면 응답을 기다리지 않기 때문에
         *  더욱 빠른 전송이 가능합니다 또한 메시지를 보내지 못했을 때 예외를 처리하게 해 에러를 기록하거나 향후 분석을 위해 에러 로그 등에
         *  기록할 수 있습니다.
         */
        try {
            producer.send(new ProducerRecord<String, String>("peter-topic", "Apache Kafka is a distributed streaming platform")
            , new PeterCallback());
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    /**
     * 프로듀서 주요 옵션
     *
     * bootstrap.servers : 처음 연결을 하기 위한 호스트와 포트 정보로 구성된 리스트 정보
     * acks : 프로듀서가 카프카 토픽의 리더에게 메시지를 보낸 후 요청을 완료하기전 ack(승인)의 수 입니다. 해당 옵션의 수가 작으면
     * 성능이 좋지만, 메시지 손실 가능성이 있음
     *  -- ack=0 : 0으로 설정하는 경우 서버로부터 어떠한 ack도 기다리지 않습니다.매우 빠르게 메시지를 보낼 수 있어 높은 처리량을 얻을 수 있습니다.
     *
     *  -- ack=1 : 리더는 데이터를 기록하지만 모든 팔로워는 확인하지 않습니다. (가장 많이 사용하는 설정값)
     *
     *  -- ack=all 또는 -1 : 만약 all 또는 -1로 설정하는 경우 리더는 ISR의 팔로워로부터 데이터에 대한 ACK를 기다립니다. 하나의 팔로워가 있는 한
     *  데이터는 손실되지 않으며, 데이터 무손실에 대해 가장 강력하게 보장합니다.
     *  
     *  buffer.memory : 프로듀서가 카프카 서버로 데이터를 보내기 위해 잠시 대기 할 수 있는 전체 메모리 바이트입니다.
     *  
     *  compression.type: 프로듀서가 데이터를 압축해서 보낼 수 있는 어떤 타입으로 압축할지를 정할 수 있습니다.
     *  
     *  retries: 일시적인 오류로 인해 전송에 실패한 데이터를 다시 보내는 횟수
     *  
     *  batch.size: 프로듀서는 같은 파티션으로 보내는 여러 데이터를 함께 배치로 보내려고 시도합니다. 이러한 동작은 클라이언트와 서버 양쪽에 성능적인 측면에서
     *  도움이 됩니다. 
     *  
     *  linger.ms : 배치형태의 메시지를 보내기 전에 추가적인 메시지들을 위해 기다리는 시간을 조정합니다. 카프카 프로듀서는 지정된 배치 사이즈에 도달하면
     *  이 옵션과 관계없이 즉시 메시지를 전송하고, 배치 사이즈에 도달하지 못한 상황에서 linger.ms 제한 시간에 도달했을 때 메시지들을 전송합니다.
     *  
     *  max.request.size : 프로듀서가 보낼 수 있느 최대 메시지 바이트 사이즈
     * 
     *
     *
     */

}
