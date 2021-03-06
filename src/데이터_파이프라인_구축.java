


public class 데이터_파이프라인_구축 {

    /**
     * 데이터 처리에 대한 기업 내 요구사항이 많아지면서 데이터의 수집 저장, 분석에 이르는 일련의 과정을 관리하는 솔루션이 출시되고 있습니다.
     * 과거에는 워크플로우를 관리하는 솔루션들이 많았는데 데이터 흐름을 처리하기 위해서 단순 워크플로가 아닌 데이터 처리에 특화된 기능이
     * 필요하게 되어 새로운 형태의 솔루션이 출시되었습니다.
     *
     * 그중 대표적인 솔루션으로 아파치 나이파이를 꼽을 수 있습니다. 아파치 오픈소스 프로젝트 중 하나인 아파치 나이파이는
     * 데이터 흐름을 정의하고, 정의된 흐름대로 자동으로 실행해주는 유용한 애플리케이션입니다.
     * 데이터 흐름 기반으로 사용하기에 유용하며 웹 기반 인터페이스를 제공하고 있습니다.
     *
     * 7.2 파일비트를 이용한 메시지 전송 (267page)
     * 카프카의 로그를 카프카의 토픽으로 전송하려면 프로듀서가 필요합니다. 프로듀서의 경우
     * 카프카 클라이언트 라이브러리를 이용해 프로그램으로 직접 구현할 수도 있고 오픈소스 애플리케이션을 이용할 수도 있습니다.
     * 여기서는 프로듀서 역할을 하는 파일비트를 이용하겠습니다.
     *
     * ** 파일비트
     * 파일비트는 엘라스틱에서 제공하고 있는 경량 데이터 수집기입니다. 브로커 서버인 peter-kafka001 서버에 접속해 파일비트를 설치하겠습니다.
     *
     * 프로듀서(파일비트) -> 카프카(브로커)
     *
     *
     * 7.3 나이파이를 이용해 메시지 가져오기
     *
     * ** 나이파이
     * 나이파이 애플리케이션은 카프카와 동일하게 클러스터 구성이 가능한 분산 애플리케이션입니다.
     *
     * 카프카(브로커) -> 컨슈머(나이파이)
     *
     * 7.4 실시간 분석을 위해 엘라스틱서치에 메시지 저장
     *
     * 나이파이 -> 저장소(엘라스틱서치)
     *
     * ** 엘라스틱서치
     * 엘라스틱서치는 엘라스틱 사의 분산형 Restful 검색 및 분석 엔진으로서, 전문 검색 질의를 이용해 원하는 데이터 분석을 빠르게
     * 할 수 있는 애플리케이션입니다.
     *
     * 7.5 키바나를 이용해 엘라스틱서치에 저장된 데이터 확인
     *
     * 저장소(엘라스틱서치) -> 키바나
     *
     * ** 키바나는 엘라스틱에서 제공하는 애플리케이션 중 하나로서 엘라스틱서치에 저장된 데이터 확인과 분석을 쉽게 할 수 있는
     * 애플리케이션입니다.
     *
     *
     *
     *
     */
    public static void main(String[] args) {

    }

}
