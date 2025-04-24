package org.cognitia.course_ms.infra.gateways;

import jakarta.transaction.Transactional;
import org.cognitia.course_ms.application.gateways.UpVoteGateway;
import org.cognitia.course_ms.domain.UpVote.UpVote;
import org.cognitia.course_ms.infra.mapppers.UpVoteMapper;
import org.cognitia.course_ms.infra.persistence.repository.UpVoteJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpVoteRepositoryGateway implements UpVoteGateway {

    private final UpVoteJpaRepository repository;
    private final UpVoteMapper mapper;

    public UpVoteRepositoryGateway(UpVoteJpaRepository repository, UpVoteMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public void create(UpVote request) {
        var upVoteEntity = mapper.toEntity(request);

        repository.save(upVoteEntity);
    }

    @Transactional
    @Override
    public void delete(Long upVoteId) {
        var up = repository.findById(upVoteId);

        if(up.isPresent()) repository.delete(up.get());
    }

    @Override
    public List<UpVote> getByAuthor(String authorId) {
        return repository.getByAuthorId(authorId).stream().map(
                up -> mapper.toDomain(up)
        ).toList();
    }

    @Override
    public UpVote findById(Long id) {
        var up = repository.findById(id);

        if(up.isPresent()) return mapper.toDomain(up.get());

        return null;
    }
}
