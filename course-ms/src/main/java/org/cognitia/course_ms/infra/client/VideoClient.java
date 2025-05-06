package org.cognitia.course_ms.infra.client;

import org.cognitia.course_ms.domain.path.dto.GetCourseVideosResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "videoClient", url = "${feign.client.video.url}")
public interface VideoClient {

    @RequestMapping(method = RequestMethod.GET, value = "/path/{pathId}")
    ResponseEntity<GetCourseVideosResponse> getByPath(@PathVariable("pathId") Long pathId);

    @RequestMapping(method = RequestMethod.DELETE, value = "/video/{videoId}")
    ResponseEntity<Void> deleteByVideo(@PathVariable("videoId") Long videoId);


}
