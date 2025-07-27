package org.cognitia.course_ms.domain.lesson.exceptions;

public class InvalidGetPathLessonsRequest extends RuntimeException {
    public InvalidGetPathLessonsRequest(String message) {
        super(message);
    }
}
