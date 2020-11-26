package ru.catstack.nfc_terminal.service;

import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.model.Application;
import ru.catstack.nfc_terminal.model.payload.request.ApplicationRequest;
import ru.catstack.nfc_terminal.repository.ApplicationRepository;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void createApplication(ApplicationRequest request) {
        var app = new Application(request.getName(), request.getPhone(), request.getEmail(), request.getInn());
        applicationRepository.save(app);
    }
}
