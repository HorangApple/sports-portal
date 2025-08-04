package com.example.common.exception;

/**
 * 리소스 중복 예외
 * 새로 생성하거나 업데이트하려는 리소스가 이미 존재하는 경우 발생하는 예외
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * 리소스 중복 예외 생성자
     *
     * @param resourceName 리소스 이름 (예: "Course", "CourseType")
     * @param fieldName 필드 이름 (예: "code", "email")
     * @param fieldValue 필드 값
     */
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s 리소스에 동일한 %s(%s)가 이미 존재합니다", resourceName, fieldName, fieldValue));
    }

    /**
     * 리소스 중복 예외 생성자
     *
     * @param message 예외 메시지
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
} 