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

    /**
     *
     * 5.4 파티션과 메시지 순서
     *
     * 프로듀서가 토픽으로 메시지를 a, b, c, d, e 순으로 보냈지만 해당 메시지들은 하나의 파티션에만
     * 순서대로 저장되는 것이 아니라 그림 5-2와 같이 각각의 파티션별로 메시지가 저장되었습니다.
     * 그리고 컨슈머가 petoer-01 토픽에서 메시지를 가져올 때 컨슈머는 프로듀서가 어떤 순서대로 메시지를 보냈는지는 알 수 없습니다.
     * 컨슈머는 오직 파티션의 오프셋 기준으로만 메시지를 가져 옵니다.
     *  --> 카프카가 컨슈머에서의 메시지 순서는 동일한 파티션 내에서는 프로듀서가 생성한 순서와 동일하게 처리하지만,
     *      파티션과 파티션사이에서는 순서를 보장하지는 않는다.
     *
     *  --> 카프카를 사용하면서 메시지의 순서를 보장해야 하는 경우에는 토픽의 파티션 수를 1로 설정하면 됩니다.
     *      단점으로 메시지의 순서는 보장되지만 파티션 수가 하나이기 때문에 분산해서 처리할 수 없고 하나의 컨슈머에서만 처리할 수 있기 때문에
     *      처리량이 높지 않습니다.
     *
     *  5.5 컨슈머 그룹
     *  컨슈머 그룹은 하나의 토픽에 여러 컨슈머 그룹이 동시에 접속해 메시지를 가져올 수 있습니다.
     *  또한 컨슈머를 확장시킬 수도 있습니다. 만약 프로듀서가 토픽에 보내는 메시지 속도가 갑자기 증가해 컨슈머가 메시지를 가져가는 속도보다 빨라지게 되면
     *  컨슈머가 처리하지 못한 메시지들이 많아지게 되어, 카프카로 메시지가 들어오는 시간과 그 메시지가 컨슈머에 의해 카프카에서 나가는 시간의 차이는
     *  점점 벌어지게 됩니다.
     *
     *  토픽의 파티션 수와 동일하게 컨슈머 수를 늘렸는데도 프로듀서가 보내는 메시지의 속도를 따라가지 못한다면 그림 5-6과 같이 컨슈머만 추가하는 것이 아니라,
     *  그림 5-7과 같이 토픽의 파티션 수를 늘려주고 컨슈머 수도 같이 늘려줘야 합니다.
     *
     *  파티션 0 -> 컨슈머01
     *  파티션 1 -> 컨슈머02
     *  파티션 2 -> 컨슈머03
     *     x    -> 컨슈머04
     *   토픽      컨슈머그룹
     *
     *   파티션 0 -> 컨슈머01
     *   파티션 1 -> 컨슈머02
     *   파티션 2 -> 컨슈머03
     *   파티션 3 -> 컨슈머04
     *    토픽      컨슈머그룹
     *
     *  5.6.1 자동커밋
     *  오프셋을 직접 관리하는 방법도 있지만, 각 파티션에 대한 오프셋 정보 관리, 파티션 변경에 대한 관리 등이
     *  매우 번거로울 수 있습니다. 그래서 컨슈머를 다루는 사용자가 오프셋 관리를 직접 하지 않는 방법이 가장 쉬운 방법입니다.
     *  자동 커밋을 사용하고 싶을 때는 컨슈머 옵션 중 enable.auto.commit = true로 설정하면 5초마다 컨슈머는 poll()를 호출할 때
     *  가장 마지막 오프셋을 커밋합니다. 5초 주기는 기본값이며, auto.commit.interval.ms 옵션을 통해 조정이 가능합니다.
     *
     *
     *
     *
     *
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
