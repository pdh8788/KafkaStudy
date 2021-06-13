import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * 비동기 전송
 * 콜백을 사용하기 위해 클래스 구현
 */
public class PeterCallback implements Callback {

    @Override
    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

        if (recordMetadata != null){
            if(recordMetadata != null){
                System.out.println("Partition : " + recordMetadata.partition() + ", Offset : " + recordMetadata.offset() + "");
            } else {
                e.printStackTrace();
            }
        }

    }
}
