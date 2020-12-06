package ru.catstack.nfc_terminal.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.catstack.nfc_terminal.exception.ResourceAlreadyInUseException;
import ru.catstack.nfc_terminal.model.Application;
import ru.catstack.nfc_terminal.model.enums.ApplicationStatus;
import ru.catstack.nfc_terminal.model.payload.request.ApplicationRequest;
import ru.catstack.nfc_terminal.repository.ApplicationRepository;
import ru.catstack.nfc_terminal.util.OffsetBasedPage;

import java.util.List;

@Service
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void createApplication(ApplicationRequest request) {
        if (applicationRepository.existsByPhone(request.getPhone()))
            throw new ResourceAlreadyInUseException("Phone", "value", request.getPhone());
        if (applicationRepository.existsByEmail(request.getEmail()))
            throw new ResourceAlreadyInUseException("Email", "address", request.getEmail());
        if (applicationRepository.existsByInn(request.getInn()))
            throw new ResourceAlreadyInUseException("INN", "value", request.getInn());

        var app = new Application(request.getName(), request.getPhone(), request.getEmail(), request.getInn());
        applicationRepository.save(app);
    }

    public List<Application> getApplicationsGap(int from, int count) {
        return findAllByStatus(new OffsetBasedPage(from, count, sort), ApplicationStatus.NOT_CONSIDERED).getContent();
    }

    public Page<Application> findAllByStatus(Pageable pageable, ApplicationStatus status) {
        return applicationRepository.findAllByStatus(pageable, status);
    }

    public void setStatusById(long id, ApplicationStatus status) {
        if (existsById(id)) {
            applicationRepository.updateStatusById(id, status);
        }
    }

    public void deleteById(long id) {
        applicationRepository.deleteById(id);
    }

    public boolean existsById(long id) {
        return applicationRepository.existsById(id);
    }
}
