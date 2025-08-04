package com.example.common.test;

/**
 * 테스트 계약 인터페이스
 * 
 * 이 인터페이스는 테스트 작성 시 따라야 할 규칙과 가이드라인을 정의합니다.
 * 테스트 클래스는 이 인터페이스를 직접 구현하지 않지만, 
 * 테스트 작성 시 이 인터페이스에 정의된 규칙을 따라야 합니다.
 */
public interface TestContract {

    /**
     * 단위 테스트 명명 규칙
     * 
     * - 테스트 메서드 이름은 다음 형식을 따라야 합니다:
     *   [테스트 대상 메서드]_[테스트 조건]_[기대 결과]
     *   
     * - 예시:
     *   createCourseType_withValidRequest_returnsCreatedCourseType
     *   getCourseTypeById_withNonExistingId_throwsResourceNotFoundException
     */
    
    /**
     * 테스트 구조 규칙
     * 
     * 각 테스트는 다음 구조를 따라야 합니다:
     * 
     * 1. given: 테스트 입력 및 환경 설정
     * 2. when: 테스트 대상 메서드 호출
     * 3. then: 결과 검증
     * 
     * 주석으로 각 섹션을 명확하게 구분하는 것을 권장합니다.
     */
    
    /**
     * 테스트 커버리지 목표
     * 
     * - 클래스 커버리지: 95% 이상
     * - 메서드 커버리지: 90% 이상
     * - 라인 커버리지: 85% 이상
     * 
     * 테스트 커버리지 리포트는 다음 명령으로 생성할 수 있습니다:
     * ./mvnw clean test jacoco:report
     * 
     * 생성된 리포트는 target/site/jacoco/index.html에서 확인할 수 있습니다.
     */
    
    /**
     * 테스트 범주
     * 
     * 1. 단위 테스트 (@Tag("unit"))
     *    - 단일 클래스 또는 메서드를 테스트
     *    - 외부 의존성은 모두 모킹(mocking)
     *    - 데이터베이스 접근 없음
     *    
     * 2. 통합 테스트 (@Tag("integration"))
     *    - 여러 컴포넌트 간의 상호작용 테스트
     *    - 실제 데이터베이스와 상호작용 가능
     *    - @DataJpaTest, @SpringBootTest 등 사용
     *    
     * 3. 컨트롤러 테스트 (@Tag("controller"))
     *    - API 엔드포인트 테스트
     *    - MockMvc 또는 WebTestClient 사용
     *    - 서비스 레이어는 모킹 가능
     */
} 