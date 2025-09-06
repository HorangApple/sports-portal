package com.example.course.service;

import com.example.common.exception.EntityNotFoundException;
import com.example.common.exception.InvalidOperationException;
import com.example.course.dto.CourseEnrollmentRequest;
import com.example.course.dto.CourseEnrollmentResponse;
import com.example.course.entity.Course;
import com.example.course.entity.CourseEnrollment;
import com.example.course.entity.CourseSession;
import com.example.course.entity.EnrollmentStatus;
import com.example.course.repository.CourseEnrollmentRepository;
import com.example.course.repository.CourseRepository;
import com.example.course.repository.CourseSessionRepository;
import com.example.user.entity.User;
import com.example.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 수강신청 관련 비즈니스 로직을 처리하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseEnrollmentService {

    private final CourseEnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final CourseSessionRepository sessionRepository;
    private final UserRepository userRepository;

    /**
     * 사용자의 수강 중인 과정 목록 조회
     * @param userId 사용자 ID
     * @return 수강 중인 과정 목록
     */
    public List<CourseEnrollmentResponse> getInProgressCourses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        return enrollmentRepository.findByUserId(userId).stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.APPROVED && !enrollment.isCompleted())
                .map(CourseEnrollmentResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 사용자의 완료한 과정 목록 조회
     * @param userId 사용자 ID
     * @return 완료한 과정 목록
     */
    public List<CourseEnrollmentResponse> getCompletedCourses(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        return enrollmentRepository.findByUserId(userId).stream()
                .filter(enrollment -> enrollment.getStatus() == EnrollmentStatus.APPROVED && enrollment.isCompleted())
                .map(CourseEnrollmentResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 새로운 수강 신청
     * @param userId 사용자 ID
     * @param request 수강신청 요청 정보
     * @return 생성된 수강신청 정보
     */
    @Transactional
    public CourseEnrollmentResponse enrollCourse(Long userId, CourseEnrollmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다. ID: " + userId));

        CourseSession session = sessionRepository.findById(request.getCourseId())
                .orElseThrow(() -> new EntityNotFoundException("교육과정 차수를 찾을 수 없습니다. ID: " + request.getCourseId()));

        // 이미 신청한 과정인지 확인
        List<CourseEnrollment> existingEnrollments = enrollmentRepository.findByUserIdAndSessionId(userId, session.getId());
        if (!existingEnrollments.isEmpty()) {
            for (CourseEnrollment existing : existingEnrollments) {
                if (existing.getStatus() == EnrollmentStatus.APPROVED || existing.getStatus() == EnrollmentStatus.PENDING) {
                    throw new InvalidOperationException("이미 신청 중이거나 승인된 과정입니다.");
                }
            }
        }

        // 과정 인원 제한 확인
        if (session.getCourse().getMaxEnrollment() != null && 
            session.getCurrentEnrollment() >= session.getCourse().getMaxEnrollment()) {
            throw new InvalidOperationException("수강 인원이 초과되었습니다.");
        }

        // 신청 기간 확인
        LocalDateTime now = LocalDateTime.now();
        if (session.getRecruitmentStartAt() != null && now.isBefore(session.getRecruitmentStartAt())) {
            throw new InvalidOperationException("수강 신청 기간이 아직 시작되지 않았습니다.");
        }
        if (session.getRecruitmentEndAt() != null && now.isAfter(session.getRecruitmentEndAt())) {
            throw new InvalidOperationException("수강 신청 기간이 종료되었습니다.");
        }

        // 수강 신청 생성
        CourseEnrollment enrollment = CourseEnrollment.builder()
                .user(user)
                .session(session)
                .status(EnrollmentStatus.PENDING) // 기본적으로 승인 대기 상태로 시작
                .appliedAt(LocalDateTime.now())
                .applyReason(request.getApplyReason())
                .completed(false)
                .build();

        CourseEnrollment savedEnrollment = enrollmentRepository.save(enrollment);
        
        // 자동 승인 처리 (옵션)
        if (true) { // 여기에 자동 승인 조건을 추가할 수 있음
            approveEnrollment(savedEnrollment.getId(), "자동 승인됨");
            savedEnrollment = enrollmentRepository.findById(savedEnrollment.getId()).get();
        }

        return CourseEnrollmentResponse.from(savedEnrollment);
    }

    /**
     * 수강 신청 취소
     * @param userId 사용자 ID
     * @param enrollmentId 수강신청 ID
     * @param reason 취소 사유
     * @return 취소된 수강신청 정보
     */
    @Transactional
    public CourseEnrollmentResponse cancelEnrollment(Long userId, Long enrollmentId, String reason) {
        CourseEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("수강신청 정보를 찾을 수 없습니다. ID: " + enrollmentId));

        // 본인 확인
        if (!enrollment.getUser().getId().equals(userId)) {
            throw new InvalidOperationException("본인의 수강신청만 취소할 수 있습니다.");
        }

        // 상태 확인
        if (enrollment.getStatus() == EnrollmentStatus.CANCELLED) {
            throw new InvalidOperationException("이미 취소된 수강신청입니다.");
        }

        // 취소 처리
        enrollment.setStatus(EnrollmentStatus.CANCELLED);
        enrollment.setCancelledAt(LocalDateTime.now());
        enrollment.setCancelReason(reason);

        // 수강 인원 감소
        CourseSession session = enrollment.getSession();
        if (enrollment.getStatus() == EnrollmentStatus.APPROVED) {
            session.decrementCurrentEnrollment();
            session.getCourse().decrementEnrollmentCount();
        }

        CourseEnrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.from(updatedEnrollment);
    }

    /**
     * 수강 신청 승인 (관리자용)
     * @param enrollmentId 수강신청 ID
     * @param reason 승인 사유
     * @return 승인된 수강신청 정보
     */
    @Transactional
    public CourseEnrollmentResponse approveEnrollment(Long enrollmentId, String reason) {
        CourseEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("수강신청 정보를 찾을 수 없습니다. ID: " + enrollmentId));

        // 상태 확인
        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new InvalidOperationException("대기 중인 수강신청만 승인할 수 있습니다.");
        }

        // 과정 인원 제한 확인
        CourseSession session = enrollment.getSession();
        Course course = session.getCourse();
        if (course.getMaxEnrollment() != null && 
            session.getCurrentEnrollment() >= course.getMaxEnrollment()) {
            throw new InvalidOperationException("수강 인원이 초과되었습니다.");
        }

        // 승인 처리
        enrollment.setStatus(EnrollmentStatus.APPROVED);
        enrollment.setProcessedAt(LocalDateTime.now());
        enrollment.setProcessReason(reason);

        // 수강 인원 증가
        session.incrementCurrentEnrollment();
        course.incrementEnrollmentCount();

        CourseEnrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.from(updatedEnrollment);
    }
    
    /**
     * 수강 신청 거절 (관리자용)
     * @param enrollmentId 수강신청 ID
     * @param reason 거절 사유
     * @return 거절된 수강신청 정보
     */
    @Transactional
    public CourseEnrollmentResponse rejectEnrollment(Long enrollmentId, String reason) {
        CourseEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("수강신청 정보를 찾을 수 없습니다. ID: " + enrollmentId));

        // 상태 확인
        if (enrollment.getStatus() != EnrollmentStatus.PENDING) {
            throw new InvalidOperationException("대기 중인 수강신청만 거절할 수 있습니다.");
        }

        // 거절 처리
        enrollment.setStatus(EnrollmentStatus.REJECTED);
        enrollment.setProcessedAt(LocalDateTime.now());
        enrollment.setProcessReason(reason);

        CourseEnrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.from(updatedEnrollment);
    }
    
    /**
     * 수강 완료 처리
     * @param enrollmentId 수강신청 ID
     * @param attendanceRate 출석률
     * @param completionRate 이수율
     * @return 완료 처리된 수강신청 정보
     */
    @Transactional
    public CourseEnrollmentResponse completeCourse(Long enrollmentId, Double attendanceRate, Double completionRate) {
        CourseEnrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new EntityNotFoundException("수강신청 정보를 찾을 수 없습니다. ID: " + enrollmentId));

        // 상태 확인
        if (enrollment.getStatus() != EnrollmentStatus.APPROVED) {
            throw new InvalidOperationException("승인된 수강신청만 완료 처리할 수 있습니다.");
        }
        
        if (enrollment.isCompleted()) {
            throw new InvalidOperationException("이미 완료된 수강입니다.");
        }

        // 완료 처리
        enrollment.setCompleted(true);
        enrollment.setCompletedAt(LocalDateTime.now());
        enrollment.setAttendanceRate(attendanceRate);
        enrollment.setCompletionRate(completionRate);

        CourseEnrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return CourseEnrollmentResponse.from(updatedEnrollment);
    }
} 