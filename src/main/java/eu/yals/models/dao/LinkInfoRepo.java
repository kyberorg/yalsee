package eu.yals.models.dao;

import eu.yals.models.LinkInfo;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LinkInfoRepo extends Repository<LinkInfo, Long> {

    List<LinkInfo> findBySession(String sessionId);

    /**
     * Saves  info to DB.
     *
     * @param linkInfoObject {@link LinkInfo} object with filled fields
     * @return same {@link LinkInfo} object, but enriched with ID field
     */
    LinkInfo save(LinkInfo linkInfoObject);

    /**
     * Number of link info records.
     *
     * @return int with number of links stored
     */
    int count();

    LinkInfo findSingleById(long id);

    void update(LinkInfo updatedLinkInfo);
}
