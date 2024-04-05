package com.example.drone.controller;

import com.example.drone.model.dto.SampleUserAndSchool;
import com.example.drone.model.dto.School;
import com.example.drone.model.error.ValidationErrorResponse;
import com.example.drone.model.error.Violation;
import com.example.drone.model.viewmodel.SampleUserForTest;
import com.example.student.util.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.drone.util.ResponseBodyMatchers.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.example.drone.service.SampleForTest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.validation.FieldError;

import static org.assertj.core.api.Assertions.*;

@WebMvcTest(controllers = SampleControllerForTesting.class)
public class TestClassForSampleController {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SampleForTest sampleForTest;

    @Test
    void makeASampleCall() throws Exception {
        mockMvc.perform(get("/test-sample/mvc-testing")).andExpect(status().isOk());
    }
    @Test//objectMapper
    void checkResponceStatusCode_And_serviceMethodCallParameters_And_returnJsonValue() throws Exception {
        User user =  new User("Yasitha","Bandara","yasitha.dev@gmail");

        //---------------test responce parameters ----------------------//
        MvcResult mvcResult = mockMvc.perform(post("/test-sample/mvc-testing-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andReturn();

        //---------------test service method parammeters ----------------------//
        ArgumentCaptor<SampleUserForTest> userCaptor = ArgumentCaptor.forClass(SampleUserForTest.class);
        verify(sampleForTest, times(1)).registerUser(userCaptor.capture(), eq(true));
        assertThat(userCaptor.getValue().getFirstName()).isEqualTo("Yasitha");
        assertThat(userCaptor.getValue().getLastName()).isEqualTo("Bandara");
        assertThat(userCaptor.getValue().getEmail()).isEqualTo("yasitha.dev@gmail");

        //-----------------check json seperatly using mvc Result----------------//
        SampleUserForTest expectedSampleUser = new SampleUserForTest("Yasitha","Bandara","yasitha.dev@gmail");
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(expectedSampleUser));

    }
    @Test
    void checkReturnJsonwithTestUtils() throws Exception {
        User user =  new User("Yasitha","Bandara","yasitha.dev@gmail");
        SampleUserForTest expectedSampleUserForTest= new SampleUserForTest("Yasitha","Bandara","yasitha.dev@gmail");
        //---------------test service method parammeters ----------------------//
        MvcResult mvcResult = mockMvc.perform(post("/test-sample/mvc-testing-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(expectedSampleUserForTest, SampleUserForTest.class) )
                .andReturn();
    }
    @Test
    void checkReturnJsonForUserWithSchoolUsingTestUtils() throws Exception {
        User user =  new User("Yasitha","Bandara","yasitha.dev@gmail");
        SampleUserAndSchool expectedSampleUserAndSchool= SampleUserAndSchool.builder()
                .firstName("Yasitha")
                .lastName("Bandara")
                .email("Illukgoda")
                .school(School.builder().name("Nalanda Collage").district("Colombo 10").build())
                .build();;
        //---------------test service method parammeters ----------------------//
        MvcResult mvcResult = mockMvc.perform(post("/test-sample/user-with-school-post")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user))
                )
                .andExpect(status().isOk())
                .andExpect(responseBody().containsObjectAsJson(expectedSampleUserAndSchool, SampleUserAndSchool.class) )
                .andReturn();
    }
    @Test
    void callWithNullValue() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/test-sample/mvc-testing-post")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Yasitha\",\"email\":\"yasitha.dev@gmail\"}")
                )
                .andExpect(status().isBadRequest())
                .andReturn();

        ValidationErrorResponse expectedErrorResponse = new ValidationErrorResponse();
        Violation violation = new Violation("lastName", "Last Name can not be empty");
        expectedErrorResponse.getViolations().add(violation);
        expectedErrorResponse.setStatus(BAD_REQUEST);
        expectedErrorResponse.setErrorMessage(ErrorCode.VALIDATION_ERRORS_106);

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        String expectedResponseBody = objectMapper.writeValueAsString(expectedErrorResponse);
        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(expectedResponseBody);
        System.out.println("" + actualResponseBody);

/*     //-------------------------------Didnt get success----------------------- //
        ValidationErrorResponse actualObject = objectMapper.readValue(actualResponseBody,ValidationErrorResponse.class);
        actualObject.setTimestamp(null);//removing time stamp
        assertThat(actualObject).isEqualToComparingFieldByField(expectedErrorResponse);
        //Error:Expecting value [com.example.drone.model.error.Violation@23bd047f] in field "violations" but was [com.example.drone.model.error.Violation@30358dc7] in com.example.drone.model.error.ValidationErrorResponse@7fac18dc.
*/
    }
    @Test
    void callWithNullValue_And_TestUtils() throws Exception {
        mockMvc.perform(post("/test-sample/mvc-testing-post")
                        .contentType("application/json")
                        .content("{\"firstName\":\"Yasitha\",\"email\":\"yasitha.dev@gmail\"}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(responseBody().containsError("lastName", "Last Name can not be empty"));
    }
}
