// package com.bhoomika.ExpenseTracker.service;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.times;
// import static org.mockito.Mockito.verify;
// import static org.mockito.Mockito.when;
// import com.bhoomika.ExpenseTracker.dto.PaymentRequest;
// import com.bhoomika.ExpenseTracker.dto.PaymentResponse;
// import com.bhoomika.ExpenseTracker.model.*;
// import com.bhoomika.ExpenseTracker.repository.*;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;

// public class PaymentServiceTest {
//     @InjectMocks
//     private PaymentService paymentService;

//     @Mock
//     private PaymentRepository paymentRepository;

//     @Mock
//     private UserRepository userRepository;

//     @Mock
//     private GroupRepository groupRepository;

//     @Mock
//     private BalanceRepository balanceRepository;

//     @Mock 
//     private TransactionLogRepository transactionLogRepository;

//     private User user1, user2;
//     private Group group;

//     @BeforeEach
//     public void setup() {
//         MockitoAnnotations.openMocks(this);
//         user1 = new User(); user1.setUserId(1L);
//         user2 = new User(); user2.setUserId(2L);
//         group = new Group(); group.setGroupId(1L);
//     }

//     @Test
//     public void testRecordPayment_success() {
//         PaymentRequest request = new PaymentRequest();
//         request.setFromUserId(1L);
//         request.setToUserId(2L);
//         request.setGroupId(1L);
//         request.setAmount(500.0);

//         when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(user1));
//         when(userRepository.findById(2L)).thenReturn(java.util.Optional.of(user2));
//         when(groupRepository.findById(1L)).thenReturn(java.util.Optional.of(group));
//         when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> {
//             Payment p = i.getArgument(0);
//             p.setPaymentId(10L);
//             return p;
//         });
//         when(balanceRepository.findByGroupAndUsers(1L, 1L, 2L)).thenReturn(null);
//         when(balanceRepository.findByGroupAndUsers(1L, 2L, 1L)).thenReturn(null);

//         PaymentResponse response = paymentService.recordPayment(request);

//         assertNotNull(response);
//         assertEquals(10L, response.getPaymentId());
//         verify(paymentRepository, times(1)).save(any(Payment.class));
//         verify(balanceRepository, times(2)).save(any(Balance.class));
//         verify(transactionLogRepository, times(1)).save(any(TransactionLog.class));
//     }
// }
