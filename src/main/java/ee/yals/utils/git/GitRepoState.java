package ee.yals.utils.git;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

/**
 * Object with properties about the git repository state at build time.
 * This information is supplied by my plugin - <b>pl.project13.maven.git-commit-id-plugin</b>
 *
 * @author Alexander Muravya (alexander.muravya@kuehne-nagel.com)
 * @since 2.0
 */
public class GitRepoState {
    private static final Logger LOG = Logger.getLogger(GitRepoState.class);
    private static GitRepoState SELF = null;

    private Properties gitProperties = new Properties();

    private GitRepoState() {
    }

    String tags;                    // =${git.tags} // comma separated tag names
    String branch;                  // =${git.branch}
    String dirty;                   // =${git.dirty}
    String remoteOriginUrl;         // =${git.remote.origin.url}
    String commitId;                // =${git.commit.id.full} OR ${git.commit.id}
    String commitIdAbbrev;          // =${git.commit.id.abbrev}
    String describe;                // =${git.commit.id.describe}
    String describeShort;           // =${git.commit.id.describe-short}
    String commitUserName;          // =${git.commit.user.name}
    String commitUserEmail;         // =${git.commit.user.email}
    String commitMessageFull;       // =${git.commit.message.full}
    String commitMessageShort;      // =${git.commit.message.short}
    String commitTime;              // =${git.commit.time}
    String closestTagName;          // =${git.closest.tag.name}
    String closestTagCommitCount;   // =${git.closest.tag.commit.count}
    String buildUserName;           // =${git.build.user.name}
    String buildUserEmail;          // =${git.build.user.email}
    String buildTime;               // =${git.build.time}
    String buildHost;               // =${git.build.host}
    String buildVersion;             // =${git.build.version}

    public static synchronized GitRepoState getInstance() {
        if (SELF == null) {
            SELF = new GitRepoState();
        } else if (SELF.correctlyInitialized()) {
            //avoiding reading properties files once again
            return SELF;
        } else {
            SELF.init();
        }

        return SELF;
    }

    private void init() {
        try {
            SELF.gitProperties.load(SELF.getClass().getClassLoader().getResourceAsStream("git.properties"));
            SELF.publishFromProperties();
        } catch (IOException ioe) {
            LOG.error("Failed to init " + GitRepoState.class.getSimpleName(), ioe);
            SELF.gitProperties.clear();
        }
    }

    private boolean correctlyInitialized() {
        return !gitProperties.isEmpty();
    }

    private void publishFromProperties() {
        this.tags = String.valueOf(gitProperties.get("git.tags"));
        this.branch = String.valueOf(gitProperties.get("git.branch"));
        this.dirty = String.valueOf(gitProperties.get("git.dirty"));
        this.remoteOriginUrl = String.valueOf(gitProperties.get("git.remote.origin.url"));

        this.commitId = String.valueOf(gitProperties.get("git.commit.id"));
        this.commitIdAbbrev = String.valueOf(gitProperties.get("git.commit.id.abbrev"));
        this.describe = String.valueOf(gitProperties.get("git.commit.id.describe"));
        this.describeShort = String.valueOf(gitProperties.get("git.commit.id.describe-short"));
        this.commitUserName = String.valueOf(gitProperties.get("git.commit.user.name"));
        this.commitUserEmail = String.valueOf(gitProperties.get("git.commit.user.email"));
        this.commitMessageFull = String.valueOf(gitProperties.get("git.commit.message.full"));
        this.commitMessageShort = String.valueOf(gitProperties.get("git.commit.message.short"));
        this.commitTime = String.valueOf(gitProperties.get("git.commit.time"));
        this.closestTagName = String.valueOf(gitProperties.get("git.closest.tag.name"));
        this.closestTagCommitCount = String.valueOf(gitProperties.get("git.closest.tag.commit.count"));

        this.buildUserName = String.valueOf(gitProperties.get("git.build.user.name"));
        this.buildUserEmail = String.valueOf(gitProperties.get("git.build.user.email"));
        this.buildTime = String.valueOf(gitProperties.get("git.build.time"));
        this.buildHost = String.valueOf(gitProperties.get("git.build.host"));
        this.buildVersion = String.valueOf(gitProperties.get("git.build.version"));
    }

}
