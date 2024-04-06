package com.patika.kredinbizdeservice.service;

import com.patika.kredinbizdeservice.client.AkbankServiceClient;
import com.patika.kredinbizdeservice.client.GarantiServiceClient;
import com.patika.kredinbizdeservice.client.dto.request.AkbankApplicationRequest;
import com.patika.kredinbizdeservice.client.dto.request.GarantiApplicationRequest;
import com.patika.kredinbizdeservice.client.dto.response.ApplicationListResponse;
import com.patika.kredinbizdeservice.client.dto.response.ApplicationResponse;
import com.patika.kredinbizdeservice.converter.ApplicationConverter;
import com.patika.kredinbizdeservice.dto.request.ApplicationRequest;
import com.patika.kredinbizdeservice.exceptions.NoSuitableLoanException;
import com.patika.kredinbizdeservice.model.Application;
import com.patika.kredinbizdeservice.model.Loan;
import com.patika.kredinbizdeservice.model.User;
import com.patika.kredinbizdeservice.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository = new ApplicationRepository();
    private final ApplicationConverter applicationConverter;
    private final UserService userService;
    private final AkbankServiceClient akbankServiceClient;
    private final GarantiServiceClient garantiServiceClient;

    public Application createApplication(ApplicationRequest request) {

        User user = userService.getByEmail(request.getEmail());
        log.info("user bulundu");

        Application application = applicationConverter.toApplication(request, user); 

        try {
            ApplicationResponse akbankApplicationResponse = akbankServiceClient.createApplication(prepareAkbankApplicationRequest(user, request));
        
            application.setLoan(akbankApplicationResponse.getLoan());

            return application;
        } catch (Exception e) {
            log.error("Akbank servisine istek atılırken hata oluştu");
        }

        try {
            ApplicationResponse garantiApplicationResponse = garantiServiceClient.createApplication(prepareGarantiApplicationRequest(user, request));
        
            application.setLoan(garantiApplicationResponse.getLoan());

            return application;
        } catch (Exception e) {
            log.error("Akbank servisine istek atılırken hata oluştu");
        }

        throw new NoSuitableLoanException("Uygun kredi bulunamadı");
    }

    private AkbankApplicationRequest prepareAkbankApplicationRequest(User user, ApplicationRequest request) {
        AkbankApplicationRequest applicationRequest = new AkbankApplicationRequest();

        applicationRequest.setUserId(1L);
        applicationRequest.setAmount(request.getAmount());
        applicationRequest.setInstallment(request.getInstallment());
        applicationRequest.setLoanType(request.getLoanType());

        return applicationRequest;
    }

    private GarantiApplicationRequest prepareGarantiApplicationRequest(User user, ApplicationRequest request) {
        GarantiApplicationRequest applicationRequest = new GarantiApplicationRequest();

        applicationRequest.setUserId(1L);
        applicationRequest.setAmount(request.getAmount());
        applicationRequest.setInstallment(request.getInstallment());
        applicationRequest.setLoanType(request.getLoanType());

        return applicationRequest;
    }

    public List<ApplicationResponse> getAllByEmail(String email) {
        User user = userService.getByEmail(email);
        log.info("user bulundu");

        List<ApplicationResponse> akbankApps = akbankServiceClient.getApplicationsByUserId(prepareFindallRequest(user));
        List<ApplicationResponse> garantiApps = garantiServiceClient.getApplicationsByUserId(prepareFindallRequest(user));

        return Stream.concat(akbankApps.stream(), garantiApps.stream()).toList();
    }

    private Long prepareFindallRequest(User user) {
        return 1L;
    }
}
