package eu.yals.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * LinkInfo table mapped POJO
 *
 * @since 2.8
 */
@Data
@Entity
@Table(name = "link_info")
public class LinkInfo {
    private static final String IDENT_COLUMN = "ident";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String QRCODE_COLUMN = "qrcode";
    private static final String SESSION_COLUMN = "session";
    private static final String CREATED_COLUMN = "created";
    private static final String UPDATED_COLUMN = "updated";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = IDENT_COLUMN, nullable = false, unique = true)
    private String ident;

    @Column(name = DESCRIPTION_COLUMN)
    private String description;

    @Column(name = QRCODE_COLUMN)
    private String qrCode;

    @Column(name = SESSION_COLUMN, length = 32)
    private String session;

    @Column(name = CREATED_COLUMN, nullable = false)
    private Timestamp created;

    @Column(name = UPDATED_COLUMN, nullable = false)
    private Timestamp updated;
}
