package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = "authority", callSuper = true)
@ToString(of = "authority", callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_authority_xref")
public class UserAuthorityXrefEntity extends AbstractAuditable<Long> {

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.DETACH, CascadeType.REFRESH})
    @JoinColumn(name = "authority_id", nullable = false)
    @Where(clause = "disabled = False")
    private AuthorityEntity authority;

}
