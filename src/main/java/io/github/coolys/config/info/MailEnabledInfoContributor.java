package io.github.coolys.config.info;

import io.github.coolys.config.CoolybotProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;

/**
 * An {@link InfoContributor} that tells if mail service is enabled.
 *
 */
public class MailEnabledInfoContributor implements InfoContributor {

    private static final String MAIL_ENABLED = "mailEnabled";

    @Autowired
    private CoolybotProperties jHipsterProperties;

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail(MAIL_ENABLED, jHipsterProperties.getMail().isEnabled());
    }
}
