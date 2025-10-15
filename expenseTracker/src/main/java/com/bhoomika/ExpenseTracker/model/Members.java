// Members.java_v4
package com.bhoomika.ExpenseTracker.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "members")
@IdClass(MemberId.class)
@Data
public class Members {
    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "user_id")
    private Long userId;
}

// class MemberId implements Serializable {
//     private Long groupId;
//     private Long userId;
//     // equals/hashCode for JPA composite PK
// }
