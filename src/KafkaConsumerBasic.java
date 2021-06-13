import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

/**
 * 컨슈머의 주요 기능은 특정 파티션을 관리하고 있는 파티션 리더에게 메시지 가져오기 요청을 하는 것입니다.
 * 각 요청은 로그의 오프셋을 명시하고 그 위치로부터 로그 메시지를 수신합니다. 그래서 컨슈머는 가져올 메시지의 위치를 조정할 수 있고
 * 필요하다면 이미 가져온 데이터도 다시 가져올 수 있습니다.
 */
public class KafkaConsumerBasic {

    /**
     *
     * bootstrap.servers : 카프카 클러스터에 처음 연결을 하기 위한 호스트와 포트 정보로 구성된 리스트 정보
     * fetch.min.bytes : 한번에 가져올 수 있는 최소 데이터 사이즈, 만약 지정한 사이즈보다 작은 경우 요청에 대해 응답하지 않고 데이터가
     * 누적될 때까지 기다립니다.
     * group.id : 컨슈머가 속한 그룹을 식별하는 식별자 입니다.
     * enable.auto.commit: 백그라운드로 주기적으로 오프셋을 커밋합니다.
     * auto.offset.reset: 카프카에서 초기 오프셋이 없거나 현재 오프셋이 더 이상 존재하지 않은 경우 다음 옵션으로 리셋합니다.
     *  -- earliest: 가장 초기의 오프셋값으로 설정
     *  -- lastest: 가장 마지막의 오프셋값으로 설정
     *  -- none: 이전 오프셋값을 찾지 못하면 에러를 나타냅니다.
     *
     *  fetch.max.bytes : 한번에 가져올 수 있는 최대 데이터 사이즈
     *
     *  request.timeout.ms: 요청에 대해 응답을 기다리는 최대 시간
     *
     *  session.timeout.ms: 컨슈머와 브로커사이의 세션 타임 아웃 시간, 브로커가 컨슈머가 살아있는 것으로 판단하는 시간. 만약 컨슈머가
     *  그룹 코디네이터에게 하트비트를 보내지 않고 session.timeout.ms가 지나면 해당 컨슈머는 종료되거나 장애가 발생한 것으로 판단하고
     *  컨슈머 그룹은 리밸런스를 시도합니다. session.timeout.ms는 하트비트 없이 얼마나 오랫동안 컨슈머가 있는지를 제어하며, 이 속성은
     *  hearbeat.interval.ms와 밀접한 관련이 있습니다.
     *
     *  hearbeat.intervals.ms : 그룹 코디네이터에 얼마나 자주 KafkaConsumer poll() 메소드로 하트비트를 보낼 것인지 조정합니다.
     *  session.timeout.ms와 밀접한 관계가 있으며 session.timeout.ms보다 낮아야 합니다.
     *
     *  max.poll.records : 단일 호출 poll()에 대한 최대 레코드 수를 조정합니다.
     *
     *  max.poll.interval.ms: 컨슈머가 살아있는지를 체크하기 위해 하트비트를 주기적으로 보내는데, 컨슈머가 계속해서
     *  하트비트만 보내고 실제로 메시지를 가져가지 않는 경우가 있을 수도 있습니다.
     *
     *  auto.commit.intervals.ms : 주기적으로 오프셋을 커밋하는 시간
     *  
     *  fetch.max.wait.ms : fetch.min.bytes에 의해 설정된 데이터보다 적은 경우 요청에 응답을 기다리는 최대 시간
     */

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "peter-kafka001:9092, peter-kafka002:9092, peter-kafka003:9092");

        // consumer에서 사용할 그룹 세팅
        props.put("group.id", "peter-consumer");

        // 자동 커밋 설정
        props.put("enable.auto.commit", "true");

        // 오프셋 리셋 값을 지정. earliest와 lastest 두가지 옵션이 있는데 earliest는 토픽의 처음부터, lastest는 토픽의 마지막 메시지부터 가져옴
        props.put("auto.offset.reset", "lastest");

        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        // subscribe() 메소드를 이용해 메시지를 가져올 토픽을 구독, 리스트 형태로 여러 개의 토픽을 입력할 수 있음
        consumer.subscribe(Arrays.asList("peter-topic"));

        try {
            while(true){

                // 컨슈머는 카프카에 폴링하는 것을 계속 유지해야 합니다. 그렇지 않으면 종료된 것으로 간주되어 컨슈머에 할당된 파티션은
                // 다른 컨슈머에게 전달되고 새로운 컨슈머에 의해 소비 됩니다. poll()은 타임 아웃 주기이고, 데이터가 컨슈머 버퍼에 없다면
                // poll()은 얼마나 오랫동안 블럭할지를 조정합니다. 만약 0으로 설정하면 poll()은 즉시 리턴하고 값을 입력하면 정해진 시간 동안 대기 합니다.
                ConsumerRecords<String, String> records = consumer.poll(100);

                // poll()은 레코드 전체를 리턴합니다.
                for( ConsumerRecord<String, String> record : records)
                    System.out.printf("Topic: %s, Partition: %s, Offset: %d, Key: %s, Value: %s \n",
                            record.topic(), record.partition(), record.offset(), record.key(), record);
            }
        }  finally {
            consumer.close();
        }

    }

}
