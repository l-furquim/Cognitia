package org.cognitia.course_ms.domain.lesson.exceptions;

public class InvalidCourseLessonsRequestException extends RuntimeException {
    public InvalidCourseLessonsRequestException(String message) {
        super(message);
    }
}
