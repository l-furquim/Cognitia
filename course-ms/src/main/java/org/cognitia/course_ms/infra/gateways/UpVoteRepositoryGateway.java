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
    public Long create(UpVote request) {
        var upVoteEntity = mapper.toEntity(request);

        return repository.save(upVoteEntity).getId();
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

    @Override
    public UpVote findByQuestionAndAuthorId(String authorId, Long questionId) {
        var up = repository.getByAuthorAndQuestionId(authorId, questionId);

        if(up.isPresent()) return mapper.toDomain(up.get());

        return null;
    }
}
