package eu.yals.services;

import eu.yals.models.LinkInfo;
import eu.yals.models.dao.LinkInfoRepo;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class LinkInfoService {
    private static final String TAG = "[" + LinkInfoService.class.getSimpleName() + "]";
    private final LinkInfoRepo repo;
    private final QRCodeService qrCodeService;

    /**
     * Constructor for Spring autowiring.
     *
     * @param linkInfoRepo  object for storing link info to DB
     * @param qrCodeService for generating qr code
     */
    public LinkInfoService(final LinkInfoRepo linkInfoRepo, final QRCodeService qrCodeService) {
        this.repo = linkInfoRepo;
        this.qrCodeService = qrCodeService;
    }

    /**
     * Stores link info into DB.
     *
     * @param ident   string with part that identifies short link
     * @param session string with session ID
     * @return saved record
     */
    @SuppressWarnings("UnusedReturnValue") //to be queried later
    @SneakyThrows
    public LinkInfo storeNew(final String ident, final String session) {
        LinkInfo linkInfo = new LinkInfo();
        linkInfo.setIdent(ident);
        linkInfo.setQrCode(qrCodeService.getQRCodeFromIdent(ident, 22));
        linkInfo.setSession(session);
        linkInfo.setCreated(Timestamp.from(Instant.now()));
        linkInfo.setUpdated(Timestamp.from(Instant.now()));
        return repo.save(linkInfo);
    }

    public List<LinkInfo> getAllRecordWithSession(String sessionID) {
        return repo.findBySession(sessionID);
    }

    public Optional<LinkInfo> getLinkInfoById(long id) {
        return Optional.of(repo.findSingleById(id));
    }

    public void update(LinkInfo updatedLinkInfo) {
        repo.update(updatedLinkInfo);
    }
}
