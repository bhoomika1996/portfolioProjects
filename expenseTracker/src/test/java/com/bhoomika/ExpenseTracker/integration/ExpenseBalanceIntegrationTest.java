// package com.bhoomika.ExpenseTracker.integration;

// import com.bhoomika.ExpenseTracker.dto.ExpenseRequest;
// import com.bhoomika.ExpenseTracker.dto.ExpenseResponse;
// import com.bhoomika.ExpenseTracker.dto.BalanceResponse;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.*;
// import org.springframework.web.client.RestTemplate;
// import com.bhoomika.ExpenseTracker.model.SplitType;

// import java.util.List;

// import static org.assertj.core.api.Assertions.assertThat;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// public class ExpenseBalanceIntegrationTest {

//     @Autowired
//     private RestTemplate restTemplate;

//     private final String baseUrl = "http://localhost:8080/api";

//     @Test
//     public void testExpenseAndBalanceFlow() {
//         // Prepare expense request
//         ExpenseRequest req = new ExpenseRequest();
//         req.setGroupId(1L);
//         req.setPaidBy(1L);
//         req.setTitle("Lunch");
//         req.setTotalAmount(1200.0);
//         req.setCurrency("INR");
//         req.setSplitType(SplitType.EQUAL);

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         // Add auth token headers if required...

//         HttpEntity<ExpenseRequest> entity = new HttpEntity<>(req, headers);

//         // Call create expense
//         ResponseEntity<ExpenseResponse> resp = restTemplate.postForEntity(baseUrl + "/expenses", entity, ExpenseResponse.class);

//         assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
//         assertThat(resp.getBody().getTotalAmount()).isEqualTo(1200.0);

//         // Fetch balances for group
//         ResponseEntity<BalanceResponse[]> balanceResp = restTemplate.getForEntity(baseUrl + "/balances/group/1/simplified", BalanceResponse[].class);

//         assertThat(balanceResp.getStatusCode()).isEqualTo(HttpStatus.OK);
//         List<BalanceResponse> balances = List.of(balanceResp.getBody());

//         // Assert balances are correct for equal split (everyone owes equal share)
//         assertThat(balances).isNotEmpty();
//     }
// }

/*
Expense Creation (Equal Split)

 * {
  "groupId": 1,
  "paidBy": 1,
  "title": "Dinner",
  "totalAmount": 900,
  "currency": "INR",
  "splitType": "EQUAL"
}


Payment Recording
{
  "fromUserId": 2,
  "toUserId": 1,
  "groupId": 1,
  "amount": 300.0
}

 */