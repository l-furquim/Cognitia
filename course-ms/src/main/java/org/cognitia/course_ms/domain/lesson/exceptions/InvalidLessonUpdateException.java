package org.cognitia.course_ms.domain.lesson.exceptions;

public class InvalidLessonUpdateException extends RuntimeException {
    public InvalidLessonUpdateException(String message) {
        super(message);
    }
}
