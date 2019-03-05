package io.mosip.authentication.service.impl.indauth.validator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.mock.env.MockEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.context.WebApplicationContext;

import io.mosip.authentication.core.dto.indauth.AdditionalFactorsDTO;
import io.mosip.authentication.core.dto.indauth.AuthRequestDTO;
import io.mosip.authentication.core.dto.indauth.AuthTypeDTO;
import io.mosip.authentication.core.dto.indauth.IdentityDTO;
import io.mosip.authentication.core.dto.indauth.IdentityInfoDTO;
import io.mosip.authentication.core.dto.indauth.KycAuthRequestDTO;
import io.mosip.authentication.core.dto.indauth.KycMetadataDTO;
import io.mosip.authentication.core.dto.indauth.RequestDTO;
import io.mosip.authentication.service.helper.IdInfoHelper;
import io.mosip.authentication.service.integration.MasterDataManager;
import io.mosip.kernel.idvalidator.uin.impl.UinValidatorImpl;
import io.mosip.kernel.idvalidator.vid.impl.VidValidatorImpl;
import io.mosip.kernel.logger.logback.appender.RollingFileAppender;

/**
 * 
 * @author Prem Kumar
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest
@ContextConfiguration(classes = { TestContext.class, WebApplicationContext.class })
public class KycAuthRequestValidatorTest {

	@Mock
	private SpringValidatorAdapter validator;

	@Mock
	Errors errors;

	@InjectMocks
	RollingFileAppender appender;

	@InjectMocks
	KycAuthRequestValidator KycAuthRequestValidator;

	@InjectMocks
	IdInfoHelper idInfoHelper;

	@InjectMocks
	AuthRequestValidator authRequestValidator;

	@Mock
	UinValidatorImpl uinValidator;

	@Mock
	VidValidatorImpl vidValidator;

	@Autowired
	Environment env;

	@Mock
	private MasterDataManager masterDataManager;

	@Before
	public void before() {
		ReflectionTestUtils.setField(authRequestValidator, "env", env);
		ReflectionTestUtils.setField(KycAuthRequestValidator, "authRequestValidator", authRequestValidator);
		ReflectionTestUtils.setField(KycAuthRequestValidator, "environment", env);
		ReflectionTestUtils.setField(KycAuthRequestValidator, "idInfoHelper", idInfoHelper);
		ReflectionTestUtils.setField(idInfoHelper, "environment", env);
		ReflectionTestUtils.setField(authRequestValidator, "idInfoHelper", idInfoHelper);

	}

	@Test
	public void testSupportTrue() {
		assertTrue(KycAuthRequestValidator.supports(KycAuthRequestDTO.class));
	}

	@Test
	public void testSupportFalse() {
		assertFalse(KycAuthRequestValidator.supports(KycAuthRequestValidator.class));
	}

	@Ignore
	@Test
	public void testValidateAuthRequest() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.TRUE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setId("id");
		// kycAuthRequestDTO.setVer("1.1");
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		// authRequestDTO.setVer("1.1");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setDemo(false);
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);

		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		idDTO.setUin("5134256294");
		RequestDTO request = new RequestDTO();
		AdditionalFactorsDTO additionalFactors = new AdditionalFactorsDTO();
		String otp = "123456";
		additionalFactors.setTotp(otp);
		request.setAdditionalFactors(additionalFactors);
		request.setIdentity(idDTO);
		request.setIdentity(idDTO);
		kycAuthRequestDTO.setRequest(request);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		kycAuthRequestDTO.setRequest(request);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertFalse(errors.hasErrors());
	}

	@Ignore
	@Test
	public void TestInvalidAuthRequest() {
		AuthRequestDTO authRequestDTO = null;
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Ignore
	@Test
	public void testInvalidAuthRequest() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.TRUE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		// authRequestDTO.setVer("1.1");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setDemo(true);
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);

		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		idDTO.setUin("5134256294");
		RequestDTO request = new RequestDTO();
		AdditionalFactorsDTO additionalFactors = new AdditionalFactorsDTO();
		String otp = "123456";
		additionalFactors.setTotp(otp);
		request.setAdditionalFactors(additionalFactors);
		request.setIdentity(idDTO);
		request.setIdentity(idDTO);
		kycAuthRequestDTO.setRequest(request);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		kycAuthRequestDTO.setRequest(request);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "baseAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Ignore
	@Test
	public void TestMUAPermissionisNotAvail() {
		MockEnvironment mockenv = new MockEnvironment();
		mockenv.merge(((AbstractEnvironment) mockenv));
		mockenv.setProperty("ekyc.mua.accesslevel.1234567890", "none");
		mockenv.setProperty("ekyc.allowed.auth.type", "otp,bio,pin");
		ReflectionTestUtils.setField(KycAuthRequestValidator, "environment", mockenv);
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.TRUE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		// authRequestDTO.setVer("1.1");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);

		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		idDTO.setUin("5134256294");
		RequestDTO request = new RequestDTO();
		AdditionalFactorsDTO additionalFactors = new AdditionalFactorsDTO();
		String otp = "123456";
		additionalFactors.setTotp(otp);
		request.setAdditionalFactors(additionalFactors);
		request.setIdentity(idDTO);
		kycAuthRequestDTO.setRequest(request);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		kycAuthRequestDTO.setRequest(request);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Ignore
	@Test
	public void TestInvalidAuthType() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.FALSE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setVersion("1.1");
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setDemo(true);
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);
		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		RequestDTO reqDTO = new RequestDTO();
		AdditionalFactorsDTO additionalFactors = new AdditionalFactorsDTO();
		String otp = "123456";
		additionalFactors.setTotp(otp);
		reqDTO.setAdditionalFactors(additionalFactors);
		reqDTO.setIdentity(idDTO);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		kycAuthRequestDTO.setRequest(reqDTO);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Test
	public void TestkycAuthRequestDtoisNull() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		kycAuthRequestDTO = null;
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Ignore
	@Test
	public void TestkycvalidateAuthType() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.TRUE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setVersion("1.1");
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setDemo(true);
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);
		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		RequestDTO reqDTO = new RequestDTO();
		reqDTO.setIdentity(idDTO);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		kycAuthRequestDTO.setRequest(reqDTO);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
		System.out.println(errors);
	}

	@Test
	public void TestkycAuthRequestisNull() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		kycAuthRequestDTO.setRequestedAuth(null);
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		assertTrue(errors.hasErrors());
	}

	@Ignore
	@Test
	public void TestInvalidConsentReq() {
		KycAuthRequestDTO kycAuthRequestDTO = new KycAuthRequestDTO();
		KycMetadataDTO dto = new KycMetadataDTO();
		dto.setConsentRequired(Boolean.FALSE);
		dto.setSecondaryLangCode("fra");
		kycAuthRequestDTO.setKycMetadata(dto);
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setVersion("1.1");
		kycAuthRequestDTO.setRequestTime(ZonedDateTime.now()
				.format(DateTimeFormatter.ofPattern(env.getProperty("datetime.pattern"))).toString());
		kycAuthRequestDTO.setId("id");
		kycAuthRequestDTO.setPartnerID("1234567890");
		kycAuthRequestDTO.setTransactionID("1234567890");
		AuthTypeDTO authTypeDTO = new AuthTypeDTO();
		authTypeDTO.setDemo(true);
		authTypeDTO.setOtp(true);
		IdentityInfoDTO idInfoDTO = new IdentityInfoDTO();
		idInfoDTO.setLanguage("EN");
		idInfoDTO.setValue("John");
		IdentityInfoDTO idInfoDTO1 = new IdentityInfoDTO();
		idInfoDTO1.setLanguage("fre");
		idInfoDTO1.setValue("Mike");
		List<IdentityInfoDTO> idInfoList = new ArrayList<>();
		idInfoList.add(idInfoDTO);
		idInfoList.add(idInfoDTO1);
		IdentityDTO idDTO = new IdentityDTO();
		idDTO.setName(idInfoList);
		RequestDTO request = new RequestDTO();
		request.setIdentity(idDTO);
		kycAuthRequestDTO.setRequestedAuth(authTypeDTO);
		String otp = "456789";
		AdditionalFactorsDTO additionalFactors = new AdditionalFactorsDTO();
		additionalFactors.setTotp(otp);
		request.setAdditionalFactors(additionalFactors);
		kycAuthRequestDTO.setRequest(request);
		Errors errors = new BeanPropertyBindingResult(kycAuthRequestDTO, "kycAuthRequestDTO");
		KycAuthRequestValidator.validate(kycAuthRequestDTO, errors);
		System.err.println(errors);
		assertTrue(errors.hasErrors());
	}

}
