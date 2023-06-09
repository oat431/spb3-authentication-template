package panomete.jwtauth.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import panomete.jwtauth.security.repository.AuthoritiesRepository;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationConfig implements ApplicationListener<ApplicationReadyEvent> {
    final AuthoritiesRepository authoritiesRepository;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("Application Started");
    }
}
