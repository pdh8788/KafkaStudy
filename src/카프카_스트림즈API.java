public class 카프카_스트림즈API {

    /**
     *
     * 8.1 스트림 프로세싱 기초
     * 8.1.1 스트림 프로세싱과 배치 프로세싱
     * 스트림 프로세싱은 원 단어가 뜻하는 바 그대로 데이터들이 지속적으로 유입되고 나가는 과정에서 이 데이터에 대한 분석이나
     * 질의를 수행하는 것을 의미합니다. 데이터가 분석 시스템이나 프로그램에 도달하자마자 처리를 하기 때문에 스트림 프로세싱은
     * 실시간 분석이라고 불리기도 합니다.
     *
     * 스트링 프로세싱과 대비되는 개념으로 배치 또는 정적 데이터 처리를 둘 수 있습니다. 배치 처리는 이미 저장된 데이터를 기반으로
     * 분석이나 질의를 수행하고 특정 시간에 처리하는 특징이 있습니다.
     *
     * 8.2 카프카 스트림즈
     * 카프카 스트리즘의 특징과 개념
     *  카프카 스트림즈는 카프카에 저장된 데이터를 처리하고 분석하기 위해 개발된 클라이언트 라이브러리입니다. 카프카 스트림즈는
     *  이벤트 시간과 처리 시간을 분리해서 다루고 다양한 시간 각격 옵션을 지원하기에 실시간 분석을 간단하지만 효율적으로 진행할 수 있습니다.
     *      - 간단하고 가벼운 클라이언트 라이브러리이기 때문에 기존 애플리케이션이나 자바 애플리케이션에서 쉽게 사용이 가능
     *      - 시스템이나 카프카에 의존성이 없습니다.
     *      - 이중화된 로컬 상태 저장소를 지원합니다.
     *      - 카프카 브로커나 클라이언트에 장애가 생기더라도 스트림에 대해선 1번만 처리 되도록 보장합니다.
     *      - 밀리초 단위의 처리 지연을 보장하기 위해 한 번에 한 레코드만 처리합니다.
     *      - 간단하게 스트림 처리 프로그램을 만들 수 있도록 고수준의 스트림 dsl을 지원하고, 저수준의 프로세싱 API도 제공합니다.
     *
     */

    public static void main(String[] args) {



    }
}
