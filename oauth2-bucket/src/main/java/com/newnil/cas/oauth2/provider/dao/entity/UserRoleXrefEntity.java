package com.newnil.cas.oauth2.provider.dao.entity;

import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(of = {"role"}, callSuper = true)
@ToString(of = "role", callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user_role_xref")
public class UserRoleXrefEntity extends AbstractAuditable<Long> {

    @NonNull
    @NotNull
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @NonNull
    @NotNull
    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    @Where(clause = "disabled = False")
    private RoleEntity role;

}
