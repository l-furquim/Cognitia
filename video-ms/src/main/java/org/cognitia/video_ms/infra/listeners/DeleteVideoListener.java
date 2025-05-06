package org.cognitia.video_ms.infra.listeners;

import lombok.extern.slf4j.Slf4j;
import org.cognitia.video_ms.application.gateways.VideoGateway;
import org.cognitia.video_ms.infra.dto.video.DeleteVideoFromPathRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeleteVideoListener {

    private static final String TRANSACTION_TOPIC = "video.path.delete";
    private static final String GROUP_ID = "cognitia";

    @Autowired
    private VideoGateway videogateway;

    @KafkaListener(topics = TRANSACTION_TOPIC, groupId = GROUP_ID)
    public void receiveRequest(DeleteVideoFromPathRequest data){
        log.info("Consumer: Solicitação de remoção de video: {}",data);

        try{
            videogateway.deleteVideoByPath(data.pathId());
            log.info("Remoção realizada com sucesso.");
        }catch (Exception e){
            log.error("Erro ao realizar a remoção: {}",e.getMessage());
        }
    }

}
