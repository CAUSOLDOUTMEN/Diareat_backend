package com.diareat.diareat.user.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@IdClass(Follow.PK.class) // 복합키를 위한 어노테이션
@Getter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"to_user", "from_user"})
}) // 중복 팔로우 방지
@Entity
public class Follow {

    @Id
    @Column(name = "to_user", insertable = false, updatable = false)
    private Long toUser;

    @Id
    @Column(name = "from_user", insertable = false, updatable = false)
    private Long fromUser;

    public static Follow makeFollow(Long toUser, Long fromUser) {
        Follow follow = new Follow();
        follow.toUser = toUser;
        follow.fromUser = fromUser;
        return follow;
    }

    public static class PK implements Serializable { // 복합키를 위한 클래스
        Long toUser;
        Long fromUser;
    }
}
