package com.aroom.util.docs;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.aroom.util.docs.CommonDocsController.SampleRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class CommonDocsTest extends RestDocsHelper {

    @Override
    public Object initController() {
        return CommonDocsController.class;
    }

    @Test
    void commons() throws Exception {

        SampleRequest requestBody = new SampleRequest("ddd", "test@email.com");

        //then
        mockMvc.perform(post("/common/docs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
            ).andExpect(status().isOk())
            .andDo(
                document("common",
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("timeStamp").description("응답 시간"),
                        fieldWithPath("detail").description("에러 메시지"),
                        fieldWithPath("data").description("응답 데이터")
                    )
                )
            );
    }
}
