public class 카프카_운영_가이드 {

    /**
     * 필수 카프카 명령어
     *
     * 카프카는 여러 대의 서버에 분산되어 실행되고, 각 토픽은 여러 개의 파티션으로 분리되어 있으며 각 브로커에
     * 복제되어 분산 되어 저장되는 등 복잡하게 운영되는 애플리케이션입니다.
     *
     * 따라서 카프카 클러스터를 운영하다 보면 다양한 이슈가 발생하게 되고, 이슈들에 대응하기 위해 토픽의
     * 상태 정보, 토픽의 설정 변경 작업 등이 매우 번번하게 발생합니다.
     *
     * ** 필수 카프카 명령어들
     * 카프카에서 기본적으로 제공해주는 명령어들이 있 으며, 해당 명령어의 리스트는 설치 경로의
     * bin 디렉토리에서 확인할 수 있습니다. 예) /usr/local/kafka/bin 디렉토리에서 확인 가능
     *
     * 6.1.1 토픽 생성
     * 토픽을 생성하기 위해 카프카에서 제공하는 명령어는 kafka-topics.sh 입니다.
     * 예) kafka-topic.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181 --replication-factor 1 --parttions 1 --topic peter-topic --create
     *
     * 6.1.2 토픽 리스트 확인
     *  kafka-topic.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181/peter-kafka --list
     *
     * 6.1.3 토픽 상세보기
     *  kafka-topic.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181/peter-kafka --topic peter-topic --describe
     *
     *  6.1.4 토픽 설정 변경
     *  운영 중인 카프카의 디스크 공간을 확보하는 가장 좋은 방법은 디스크 공간을 가장 많이 차지하는 토픽의 보관 주기를 줄여주는 것입니다.
     *  기본 보관주기는 7일입니다.
     *  예) 보관주기 7일 -> 1시간
     *  kafka-config.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181/peter-kafka --alter --entity-type topics --entity-name-topic --add-config retention.ms=3600000
     *
     *  예) 1보관주기 1시간 설정을 삭제
     *  kafka-config.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181/peter-kafka --alter --entity-type topics --entity-name-topic --delete-config retention.ms
     *
     *  6.1.5 토픽의 파티션 수 변경
     *  카프카에서는 토픽의 파티션 수는 증가만 가능하고 감소는 불가능합니다.
     *  예)
     *  kafka-topics.sh --zookeeper perter-zkzk001:2181, peter-zk002:2181/peter-kafka --alter --topic peter-topic --partitions 2
     *
     *  6.1.6 토픽의 리플리케이션 팩터 변경
     *  토픽의 리플리케이션 팩터를 변경하려면 먼저 json 형식의 파일을 만들어야 합니다.
     *
     *  리플리케이션 팩터 변경을 위한 rf.json 파일
     *  { "version":1 ,
     *      "partitions": [
     *          {"topic":"peter-topic", "partition":0, "replicas":[1,2]},
     *          {"topic":"peter-topic", "partition":1, "replicas":[2,3]}
     *      ]}
     *
     *  "replicas":[1,2] 에서 파티션 0의 복제 수는 2이며, 앞에 있는 숫자가 리더를 의미하기 때문에 리더는 브로커1이고,
     *  리플리카는 브로커2라는 의미입니다. 동일하게 파티션 1번("partition":1)도 리플리케이션 팩터는 2이고, 리더는 브로커2, 리플리카는 브로커 3이라는 의미입니다.
     *  예)
     *  kafka-reassign-partitions.sh --zookeeper perter-zk001:2181, peter-zk002:2181/peter-kafka --reassignment-json-file /usr/local/kafka/rf.json --execute
     *
     *  6.1.7 컨슈머 그룹 리스트 확인
     *  예)
     *  kafka-consumer-groups.sh --bootstrap-server perter-zk001:9092, peter-zk002:9092 --list
     *
     *  6.1.8 컨슈머 상태와 오프셋 확인 (229page)
     *  kafka-consumer-groups.sh --bootstrap-server perter-zk001:9092, peter-zk002:9092 --group peter-consumer --describe
     *
     *  6.2 주키퍼 스케일 아웃(230page)
     *
     *  6.3 카프카 스케일 아웃(234page)
     *  
     *  6.4 카프카 모니터링
     *
     */
}
