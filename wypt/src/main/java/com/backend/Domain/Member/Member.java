package com.backend.Domain.Member;


import com.backend.Domain.Trainer.Trainer;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Entity
@Table(name = "Member")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trainer_id", nullable = false)
    private Long trainerId;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Column(name = "kakao_id", nullable = false, unique = true, length = 30)
    private String kakaoId;

    private Integer age;

    @Column(name = "height_cm", precision = 5, scale = 1)
    private BigDecimal heightCm;

    @Column(name = "weight_kg", precision = 5, scale = 1)
    private BigDecimal weightKg;

    @Column(name = "body_fat_pct", precision = 5, scale = 1)
    private BigDecimal bodyFatPct;

    @Column(name = "muscle_mass_kg", precision = 5, scale = 1)
    private BigDecimal muscleMassKg;

    @Column(precision = 5, scale = 1)
    private BigDecimal bmi;

    @Column(length = 200)
    private String goal;

    @Column(length = 500)
    private String feature;

    @Column(name = "pt_count")
    private Integer ptCount = 0;

    @Column(name = "used_pt")
    private Integer usedPt = 0;
}


