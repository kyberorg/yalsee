package eu.yals.services;

import eu.yals.models.Link;
import eu.yals.models.dao.LinkRepo;
import eu.yals.result.GetResult;
import eu.yals.result.StoreResult;
import eu.yals.utils.push.Broadcaster;
import eu.yals.utils.push.Push;
import eu.yals.utils.push.PushMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import reactor.core.publisher.UnicastProcessor;

import java.util.Optional;

import static eu.yals.utils.push.PushCommand.LINK_SAVED;

/**
 * Service, which interacts with database to store/retrieve links.
 *
 * @since 2.0
 */
@Slf4j
@Service
public class LinkService {
    private static final String TAG = "[" + LinkService.class.getSimpleName() + "]";
    private final LinkRepo repo;
    private final LinkInfoService linkInfoService;
    private final UnicastProcessor<PushMessage> broadcaster;

    /**
     * Constructor for Spring autowiring.
     *
     * @param repo            object for communicating with DB
     * @param linkInfoService service storing link info
     */
    public LinkService(final LinkRepo repo, final LinkInfoService linkInfoService, final UnicastProcessor<PushMessage> broadcaster) {
        this.repo = repo;
        this.linkInfoService = linkInfoService;
        this.broadcaster = broadcaster;
    }

    /**
     * Provides stored link by its ident.
     *
     * @param ident string with ident to search against
     * @return search result
     */
    public GetResult getLink(final String ident) {
        Optional<Link> result;
        try {
            result = repo.findSingleByIdent(ident);
        } catch (DataAccessResourceFailureException e) {
            return new GetResult.DatabaseDown().withException(e);
        } catch (Exception e) {
            return new GetResult.Fail().withException(e);
        }

        return result.<GetResult>map(link -> new GetResult.Success(link.getLink()))
                .orElseGet(GetResult.NotFound::new);
    }

    /**
     * Stores new link into DB.
     *
     * @param ident string with part that identifies short link
     * @param link  string with long URL
     * @return store result
     */
    public StoreResult storeNew(final String ident, final String link) {
        return storeNew(ident, link, null);
    }

    /**
     * Stores new link into DB.
     *
     * @param ident   string with part that identifies short link
     * @param link    string with long URL
     * @param session session ID
     * @return store result
     */
    public StoreResult storeNew(final String ident, final String link, final String session) {
        Link linkObject = Link.create(ident, link);
        try {
            repo.save(linkObject);
            linkInfoService.storeNew(ident, session);
            Broadcaster.broadcast(Push.command(LINK_SAVED).toString());
            broadcaster.onNext(PushMessage.withCommand(LINK_SAVED));
            return new StoreResult.Success();
        } catch (CannotCreateTransactionException e) {
            return new StoreResult.DatabaseDown().withException(e);
        } catch (Exception e) {
            log.error("{} Exception on storing new {}", TAG, Link.class.getSimpleName());
            log.debug("", e);
            return new StoreResult.Fail("Failed to add new record");
        }
    }
}
